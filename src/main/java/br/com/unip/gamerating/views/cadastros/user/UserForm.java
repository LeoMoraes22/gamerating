package br.com.unip.gamerating.views.cadastros.user;

import java.io.Serial;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.com.unip.gamerating.controller.UserController;
import br.com.unip.gamerating.exception.DefaultException;
import br.com.unip.gamerating.model.User;
import br.com.unip.gamerating.views.MainView;
import br.com.unip.gamerating.views.cadastros.utils.GenericForm;
import jakarta.annotation.security.PermitAll;

@Route(value = "user", layout = MainView.class)
@PageTitle("Users")
@PermitAll
public class UserForm extends GenericForm<User, String, UserController> implements HasUrlParameter<String>{

	@Serial
	private static final long serialVersionUID = 1L;

	private TextField txtUsername;
	
	private PasswordField txtPassword;
	
	private TextField txtName;
	
	public UserForm(@Autowired UserController controller) {
		super(User.class, controller, new User());
		setTitle("Users");
		setSuccessRoute("users");
		
		instanciarCampos();
		
		btnSalvar.addClickListener(e -> salvar());
		
		addInForm(txtUsername, 3, "username");
		addInForm(txtPassword, 3, "password");
		addInForm(txtName, 3, "name");
		
		createBinder();
	}

	private void salvar() {
		try {
			binder.writeBean(currentBean);
			controller.save(currentBean);
			voltar();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}

	private void instanciarCampos() {
		txtUsername = new TextField("Username");
		
		txtPassword = new PasswordField("Password");
		
		txtName = new TextField("Name");
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
		try {
			if (parameter != null) {
				User user = controller.load(parameter);
				if (user != null) {
					setBean(user);
					if (getCurrentBean().getUsername() != null) {
						txtUsername.setReadOnly(true);
						txtPassword.setReadOnly(true);
					}
				}
			}
		} catch (DefaultException e) {
			mostrarAviso();
		}
		createBinder();
	}

	private void mostrarAviso() {
		ConfirmDialog dialogMessage = new ConfirmDialog();
		dialogMessage.setHeader("Registro não encontrado");
		dialogMessage.setText(new Html("<p>Clique no botão para visualizar todos os registros</p>"));

		dialogMessage.setConfirmText("Redirecionar");

		dialogMessage.addConfirmListener(ev -> {
			voltar();
			dialogMessage.close();
		});
		dialogMessage.open();
	}
}
