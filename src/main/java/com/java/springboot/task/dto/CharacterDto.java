package com.java.springboot.task.dto;

import com.java.springboot.task.entity.Comic;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterDto {
    private Long id;
    private String nameStartsWith;
    private Integer age;
    private String name;
    private ComicDto comic;
}