package br.com.unip.gamerating.views.cadastros.utils;


import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.io.Serial;

public class NotificationComponent extends Div {

	@Serial
	private static final long serialVersionUID = 1L;

	public NotificationComponent(String message, String type, String position) {
		show(message, type, position);
	}

	public Notification show(String m, String t, String p) {

		Notification notification = new Notification();
		switch (t) {
		case "success":
			notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
			break;
		case "error":
			notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
			break;
		default:
			throw new IllegalArgumentException("Unexpected type: " + t);
		}

		Div text = new Div(new Text(m));

		Button closeButton = new Button(new Icon("lumo", "cross"));
		closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

		closeButton.addClickListener(event -> {
			notification.close();
		});

		HorizontalLayout layout = new HorizontalLayout(text, closeButton);
		layout.setAlignItems(Alignment.CENTER);

		notification.add(layout);
		notification.open();

		switch (p) {
		case "middle":
			notification.setPosition(Notification.Position.MIDDLE);
			break;
		case "top_start":
			notification.setPosition(Notification.Position.TOP_START);
			break;
		case "top_end":
			notification.setPosition(Notification.Position.TOP_END);
			break;
		default:
			notification.setPosition(Notification.Position.TOP_END);
		}

		return notification;

	}

}
