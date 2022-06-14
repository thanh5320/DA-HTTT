package org.logstashplugins;

import co.elastic.logstash.api.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.logstashplugins.config.ObjectMapperConfig;
import org.logstashplugins.config.database.DetectProject;
import org.logstashplugins.config.database.PostgreSqlConnection;
import org.logstashplugins.mapper.NewsPostToArticleMapper;
import org.logstashplugins.mapper.NewsPostToArticleMapperImpl;
import org.logstashplugins.model.Article;
import org.logstashplugins.model.kafka.NewsPost;
import org.logstashplugins.sentiment.SentimentDetection;
import org.logstashplugins.utils.NewsSourceClassify;
import org.logstashplugins.utils.ValidNewsPost;
import org.logstashplugins.utils.processing.StringNormalize;

import java.util.Collection;
import java.util.Collections;

@LogstashPlugin(name = "news_post_filter")
public class NewsPostFilter implements Filter {
    private static final Logger log = LogManager.getLogger(NewsPostFilter.class);
    private static final DetectProject detectProject = new DetectProject();
    public static final PluginConfigSpec<String> SOURCE_CONFIG =
            PluginConfigSpec.stringSetting("message", "message");

    private String id;
    private String sourceField;
    private ObjectMapper objectMapper;
    private NewsPostToArticleMapper mapper;
    private SentimentDetection sentimentDetection;

    public NewsPostFilter(String id, Configuration config, Context context) {
        this.id = id;
        this.sourceField = config.get(SOURCE_CONFIG);
        this.objectMapper = ObjectMapperConfig.getObjectMapper();
        mapper = new NewsPostToArticleMapperImpl();
        sentimentDetection = new SentimentDetection();
        DetectProject.projectMap = PostgreSqlConnection.getProjectMap();
    }

    @Override
    public Collection<Event> filter(Collection<Event> events, FilterMatchListener matchListener) {

        for (Event e : events) {
            Object str = e.getField(sourceField);
            System.out.println("filter" + str);
            if (str instanceof String){
                String result = process((String) str);
                System.out.println("result" + result);
                if(result==null){
                    log.error("Error messages will be ignored: {}", str.toString());
                }
                e.setField(sourceField, result);
                matchListener.filterMatched(e);
//                e.setField(sourceField, null);
//                matchListener.filterMatched(e);
            }
        }
        return events;
    }

    @Override
    public Collection<PluginConfigSpec<?>> configSchema() {
        return Collections.singletonList(SOURCE_CONFIG);
    }

    @Override
    public String getId() {
        return this.id;
    }


    public NewsPost convertStrToObj(String message){
        NewsPost newsPost = null;

        try {
            newsPost = ObjectMapperConfig.getObjectMapper().readValue(message, NewsPost.class);
        } catch (JsonProcessingException ex) {
            log.error("invalid news_post from kafka", ex.getMessage(), ex);
        }

        if (!ValidNewsPost.isValid(newsPost)) {
            log.error("invalid news_post");
        }
        return newsPost;
    }


    public String process(String input){
        NewsPost newsPost = convertStrToObj(input);
        Article article = mapper.newsPostToArticle(newsPost);
        article = StringNormalize.processArticle(article);
        article.setSentiment(sentimentDetection.detect(article.getContent()));
        article.setProjectIds(detectProject.detect(article.getContent()));
        String message = null;
        try {
            message = objectMapper.writeValueAsString(article);
        } catch (JsonProcessingException ex) {
            log.error("error in too transfer Article to message");
        }
        System.out.println(message);
        return message;
    }
}
