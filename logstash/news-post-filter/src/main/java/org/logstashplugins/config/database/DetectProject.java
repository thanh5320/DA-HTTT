package org.logstashplugins.config.database;

import org.apache.commons.lang3.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;

public class DetectProject {
   public static Map<Long, Project> projectMap;
    public DetectProject(){
        Timer time = new Timer();
        ScheduledTask st = new ScheduledTask();
        time.schedule(st, 0, 1000*30);
    }

    public Set<Long> detect(String content){
        Set<Long> projectIds = new HashSet<>();
        Set<Long> keySet = projectMap.keySet();
        Iterator<Long> keySetIterator = keySet.iterator();
        while (keySetIterator.hasNext()) {
            Long key = keySetIterator.next();
//            projectIds.add()
            Project project = projectMap.get(key);
            List<Rule> rules = project.getRules();
            Optional<Rule> result = rules.stream().filter(rule -> detectByRule(content, rule)).findFirst();
            if(result.isPresent()) projectIds.add(key);
        }
        return projectIds;
    }

    public static boolean detectByRule(String content, Rule rule){
        List<String> mainKeywords = rule.mainKeywords.stream()
                .filter(keyword -> detectByKeyword(content, keyword))
                .collect(Collectors.toList());

        List<String> subKeywords = rule.subKeywords.stream()
                .filter(keyword -> detectByKeyword(content, keyword))
                .collect(Collectors.toList());

        List<String> excludeKeywords = rule.excludeKeywords.stream()
                .filter(keyword -> detectByKeyword(content, keyword))
                .collect(Collectors.toList());

        if(excludeKeywords.size()>0) return false;
        else if(mainKeywords.size()<rule.getMainKeywords().size()) return false;
        else if (rule.getMainKeywords().size()==0 && subKeywords.size() == 0) return false;
        return true;
    }

    public static boolean detectByKeyword(String content, String keyword){
        if(content==null || content.equals("")) return false;
        return content.contains(keyword);
    }
}
