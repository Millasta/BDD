package Controler;

import java.sql.SQLException;
import java.text.ParseException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
	
	@FXML private PasswordField empPwdField;
	@FXML private PasswordField custPwdField;
	@FXML private TextField courrielField;
	@FXML private TextField matriculeField;
	
	@FXML private Label errCustConnectLabel;
	
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
		//TesterCommunicationBDD();
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
	 * Remplie la base de donnees avec des donnees de test
	 * @throws ParseException 
	 * @throws SQLException 
	 */
	private void TesterCommunicationBDD() throws SQLException, ParseException
	{		
		//Test location de film
		if(ControlerFilm.RechercherCopieLoue("Nom3", "Prenom3", "Venom") == null)
		{
			ControlerFilm.Louer("Nom3", "Prenom3", "Venom");
		}
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
			System.out.println("Customer " + courrielField.getText() + " try to connect..");
			if(ControlerClient.SeConnecter(courrielField.getText(), custPwdField.getText())) {
				System.out.println("Connexion réussie !");
				errCustConnectLabel.setVisible(false);
				custConnectPane.setVisible(false);
				searchFilmPane.setVisible(true);
			}
			else {
				System.out.println("Echec de la connexion !");
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
	}
	
	@FXML
	public void consultActeursPane() {
		searchFilmPane.setVisible(false);
		acteursPane.setVisible(true);
	}
	
	@FXML
	public void consultGenresPane() {
		searchFilmPane.setVisible(false);
		genresPane.setVisible(true);
	}
	
	@FXML
	public void consultPaysPane() {
		searchFilmPane.setVisible(false);
		paysPane.setVisible(true);
	}
	
	@FXML
	public void consultResumePane() {
		searchFilmPane.setVisible(false);
		resumePane.setVisible(true);
	}
	
	@FXML
	public void rent() {
		//TODO
	}
	
	@FXML
	public void backSearch() {
		errSearchPane.setVisible(false);
		scenarPane.setVisible(false);
		acteursPane.setVisible(false);
		genresPane.setVisible(false);
		paysPane.setVisible(false);
		resumePane.setVisible(false);
		searchFilmPane.setVisible(true);
	}
	
}









