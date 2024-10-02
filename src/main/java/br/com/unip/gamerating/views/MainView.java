package br.com.unip.gamerating.views;

import java.io.Serial;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;

import br.com.unip.gamerating.model.User;
import br.com.unip.gamerating.security.SecurityService;
import br.com.unip.gamerating.views.cadastros.review.ReviewGrid;
import br.com.unip.gamerating.views.cadastros.user.UserGrid;

public class MainView extends AppLayout {

    @Serial
	private static final long serialVersionUID = 1L;

    public MainView(@Autowired SecurityService securityService) {
        DrawerToggle toggle = new DrawerToggle();
       
        H1 title = new H1("GameRating");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = getTabs();
        
        Button btnPainel = new Button("Painel");
        
        Optional<User> userOptional = securityService.getCurrentUser();
        
        if(userOptional.isPresent()) {
        	btnPainel = new Button(userOptional.get().getName(), 
        			new Icon(VaadinIcon.USER));
        	btnPainel.getStyle().set("margin", "0 10px 0 10px");
        	btnPainel.addClickListener(e -> {
        		getUI().ifPresent(ui -> ui.navigate("user/" + userOptional.get().getUsername()));
        	});
        	
        }
        
        Button btnLogout = new Button("Logout");
        btnLogout.addClickListener(e -> {
        	securityService.logout();
        });
        
        Button btnHome = new Button(new Icon(VaadinIcon.HOME));
        btnHome.addClickListener(e -> {
        	getUI().ifPresent(ui -> ui.navigate("/"));
        });
        
        HorizontalLayout layout = new HorizontalLayout(toggle, title);
        layout.setAlignItems(Alignment.CENTER);
        
        HorizontalLayout botoes = new HorizontalLayout(btnHome, btnPainel, btnLogout);
        botoes.setAlignItems(Alignment.CENTER);
             
        HorizontalLayout horizontalLayout = new HorizontalLayout(layout, botoes);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setWidthFull();
               
        
        addToDrawer(tabs);
        addToNavbar(horizontalLayout);
    }

	private Tabs getTabs() {
        Tabs tabs = new Tabs();
        
    	tabs.add(createTab(VaadinIcon.USER, "User", UserGrid.class),
       		 createTab(VaadinIcon.FILE_TEXT, "Review", ReviewGrid.class)
       		 );

                
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }
    
    private Tab createTab(VaadinIcon viewIcon, String viewName, Class viewClass) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        
        link.setRoute(viewClass);
        link.setTabIndex(-1);

        return new Tab(link);
    }

}
