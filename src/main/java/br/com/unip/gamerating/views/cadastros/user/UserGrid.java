package br.com.unip.gamerating.views.cadastros.user;

import java.io.Serial;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.com.unip.gamerating.controller.UserController;
import br.com.unip.gamerating.model.User;
import br.com.unip.gamerating.views.MainView;
import br.com.unip.gamerating.views.cadastros.utils.GenericGrid;
import jakarta.annotation.security.PermitAll;

@Route(value = "users", layout = MainView.class)
@PageTitle("Users")
@PermitAll
public class UserGrid extends GenericGrid<User, String, UserController> {

	@Serial
	private static final long serialVersionUID = 1L;

	public UserGrid(@Autowired UserController controller) {
		super(controller, User.class, User::getUsername);
		setTitle("Users");
		setRotaForm("user");
		configuraGrid();
		atualizaGrid();
		search.addValueChangeListener(this::searchValueChange); 
		btnDelete.addClickListener(e -> deletaRegistro(controller, User.class));
	}

	private void configuraGrid() { 
        getGrid().addColumn(user -> user.getUsername() != null ? user.getUsername(): "").setHeader("Usuário");
        getGrid().addColumn(user -> user.getName() != null ? user.getName(): "").setHeader("Nome"); 
    }
	
	protected void atualizaGrid() {
		getGrid().setItems(controller.list());
	}
	
	private void searchValueChange(HasValue.ValueChangeEvent<String> event) {
    	String filter = event.getValue();
		List<User> filtereds = controller.list().stream().filter(item -> item.toString().contains(filter)).collect(Collectors.toList());
		refreshList(filtereds);
    }
	
	private void deletaRegistro(UserController controller2, Class<User> entityClass) {
		this.controller = controller2;
		Set<User> item = getGrid().getSelectedItems();
		
		if (!item.isEmpty()) {
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.setHeader("Deletar item");
			confirmDialog.setText("Deseja realmente deletar o item selecionado?");
			confirmDialog.setCancelable(true);
			confirmDialog.setConfirmText("Deletar");
			confirmDialog.addConfirmListener(e -> {
				for (User selectedT : item) {
					controller2.delete(selectedT);
				UI.getCurrent().getPage().reload();
				}	
			});
			confirmDialog.open();
		}
	}
}
