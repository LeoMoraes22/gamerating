package br.com.unip.gamerating.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.unip.gamerating.model.User;
import br.com.unip.gamerating.repository.UserRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = repository
				.findById(username)
				.orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

		return org.springframework.security.core.userdetails.User
				.withUsername(user.getUsername())
				.password("{bcrypt}" + user.getPassword())
				.build();
	}
}
