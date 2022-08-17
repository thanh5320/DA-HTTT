package com.hust.movie_review.es_repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.movie_review.common.Constant;
import com.hust.movie_review.data.request.chart.OrmRequest;
import com.hust.movie_review.data.request.news.SearchMultiRuleOrRequest;
import com.hust.movie_review.data.request.news.ViewArticleRequest;
import com.hust.movie_review.data.response.chart.SentimentStatistic;
import com.hust.movie_review.data.response.chart.SourceStatistic;
import com.hust.movie_review.data.response.chart.TimeStatistic;
import com.hust.movie_review.data.response.news.XSearchResponse;
import com.hust.movie_review.data.response.project.KeywordInfo;
import com.hust.movie_review.data.response.project.RuleInfo;
import com.hust.movie_review.es_repository.agg.AddAggsCommand;
import lombok.SneakyThrows;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.tomcat.jni.Time;
import org.elasticsearch.action.search.*;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.hust.movie_review.common.Constant.ARTICLE_INDEX;
import static com.hust.movie_review.common.Constant.STATISTIC_TIMEOUT;
import static com.hust.movie_review.es_repository.EsQueryUtils.*;
import static com.hust.movie_review.es_repository.EsTimeUtils.buildArticleIndices;
import static com.hust.movie_review.es_repository.EsTimeUtils.buildArticleTime;
import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;

@Repository
public class ArticleRepositoryImpl implements IArticleRepository {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient client;

    SimpleDateFormat sdf = new SimpleDateFormat(Constant.SDF_FORMAT);

    @SneakyThrows
    @Override
    public XSearchResponse searchMultipleSampleRuleOr(SearchMultiRuleOrRequest request) {
        BoolQueryBuilder mainQuery = QueryBuilders.boolQuery();

        addBoolFilter(mainQuery, buildRangeQueryBuilder("published_time", request.getDateFrom(), request.getDateTo()));
//        if (isNotEmpty(request.getSources()))
//            addBoolFilter(mainQuery, buildTermsQueryBuilder("source_id", request.getSources()));
//        if (isNotEmpty(request.getAuthorIds()))
//            addBoolFilter(mainQuery, buildTermsQueryBuilder("author_id.keyword", request.getAuthorIds()));
        if (isNotEmpty(request.getSentiments()))
            addBoolFilter(mainQuery, buildTermsQueryBuilder("sentiment", request.getSentiments()));
        if (isNotEmpty(request.getDomains()))
            addBoolFilter(mainQuery, buildTermsQueryBuilder("domain", request.getDomains()));
//        if (isNotEmpty(request.getArticleIds()))
//            addBoolFilter(mainQuery, buildTermsQueryBuilder("id.keyword", request.getArticleIds()));
        if (isNotEmpty(request.getKeywords()))
            addBoolMust(mainQuery, boolShouldMatchPhraseQuery("content", request.getKeywords()));
        if (isNotEmpty(request.getRuleInfos())) queryMainKeywordAndSubKeyword(request.getRuleInfos(), mainQuery);

        if (isNotEmpty(request.getRuleInfos())) {
            queryExcludeKeyword(request.getRuleInfos(), mainQuery);
        }

        mainQuery.minimumShouldMatch(0);

        SearchRequest searchRequest = buildDefaultSearchRequest(
                ARTICLE_INDEX, mainQuery, request.getSize(), request.getPage(),
                request.getDateFrom(), request.getDateTo(), STATISTIC_TIMEOUT);


        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return new ArticleEsParser().getSearchResponse(request, searchResponse.getHits());
    }

    private void queryExcludeKeyword(List<RuleInfo> ruleInfos, BoolQueryBuilder mainQuery) {
        List<MatchPhraseQueryBuilder> builders = new ArrayList<>();
        ruleInfos.forEach(keywords -> {
            List<String> strs = Optional.ofNullable(keywords.getExcludeKeywords()).orElse(new ArrayList<>())
                    .stream()
                    .map(KeywordInfo::getValue)
                    .collect(Collectors.toList());
            builders.addAll(buildMatchPhraseQuery("content", strs));
        });

        addBoolMustNot(mainQuery, builders);
    }

