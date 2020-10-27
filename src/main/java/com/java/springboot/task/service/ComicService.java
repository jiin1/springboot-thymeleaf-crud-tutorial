package com.java.springboot.task.service;

import com.java.springboot.task.entity.Comic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ComicService {
    Optional<Comic> findById(Long id);

    List<Comic> list();

    Page<Comic> list(Integer from, Integer pageSize, String sortBy, Sort.Direction ordering);

    List<Comic> findByName(String name);

    Comic create(Comic comic);

    Comic update(Comic comic,Long id);

    void delete(Long id);
}
