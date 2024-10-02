package br.com.unip.gamerating.views.cadastros.user;


import java.io.Serial;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import br.com.unip.gamerating.controller.UserController;
import br.com.unip.gamerating.model.User;
import br.com.unip.gamerating.views.PrincipalView;
import br.com.unip.gamerating.views.cadastros.utils.NotificationComponent;


@Route(value = "/cadastrar", layout = PrincipalView.class)
@PageTitle("GameRating | Cadastro")
@AnonymousAllowed
public class RegisterView extends VerticalLayout{

	@Serial
    private static final long serialVersionUID = 1L;

    private final Binder<User> binder = new Binder<>(User.class);

    User currentBean;

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public RegisterView(@Autowired UserController controller) {

        currentBean = new User();

        setAlignItems(Alignment.CENTER);
        Div loginContainer = new Div();
        loginContainer.setMaxWidth("500px");

        TextField username = new TextField("Username");
        username.setWidthFull();

        PasswordField password = new PasswordField("Password");
        password.setWidthFull();

        Button registerButton = new Button("Cadastrar");
        registerButton.getStyle().set("margin-top", "15px");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.setWidthFull();

        TextField name = new TextField("Nome");
        name.setWidthFull();


        binder.readBean(currentBean);
        binder.forField(name).asRequired("Nome é obrigatório").bind("name");
        binder.forField(username).asRequired("Username é obrigatório").bind("username");
        binder.forField(password).asRequired("Password é obrigatório").bind("password");


        registerButton.addClickListener(event -> {

            NotificationComponent notification = null;

            try {

                binder.writeBean(currentBean);
                currentBean.setPassword(PASSWORD_ENCODER.encode(currentBean.getPassword()));
                controller.save(currentBean);

                notification = new NotificationComponent("Cadastro realizado com sucesso",
                        "success", "top_end");
                add(notification);

                String rota = "/user/" + currentBean.getUsername();

                getUI().ifPresent(ui -> ui.navigate(rota));
            } catch (ValidationException e) {

                notification = new NotificationComponent("Preencha todos os campos", "error",
                        "top_end");

                add(notification);
                e.printStackTrace();
            } catch (Exception e) {
                notification = new NotificationComponent(e.getMessage(), "error", "top_end");

                add(notification);
                e.printStackTrace();
            }

        });

        loginContainer.add(username, name, password, registerButton);

        add(loginContainer);

    }
}

