package org.logstashplugins.utils.processing;

import org.logstashplugins.model.Article;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetectHashtag {
    private static final Pattern hashtag = Pattern.compile("(^|\\W)#(\\w*[\\p{L}]+\\w*)", Pattern.UNICODE_CHARACTER_CLASS);

    public static List<String> detect(String text) {
        if (text == null || text.isBlank()) {
            return new LinkedList<>();
        }

        Set<String> hashtags = new HashSet<>();
        Matcher matcher = hashtag.matcher(text);
        while (matcher.find()) {
            String hashtag = matcher.group(2).toLowerCase();
            hashtags.add(hashtag);
        }
        return new ArrayList<>(hashtags);
    }

//    public static Article process(Article article) {
//        article.setHashtag(detect(article.getContent()));
//        return article;
//    }
}
