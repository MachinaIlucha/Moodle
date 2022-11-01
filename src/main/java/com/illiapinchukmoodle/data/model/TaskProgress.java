package com.illiapinchukmoodle.data.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "task_progresses")
@Data
public class TaskProgress extends BaseEntity {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    @Column(name = "attachments")
    private String attachments;

    @Column(name = "score")
    private int score;

    @Column(name = "verified")
    private boolean verified;

}
