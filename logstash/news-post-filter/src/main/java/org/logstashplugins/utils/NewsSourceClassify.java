package org.logstashplugins.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.logstashplugins.model.Article;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

import static org.logstashplugins.constant.Constant.ConstantNewsSourceClassfy.*;

public class NewsSourceClassify {
    private static final Logger log = LogManager.getLogger(NewsSourceClassify.class);

    private final Map<String, Integer> domainSource;

    public NewsSourceClassify() {
        List<List<String>> records = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                this.getClass().getClassLoader().getResourceAsStream(DOMAIN_PATH)));) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (Exception e) {
            log.error("load domain source failed: " + e.getMessage(), e);
            System.exit(1);
        }

        Map<String, Integer> domainSource = new HashMap<>();
        try {
            for (List<String> record : records) {
                String domain = record.get(0);
                Integer source = Integer.parseInt(record.get(1));
                if (domainSource.containsKey(domain)) {
                    log.info("duplicate: {} {} {}", domain, domainSource.get(domain), source);
                }
                domainSource.put(domain, source);
            }
        } catch (Exception e) {
            log.error("parse domain source failed: " + e.getMessage(), e);
            System.exit(1);
        }

        this.domainSource = Collections.unmodifiableMap(domainSource);
        //log.info("load source domain success!! size: " + domainSource.size());
    }

    public Article process(Article article) {
        String domain = article.getDomain();
        Integer sourceLevel1 = domainSource.getOrDefault(domain, OTHER);
//        article.setSourceIdLevel1(sourceLevel1);
        return article;
    }
}
