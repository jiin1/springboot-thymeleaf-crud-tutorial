package com.java.springboot.task.service.impl;

import com.google.common.collect.Ordering;
import com.java.springboot.task.entity.Character;
import com.java.springboot.task.repository.CharacterRepository;
import com.java.springboot.task.service.CharacterService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service("characterService")
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;

    @Autowired
    public CharacterServiceImpl(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
    }

    @Override
    public Optional<Character> findById(Long id) {
        return characterRepository.findById(id);
    }

    @Override
    public List<Character> list() {
        return IteratorUtils.toList(characterRepository.findAll().iterator());
    }

    @Override
    public Page<Character> list(Integer from, Integer pageSize, String sortBy, Sort.Direction ordering) {
        return characterRepository.findAll(Pageable.unpaged());
    }

    @Override
    public List<Character> findByName(String name) {
        return characterRepository.findByName(name);
    }

    @Override
    @Transactional
    public Character create(Character character) { return characterRepository.save(character);
    }

    @Override
    @Transactional
    public Character update(Character character, Long id) {
        Optional<Character> charac = characterRepository.findById(id);
        if (charac.isPresent()) {
            if (!character.getName().isEmpty())
                charac.get().setName(character.getName());
            if (!character.getNameStartsWith().isEmpty())
                charac.get().setNameStartsWith(character.getNameStartsWith());
            if (!(character.getComic() == null))
                charac.get().setComic(character.getComic());
            if (!(character.getAge() == null))
                charac.get().setAge(character.getAge());
        } else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Character not found");
        return characterRepository.save(charac.get());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        characterRepository.deleteById(id);
    }


    public List<Character> retrieveAllWithAgeMoreThan(Integer age, Boolean sort) {

        List<Character> charactersList = (List<Character>) characterRepository.findAll();

        List<Character> filteredCharacters = new ArrayList<>();

        for (Character characters : charactersList) {
            if (characters.getAge() >= age) {
                filteredCharacters.add(characters);
            }
        }
        if (sort) {
            filteredCharacters.sort(Comparator.comparing(Character::getName));
            return filteredCharacters;
        }
        return filteredCharacters;
    }
}
