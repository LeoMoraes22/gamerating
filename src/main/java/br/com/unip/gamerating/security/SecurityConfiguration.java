package br.com.unip.gamerating.security;


import java.util.Base64;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.JwsAlgorithms;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import br.com.unip.gamerating.views.cadastros.user.LoginView;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends VaadinWebSecurity{
	
	@Value("${jwt.auth.secret}")
	private String authSecret;

	@Autowired
	private UserService userService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.userDetailsService(userService)
	        .authorizeHttpRequests()  // Using authorizeRequests
	        .requestMatchers("/**")
	        .permitAll();

		super.configure(http);

		setLoginView(http, LoginView.class);

		setStatelessAuthentication(http, new SecretKeySpec(Base64.getDecoder().decode(authSecret), JwsAlgorithms.HS256),
				"com.example.application");
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// Customize your WebSecurity configuration.
		super.configure(web);
	}
	
}



