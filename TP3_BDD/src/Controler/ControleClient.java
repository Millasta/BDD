package Controler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import Model.Clients;
import Model.ClientsId;
import Model.Copies;
import Model.Utilisateurs;
import Model.UtilisateursId;

public class ControleClient {

	/**
	 * Liste des clients
	 */
	private ArrayList<Clients> clients;
	
	/**
	 * Controler des forfaits
	 */
	private ControleForfait ControlerForfait;

	/**
	 * Construit le controler des clients
	 * Initialise la liste des clients avec la bdd
	 * @param controlerForfait : le controler des forfaits
	 */
	public ControleClient(ControleForfait controlerForfait)
	{
		clients = new ArrayList<Clients>();
		ControlerForfait = controlerForfait;
		Initialiser();
	}
	
	/**
	 * Getter de la liste des forfaits
	 * @return
	 */
	public ArrayList<Clients> getClients()
	{
		return clients;
	}
	
	/**
	 * Parse un string en date
	 * @param date : le string a transformer
	 * @return Date parsedDate
	 * @throws ParseException
	 */
	private Date StringToDate(String date) throws ParseException
	{
		Date parsedDate = null;
		
		SimpleDateFormat format = new SimpleDateFormat("dd/mm/yyyy");
		parsedDate = format.parse(date);
		
		return parsedDate;
	}
	
