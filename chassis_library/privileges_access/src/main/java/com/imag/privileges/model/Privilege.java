package com.imag.privileges.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "privileges")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "parent_id")
    private Long parentId;
    @Column(name = "code")
    private String code;
    @Column(name = "status")
    private Boolean status = false;

    public Privilege(String name, String description, Long parentId, String code) {
        this.name = name;
        this.description = description;
        this.parentId = parentId;
        this.code = code;
    }

    @ManyToMany(mappedBy = "privileges")
//    @JsonIgnoreProperties(value = "privileges")
    @JsonIgnore
    private List<Group> groups;

    @OneToMany
    @JoinColumn(name = "parent_id", referencedColumnName = "id", insertable = false, updatable = false)
    private List<Privilege> privileges;

    @ManyToMany(mappedBy = "privileges")
    @JsonIgnore
    private List<User> users;
}