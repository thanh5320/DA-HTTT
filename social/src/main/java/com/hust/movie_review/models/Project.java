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
@Table(name = "project")
@Entity
@ToString(exclude ={"rules", "chartSettings", "user"})
@EqualsAndHashCode(exclude = {"rules", "chartSettings", "user"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Project {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "project")
    private List<Rule> rules;

    @ManyToMany(mappedBy = "projects")
    private List<ChartSetting> chartSettings;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


}

