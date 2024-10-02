package br.com.unip.gamerating.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.unip.gamerating.exception.DefaultException;
import br.com.unip.gamerating.model.User;
import br.com.unip.gamerating.repository.UserRepository;

@Service
public class UserController {
	
	@Autowired
	private UserRepository repository;

	public User save(User entity) {
		return repository.save(entity);
	}
	
	public User update(User entity) {
		return repository.save(entity);
	}
	
	public void delete(User entity) {
		repository.delete(entity);
	}
	
	public List<User> list() {
		return repository.findAll();
	}
	
	public User load(String id) throws DefaultException {
		return repository.findById(id).orElseThrow(() -> new DefaultException("Não existe!"));
	}
}