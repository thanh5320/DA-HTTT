package com.hust.movie_review.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Data
@Accessors(chain = true)
@Table(name = "exclude_keyword")
@Entity
@ToString(exclude ={"rule"})
@EqualsAndHashCode(exclude = {"rule"})
@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class ExcludeKeyword {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "value", nullable = false)
    private String value;
    @ManyToOne
    @JoinColumn(name = "rule_id", nullable = false)
    private Rule rule;
}
