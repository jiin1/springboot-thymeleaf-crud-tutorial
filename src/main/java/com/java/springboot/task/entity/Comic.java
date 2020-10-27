package com.java.springboot.task.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "comic")
public class Comic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotBlank(message = "Name's required")
    @Column(name = "name")
    private String name;


    @OneToMany(mappedBy = "comic", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private Set<Character> characters;

    public Comic(String name) {
        this.name = name;
    }

    public Comic(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}