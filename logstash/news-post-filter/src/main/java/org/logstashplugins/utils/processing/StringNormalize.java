package org.logstashplugins.utils.processing;

import org.logstashplugins.model.Article;
import org.logstashplugins.model.Comment;

import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;

public class StringNormalize {
    private static String normalize(String string) {
        return string == null || string.isBlank() ? null : Normalizer.normalize(string, Normalizer.Form.NFC);
    }

    private static List<String> normNewsTag(List<String> newsTag) {
        List<String> normedNewsTag = new LinkedList<>();
        if (newsTag != null && !newsTag.isEmpty()) {
            for (String tag : newsTag) {
                String normTag = normalize(tag);
                if (normTag != null && !normTag.isBlank() && !"null".equalsIgnoreCase(normTag)) {
                    normedNewsTag.add(normTag);
                }
            }
        }
        return normedNewsTag;
    }

    public static Article processArticle(Article article) {
        article.setTitle(normalize(article.getTitle()));
        article.setContent(normalize(article.getContent()));
        article.setSummary(normalize(article.getSummary()));
        article.setAuthorDisplayName(normalize(article.getAuthorDisplayName()));
        return article;
    }

    public static Comment processComment(Comment c){
        c.setContent(normalize(c.getContent()));
        c.setAuthorDisplayName(normalize(c.getAuthorDisplayName()));
        return c;
    }
}