	/**
	 * Initialise la liste des clients avec la bdd
	 */
	private void Initialiser()
	{		
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Clients client = null;
		List<?> lesClients = hbSession.createQuery("from Clients").list();
		System.out.println("Nb clients : " + lesClients.size());
		
		for(Iterator<?> iClient = (Iterator<?>) lesClients.iterator(); iClient.hasNext();)
		{
			client = (Clients) iClient.next();
			clients.add(client);
			ControlerForfait.Rechercher(client.getForfaits().getTypeforfait()).getClientses().add(client);
		}
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Cree un nouveau client
	 * @param nom : le nom du nouveau client
	 * @param prenom : le prenom du nouveau client
	 * @param courriel : le courriel du nouveau client
	 * @param telephone : le telephone du nouveau client
	 * @param dateNaissance : la date de naissance du nouveau client
	 * @param motDePass : le mot de passe du nouveau client
	 * @param adresse : l'adresse du nouveau client
	 * @param typeCarte : le type de carte de credit du nouveau client
	 * @param numeroCarte : le numero de carte de credit du nouveau client
	 * @param dateExpiration : la date d'expiration de la carte de credit du nouveau client
	 * @param typeForfait : le type de forfait du nouveau client
	 * @throws ParseException 
	 */
	public void Creer(String nom, String prenom, String courriel, String telephone, String dateNaissance, String motDePasse, String adresse, String typeCarte, long numeroCarte, String dateExpiration, String typeForfait) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		UtilisateursId idUtilisateur = new UtilisateursId(nom, prenom);
		Utilisateurs utilisateur = new Utilisateurs(idUtilisateur, courriel, telephone, StringToDate(dateNaissance), motDePasse, adresse);
		ClientsId idClient = new ClientsId(utilisateur);
		Clients client = new Clients(idClient, ControlerForfait.Rechercher(typeForfait), typeCarte, numeroCarte, StringToDate(dateNaissance));
		
		ControlerForfait.Rechercher(typeForfait).getClientses().add(client);
		System.out.println("allo***********************************************************************************");
		hbSession.save(utilisateur);
		hbSession.save(client);
		clients.add(client);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Modifie le client correspondant aux nom et prenom passe en argument
	 * @param nom : le nom du client a modifier
	 * @param prenom : le prenom du client a modifier
	 * @param courriel : le nouveau courriel du client
	 * @param telephone : le nouveau telephone du client
	 * @param dateNaissance : la nouvelle date de naissance du client
	 * @param motDePass : le nouveau mot de passe du client
	 * @param adresse : la nouvelle adresse du client
	 * @param typeCarte : le nouveau type de carte de credit du client
	 * @param numeroCarte : le nouveau numero de carte de credit du client
	 * @param dateExpiration : la nouvelle date d'expiration de la carte de credit du client
	 * @param typeForfait : le nouveau type de forfait du client
	 * @throws ParseException 
	 */
	public void Modifier(String nom, String prenom, String courriel, String telephone, String dateNaissance, String motDePasse, String adresse, String typeCarte, int numeroCarte, String dateExpiration, String typeForfait) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Clients bdClient = (Clients) hbSession.createQuery("from Clients where Clients.Nom = :nom and Clients.Prenom = :prenom").setParameter("nom", nom).setParameter("prenom", prenom).list().iterator().next();
		Clients alClient = RechercherParNom(nom, prenom);
		
		bdClient.getId().getUtilisateurs().setCourriel(courriel);
		bdClient.getId().getUtilisateurs().setTelephone(telephone);
		bdClient.getId().getUtilisateurs().setNaissance(StringToDate(dateNaissance));
		bdClient.getId().getUtilisateurs().setMotdepasse(motDePasse);
		bdClient.getId().getUtilisateurs().setAdresse(adresse);
		bdClient.setTypecarte(typeCarte);
		bdClient.setNumerocarte(numeroCarte);
		bdClient.setDateexpiration(StringToDate(dateNaissance));
		bdClient.setForfaits(ControlerForfait.Rechercher(typeForfait));
		
		alClient.getId().getUtilisateurs().setCourriel(courriel);
		alClient.getId().getUtilisateurs().setTelephone(telephone);
		alClient.getId().getUtilisateurs().setNaissance(StringToDate(dateNaissance));
		alClient.getId().getUtilisateurs().setMotdepasse(motDePasse);
		alClient.getId().getUtilisateurs().setAdresse(adresse);
		alClient.setTypecarte(typeCarte);
		alClient.setNumerocarte(numeroCarte);
		alClient.setDateexpiration(StringToDate(dateNaissance));
		alClient.setForfaits(ControlerForfait.Rechercher(typeForfait));
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Retourne le client correspondant au courriel passe en argument
	 * @param courriel : le courriel du client a rechercher
	 * @return Client client ou null
	 */
	public Clients RechercherParCourriel(String courriel)
	{
		Clients client = null;
		
		for(Iterator<Clients> iClient = clients.iterator(); client == null && iClient.hasNext();)
		{
			client = iClient.next();
			if(!client.getId().getUtilisateurs().getCourriel().equals(courriel))
			{
				client = null;
			}
		}
		
		return client;
	}
	
	/**
	 * Retourne le client correspondant aux nom et prenom passe en argument
	 * @param nom : le nom du client rechercher
	 * @param prenom : le prenom du client rechercher
	 * @return Client client ou null
	 */
	public Clients RechercherParNom(String nom, String prenom)
	{
		Clients client = null;
		
		for(Iterator<Clients> iClient = clients.iterator(); client == null && iClient.hasNext();)
		{
			client = iClient.next();
			if(!client.getId().getUtilisateurs().getId().getNom().equals(nom) && !client.getId().getUtilisateurs().getId().getPrenom().equals(prenom))
			{
				client = null;
			}
		}
		
		return client;
	}
	
	/**
	 * Permet la connexion d'un client si celui-ci est reconnue
	 * @param courriel : le courriel du client
	 * @param motDePass : le mot de passe du client
	 * @return boolean connexion
	 */
	public boolean SeConnecter(String courriel, String motDePass)
	{
		boolean connexion = false;
		Clients client = RechercherParCourriel(courriel);
		
		if(client != null)
		{
			if(client.getId().getUtilisateurs().getMotdepasse().equals(motDePass))
			{
				connexion = true;
			}
		}
		
		return connexion;
	}
	
	/**
	 * Retourne le retard par rapport aux dates passe en argument
	 * @param dateLocation : la date de location
	 * @param date : la date d'aujourd'hui
	 * @return int retard
	 */
	public int CalculerRetard(Date dateLocation, Date date)
	{
		int retard = 0;
		
		// A refaire avec type DATE
		
		/*int diffAnnee = Integer.parseInt(date.substring(0, 4)) - Integer.parseInt(dateLocation.substring(0, 4));
		int diffMois = Integer.parseInt(date.substring(5, 7)) - Integer.parseInt(dateLocation.substring(5, 7));
		int diffJour = Integer.parseInt(date.substring(8, 10)) - Integer.parseInt(dateLocation.substring(8, 10));
		
		retard = diffJour + diffMois * 30 + diffAnnee * 365;
		
		if(retard < 0)
		{
			retard = 0;
		}*/
		
		return retard;
	}
	
	/**
	 * Retourne la liste des copies en retard
	 * @param nom : le nom du client
	 * @param prenom : le prenom du client
	 * @return ArrayList<Copie> enRetard
	 */
	public ArrayList<Copies> ConsulterRetard(String nom, String prenom)
	{
		Clients client = RechercherParNom(nom, prenom);
		ArrayList<Copies> enRetard = new ArrayList<Copies>();
		Copies tmpCopie = null;
		
		for(Iterator<?> iCopie = (Iterator<?>) client.getCopieses().iterator(); iCopie.hasNext();)
		{
			tmpCopie = (Copies) iCopie.next();
			
			if(CalculerRetard(tmpCopie.getDateLocation(), Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())) > client.getForfaits().getDureemax())
			{
				enRetard.add(tmpCopie);
			}
		}
		
		return enRetard;
	}
	
	/**
	 * Supprime le client correspondant aux nom et prenom passe en argument
	 * @param nom : le nom du client
	 * @param prenom : le prenom du client
	 */
	public void Supprimer(String nom, String prenom) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Clients client = (Clients) hbSession.createQuery("from Clients where Clients.Nom = :nom and Clients.Prenom = :prenom").setParameter("nom", nom).setParameter("prenom", prenom).list().iterator().next();
		hbSession.delete(client);
		clients.remove(RechercherParNom(nom, prenom));
		
		HibernateUtil.RealiserTransaction();
	}
}
