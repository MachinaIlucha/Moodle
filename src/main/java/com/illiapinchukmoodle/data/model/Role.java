package com.illiapinchukmoodle.data.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * @author Illia Pinchuk
 */
@Entity
@Table(name = "roles")
@Data
public class Role extends BaseEntity {

    @Column(name = "role_name")
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;
}
