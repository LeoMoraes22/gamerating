package br.com.unip.gamerating.views.cadastros.review;

import java.io.Serial;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import br.com.unip.gamerating.controller.ReviewController;
import br.com.unip.gamerating.model.Review;
import br.com.unip.gamerating.views.MainView;
import br.com.unip.gamerating.views.cadastros.utils.GenericGrid;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Route(value = "reviews", layout = MainView.class)
@PageTitle("Reviews | GameRating")
@AnonymousAllowed
public class ReviewGrid extends GenericGrid<Review, Long, ReviewController> {
	
	
	@Serial
	private static final long serialVersionUID = 1L;

	public ReviewGrid(@Autowired ReviewController reviewController) {
		super(reviewController, Review.class, Review::getId);
		setTitle("Reviews");
		setRotaForm("review");
		configurarGrid();
		atualizaGrid();
		search.addValueChangeListener(this::searchValueChange); 
		btnDelete.addClickListener(e -> deletaRegistro(controller, Review.class));
		setPrintVisible(true);
		btnPrint.addClickListener(e -> {
			imprimirRelatorio();
		});
	}

	private void imprimirRelatorio() {
		try {
	        // Obtenha a lista de reviews do DataProvider
	        List<Review> reviews = getGrid().getDataProvider().fetch(new Query<>()).toList();

	        // Compila o arquivo .jrxml para um objeto JasperReport
	        JasperReport jasperReport = JasperCompileManager.compileReport(getClass().getResourceAsStream("/jrxml/reviews.jrxml"));

	        // Cria a fonte de dados a partir da lista de reviews
	        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reviews);

	        // Preenche o relatório com a lista de dados (sem usar conexão com banco)
	        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, new HashMap<>(), dataSource);

	        // Exporta o relatório preenchido para um byte array em formato PDF
	        byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
	        
	        // Converte o byte[] em uma string Base64
	        String base64 = Base64.encodeBase64String(bytes);
	        
	        // Gera o JavaScript para abrir o PDF embutido no navegador
	        String js = "var byteCharacters = atob('" + base64 + "');"
	                  + "var byteNumbers = new Array(byteCharacters.length);"
	                  + "for (var i = 0; i < byteCharacters.length; i++) {"
	                  + "    byteNumbers[i] = byteCharacters.charCodeAt(i);"
	                  + "}"
	                  + "var byteArray = new Uint8Array(byteNumbers);"
	                  + "var blob = new Blob([byteArray], {type: 'application/pdf'});"
	                  + "var url = URL.createObjectURL(blob);"
	                  + "window.open(url);";
	        
	        // Executa o JavaScript para abrir o PDF no Vaadin
	        UI.getCurrent().getPage().executeJs(js);
	        
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	private void configurarGrid() {
        getGrid().addColumn(Review::getId).setHeader("Id");
        getGrid().addColumn(Review::getTitle).setHeader("Título");
        getGrid().addColumn(Review::getContent).setHeader("Conteúdo");
        getGrid().addColumn(review -> DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).format(review.getDate())).setHeader("Data");
        getGrid().addColumn(Review::getPlatform).setHeader("Plataforma");
        getGrid().addColumn(Review::getRating).setHeader("Pontuação");
        getGrid().addColumn(review -> review.getUser() != null ? review.getUser().getName() : "").setHeader("Usuário");
        
    }
	
	protected void atualizaGrid() {
		getGrid().setItems(controller.list());
	}
	
	private void searchValueChange(HasValue.ValueChangeEvent<String> event) {
    	String filter = event.getValue();
		List<Review> filtereds = controller.list().stream().filter(item -> item.toString().contains(filter)).collect(Collectors.toList());
		refreshList(filtereds);
    }
	
	private void deletaRegistro(ReviewController controller2, Class<Review> entityClass) {
		this.controller = controller2;
		Set<Review> item = getGrid().getSelectedItems();
		
		if (!item.isEmpty()) {
			ConfirmDialog confirmDialog = new ConfirmDialog();
			confirmDialog.setHeader("Deletar item");
			confirmDialog.setText("Deseja realmente deletar o item selecionado?");
			confirmDialog.setCancelable(true);
			confirmDialog.setConfirmText("Deletar");
			confirmDialog.addConfirmListener(e -> {
				for (Review selectedT : item) {
					controller2.delete(selectedT);
				UI.getCurrent().getPage().reload();
				}	
			});
			confirmDialog.open();
		}
	}

}
