package com.illiapinchukmoodle.data.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tasks_progress")
public class TaskProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

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

    @Column(name = "is_verified")
    private boolean verified;

}
