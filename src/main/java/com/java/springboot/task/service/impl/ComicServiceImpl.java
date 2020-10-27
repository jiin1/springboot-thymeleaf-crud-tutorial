package com.java.springboot.task.service.impl;

import com.java.springboot.task.entity.Comic;
import com.java.springboot.task.repository.ComicRepository;
import com.java.springboot.task.service.ComicService;
import org.apache.commons.collections4.IteratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;


@Service("comicService")
public class ComicServiceImpl implements ComicService {

	private final ComicRepository comicRepository;

	@Autowired
	public ComicServiceImpl(ComicRepository comicRepository) {

		this.comicRepository = comicRepository;
	}

	@Override
	public Optional<Comic> findById(Long id) {
		return comicRepository.findById(id);
	}

	@Override
	public List<Comic> list() {
		return IteratorUtils.toList( comicRepository.findAll().iterator() );
	}

	@Override
	public Page<Comic> list(Integer from, Integer pageSize, String sortBy, Sort.Direction ordering) {
		return comicRepository.findAll(
				PageRequest.of(from, pageSize,ordering, sortBy)
		);
	}


	@Override
	public List<Comic> findByName(String name) {
		return comicRepository.findByName( name );
	}

	@Override
	@Transactional
	public Comic create(Comic comic) {
		return comicRepository.save(comic);
	}

	@Override
	@Transactional
	public Comic update(Comic comic,Long id) {

		Optional<Comic> comic1 = comicRepository.findById(id);
		if (comic1.isPresent()) {
			if (!comic.getName().isEmpty())
				comic1.get().setName(comic.getName());
			if (!comic.getCharacters().isEmpty())
				comic1.get().setCharacters(comic.getCharacters());

		}else
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Character not found");
		return comicRepository.save(comic1.get());
	}

	@Override
	@Transactional
	public void delete(Long id) {
		comicRepository.deleteById(id);
	}
}
