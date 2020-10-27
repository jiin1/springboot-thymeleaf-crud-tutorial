package com.java.springboot.task;

import com.java.springboot.task.entity.Character;
import com.java.springboot.task.entity.Comic;
import com.java.springboot.task.repository.ComicRepository;
import com.java.springboot.task.repository.CharacterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableSwagger2
public class Application implements CommandLineRunner {

    @Autowired
    ComicRepository comicRepository;

    @Autowired
    CharacterRepository characterRepository;

    private static List<Comic> comics = new ArrayList<>();



    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        comics.add(comicRepository.save(new Comic("Amazing Spider-Man")));
        comics.add(comicRepository.save(new Comic("Conan The Barbarian")));
        comics.add(comicRepository.save(new Comic("Daredevil")));
        comics.add(comicRepository.save(new Comic("Fantastic Four")));


        characterRepository.save(new Character("Aquaman", "jones", 19, comics.get(0)));
        characterRepository.save(new Character("Batman", "petrovs", 18,  comics.get(1)));
        characterRepository.save(new Character("Catwoman", "vasil", 20,  comics.get(2)));
        characterRepository.save(new Character("Dark Nights", "rogovkn", 21,  comics.get(3)));
        characterRepository.save(new Character("Justice League", "vodolazov", 23,  comics.get(1)));
        characterRepository.save(new Character("Metal Men", "karapet", 17,  comics.get(2)));
        characterRepository.save(new Character("Scooby-Doo", "batist", 30,  comics.get(2)));
        characterRepository.save(new Character("Scoobyo", "batistutag", 30,  comics.get(2)));

    }
}