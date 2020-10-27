package com.java.springboot.task.controller;

import com.java.springboot.task.Application;
import com.java.springboot.task.dto.CharacterDto;
import com.java.springboot.task.dto.ComicDto;
import com.java.springboot.task.entity.Character;
import com.java.springboot.task.entity.Comic;
import com.java.springboot.task.exeptions.NotFoundExeption;
import com.java.springboot.task.mapper.CharacterMapper;
import com.java.springboot.task.mapper.ComicsMapper;
import com.java.springboot.task.service.CharacterService;
import com.java.springboot.task.service.ComicService;
import com.sun.imageio.plugins.jpeg.JPEG;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Document;
import javax.validation.Valid;
import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1/public")
public class RestCharacterController {

    private final CharacterService characterService;
    private final ComicService comicService;
    private Integer page;
    private Integer pageSize;
    private String sortBy;
    private Sort.Direction order;

    public RestCharacterController(CharacterService characterService, ComicService comicService) {
        this.characterService = characterService;
        this.comicService = comicService;

    }

    @GetMapping("/list_pageable")
    public Page<CharacterDto> showUpdateForm(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                             @RequestParam(defaultValue = "5") Integer pageSize,
                                             @RequestParam(defaultValue = "id") String sortBy,
                                             @RequestParam(defaultValue = "ASC") Sort.Direction order) {
        this.page = page;
        this.pageSize = pageSize;
        this.sortBy = sortBy;
        this.order = order;
        return CharacterMapper.MAPPER.mapCollectionToDto(characterService.list(page, pageSize, sortBy, order));
    }


    @GetMapping("/characters")
    public List<CharacterDto> showUpdateForm() {
        return CharacterMapper.MAPPER.mapCollectionToDto(characterService.list());
    }

    @GetMapping("/characters/filter_retrieve_age/{age},{sort}")
    public List<CharacterDto> showFilteredUpdateForm(@PathVariable Integer age,
                                                     @PathVariable Boolean sort) {
        return CharacterMapper.MAPPER.mapCollectionToDto(characterService.retrieveAllWithAgeMoreThan(age, sort));
    }

    @GetMapping("/character/{id}")
    public CharacterDto showCharacterId(@PathVariable Long id) {
        Optional<Character> var = characterService.findById(id);
        if (var.isPresent()) {
            return CharacterMapper.MAPPER.mapToDto(var.get());
        } else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Character not found");
    }

    @GetMapping("/character/{id}/comics")
    public ComicDto showCharacterIdComics(@PathVariable Long id) {
        Optional<Character> var = characterService.findById(id);
        if (var.isPresent()) {
            CharacterDto ch = CharacterMapper.MAPPER.mapToDto(var.get());
            return ch.getComic();
        } else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Character not found");
    }

    @PostMapping("/character/add")
    public void createCharacter(@Valid @RequestBody CharacterDto characterDto) {
        Character character = CharacterMapper.MAPPER.mapToEntity(characterDto);
        characterService.create(character);

    }

    @GetMapping("/character/add")
    public Long addCharacter(@RequestParam(value = "name") String name,
                             @RequestParam(value = "nameStartsWith", required = false) String nameStartsWith,
                             @RequestParam(value = "age", required = false) Integer age,
                             @RequestParam(value = "comic_id") Long id) {


        CharacterDto characterDto = new CharacterDto();
        if (characterService.findByName(name).isEmpty()) {

            characterDto.setName(name);
            characterDto.setNameStartsWith(nameStartsWith);
            characterDto.setAge(age);
            Optional<Comic> var = comicService.findById(id);
            if (var.isPresent())
                characterDto.setComic(ComicsMapper.MAPPER.mapToDto(var.get()));
            else throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Wrong comic id");
        } else {
            String msg = String.format("Character with name %s already exists", characterDto.getName());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, msg);

        }
        Character character = CharacterMapper.MAPPER.mapToEntity(characterDto);
        characterService.create(character);

        return character.getId();
    }

    @PutMapping("/character/update/{idCharacter}")
    public CharacterDto updateCharacter(@PathVariable(value = "id") long id, @Valid @RequestBody CharacterDto characterDto) {
        characterService.update(CharacterMapper.MAPPER.mapToEntity(characterDto), id);
        return CharacterMapper.MAPPER.mapToDto(characterService.findById(id).get());
    }

    @GetMapping("/character/update/{idCharacter}")
    public Long updateCharacter(@PathVariable Long idCharacter,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "nameStartsWith", required = false) String nameStartsWith,
                                @RequestParam(value = "age", required = false) Integer age,
                                @RequestParam(value = "comic_id", required = false) Long id) {
        Optional<Character> var = characterService.findById(id);
        if (var.isPresent()) {
            CharacterDto ch = CharacterMapper.MAPPER.mapToDto(var.get());
            if (!name.isEmpty())
                ch.setName(name);
            if (!nameStartsWith.isEmpty())
                ch.setNameStartsWith(nameStartsWith);
            if (!(age == null))
                ch.setAge(age);
            if (id != null)
                ch.getComic().setId(id);
            characterService.create(CharacterMapper.MAPPER.mapToEntity(ch));

        } else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Character not found");

        return CharacterMapper.MAPPER.mapToDto(var.get()).getId();
    }

    @DeleteMapping("/character/{id}")
    public void deleteCharacter(@PathVariable(value = "id") Long id) {
        characterService.delete(id);
    }

    @RequestMapping("/show/{id}")
    public ResponseEntity<Object> downloadFile(@PathVariable Long id,
                                               HttpServletResponse resp) throws IOException {

        Optional<Character> var = characterService.findById(id);
        if (var.isPresent()) {
            String name = var.get().getName();
            File outputFile = new File("D:\\MyApp\\", name + ".jpeg");
            resp.reset();
            resp.setContentLength((int) outputFile.length());

            BufferedInputStream in = new BufferedInputStream(new FileInputStream(outputFile));

            FileCopyUtils.copy(in, resp.getOutputStream());
            resp.flushBuffer();
        } else new ResponseEntity<>("Character with this ID does not existed", HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>("File send successfully", HttpStatus.OK);
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> uploadFile(@RequestParam(value = "file") MultipartFile file,
                                             @RequestParam("id") long id) throws IOException {
        Optional<Character> var = characterService.findById(id);
        if (var.isPresent()) {
            String str = var.get().getName();
            if (!str.endsWith("jpeg")) new ResponseEntity<>("Wrong data format", HttpStatus.INTERNAL_SERVER_ERROR);
            ;
            File convertFile = new File("D:\\MyApp\\" + str + ".jpeg");
            if (!convertFile.createNewFile()) throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            FileOutputStream fout = new FileOutputStream(convertFile);
            fout.write(file.getBytes());
            fout.close();
            return new ResponseEntity<>("File upload successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Character with this ID does not existed", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}