    private void queryMainKeywordAndSubKeyword(List<RuleInfo> ruleInfos, BoolQueryBuilder mainQuery) {
        List<BoolQueryBuilder> builders = new ArrayList<>();
        BoolQueryBuilder query = QueryBuilders.boolQuery();
        ruleInfos
                .forEach(keywords -> {
                    List<String> strs = keywords.getMainKeywords()
                            .stream()
                            .map(KeywordInfo::getValue)
                            .collect(Collectors.toList());
                    addBoolMust(query, buildMatchPhraseQuery("content", strs));
                });

        ruleInfos
                .forEach(keywords -> {
                    List<String> strs = keywords.getSubKeywords()
                            .stream()
                            .map(KeywordInfo::getValue)
                            .collect(Collectors.toList());
                    addBoolShould(query, buildMatchPhraseQuery("content", strs));
                });

        builders.add(query);

        addBoolShould(mainQuery, builders);
    }


    @SneakyThrows
    @Override
    public List<SentimentStatistic> statisticSentimentByDays(OrmRequest queryInput) {
        MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
        multiSearchRequest.indicesOptions(IndicesOptions.fromOptions(Constant.IGNORE_UNAVAILABLE,
                Constant.ALLOW_NO_INDICES, Constant.EXPAND_TO_OPEN_INDICES,
                Constant.EXPAND_TO_CLOSED_INDICES));

        if (ObjectUtils.isNotEmpty(queryInput.getSentiments())) {
            queryInput.getSentiments().clear();
        }
        SearchRequest totalCountRequest = this.buildSearchRequest(queryInput,
                Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS);
        multiSearchRequest.add(totalCountRequest);
        queryInput.setSentiments(positiveSentiment());
        SearchRequest positiveCountRequest = buildSearchRequest(queryInput,
                Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS);
        multiSearchRequest.add(positiveCountRequest);
        queryInput.setSentiments(neutralSentiment());
        SearchRequest neutralCountRequest = buildSearchRequest(queryInput,
                Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS);
        multiSearchRequest.add(neutralCountRequest);
        queryInput.setSentiments(negativeSentiment());
        SearchRequest negativeCountRequest = buildSearchRequest(queryInput,
                Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS);
        multiSearchRequest.add(negativeCountRequest);
        MultiSearchResponse multiSearchResponse = client.msearch(multiSearchRequest, RequestOptions.DEFAULT);


        MultiSearchResponse.Item[] items = multiSearchResponse.getResponses();

        List<SentimentStatistic> responses = processingResultsStatisticSentimentByDays(items);

        if(responses.isEmpty()){
            responses = buildStatisticSentimentByDaysEmpty(queryInput.getDateFrom(), queryInput.getDateTo());
        }
        return responses;
    }

    public List<SentimentStatistic> buildStatisticSentimentByDaysEmpty(Date from, Date to){
        List<Date> dates = buildArticleTime(from, to);
        List<SentimentStatistic> responses = new ArrayList<>();
        for(Date date: dates){
            responses.add(new SentimentStatistic().setDate(date));
        }
        return responses;
    }

