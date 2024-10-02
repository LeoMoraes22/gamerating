package br.com.unip.gamerating.views.cadastros.utils;


import java.io.Serial;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.BeanValidationBinder;

public abstract class GenericForm<T, ID, C> extends VerticalLayout {

	@Serial
	private static final long serialVersionUID = 1L;
	
	protected BeanValidationBinder<T> binder;
	
	protected C controller;
	
	protected T currentBean;
	
	private FormLayout form;
	
	private String successRoute;
	
	private H2 title;
	
	protected Button btnSalvar;
	
	protected Button btnVoltar;

	public GenericForm(Class<T> beanClass, C controller, T bean) {
		this.controller = controller;

		setPadding(true);

		title = new H2("");
		title.getStyle().set("margin", "0");

		btnSalvar = new Button("Salvar");
		btnSalvar.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

		btnVoltar = new Button("Voltar");
		btnVoltar.getStyle().set("margin-left", "auto");
		btnVoltar.addClickListener(e -> voltar());

		form = new FormLayout();
		currentBean = bean;
		binder = new BeanValidationBinder<>(beanClass);

		configForm();

		add(definirCabecalho(), form);

	}

	private void configForm() {
		form.setWidthFull();
		form.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("320px", 12));
	}

	private HorizontalLayout definirCabecalho() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		horizontalLayout.setWidthFull();
		horizontalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		horizontalLayout.setAlignItems(Alignment.CENTER);

		horizontalLayout.add(title, btnVoltar, btnSalvar);
		return horizontalLayout;
	}

	protected void createBinder() {
		binder.readBean(currentBean);
		form.getChildren().forEach(e -> {
			if (e instanceof HasValue<?, ?> hasValue && e.getId().isPresent()) {
				binder.forField(hasValue).bind(e.getId().get());
			}
		});
	}

	protected void addInForm(Component component, Integer colSpan, String fieldName) {
		component.setId(fieldName);
		form.add(component, colSpan);
	}

	protected void setSuccessRoute(String successRoute) {
		this.successRoute = successRoute;
	}

	protected void setBean(T bean) {
		this.currentBean = bean;
	}

	protected T getCurrentBean() {
		return currentBean;
	}

	protected BeanValidationBinder<T> getBinder() {
		return this.binder;
	}

	protected void voltar() {
		getUI().ifPresent(ui -> ui.navigate(successRoute));
	}

	protected void setTitle(String title) {
		this.title.setText(title);
	}

}

