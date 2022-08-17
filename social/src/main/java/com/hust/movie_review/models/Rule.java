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
@Table(name = "rule")
@Entity
@ToString(exclude ={"project", "excludeKeywords" , "subKeywords", "mainKeywords"})
@EqualsAndHashCode(exclude = {"project", "excludeKeywords" , "subKeywords", "mainKeywords"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Rule {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @OneToMany(mappedBy = "rule")
    private List<MainKeyword> mainKeywords;
    @OneToMany(mappedBy = "rule")
    private List<SubKeyword> subKeywords;
    @OneToMany(mappedBy = "rule")
    private List<ExcludeKeyword> excludeKeywords;
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}