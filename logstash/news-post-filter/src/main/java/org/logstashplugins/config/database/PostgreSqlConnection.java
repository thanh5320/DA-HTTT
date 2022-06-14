package org.logstashplugins.config.database;


import java.sql.*;
import java.util.*;

public class PostgreSqlConnection {

    public static Map<Long, Project> getProjectMap(){

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://alert-dev:5320/alert", "postgres", "mh")) {
            System.out.println("Connected to PostgreSQL database!");
//            return connection;

//            Connection connection = getConnection();
            if(connection == null){
                return null;
            }
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("select * from alert.\"alert-tc2\".project join alert.\"alert-tc2\".project_rule pr on alert.\"alert-tc2\".project.id = pr.project_id join alert.\"alert-tc2\".rule r on r.id = pr.rule_id where alert.\"alert-tc2\".project.status = 'active' and alert.\"alert-tc2\".project.deleted_at is null;");
                Map<Long, Project> projectMap = new HashMap<>();
                while (resultSet.next()) {
                    Long id = resultSet.getLong(1);
                    String mainKeyword = resultSet.getString("main_keyword");
                    String subKeyword = resultSet.getString("sub_keyword");
                    String excludeKeyword = resultSet.getString("exclude_keyword");


                    List<String> mainKeywords = new ArrayList<>();
                    if(mainKeyword!=null) mainKeywords = List.of(mainKeyword.split(";"));

                    List<String> subKeywords = new ArrayList<>();
                    if(subKeyword!=null) subKeywords = List.of(subKeyword.split(";"));

                    List<String> excludeKeywords = new ArrayList<>();
                    if(excludeKeyword!=null) excludeKeywords = List.of(excludeKeyword.split(";"));


                    Rule rule = new Rule()
                            .setMainKeywords(mainKeywords)
                            .setSubKeywords(subKeywords)
                            .setExcludeKeywords(excludeKeywords);

                    if (projectMap.containsKey(id)) {
                        Project project = projectMap.get(id);

                        project.getRules().add(rule);
                    }else {
                        Project project = new Project()
                                .setId(id);
                        project.getRules().add(rule);
                        projectMap.put(id, project);
                    }
                }

                return projectMap;
            }catch (SQLException sqlException){
                System.out.println(sqlException);
            }
            return null;

        }catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }

        return null;
    }

    public static Connection getConnection() {
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://alert-dev:5320/alert", "postgres", "mh")) {
            System.out.println("Connected to PostgreSQL database!");
            return connection;

        }catch (SQLException e) {
            System.out.println("Connection failure.");
            e.printStackTrace();
        }
        return null;
    }
}
