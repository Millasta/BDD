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

import org.hibernate.Hibernate;
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
		/*try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date dateNaissance = format.parse ( "1995-12-31" );  
			Date   dateExpi = format.parse ( "2019-12-31" );  
			//ControlerForfait.Creer("DEBUTANT", 12.34, (byte)10, (short)60);
			Creer("IENCLIT", "PAUL", "courriel@client.ca", "0123456789", dateNaissance, "mdpclient", "Adresse cool", "Visa", 123456789, dateExpi, "DEBUTANT");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}*/
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
	 * Initialise la liste des clients avec la bdd
	 */
	@SuppressWarnings("unchecked")
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
	 */
	@SuppressWarnings("unchecked")
	public void Creer(String nom, String prenom, String courriel, String telephone, Date dateNaissance, String motDePasse, String adresse, String typeCarte, long numeroCarte, Date dateExpiration, String typeForfait) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		UtilisateursId idUtilisateur = new UtilisateursId(nom, prenom);
		Utilisateurs utilisateur = new Utilisateurs(idUtilisateur, courriel, telephone, dateNaissance, motDePasse, adresse);
		ClientsId idClient = new ClientsId(utilisateur);
		Clients client = new Clients(idClient, ControlerForfait.Rechercher(typeForfait), typeCarte, numeroCarte, dateExpiration);
		
		ControlerForfait.Rechercher(typeForfait).getClientses().add(client);
		
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
	 */
	public void Modifier(String nom, String prenom, String courriel, String telephone, Date dateNaissance, String motDePasse, String adresse, String typeCarte, int numeroCarte, Date dateExpiration, String typeForfait) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Clients bdClient = (Clients) hbSession.createQuery("from Clients where Clients.Nom = :nom and Clients.Prenom = :prenom").setParameter("nom", nom).setParameter("prenom", prenom).list().iterator().next();
		Clients alClient = RechercherParNom(nom, prenom);
		
		bdClient.getId().getUtilisateurs().setCourriel(courriel);
		bdClient.getId().getUtilisateurs().setTelephone(telephone);
		bdClient.getId().getUtilisateurs().setNaissance(dateNaissance);
		bdClient.getId().getUtilisateurs().setMotdepasse(Integer.toString(motDePasse.hashCode()));
		bdClient.getId().getUtilisateurs().setAdresse(adresse);
		bdClient.setTypecarte(typeCarte);
		bdClient.setNumerocarte(numeroCarte);
		bdClient.setDateexpiration(dateExpiration);
		bdClient.setForfaits(ControlerForfait.Rechercher(typeForfait));
		
		alClient.getId().getUtilisateurs().setCourriel(courriel);
		alClient.getId().getUtilisateurs().setTelephone(telephone);
		alClient.getId().getUtilisateurs().setNaissance(dateNaissance);
		alClient.getId().getUtilisateurs().setMotdepasse(Integer.toString(motDePasse.hashCode()));
		alClient.getId().getUtilisateurs().setAdresse(adresse);
		alClient.setTypecarte(typeCarte);
		alClient.setNumerocarte(numeroCarte);
		alClient.setDateexpiration(dateExpiration);
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
			if(client.getId().getUtilisateurs().getMotdepasse().equals(Integer.toString(motDePass.hashCode())))
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
