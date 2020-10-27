package com.java.springboot.task.controller;

import com.java.springboot.task.entity.Comic;
import com.java.springboot.task.service.ComicService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/comics/")
public class ComicController {

    private static final String COMIC = "comic";
    private static final String COMICS = "comics";

    private final ComicService comicService;

    @Autowired
    public ComicController(ComicService comicService) {
        this.comicService = comicService;
    }


    @GetMapping("list")
    public String showUpdateForm(@NotNull Model model) {
         model.addAttribute(COMIC, new Comic());
        model.addAttribute(COMICS, comicService.list());
        return COMICS;
    }

    @PostMapping("add")
    public String addComic(@Valid Comic comic, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute(COMIC, comic);
            model.addAttribute(COMICS, comicService.list());
            return COMICS;
        } else {
            if (comicService.findByName(comic.getName()).isEmpty()) {
                comicService.create(comic);
            } else {
                String msg = String.format("Comic book with name %s already exists", comic.getName());
                result.addError(new FieldError(COMIC, "name", comic.getName(), false, null, null, msg));
                model.addAttribute(COMIC, comic);
                model.addAttribute(COMICS, comicService.list());
                return COMICS;
            }
        }


        return "redirect:/comics/list";
    }

    @GetMapping("delete/{id}")
    public String deleteComic(@PathVariable("id") Long id, Model model) {
        comicService.delete(id);
        model.addAttribute(COMICS, comicService.list());
        return "redirect:/comics/list";
    }
}
