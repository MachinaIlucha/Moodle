package com.illiapinchukmoodle.data.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * @author Illia Pinchuk
 */
@Entity
@Table(name = "tasks")
@Data
public class Task extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name="course_id", nullable=false)
    private Course course;
}
