package Controler;

import java.sql.SQLException;
import java.util.ArrayList;

import Model.Films;
import Model.Personnes;
import Model.Rolesacteurs;
import Model.Scenaristes;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class ControlePrincipal {
	
	@FXML private AnchorPane mainPane;
	@FXML private AnchorPane welcomePane;
	@FXML private AnchorPane custConnectPane;
	@FXML private AnchorPane empConnectPane;
	@FXML private AnchorPane searchFilmPane;
	@FXML private AnchorPane rentFilmPane;
	@FXML private AnchorPane errSearchPane;
	@FXML private AnchorPane scenarPane;
	@FXML private AnchorPane acteursPane;
	@FXML private AnchorPane genresPane;
	@FXML private AnchorPane paysPane;
	@FXML private AnchorPane resumePane;
	@FXML private AnchorPane infoPersonnePane;
	
	@FXML private Button custButton;
	@FXML private Button empButton;
	@FXML private Button backEmpButton;
	@FXML private Button backCustButton;
	@FXML private Button errSearchButton;
	@FXML private Button backScenarButton;
	@FXML private Button backActeursButton;
	@FXML private Button backGenresButton;
	@FXML private Button backPaysButton;
	@FXML private Button backResumeButton;
	@FXML private Button deconnectButton;
	@FXML private Button searchButton;
	@FXML private Button backInfoButton;
	@FXML private Button scenarInfoButton;
	@FXML private Button acteurInfoButton;
	@FXML private Button rentButton;
	
	@FXML private PasswordField empPwdField;
	@FXML private PasswordField custPwdField;
	@FXML private TextField courrielField;
	@FXML private TextField matriculeField;
	@FXML private TextField titreField;
	@FXML private TextField annDeField;
	@FXML private TextField annAField;
	@FXML private TextField Field;
	@FXML private TextField paysField;
	@FXML private TextField langueField;
	@FXML private TextField genreField;
	@FXML private TextField scenarField;
	@FXML private TextField acteurField;
	
	@FXML private Label errCustConnectLabel;
	@FXML private Label errFilmLabel;
	@FXML private Label genresLabel;
	@FXML private Label paysLabel;
	@FXML private Label resumeLabel;
	@FXML private Label infoPersonneLabel;
	
	@FXML private TableView<Films> filmTable;
	@FXML private TableColumn<String, String> titreCol;
	@FXML private TableColumn<String, Integer> annCol;
	@FXML private TableColumn<String, String> langueCol;
	@FXML private TableColumn<String, Short> dureeCol;
	@FXML private TableColumn<String, Integer> copiesCol;
	
	@FXML private ListView<String> acteurList;
	@FXML private ListView<String> scenarList;
	
	/**
	 * Declaration des différents sous controlers
	 */
	private ControleForfait ControlerForfait = null;
	private ControleClient ControlerClient = null;
	private ControleEmploye ControlerEmploye = null;
	private ControleFilm ControlerFilm = null;
	
	/**
	 * Construit le controler dans sa totalite en créant les sous controlers
	 * @throws Exception, SQLException
	 */
	public ControlePrincipal() throws SQLException, Exception
	{
		ControlerForfait = new ControleForfait();
		ControlerClient = new ControleClient(ControlerForfait);
		ControlerFilm = new ControleFilm(ControlerClient);
		ControlerEmploye = new ControleEmploye();
	}
	
	/**
	 * Destructeur du controler
	 * Ferme la session factory de hibernate
	 */
	public void finalize()
	{
		HibernateUtil.shutdown();
	}

	/**
	 * Getter du controler des clients
	 * @return ControleClient ControlerClient
	 */
	public ControleClient getControlerClient()
	{
		return ControlerClient;
	}

	/**
	 * Getter du controler des employes
	 * @return ControleEmploye ControlerEmploye
	 */
	public ControleEmploye getControlerEmploye()
	{
		return ControlerEmploye;
	}

	/**
	 * Getter du controler des films
	 * @return ControleFilm ControlerFilm
	 */
	public ControleFilm getControlerFilm()
	{
		return ControlerFilm;
	}

	/**
	 * Getter du controler des forfaits
	 * @return ControleForfait ControlerForfait
	 */
	public ControleForfait getControlerForfait() 
	{
		return ControlerForfait;
	}
	
	@FXML
	public void custChoice() {
		welcomePane.setVisible(false);
		custConnectPane.setVisible(true);
	}
	
	@FXML
	public void empChoice() {
		welcomePane.setVisible(false);
		empConnectPane.setVisible(true);
	}
	
	@FXML
	public void backWelcome() {
		empConnectPane.setVisible(false);
		custConnectPane.setVisible(false);
		welcomePane.setVisible(true);
	}
	
	@FXML
	public void deconnect() {
		searchFilmPane.setVisible(false);
		welcomePane.setVisible(true);
	}
	
	@FXML
	public void custConnexion() {
		if(!courrielField.getText().isEmpty()) {
			if(ControlerClient.SeConnecter(courrielField.getText(), custPwdField.getText())) {
				errCustConnectLabel.setVisible(false);
				custConnectPane.setVisible(false);
				searchFilmPane.setVisible(true);
			}
			else {
				errCustConnectLabel.setVisible(true);
			}
		}
	}
	
	@FXML
	public void empConnexion() {
		if(!matriculeField.getText().isEmpty()) {
			System.out.println("Employe " + matriculeField.getText() + " try to connect..");
			if(ControlerEmploye.SeConnecter(Integer.valueOf(matriculeField.getText()), empPwdField.getText())) {
				System.out.println("Connexion réussie !");
			}
			else {
				System.out.println("Echec de la connexion !");
			}
		}
	}
	
	@FXML
	public void consultScenarPane() {
		searchFilmPane.setVisible(false);
		scenarPane.setVisible(true);
		ObservableList<String> items = FXCollections.observableArrayList();
		Films filmSelect = filmTable.getSelectionModel().getSelectedItem();
		for(Object p : filmSelect.getScenaristes()) {
			Scenaristes personne = (Scenaristes)p;
			items.add(new String(personne.getId().getPersonnes().getId().getNom() + " " + personne.getId().getPersonnes().getId().getPrenom()));
		}
		scenarList.setItems(items);
	}
	
	@FXML
	public void consultActeursPane() {
		searchFilmPane.setVisible(false);
		acteursPane.setVisible(true);
		Films filmSelect = filmTable.getSelectionModel().getSelectedItem();
		ObservableList<String> items = FXCollections.observableArrayList();
		for(Object ra : filmSelect.getRolesacteurses()) {
			Rolesacteurs roleActeur = (Rolesacteurs)ra;
			items.add(new String(roleActeur.getId().getPersonnes().getId().getNom() + " " + roleActeur.getId().getPersonnes().getId().getPrenom() + " : " + roleActeur.getNompersonnage()));
		}
		acteurList.setItems(items);
	}
	
	@FXML
	public void consultGenresPane() {
		searchFilmPane.setVisible(false);
		genresPane.setVisible(true);
		Films filmSelect = filmTable.getSelectionModel().getSelectedItem();
		genresLabel.setText(filmSelect.getGenres());
	}
	
	@FXML
	public void consultPaysPane() {
		searchFilmPane.setVisible(false);
		paysPane.setVisible(true);
		Films filmSelect = filmTable.getSelectionModel().getSelectedItem();
		paysLabel.setText(filmSelect.getPaysproduction());
	}
	
	@FXML
	public void consultResumePane() {
		searchFilmPane.setVisible(false);
		resumePane.setVisible(true);
		Films filmSelect = filmTable.getSelectionModel().getSelectedItem();
		resumeLabel.setText(filmSelect.getResume());
	}
	
	@FXML
	public void infoPersonneScenar() {
		String scenar = scenarList.getSelectionModel().getSelectedItem();
		if(scenar != null) {
			scenarPane.setVisible(false);
			infoPersonnePane.setVisible(true);
			String info = null;
			Films f = filmTable.getSelectionModel().getSelectedItem();
			for(Object o : f.getScenaristes()) {
				Scenaristes p = (Scenaristes)o;
				if(scenarList.getSelectionModel().getSelectedItem().equals(p.getId().getPersonnes().getId().getNom() + " " + p.getId().getPersonnes().getId().getPrenom()));
					info = new String(scenarList.getSelectionModel().getSelectedItem() + '\n'
						+ "Date de naissance : " + p.getId().getPersonnes().getNaissance() + '\n'
						+ "Lieu de naissance : " + p.getId().getPersonnes().getLieunaissance() + '\n'
						+ "Biographie : " + p.getId().getPersonnes().getBiographie());
			}
			infoPersonneLabel.setText(info);
		}
	}
	
	@FXML
	public void infoPersonneActeur() {
		String acteur = acteurList.getSelectionModel().getSelectedItem();
		if(acteur != null) {
			acteursPane.setVisible(false);
			infoPersonnePane.setVisible(true);
			String info = null;
			Films f = filmTable.getSelectionModel().getSelectedItem();
			for(Object o : f.getRolesacteurses()) {
				Personnes p = ((Rolesacteurs)o).getId().getPersonnes();
				if(acteurList.getSelectionModel().getSelectedItem().equals(p.getId().getNom() + " " + p.getId().getPrenom()));
					info = new String(acteurList.getSelectionModel().getSelectedItem() + '\n'
						+ "Date de naissance : " + p.getNaissance() + '\n'
						+ "Lieu de naissance : " + p.getLieunaissance() + '\n'
						+ "Biographie : " + p.getBiographie());
			}
			infoPersonneLabel.setText(info);
		}
	}
	
	@FXML
	public void rent() {
		/*if(filmTable.getSelectionModel().getSelectedItem() != null) {
			Films filmSelect = filmTable.getSelectionModel().getSelectedItem();
			Clients client = ControlerClient.RechercherParCourriel(courrielField.getText());
			try {
				ControlerFilm.Louer(client.getId().getUtilisateurs().getId().getNom(), client.getId().getUtilisateurs().getId().getPrenom(), filmSelect.getTitre());
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}*/
	}
	
	@FXML
	public void backSearch() {
		errSearchPane.setVisible(false);
		scenarPane.setVisible(false);
		acteursPane.setVisible(false);
		genresPane.setVisible(false);
		paysPane.setVisible(false);
		resumePane.setVisible(false);
		infoPersonnePane.setVisible(false);
		searchFilmPane.setVisible(true);
	}
	
	@FXML
	public void search() {
		
		filmTable.getItems().clear();
		
		// Initialisation des colonnes
		titreCol.setCellValueFactory(new PropertyValueFactory<String, String>("titre"));
		annCol.setCellValueFactory(new PropertyValueFactory<String, Integer>("annee"));
		langueCol.setCellValueFactory(new PropertyValueFactory<String, String>("langue"));
		dureeCol.setCellValueFactory(new PropertyValueFactory<String, Short>("duree"));
		copiesCol.setCellValueFactory(new PropertyValueFactory<String, Integer>("copies"));
		
		// Recherches séparées
		ArrayList<Films> filmsTitre = new ArrayList<Films>();
		ArrayList<Films> filmsAnn = new ArrayList<Films>();
		ArrayList<Films> filmsPays = new ArrayList<Films>();
		ArrayList<Films> filmsLangue = new ArrayList<Films>();
		ArrayList<Films> filmsGenre = new ArrayList<Films>();
		ArrayList<Films> filmsScenar = new ArrayList<Films>();
		ArrayList<Films> filmsActeur = new ArrayList<Films>();
		
		boolean erreur = false;
		boolean aucunCritere = true;
		
		int annDe = 0;
		int annA = 0;
		
		if(!titreField.getText().equals("")) {
			filmsTitre = ControlerFilm.RechercherFilmParTitre(titreField.getText());
			if(filmsTitre.size() == 0) erreur = true;
			aucunCritere = false;
		}
		
		if(!annDeField.getText().equals(""))
			annDe = Integer.valueOf(annDeField.getText());
		if(!annAField.getText().equals(""))
			annA = Integer.valueOf(annAField.getText());
		if(annDe > 0 || annA > 0) {
			if(annDe > annA) {
				annDe += annA;
				annA = annDe - annA;
				annDe -= annA;
			}
			
			filmsAnn = ControlerFilm.RechercherFilmParAnnee(annDe, annA);
			if(filmsAnn.size() == 0) erreur = true;
			aucunCritere = false;
		}
		
		if(!paysField.getText().equals("")) {
			filmsPays = ControlerFilm.RechercherFilmParPaysProduction(paysField.getText());
			if(filmsPays.size() == 0) erreur = true;
			aucunCritere = false;
		}
		
		if(!langueField.getText().equals("")) {
			filmsLangue = ControlerFilm.RechercherFilmParLangue(langueField.getText());
			if(filmsLangue.size() == 0) erreur = true;
			aucunCritere = false;
		}
		
		if(!genreField.getText().equals("")) {
			filmsGenre = ControlerFilm.RechercherFilmParGenre(genreField.getText());
			if(filmsGenre.size() == 0) erreur = true;
			aucunCritere = false;
		}
			
		if(!scenarField.getText().equals("")) {
			filmsScenar = ControlerFilm.RechercherFilmParScenariste(scenarField.getText());
			if(filmsScenar.size() == 0) erreur = true;
			aucunCritere = false;
		}
		
		if(!acteurField.getText().equals("")) {
			filmsActeur = ControlerFilm.RechercherFilmParActeur(acteurField.getText());
			if(filmsActeur.size() == 0) erreur = true;
			aucunCritere = false;
		}
		
		if(!erreur) {
			ArrayList<Films> filmsTrouves = new ArrayList<Films>();
			
			if(aucunCritere) {
				filmsTrouves = ControlerFilm.getFilms();
			}
			else {
				// Concaténation des recherches
				int nbCritere = 0;
				if(filmsTitre.size() > 0) nbCritere++;
				if(filmsAnn.size() > 0) nbCritere++;
				if(filmsPays.size() > 0) nbCritere++;
				if(filmsLangue.size() > 0) nbCritere++;
				if(filmsGenre.size() > 0) nbCritere++;
				if(filmsScenar.size() > 0) nbCritere++;
				if(filmsActeur.size() > 0) nbCritere++;
				
				ArrayList<Films> liste = new ArrayList<Films>();
				liste.addAll(filmsTitre);
				liste.addAll(filmsAnn);
				liste.addAll(filmsPays);
				liste.addAll(filmsLangue);
				liste.addAll(filmsGenre);
				liste.addAll(filmsScenar);
				liste.addAll(filmsActeur);
				
				for(Films f : liste) {
					if(filmCount(liste, f) == nbCritere)
						filmsTrouves.add(f);
				}
			}
			
			for(Films f : filmsTrouves)
				f.setCopies(f.getCopieses().size());
			filmTable.getItems().addAll(filmsTrouves);
			errFilmLabel.setVisible(false);
		}
		else
			errFilmLabel.setVisible(true);
	}
	
	/**
	 * Compte le nombre d'itération d'un film spécifique dans une liste de films.
	 * @param list de films
	 * @param film à compter
	 * @return le nombre d'itérations trouvées
	 */
	private int filmCount(ArrayList<Films> liste, Films film) {
		int count = 0;
		
		for(Films f : liste)
			if(f == film)
				count++;
		return count;
	}
	
}