    private List<SentimentStatistic> processingResultsStatisticSentimentByDays(MultiSearchResponse.Item[] items) {
        List<Histogram.Bucket> totalBuckets = new ArrayList<>();
        List<Histogram.Bucket> positiveBuckets = new ArrayList<>();
        List<Histogram.Bucket> neutralBuckets = new ArrayList<>();
        List<Histogram.Bucket> negativeBuckets = new ArrayList<>();
        if (items[0].getResponse().getHits().getTotalHits().value != 0) {
            totalBuckets = (List<Histogram.Bucket>) ((Histogram) items[0]
                    .getResponse()
                    .getAggregations()
                    .get(Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS))
                    .getBuckets();
        }
        if (items[1].getResponse().getHits().getTotalHits().value != 0) {
            positiveBuckets = (List<Histogram.Bucket>) ((Histogram) items[1]
                    .getResponse().getAggregations()
                    .get(Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS))
                    .getBuckets();
        }

        if (items[2].getResponse().getHits().getTotalHits().value != 0) {
            neutralBuckets = (List<Histogram.Bucket>) ((Histogram) items[2]
                    .getResponse()
                    .getAggregations()
                    .get(Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS))
                    .getBuckets();
        }

        if (items[3].getResponse().getHits().getTotalHits().value != 0) {
            negativeBuckets = (List<Histogram.Bucket>) ((Histogram) items[3]
                    .getResponse()
                    .getAggregations()
                    .get(Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS))
                    .getBuckets();
        }

        HashMap<String, Histogram.Bucket> mapPositiveBuckets = (HashMap<String, Histogram.Bucket>) positiveBuckets
                .stream()
                .collect(Collectors.toMap(Histogram.Bucket::getKeyAsString, Function.identity()));
        HashMap<String, Histogram.Bucket> mapNeutralBuckets = (HashMap<String, Histogram.Bucket>) neutralBuckets
                .stream()
                .collect(Collectors.toMap(Histogram.Bucket::getKeyAsString, Function.identity()));
        HashMap<String, Histogram.Bucket> mapNegativeBuckets = (HashMap<String, Histogram.Bucket>) negativeBuckets
                .stream()
                .collect(Collectors.toMap(Histogram.Bucket::getKeyAsString, Function.identity()));


        List<SentimentStatistic> statistics = new LinkedList<>();

        buildSentimentStatisticStatisticSentimentByDays(totalBuckets,
                mapPositiveBuckets,
                mapNeutralBuckets,
                mapNegativeBuckets,
                statistics);
        return statistics;
    }

    @SneakyThrows
    private void buildSentimentStatisticStatisticSentimentByDays(List<Histogram.Bucket> totalBuckets,
                                                                 HashMap<String, Histogram.Bucket> mapPositiveBuckets,
                                                                 HashMap<String, Histogram.Bucket> mapNeutralBuckets,
                                                                 HashMap<String, Histogram.Bucket> mapNegativeBuckets,
                                                                 List<SentimentStatistic> statistics) {
        for (int index = 0; index < totalBuckets.size(); index++) {
            SentimentStatistic statistic = new SentimentStatistic();
            String key = totalBuckets.get(index).getKeyAsString();

            Histogram.Bucket positiveBucket = mapPositiveBuckets.get(key);
            Histogram.Bucket neutralBucket = mapNeutralBuckets.get(key);
            Histogram.Bucket negativeBucket = mapNegativeBuckets.get(key);

            long positiveCount = positiveBucket != null ? positiveBucket.getDocCount() : 0;
            long neutralCount = neutralBucket != null ? neutralBucket.getDocCount() : 0;
            long negativeCount = negativeBucket != null ? negativeBucket.getDocCount() : 0;

            statistic.setDate(sdf.parse(key));
            statistic.setTotalCount(totalBuckets.get(index).getDocCount());
            statistic.setPositiveCount(positiveCount);
            statistic.setNeutralCount(neutralCount);
            statistic.setNegativeCount(negativeCount);
            statistics.add(statistic);
        }
    }


    private static final List<Integer> negativeSentiment() {
        return new LinkedList<Integer>() {{
            add(Constant.SentimentType.NEGATIVE);
        }};
    }

    private static final List<Integer> positiveSentiment() {
        return new LinkedList<Integer>() {{
            add(Constant.SentimentType.POSITIVE);
        }};
    }

    private static final List<Integer> neutralSentiment() {
        return new LinkedList<Integer>() {{
            add(Constant.SentimentType.NEUTRAL);
        }};
    }

    @SneakyThrows
    @Override
    public List<TimeStatistic> statisticSourceByDays(OrmRequest input) {
        SearchRequest searchRequest = buildSearchRequest(input, Constant.AggregationType.STATISTIC_SOURCE_BY_DAYS);
        SearchResponse searchResponse = this.client.search(searchRequest, RequestOptions.DEFAULT);
        List<TimeStatistic> responses = statisticSourceByDaysResults(searchResponse);
        if (responses.isEmpty()){
            responses = buildStatisticSourceByDaysEmpty(input.getDateFrom(), input.getDateTo());
        }
        return responses;
    }

