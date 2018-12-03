package Controler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import Model.Employes;
import Model.Utilisateurs;
import Model.UtilisateursId;

public class ControleEmploye {

	/**
	 * Liste des employes
	 */
	private ArrayList<Employes> employes;

	/**
	 * Construit le controler des employes
	 * Initialise la liste des employes avec la bdd
	 * Cree l'administrateur systeme
	 * @throws ParseException 
	 */
	public ControleEmploye() throws SQLException, ParseException
	{
		employes = new ArrayList<Employes>();
		Initialiser();
		CreerAdministrateur();
	}
	
	/**
	 * Getter de la liste des employes
	 * @return
	 */
	public ArrayList<Employes> getEmployes()
	{
		return employes;
	}	
	
	/**
	 * Initialise la liste des employes avec la bdd
	 */
	private void Initialiser()
	{	
		
		Session hbSession = HibernateUtil.DemarerTransaction();
		List<?> lesEmployes = hbSession.createQuery("FROM Employes").list();
		
		for(Iterator<?> iEmploye = lesEmployes.iterator(); iEmploye.hasNext();)
		{
			Employes e = (Employes) iEmploye.next();
			employes.add(e);
		}
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Cree l'administrateur systeme si il n'existe pas afin de pouvoir utiliser l'application au premier lancement
	 * @throws ParseException 
	 */
	private void CreerAdministrateur() throws SQLException, ParseException
	{
		if(Rechercher(0) == null)
		{
			Creer(0, "system", "admin", "admin.system@gmail.com", "confidentiel", "09/04/1996", "admin", "confidentiel");
		}
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
	 * Cree un nouvel employe
	 * @param matricule : le matricule du nouvel employe
	 * @param nom : le nom du nouvel employe
	 * @param prenom : le prenom du nouvel employe
	 * @param courriel : le courriel du nouvel employe
	 * @param telephone : le telephone du nouvel employe
	 * @param dateNaissance : la date de naissance du nouvel employe
	 * @param motDePasse : le mot de passe du nouvel employe
	 * @param adresse : l'adresse du nouvel employe
	 * @throws ParseException 
	 */
	public void Creer(int matricule, String nom, String prenom, String courriel, String telephone, String dateNaissance, String motDePasse, String adresse) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		UtilisateursId idUtilisateur = new UtilisateursId(nom, prenom);
		Utilisateurs utilisateur = new Utilisateurs(idUtilisateur, courriel, telephone, StringToDate(dateNaissance), motDePasse, adresse);
		Employes employe = new Employes(matricule, utilisateur);
		
		hbSession.save(utilisateur);
		hbSession.save(employe);
		employes.add(employe);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Modifie l'employe correspondant au matricule passe en argument
	 * @param matricule : le nouveau matricule de l'employe
	 * @param courriel : le nouveau courriel de l'employe
	 * @param telephone : le nouveau telephone de l'employe
	 * @param dateNaissance : la nouvelle date de naissance de l'employe
	 * @param motDePasse : le nouveau mot de passe de l'employe
	 * @param adresse : la nouvelle adresse de l'employe
	 * @throws ParseException 
	 */
	public void Modifier(int matricule, String courriel, String telephone, String dateNaissance, String motDePasse, String adresse) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Employes bdEmploye = (Employes) hbSession.createQuery("from Employes where Employes.Matricule = :id").setParameter("id", matricule).list().iterator().next();
		Employes alEmploye = Rechercher(matricule);
		
		bdEmploye.getUtilisateurs().setCourriel(courriel);
		bdEmploye.getUtilisateurs().setTelephone(telephone);
		bdEmploye.getUtilisateurs().setNaissance(StringToDate(dateNaissance));
		bdEmploye.getUtilisateurs().setMotdepasse(motDePasse);
		bdEmploye.getUtilisateurs().setAdresse(adresse);
		
		alEmploye.getUtilisateurs().setCourriel(courriel);
		alEmploye.getUtilisateurs().setTelephone(telephone);
		alEmploye.getUtilisateurs().setNaissance(StringToDate(dateNaissance));
		alEmploye.getUtilisateurs().setMotdepasse(motDePasse);
		alEmploye.getUtilisateurs().setAdresse(adresse);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Retourne l'employe correspondant au matricule passe en argument
	 * @param matricule : le matricule de l'employe rechercher
	 * @return Employe employe ou null
	 */
	public Employes Rechercher(int matricule)
	{
		Employes employe = null;
		
		for(Iterator<Employes> iEmploye = employes.iterator(); employe == null && iEmploye.hasNext();)
		{
			employe = iEmploye.next();
			if(employe.getMatricule() != matricule)
			{
				employe = null;
			}
		}
		
		return employe;
	}
	
	/**
	 * Permet la connexion d'un employe si celui-ci est reconnue
	 * @param matricule : le matricule de l'employe
	 * @param motDePasse : le mot de passe de l'employe
	 * @return boolean connexion
	 */
	public boolean SeConnecter(int matricule, String motDePasse)
	{
		boolean connexion = false;
		Employes employe = Rechercher(matricule);
		
		System.out.println("Emp : " + employe.getMatricule() + " mdp : " + employe.getUtilisateurs().getMotdepasse());

		if(employe != null)
		{
			if(employe.getUtilisateurs().getMotdepasse().equals(motDePasse))
			{
				connexion = true;
			}
		}
		
		return connexion;
	}
	
	/**
	 * Supprime l'employe correspondant au matricule passe en argument
	 * @param matricule : le matricule de l'employe
	 */
	public void Supprimer(int matricule) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Employes employe = (Employes) hbSession.createQuery("from Employes where Employes.Matricule = :id").setParameter("id", matricule).list().iterator().next();
		hbSession.delete(employe);
		employes.remove(Rechercher(matricule));
		
		HibernateUtil.RealiserTransaction();
	}
}
