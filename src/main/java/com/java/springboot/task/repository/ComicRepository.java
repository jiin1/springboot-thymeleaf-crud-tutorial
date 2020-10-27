package com.java.springboot.task.repository;

import com.java.springboot.task.entity.Comic;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComicRepository extends PagingAndSortingRepository<Comic, Long> {
    List<Comic> findByName(String name);
}
