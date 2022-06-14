package org.logstashplugins.config.database;

import java.util.TimerTask;

public class ScheduledTask extends TimerTask {
    public void run() {
        System.out.println("run task");
        DetectProject.projectMap = PostgreSqlConnection.getProjectMap();
    }
}
