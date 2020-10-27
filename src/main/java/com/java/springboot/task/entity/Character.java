package com.java.springboot.task.entity;


import com.java.springboot.task.dto.ComicDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "character")
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "name")
    private String name;


    @Column(name = "nameStartsWith")
    private String nameStartsWith;

    @Column(name = "age")
    private Integer age;





    @ManyToOne
    @JoinColumn(name = "comic_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comic_id"))
    private Comic comic;

    public Character(String name, String nameStartsWith, Integer age, Comic comic) {
        this.name = name;
        this.nameStartsWith = nameStartsWith;
        this.age = age;
        this.comic = comic;


    }
}