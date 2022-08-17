package com.hust.movie_review.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Data
@Accessors(chain = true)
@Table(name = "chart_setting")
@Entity
@ToString(exclude ={"chartType", "chartCategory", "dashboardSetting" , "projects"})
@EqualsAndHashCode(exclude = {"chartType", "chartCategory", "dashboardSetting", "projects"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ChartSetting {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "chart_type_id", nullable = false)
    private ChartType chartType;
    @ManyToOne
    @JoinColumn(name = "chart_category_id", nullable = false)
    private ChartCategory chartCategory;
    @ManyToOne
    @JoinColumn(name = "dashboard_setting_id", nullable = false)
    private DashboardSetting dashboardSetting;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "project_chart_setting",
            joinColumns = @JoinColumn(name = "chart_setting_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects;
}
