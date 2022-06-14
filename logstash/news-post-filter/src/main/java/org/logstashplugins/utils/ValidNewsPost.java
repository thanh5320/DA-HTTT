package org.logstashplugins.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.logstashplugins.model.kafka.NewsPost;

import java.net.URL;
import java.util.Objects;
import java.util.stream.Stream;

public class ValidNewsPost {
    private static final Logger log = LogManager.getLogger(ValidNewsPost.class);

    public static boolean isValid(NewsPost post) {
        return Stream.of(
                post,
                post.getRawUrl(),
                post.getDomain(),
                post.getPublishedTime(),
                post.getFirstCrawledTime()
        ).allMatch(Objects::nonNull) &&
                isValidUrl(post.getRawUrl()) &&
                isValidDomain(post.getRawUrl(), post.getDomain()) &&
                isNormalizedDomain(post.getDomain());
    }


    public static boolean isValidUrl(String strUrl) {
        try {
            new URL(strUrl).toURI();
        } catch (Exception e) {
            log.error("invalid url: {}", e.getMessage(), e);
            return false;
        }
        return true;
    }


    public static boolean isValidDomain(String strUrl, String domain) {
        try {
            URL url = new URL(strUrl);
            String parsedDomain = url.getHost();
            return parsedDomain.contains(domain);
        } catch (Exception e) {
            log.error("invalid domain & url: {}", e.getMessage(), e);
            return false;
        }
    }


    public static boolean isNormalizedDomain(String domain) {
        return !domain.startsWith("www.");
    }
}
