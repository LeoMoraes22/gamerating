package br.com.unip.gamerating.security;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;

import br.com.unip.gamerating.model.User;
import br.com.unip.gamerating.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

@Component
public class SecurityService {

    private static final String LOGOUT_SUCCESS_URL = "/";

    @Autowired
    private UserRepository userRepository;

    private Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication())
                .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken));
    }

    public Optional<User> getCurrentUser() {
        Optional<Authentication> authentication = getAuthentication();

        return authentication.flatMap(value -> userRepository.findById(value.getName()));
    }

    public boolean isUserAuthenticated() {
        Optional<Authentication> authentication = getAuthentication();
        return authentication.isPresent() && !(authentication.get() instanceof AnonymousAuthenticationToken);
    }

    public void logout() {
        UI.getCurrent().getPage().setLocation(LOGOUT_SUCCESS_URL);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);
    }

}