    public List<TimeStatistic> buildStatisticSourceByDaysEmpty(Date from, Date to){
        List<Date> dates = buildArticleTime(from, to);
        List<TimeStatistic> responses= new ArrayList<>();
        for(Date date : dates){
            List<SourceStatistic> sourceStatistics = new ArrayList<>();
            for(int i=0;i<8;i++){
                SourceStatistic sourceStatistic = new SourceStatistic()
                        .setSourceId(i);
                sourceStatistics.add(sourceStatistic);
            }
            TimeStatistic timeStatistic = new TimeStatistic().setDate(date)
                    .setData(sourceStatistics);
            responses.add(timeStatistic);
        }
        return responses;
    }

    @SneakyThrows
    @Override
    public String view(ViewArticleRequest request) {
        BoolQueryBuilder mainQuery = QueryBuilders.boolQuery();

        if (isNotEmpty(request.getArticleId())) {
            List<String> articleIds = Collections.singletonList(request.getArticleId());
            addBoolFilter(mainQuery, buildTermsQueryBuilder("id.keyword", articleIds));

        }

//        mainQuery.minimumShouldMatch(1);

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.timeout(TimeValue.timeValueSeconds(STATISTIC_TIMEOUT));
        builder.query(mainQuery);

        SearchRequest searchRequest = new SearchRequest().indices(request.getIndexName());
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest.source(builder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());


        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        for (SearchHit searchHit : searchResponse.getHits()) {
            String source = searchHit.getSourceAsString();
            String index = searchHit.getIndex();
            ArticleResponse articleResponse = this.objectMapper.readValue(source, ArticleResponse.class);
            articleResponse.setIndexName(index);
            return articleResponse.getContent();
        }
        return null;
    }


    @SneakyThrows
    public List<TimeStatistic> statisticSourceByDaysResults(SearchResponse searchResponse) {
        List<TimeStatistic> statistics = new LinkedList<>();
        List<Histogram.Bucket> totalDateHistogramBuckets = new ArrayList<>();
        if(searchResponse.getHits().getTotalHits().value!=0){
            totalDateHistogramBuckets = (List<Histogram.Bucket>) ((Histogram) searchResponse
                    .getAggregations()
                    .get(Constant.AggregationType.STATISTIC_SENTIMENT_BY_DAYS))
                    .getBuckets();

        }

        for (int i = 0; i < totalDateHistogramBuckets.size(); i++) {
            List<SourceStatistic> sourceStatistics = new LinkedList<>();
            Histogram.Bucket dateHistogramBucket = totalDateHistogramBuckets.get(i);

            Terms sources = dateHistogramBucket.getAggregations().get(Constant.AggregationType.STATISTIC_SOURCE_BY_DAY);

            List<Terms.Bucket> termBuckets = (List<Terms.Bucket>) sources.getBuckets();

            buildTotalSourceStatistics(sourceStatistics, termBuckets);

            buildSourceStatisticByPlatform(sourceStatistics, termBuckets);

            TimeStatistic timeStatistic = new TimeStatistic()
                    .setDate(sdf.parse(dateHistogramBucket.getKeyAsString()))
                    .setData(sourceStatistics);
            statistics.add(timeStatistic);
        }
        return statistics;
    }

    private void buildTotalSourceStatistics(List<SourceStatistic> sourceStatistics, List<Terms.Bucket> termBuckets) {
        long total = termBuckets.stream().mapToLong(MultiBucketsAggregation.Bucket::getDocCount).sum();
        SourceStatistic sourceStatistic = new SourceStatistic();
        sourceStatistic.setSourceId(Constant.Source.Type.TOTAL);
        sourceStatistic.setTotalCount(total);
        sourceStatistics.add(sourceStatistic);

    }

