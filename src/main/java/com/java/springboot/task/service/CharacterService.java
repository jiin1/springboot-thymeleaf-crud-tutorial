package com.java.springboot.task.service;


import com.java.springboot.task.entity.Character;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CharacterService {
    Optional<Character> findById(Long id);

    List<Character> list();

    Page<Character> list(Integer from, Integer pageSize, String sortBy, Sort.Direction ordering);

    List<Character> findByName(String name);

    Character create(Character character);

    Character update(Character character, Long id);

    void delete(Long id);

    List<Character> retrieveAllWithAgeMoreThan(Integer age, Boolean sort);

}
