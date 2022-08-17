package com.hust.movie_review.common;

public class Constant {
    public static final int SUCCESS_CODE = 200;
    public static final String SUCCESS_MESSAGE = "success";
    public static final String ROLE_MEMBER = "MEMBER";
    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_USER = "USER";

    public static final boolean IGNORE_UNAVAILABLE = true;
    public static final boolean ALLOW_NO_INDICES = false;
    public static final boolean EXPAND_TO_OPEN_INDICES = true;
    public static final boolean EXPAND_TO_CLOSED_INDICES = true;

    public static final int PAGE_DEFAULT = 1;
    public static final int PAGE_SIZE_DEFAULT = 10;
    public static final String SORT_BY_DEFAULT = "createdAt";

    public static final String SDF_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String SDF_FORMAT_INDEX = "yyMMdd";
    public static final String ARTICLE_INDEX = "article_v1-";
    public static final int STATISTIC_TIMEOUT = 30;

    public static class ChartCategory{
        public static final int NUMBER_POSTS = 1; // luot nhac toi
        public static final int SENTIMENT = 2; // sac thai
        public static final int DISTRIBUTION_OF_SENTIMENT = 3; // phan bo sac thai
        public static final int TOP_DOMAIN_PAGE_GROUP = 4; // domain noi bat
     }

    public static class AggregationType {
        public static final String STATISTIC_SOURCE_BY_DAYS = "statistic_source_by_days";
        public static final String STATISTIC_SENTIMENT_BY_DAYS = "statistic_by_days";

        public static final String STATISTIC_SOURCE_BY_DAY = "statistic_source_by_day";
    }

    public static class SentimentType {
        public static final int POSITIVE = 1;
        public static final int NEUTRAL = 0;
        public static final int NEGATIVE = -1;
        public static final int NOT_NEGATIVE = 10;
        public static final int NOT_POSITIVE = -10;
        public static final int NOT_NEUTRAL = -11;
        public static final int ALL = -101;
    }


    public static class Source {
        public static class Type {
            public static final int TOTAL = 0;
            public static final int NEWS = 1;
            public static final int FACEBOOK = 2;
            public static final int YOUTUBE = 3;
            public static final int FORUM = 4;
            public static final int OTHER = 5;
            public static final int NEWSPAPER = 6;
            public static final int VIDEO = 7;
        }

        public static class TypeV1 {
            public static final int NEWS_101 = 101;
            public static final int NEWS_102 = 102;
            public static final int NEWS_103 = 103;
            public static final int NEWS_104 = 104;
            public static final int NEWS_105 = 105;
        }
    }
}

