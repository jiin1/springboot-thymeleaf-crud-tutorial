package com.java.springboot.task.controller;

import com.java.springboot.task.entity.Character;
import com.java.springboot.task.entity.Comic;
import com.java.springboot.task.service.CharacterService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/characters/")
public class CharacterController {

    private final CharacterService characterService;

    private static final String CHARACTER = "character";
    private static final String CHARACTERS = "characters";

    @Autowired
    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }


    @GetMapping("list")
    public String showUpdateForm(@NotNull Model model) {
        model.addAttribute(CHARACTER, new Character());
        model.addAttribute(CHARACTERS, characterService.list());
        return CHARACTERS;
    }


    @PostMapping("add")
    public String addCharacter(@Valid Character character, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute(CHARACTER, character);
            model.addAttribute(CHARACTERS, characterService.list());
            return CHARACTERS;
        } else if (characterService.findByName(character.getName()).isEmpty()) {

            if (character.getComic() == null
                    || character.getAge() == null
                    || character.getNameStartsWith() == null) {
                String msg = " All Fields must be defined ";
                result.addError(new FieldError(CHARACTER, "comic", character.getComic(), false, null, null, msg));
                model.addAttribute(CHARACTER, character);
                model.addAttribute(CHARACTERS, characterService.list());
                return CHARACTERS;
            }


            characterService.create(character);
        } else {
            String msg = String.format("Character with name %s already exists", character.getName());
            result.addError(new FieldError(CHARACTER, "name", character.getName(), false, null, null, msg));
            model.addAttribute(CHARACTER, character);
            model.addAttribute(CHARACTERS, characterService.list());
            return CHARACTERS;
        }
        return"redirect:/characters/list";
    }





    @GetMapping("delete/{id}")
    public String deleteCharacter(@PathVariable("id") Long id, Model model) {
        characterService.delete(id);
        model.addAttribute(CHARACTERS, characterService.list());
        return "redirect:/characters/list";
    }
}