    private void buildSourceStatisticByPlatform(List<SourceStatistic> sourceStatistics, List<Terms.Bucket> termBuckets) {
        List<Integer> sources = new ArrayList<>(
                Arrays.asList(Constant.Source.Type.NEWS,
                        Constant.Source.Type.FACEBOOK,
                        Constant.Source.Type.YOUTUBE,
                        Constant.Source.Type.FORUM,
                        Constant.Source.Type.NEWSPAPER,
                        Constant.Source.Type.VIDEO,
                        Constant.Source.Type.OTHER));

        for (Terms.Bucket termBucket : termBuckets) {
            int termKey = termBucket.getKeyAsNumber().intValue();
            long docCount = termBucket.getDocCount();
            sources.remove(Integer.valueOf(termKey));
            switch (termKey) {
                case Constant.Source.Type.NEWS:
                    sourceStatistics.add(buildSourceStatistic(docCount, Constant.Source.Type.NEWS));
                    break;
                case Constant.Source.Type.YOUTUBE:
                    sourceStatistics.add(buildSourceStatistic(docCount, Constant.Source.Type.YOUTUBE));
                    break;
                case Constant.Source.Type.FACEBOOK:
                    sourceStatistics.add(buildSourceStatistic(docCount, Constant.Source.Type.FACEBOOK));
                    break;
                case Constant.Source.Type.FORUM:
                    sourceStatistics.add(buildSourceStatistic(docCount, Constant.Source.Type.FORUM));
                    break;
                case Constant.Source.Type.NEWSPAPER:
                    sourceStatistics.add(buildSourceStatistic(docCount, Constant.Source.Type.NEWSPAPER));
                    break;
                case Constant.Source.Type.VIDEO:
                    sourceStatistics.add(buildSourceStatistic(docCount, Constant.Source.Type.VIDEO));
                    break;
                case Constant.Source.Type.OTHER:
                    sourceStatistics.add(buildSourceStatistic(docCount, Constant.Source.Type.OTHER));
                    break;
                default:
                    break;
            }
        }

        sources.forEach(source -> {
            sourceStatistics.add(buildSourceStatistic(0, source));
        });
    }

    private SourceStatistic buildSourceStatistic(long total, int sourceId) {
        SourceStatistic sourceStatistic = new SourceStatistic();
        sourceStatistic.setSourceId(sourceId);
        sourceStatistic.setTotalCount(total);
        return sourceStatistic;
    }

    public SearchRequest buildSearchRequest(OrmRequest input, String type) {
        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.size(0);
        builder.timeout(TimeValue.timeValueSeconds(32));
        builder.query(buildMainQuery(input));
        builder = AddAggsCommand.addAggs(input, type, builder);

        SearchRequest searchRequest = new SearchRequest().indices(buildArticleIndices(input.getDateFrom(), input.getDateTo(), ARTICLE_INDEX));
        searchRequest.searchType(SearchType.DFS_QUERY_THEN_FETCH);
        searchRequest.source(builder);
        searchRequest.indicesOptions(IndicesOptions.lenientExpandOpen());
        return searchRequest;
    }

    private BoolQueryBuilder buildMainQuery(OrmRequest request) {
        BoolQueryBuilder mainQuery = QueryBuilders.boolQuery();

        addBoolFilter(mainQuery, buildRangeQueryBuilder("published_time", request.getDateFrom(), request.getDateTo()));
//        if (isNotEmpty(request.getSources()))
//            addBoolFilter(mainQuery, buildTermsQueryBuilder("source_id", request.getSources()));
//        if (isNotEmpty(request.getAuthorIds()))
//            addBoolFilter(mainQuery, buildTermsQueryBuilder("author_id.keyword", request.getAuthorIds()));
        if (isNotEmpty(request.getSentiments()))
            addBoolFilter(mainQuery, buildTermsQueryBuilder("sentiment", request.getSentiments()));
//        if (isNotEmpty(request.getDomains()))
//            addBoolFilter(mainQuery, buildTermsQueryBuilder("domain", request.getDomains()));
//        if (isNotEmpty(request.getArticleIds()))
//            addBoolFilter(mainQuery, buildTermsQueryBuilder("id.keyword", request.getArticleIds()));
//        if (isNotEmpty(request.getKeywords()))
//            addBoolMust(mainQuery, boolShouldMatchPhraseQuery("content", request.getKeywords()));
        if (isNotEmpty(request.getRuleInfos())) queryMainKeywordAndSubKeyword(request.getRuleInfos(), mainQuery);

        if (isNotEmpty(request.getRuleInfos())) {
            queryExcludeKeyword(request.getRuleInfos(), mainQuery);
        }

        mainQuery.minimumShouldMatch(1);

        return mainQuery;
    }
}
