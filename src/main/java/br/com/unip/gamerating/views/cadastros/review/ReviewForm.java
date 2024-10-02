package br.com.unip.gamerating.views.cadastros.review;


import java.io.Serial;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import br.com.unip.gamerating.controller.ReviewController;
import br.com.unip.gamerating.controller.UserController;
import br.com.unip.gamerating.exception.DefaultException;
import br.com.unip.gamerating.model.Platform;
import br.com.unip.gamerating.model.Review;
import br.com.unip.gamerating.model.User;
import br.com.unip.gamerating.views.MainView;
import br.com.unip.gamerating.views.cadastros.utils.GenericForm;
import jakarta.annotation.security.PermitAll;


@Route(value = "review", layout = MainView.class)
@PageTitle("Reviews | GameRating")
@PermitAll
public class ReviewForm extends GenericForm<Review, Long, ReviewController> implements HasUrlParameter<Long> {

	@Serial
	private static final long serialVersionUID = 1L;

	private TextField txtTitle;
	
	private TextField txtContent;
	
	private DatePicker.DatePickerI18n dtDateFormat;
	
	private DateTimePicker dtDate;
	
	private ComboBox<Platform> cbxPlatform;
	
	private ComboBox<User> cbxUser; 
	
	private IntegerField rating;
	
	private UserController userController;
	
	public ReviewForm(@Autowired ReviewController reviewController, @Autowired UserController userController) {
		super(Review.class, reviewController, new Review());
		setTitle("Reviews");
		setSuccessRoute("reviews");
		this.userController = userController;
		
		instanciarCampos();
		
		addInForm(cbxPlatform, 3, "platform");
		addInForm(txtTitle, 3, "title");
		addInForm(txtContent, 3, "content");
		addInForm(cbxUser, 3, "user");
		addInForm(dtDate, 3, "date");
		addInForm(rating, 2, "rating");
		
		btnSalvar.addClickListener(e -> salvar());
		createBinder();
	}

	private void instanciarCampos() {
		txtTitle = new TextField("Título");
		
		txtContent = new TextField("Conteúdo");	
		
		dtDateFormat = new DatePicker.DatePickerI18n();
		dtDateFormat.setDateFormat("dd/MM/yyyy");	
		dtDate = new DateTimePicker("Data");
		dtDate.setDatePickerI18n(dtDateFormat);	
		
		cbxPlatform= new ComboBox<Platform>("Plataforma");
		cbxPlatform.setItems(Platform.values());
		
		cbxUser = new ComboBox<User>("Usuário");
		cbxUser.setItems(userController.list());
		cbxUser.setItemLabelGenerator(User::getName);
		
		rating = new IntegerField("Pontuação");
		rating.setMin(1);
		rating.setMax(5);
		rating.setValue(1);
		rating.setStepButtonsVisible(true);
	}

	@Override
	public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
		try {

			if (parameter != null) {
				Review review = controller.load(parameter);
				if (review != null) {
					setBean(review);
				}
			} else {
				setBean(new Review());
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
	
	private void salvar() {
		try {
			binder.writeBean(currentBean);
			controller.save(currentBean);
			voltar();
		} catch (ValidationException e) {
			e.printStackTrace();
		}
	}
}