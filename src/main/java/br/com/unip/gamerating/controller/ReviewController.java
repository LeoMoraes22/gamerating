package br.com.unip.gamerating.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unip.gamerating.exception.DefaultException;
import br.com.unip.gamerating.model.Review;
import br.com.unip.gamerating.repository.ReviewRepository;

@Service
public class ReviewController {

	@Autowired
	private ReviewRepository repository;

	public Review save(Review entity) {
		return repository.save(entity);
	}

	public Review update(Review entity) {
		return repository.save(entity);
	}

	public void delete(Review entity) {
		repository.delete(entity);
	}

	public List<Review> list() {
		return repository.findAll();
	}

	public Review load(Long id) throws DefaultException {
		return repository.findById(id).orElseThrow(() -> new DefaultException("Não existe!"));
	}
}
