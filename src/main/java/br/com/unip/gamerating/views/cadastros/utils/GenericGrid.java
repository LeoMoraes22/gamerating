package br.com.unip.gamerating.views.cadastros.utils;


import java.io.Serial;
import java.util.List;
import java.util.function.Function;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

public abstract class GenericGrid<T, ID, C> extends VerticalLayout {
	
	@Serial
	private static final long serialVersionUID = 1L;
	
	protected Function<T, ID> idProvider;
	
	protected C controller;
	
	private Grid<T> grid;
	
	private String formRoute;
	
	private H2 h2Title;
	
	protected Button btnNovo;
	
	protected Button btnDelete;
	
	protected Button btnPrint;
	
	protected TextField search;
	
	public GenericGrid(C controller, Class<T> entityClass, Function<T, ID> idProvider){
		this.controller = controller;
		this.idProvider = idProvider;
		
		setPadding(true);
		
		grid = new Grid<>(entityClass, false);
		grid.setSelectionMode(Grid.SelectionMode.MULTI);
		
//		refreshList();
		
		grid.addItemClickListener(e -> {
			T item = e.getItem();
			ID itemId = idProvider.apply(item);
			getUI().ifPresent(ui -> ui.navigate(formRoute + "/" + itemId));
		});
		
		h2Title = new H2("");
		h2Title.getStyle().set("margin", "0");
		
		btnNovo = new Button("Novo", VaadinIcon.PLUS.create());
		btnNovo.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		btnNovo.addClickListener(e -> adicionarNovo());
		
		btnDelete = new Button("Delete", VaadinIcon.TRASH.create());
        btnDelete.addThemeVariants(ButtonVariant.LUMO_ERROR);
//        btnDelete.addClickListener(e -> deletaRegistro(controller, entityClass));
        
        btnPrint = new Button("Imprimir", VaadinIcon.FILE_TEXT.create());
        btnPrint.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        btnPrint.setVisible(false);
        
        search = new TextField();
		search.setPlaceholder("Search");
		search.setPrefixComponent(VaadinIcon.SEARCH.create());
//		search.addValueChangeListener(this::searchValueChange);        
		
		add(configurarCabecalho(), grid);
	}

	private HorizontalLayout configurarCabecalho() {
		HorizontalLayout horizontalLayout = new HorizontalLayout();
		HorizontalLayout botoes = new HorizontalLayout(btnNovo, btnDelete, btnPrint);
		botoes.setSpacing(true);
		horizontalLayout.setWidthFull();
		horizontalLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
		horizontalLayout.setAlignItems(Alignment.CENTER);
		horizontalLayout.add(h2Title,search, botoes);
		
		return horizontalLayout;
	}
	
	protected void setTitle(String title) {
		this.h2Title.setText(title);
	}
	
	protected void setRotaForm(String route) {		
		this.formRoute = route;
	}
	
//	protected void refreshList() {
//		grid.setItems(controller.list());
//	}
	
    protected void refreshList(List<T> items) {
        grid.setItems(items);
    }
    
//    private void searchValueChange(HasValue.ValueChangeEvent<String> event) {
//    	String filter = event.getValue();
//    	try {
//    		List<T> filtereds = controller.list().stream().filter(item -> item.toString().contains(filter)).collect(Collectors.toList());
//			refreshList(filtereds);
//		} catch (DefaultException e) {
//			e.printStackTrace();
//		}
//    }
	
	protected Grid<T> getGrid() {
		return grid;
	}
	
	protected String getSearch() {
		return search.getValue();
	}
	
	private void adicionarNovo() {
		getUI().ifPresent(ui -> ui.navigate(formRoute));
	}
	
	public void setPrintVisible(boolean enable) {
		btnPrint.setVisible(enable);
	}
	
//	private void deletaRegistro(C controller2, Class<T> entityClass) {
//		this.controller = controller2;
//		Set<T> item = grid.getSelectedItems();
//		
//		if (!item.isEmpty()) {
//			ConfirmDialog confirmDialog = new ConfirmDialog();
//			confirmDialog.setHeader("Deletar item");
//			confirmDialog.setText("Deseja realmente deletar o item selecionado?");
//			confirmDialog.setCancelable(true);
//			confirmDialog.setConfirmText("Deletar");
//			confirmDialog.addConfirmListener(e -> {
//				try {
//					for (T selectedT : item) {
//						controller2.delete(selectedT);
//					}
//					UI.getCurrent().getPage().reload();
//				} catch (DefaultException ex) {
//					ex.printStackTrace();
//				}
//			});
//			confirmDialog.open();
//		}
//	}
}

