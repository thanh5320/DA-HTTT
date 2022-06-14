package org.logstashplugins.utils;

import org.apache.commons.codec.digest.MurmurHash3;
import org.logstashplugins.constant.Constant;
import org.logstashplugins.utils.crypto.AES;

public class GenFieldArticle {
    public static String genIdFromUrl(String url) {
        Integer hashValue = MurmurHash3.hash32x86(url.getBytes());
        return AES.encrypt(String.format(Constant.NEWS_ID_PATTERN, hashValue));
    }
}
