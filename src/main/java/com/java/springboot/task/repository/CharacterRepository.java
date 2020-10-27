package com.java.springboot.task.repository;

import com.java.springboot.task.entity.Character;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends PagingAndSortingRepository<Character, Long> {
    List<Character> findByName(String name);


}
