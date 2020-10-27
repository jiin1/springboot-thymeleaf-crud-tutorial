package com.java.springboot.task.controller;

import com.java.springboot.task.dto.ComicDto;
import com.java.springboot.task.entity.Comic;
import com.java.springboot.task.mapper.ComicsMapper;
import com.java.springboot.task.service.ComicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/v1/public")
public class RestComicController {

    private final ComicService comicService;
    private Integer page;
    private Integer pageSize;
    private String sortBy;
    private Sort.Direction order;

    public RestComicController(ComicService comicService) {

        this.comicService = comicService;
    }

    @GetMapping("comics")
    public Page<ComicDto> showUpdateForm(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(defaultValue = "5") Integer pageSize,
                                         @RequestParam(defaultValue = "id") String sortBy,
                                         @RequestParam(defaultValue = "ASC") Sort.Direction order) {
        this.page = page;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.order = order;
        return ComicsMapper.MAPPER.mapCollectionToDto(comicService.list(page, pageSize, sortBy, order));
    }


    @GetMapping("comics/{id}")
    public ComicDto showComicsId(@PathVariable Long id) {
        Optional<Comic> var = comicService.findById(id);
        if (var.isPresent()) {
            return ComicsMapper.MAPPER.mapToDto(var.get());
        } else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Comic not found");
    }

    @GetMapping("comics/{id}/characters")
    public String showCharacterId(@PathVariable Long id) {
        Optional<Comic> var = comicService.findById(id);
        if (var.isPresent()) {
            return ComicsMapper.MAPPER.mapToDto(var.get()).getName();
        } else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Comic not found");
    }

    @PostMapping("comics/add")
    public void createCharacter(@Valid @RequestBody ComicDto comicDto) {
        Comic comic = ComicsMapper.MAPPER.mapToEntiy(comicDto);
        comicService.create(comic);
    }

    @PutMapping("comics/update/{id}")
    public ComicDto updateCharacter(@PathVariable(value = "id") long id, @Valid @RequestBody ComicDto comicDto) {
        if (comicService.findById(id).isPresent())
            comicService.update(ComicsMapper.MAPPER.mapToEntiy(comicDto), id);
        else throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Comic not found");
        return ComicsMapper.MAPPER.mapToDto(comicService.findById(id).get());
    }


    @DeleteMapping("comics/{id}")
    public void deleteCharacter(@PathVariable(value = "id") Long id) {
        comicService.delete(id);
    }
}
