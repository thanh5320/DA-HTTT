package org.logstashplugins.mapper;

import org.logstashplugins.constant.Constant;
import org.logstashplugins.model.Article;
import org.logstashplugins.model.kafka.NewsPost;
import org.logstashplugins.utils.GenFieldArticle;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class NewsPostToArticleMapper {

    public abstract Article newsPostToArticle(NewsPost source);

    @AfterMapping
    void setAfter(@MappingTarget Article target) {
        target.setId(GenFieldArticle.genIdFromUrl(target.getRawUrl()));
        target.setSourceId(Constant.SOURCE_ID);
    }
}
