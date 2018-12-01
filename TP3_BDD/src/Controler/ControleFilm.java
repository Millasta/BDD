package Controler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.hibernate.Session;

import Model.Clients;
import Model.Copies;
import Model.Films;
import Model.Personnes;
import Model.PersonnesId;
import Model.Rolesacteurs;
import Model.RolesacteursId;


public class ControleFilm {

	/**
	 * Listes des films, acteurs, scenaristes et copies
	 */
	private ArrayList<Films> Films;
	private ArrayList<Rolesacteurs> RolesActeurs;
	private ArrayList<Personnes> Scenaristes;
	private ArrayList<Copies> Copies;
	
	/**
	 * Controler des clients
	 */
	private ControleClient ControlerClient;

	/**
	 * Construit le controler des films
	 * Initialise les listes avec la bdd
	 * @param controlerClient : le controler des clients
	 */
	public ControleFilm(ControleClient controlerClient)
	{
		Films = new ArrayList<Films>();
		RolesActeurs = new ArrayList<Rolesacteurs>();
		Scenaristes = new ArrayList<Personnes>();
		Copies = new ArrayList<Copies>();
		ControlerClient = controlerClient;
		Initialiser();
	}
	
	/**
	 * Getter de la liste des films
	 * @return ArrayList<Film> Films
	 */
	public ArrayList<Films> getFilms() 
	{
		return Films;
	}
	
	/**
	 * Initialise la liste des films, acteurs, scenaristes et copies
	 */
	private void Initialiser()
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Films film = null;
		List<?> lesFilms = hbSession.createQuery("from Films").list();
		
		for(Iterator<?> iFilm = lesFilms.iterator(); iFilm.hasNext();)
		{
			film = (Films) iFilm.next();
			Films.add(film);
			
			for(Iterator<?> iPersonne = (Iterator<?>) film.getPersonneses().iterator(); iPersonne.hasNext();)
			{
				Scenaristes.add((Personnes) iPersonne.next());
			}
			
			for(Iterator<?> iPersonne = (Iterator<?>) film.getRolesacteurses().iterator(); iPersonne.hasNext();)
			{
				RolesActeurs.add((Rolesacteurs) iPersonne.next());
			}
			
			for(Iterator<?> iCopie = (Iterator<?>) film.getCopieses().iterator(); iCopie.hasNext();)
			{
				Copies.add((Copies) iCopie.next());
			}
		}
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Cree un nouveau film
	 * @param titre : le titre du nouveau film
	 * @param annee : l'annee du nouveau film
	 * @param duree : la duree du nouveau film
	 * @param langues : les langues du nouveau film
	 * @param resume : le resume du nouveau film
	 * @param genres : les genres du nouveau film
	 * @param paysProductions : les pays de productions du nouveau film
	 * @param nbCopie : le nombre de copie du nouveau film
	 */
	public void CreerFilm(String titre, int annee, short duree, String langues, String resume, String genres, String paysProduction, int nbCopie) throws Exception, SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Films film = new Films(titre, annee, duree, langues, resume, genres, paysProduction);

		if(nbCopie > 0)
		{
			for(int iterator = 0; iterator <= nbCopie; iterator++)
			{
				film.getCopieses().add(new Copies(Copies.size() + 1, film));
			}
		}
		else
		{
			throw new Exception("Un nouveau film doit avoir au moins une copie");
		}
		
		hbSession.save(film);
		Films.add(film);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Cree un nouvel acteur
	 * @param titreFilm : le titre du film dans lequel joue le nouvel acteur
	 * @param nom : le nom du nouvel acteur
	 * @param prenom : le prenom du nouvel acteur
	 * @param dateNaissance : la date de naissance du nouvel acteur
	 * @param lieuNaissance : le lieu de naissance du nouvel acteur
	 * @param biographie : la biographie du nouvel acteur
	 * @param role : rôle qu'il joue dans le film
	 */
	public void CreerRolesActeur(String titreFilm, String nom, String prenom, Date dateNaissance, String lieuNaissance, String biographie, String role) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		Films film = RechercherFilm(titreFilm);
		PersonnesId idPersonne = new PersonnesId(nom, prenom);
		Personnes personne = new Personnes(idPersonne, dateNaissance, lieuNaissance, biographie);
		RolesacteursId idActeur = new RolesacteursId(personne, film); 
		Rolesacteurs acteur = new Rolesacteurs(idActeur, role);
		
		RechercherFilm(titreFilm).getRolesacteurses().add(acteur);
		
		hbSession.save(acteur);
		RolesActeurs.add(acteur);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Cree un nouveau scenariste
	 * @param titreFilm : le titre du film scenarise par le scenariste
	 * @param nom : le nom du nouveau scenariste
	 * @param prenom : le prenom du nouveau scenariste
	 * @param dateNaissance : la date de naissance du nouveau scenariste
	 * @param lieuNaissance : le lieu de naissance du nouveau scenariste
	 * @param biographie : la biographie du nouveau scenariste
	 */
	public void CreerScenariste(String titreFilm, String nom, String prenom, Date dateNaissance, String lieuNaissance, String biographie) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		PersonnesId idPersonne = new PersonnesId(nom, prenom);
		Personnes scenariste = new Personnes(idPersonne, dateNaissance, lieuNaissance, biographie);
	
		RechercherFilm(titreFilm).getPersonneses().add(scenariste);
		
		hbSession.save(scenariste);
		Scenaristes.add(scenariste);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Modifie le film correspondant au titre passe en argument
	 * @param titre : le titre du film a modifier
	 * @param annee : la nouvelle annee du film
	 * @param duree : la nouvelle duree du film
	 * @param langues : les nouvelles langues du film
	 * @param resume : le nouveau resume du film
	 * @param genres : les nouveaux genres du film
	 * @param paysProductions : les nouveaux pays de productions du film
	 */
	public void ModifierFilm(String titre, int annee, short duree, String langue, String resume, String genres, String paysProduction) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Films bdFilm = (Films) hbSession.createQuery("from Films where Films.Titre = :titre").setParameter("titre", titre).list().iterator().next();
		Films alFilm = RechercherFilm(titre);
		
		bdFilm.setAnnee(annee);
		bdFilm.setDuree(duree);
		bdFilm.setLangue(langue);
		bdFilm.setResume(resume);
		bdFilm.setGenres(genres);
		bdFilm.setPaysproduction(paysProduction);
		
		alFilm.setAnnee(annee);
		alFilm.setDuree(duree);
		alFilm.setLangue(langue);
		alFilm.setResume(resume);
		alFilm.setGenres(genres);
		alFilm.setPaysproduction(paysProduction);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Modifie l'acteur correspondant aux nom et prenom passeen argument
	 * @param nom : le nom de l'acteur recherche
	 * @param prenom : le prenom de l'acteur recherche
	 * @param titreFilm : le titre du film dans lequel l'acteur joue
	 * @param dateNaissance : la nouvelle date de naissance de l'acteur
	 * @param lieuNaissance : le nouveau lieu de naissance de l'acteur
	 * @param biographie : la nouvelle biographie de l'acteur
	 */
	public void ModifierRolesActeur(String nom, String prenom, String titreFilm, Date dateNaissance, String lieuNaissance, String biographie) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Rolesacteurs bdActeur = (Rolesacteurs) hbSession.createQuery("from RolesActeurs ra where ra.Nom = :nom and ra.Prenom = :prenom and ra.TitreFilm = :titre").setParameter("nom", nom).setParameter("prenom", prenom).setParameter("titre", titreFilm).list().iterator().next();
		Rolesacteurs alActeur = RechercherActeur(nom, prenom);
		
		bdActeur.getId().getPersonnes().setNaissance(dateNaissance);
		bdActeur.getId().getPersonnes().setLieunaissance(lieuNaissance);
		bdActeur.getId().getPersonnes().setBiographie(biographie);
		
		alActeur.getId().getPersonnes().setNaissance(dateNaissance);
		alActeur.getId().getPersonnes().setLieunaissance(lieuNaissance);
		alActeur.getId().getPersonnes().setBiographie(biographie);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Modifie le scenariste correspondant aux nom et prenom passe en argument
	 * @param nom : le nom du scenariste recherche
	 * @param prenom : le prenom du scenariste recherche
	 * @param titreFilm : le titre du film scenarise par le scenariste
	 * @param dateNaissance : la nouvelle date de naissance du scenariste
	 * @param lieuNaissance : le nouveau lieu de naissance du scenariste
	 * @param biographie : la nouvelle biographie du scenariste
	 */
	public void ModifierScenariste(String nom, String prenom, String titreFilm, Date dateNaissance, String lieuNaissance, String biographie) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Personnes bdScenariste = (Personnes) hbSession.createQuery("from Scenaristes rea where rea.Nom = :nom and rea.Prenom = :prenom and rea.TitreFilm = :titre").setParameter("nom", nom).setParameter("prenom", prenom).setParameter("titre", titreFilm).list().iterator().next();
		Personnes alScenariste = RechercherScenariste(nom, prenom);
		
		bdScenariste.setNaissance(dateNaissance);
		bdScenariste.setLieunaissance(lieuNaissance);
		bdScenariste.setBiographie(biographie);
		
		alScenariste.setNaissance(dateNaissance);
		alScenariste.setLieunaissance(lieuNaissance);
		alScenariste.setBiographie(biographie);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Permet au client demandant de louer le film demande
	 * @param nom : le nom du client demandant
	 * @param prenom : le prenom du client demandant
	 * @param titre : le titre du film demande
	 * @return boolean louable
	 */
	public boolean Louer(String nom, String prenom, String titre) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Copies bdALouer = null;
		
		boolean louable = false;
		Clients loueur = ControlerClient.RechercherParNom(nom, prenom);
		Copies alALouer = PeutEtreLouer(titre);
		
		if(alALouer != null && loueur.getCopieses().size() < loueur.getForfaits().getLocationmax())
		{
			bdALouer = (Copies) hbSession.createQuery("from Copies where Copies.NumeroCopie = :num").setParameter("num", alALouer.getNumerocopie()).list().iterator().next();
			
			bdALouer.setClients(loueur);
			
			alALouer.setClients(loueur);
			loueur.getCopieses().add(alALouer);
			louable = true;
		}
		
		HibernateUtil.RealiserTransaction();
		
		return louable;
	}
	
	/**
	 * Permet au client demandant de rendre un film
	 * @param nom : le nom du client demandant
	 * @param prenom : le prenom du client demandant
	 * @param titre : le titre du film a rendre
	 */
	public void Rendre(String nom, String prenom, String titre) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Clients loueur = ControlerClient.RechercherParNom(nom, prenom);
		Copies alARendre = RechercherCopieLoue(nom, prenom, titre);
		
		Copies bdARendre = (Copies) hbSession.createQuery("from Copies where Copies.NumeroCopie = :num").setParameter("num", alARendre.getNumerocopie()).list().iterator().next();
		
		bdARendre.setClients(null);
		
		alARendre.setClients(null);
		loueur.getCopieses().remove(alARendre);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Retourne le film correspondant au titre passe en argument
	 * @param titre : le titre du film
	 * @return Film film
	 */
	public Films RechercherFilm(String titre)
	{
		Films film = null;
		
		for(Iterator<Films> iFilm = Films.iterator(); film == null && iFilm.hasNext();)
		{
			film = iFilm.next();
			if(!film.getTitre().equals(titre))
			{
				film = null;
			}
		}
		
		return film;
	}	
	
	/**
	 * Retourne la liste des films dont le titre contient la séquence passe en argument
	 * @param partTitre : la séquence a rechercher
	 * @return ArrayList<Film> resultats
	 */
	public ArrayList<Films> RechercherFilmParTitre(String partTitre)
	{
		ArrayList<Films> resultats = new ArrayList<Films>();
		Films tmpFilm = null;
		
		for(Iterator<Films> iFilm = Films.iterator(); iFilm.hasNext();)
		{
			tmpFilm = iFilm.next();
			if(tmpFilm.getTitre().contains(partTitre))
			{
				resultats.add(tmpFilm);
			}
		}
		
		return resultats;
	}
	
	/**
	 * Retourne la liste des films dont l'annee de parution est comprise entre les annees passe en argument
	 * @param AnneeMin : l'annee minimal de recherche
	 * @param AnneeMax : l'annee maximal de recherche
	 * @return ArrayList<Film> resultats
	 */
	public ArrayList<Films> RechercherFilmParAnnee(int AnneeMin, int AnneeMax)
	{
		ArrayList<Films> resultats = new ArrayList<Films>();
		Films tmpFilm = null;
		
		for(Iterator<Films> iFilm = Films.iterator(); iFilm.hasNext();)
		{
			tmpFilm = iFilm.next();
			if(tmpFilm.getAnnee() >= AnneeMin && tmpFilm.getAnnee() <= AnneeMax)
			{
				resultats.add(tmpFilm);
			}
		}
		
		return resultats;
	}
	
	/**
	 * Retourne la liste des films qui ont un pays de production correspondant au pays passe en argument
	 * @param paysProd : le pays a rechercher
	 * @return ArrayList<Film> resultats
	 */
	public ArrayList<Films> RechercherFilmParPaysProduction(String paysProd)
	{
		ArrayList<Films> resultats = new ArrayList<Films>();
		Films tmpFilm = null;
		
		for(Iterator<Films> iFilm = Films.iterator(); iFilm.hasNext();)
		{
			tmpFilm = iFilm.next();
			if(tmpFilm.getPaysproduction().contains(paysProd))
			{
				resultats.add(tmpFilm);
			}
		}
		
		return resultats;
	}
	
	/**
	 * Retourne la liste des films dont la langue correspond a la langue passe en argument
	 * @param langue : la langue a rechercher
	 * @return ArrayList<Film> resultats
	 */
	public ArrayList<Films> RechercherFilmParLangue(String langue)
	{
		ArrayList<Films> resultats = new ArrayList<Films>();
		Films tmpFilm = null;
		
		for(Iterator<Films> iFilm = Films.iterator(); iFilm.hasNext();)
		{
			tmpFilm = iFilm.next();
			if(tmpFilm.getPaysproduction().equals(langue))
			{
				resultats.add(tmpFilm);
			}
		}
		
		return resultats;
	}
	
	/**
	 * Retourne la liste des films qui ont un genre correspondant au genre passe en argument
	 * @param genre : le genre a rechercher
	 * @return ArrayList<Film> resultats
	 */
	public ArrayList<Films> RechercherFilmParGenre(String genre)
	{
		ArrayList<Films> resultats = new ArrayList<Films>();
		Films tmpFilm = null;
		
		for(Iterator<Films> iFilm = Films.iterator(); iFilm.hasNext();)
		{
			tmpFilm = iFilm.next();
			if(tmpFilm.getPaysproduction().contains(genre))
			{
				resultats.add(tmpFilm);
			}
		}
		
		return resultats;
	}
	
	/**
	 * Retourne la liste des films dont le nom du scenariste contient la chaine passe en argument passe en argument
	 * @param partNomScenariste : la chaine a rechercher
	 * @return ArrayList<Film> resultats
	 */
	public ArrayList<Films> RechercherFilmParScenariste(String partNomScenariste)
	{
		ArrayList<Films> resultats = new ArrayList<Films>();
		Films tmpFilm = null;
		String nom = null;
		
		for(Iterator<Films> iFilm = Films.iterator(); iFilm.hasNext();)
		{
			tmpFilm = iFilm.next();
			nom = new String(((Personnes) tmpFilm.getPersonneses().iterator().next()).getId().getPrenom() + " " + ((Personnes) tmpFilm.getPersonneses().iterator().next()).getId().getNom());
			if(nom.contains(partNomScenariste))
			{
				resultats.add(tmpFilm);
			}
		}
		
		return resultats;
	}
	
	/**
	 * Retourne la liste des films dont le nom d'un acteur contient la chaine passe en argument passe en argument
	 * @param partNomActeur : la chaine a rechercher
	 * @return ArrayList<Film> resultats
	 */
	public ArrayList<Films> RechercherFilmParActeur(String partNomActeur)
	{
		ArrayList<Films> resultats = new ArrayList<Films>();
		Films tmpFilm = null;
		String nom = null;
		
		for(Iterator<Films> iFilm = Films.iterator(); iFilm.hasNext();)
		{
			tmpFilm = iFilm.next();
			for(Iterator<?> iActeur = (Iterator<?>) tmpFilm.getRolesacteurses().iterator(); nom == null && iActeur.hasNext();)
			{
				nom = new String(((Rolesacteurs) iActeur.next()).getId().getPersonnes().getId().getPrenom() + " " + ((Rolesacteurs) iActeur.next()).getId().getPersonnes().getId().getNom());
				
				if(nom.contains(partNomActeur))
				{
					resultats.add(tmpFilm);
				}
				else
				{
					nom = null;
				}
			}
		}
		
		return resultats;
	}
	
	/**
	 * Retourne l'acteur correspondant aux nom et prenom passe en argument
	 * @param nom : le nom de l'acteur
	 * @param prenom : le prenom de l'acteur
	 * @return RolesActeur acteur
	 */
	public Rolesacteurs RechercherActeur(String nom, String prenom)
	{
		Rolesacteurs acteur = null;
		
		for(Iterator<Rolesacteurs> iActeur = RolesActeurs.iterator(); acteur == null && iActeur.hasNext();)
		{
			acteur = iActeur.next();
			if(!acteur.getId().getPersonnes().getId().getNom().equals(nom) && !acteur.getId().getPersonnes().getId().getPrenom().equals(prenom))
			{
				acteur = null;
			}
		}
		
		return acteur;
	}
	
	/**
	 * Retourne le scenariste correspondant aux nom et prenom passe en argument
	 * @param nom : le nom du scenariste
	 * @param prenom : le prenom du scenariste
	 * @return Personnes scenariste
	 */
	public Personnes RechercherScenariste(String nom, String prenom)
	{
		Personnes scenariste = null;
		
		for(Iterator<Personnes> iScenariste = Scenaristes.iterator(); scenariste == null && iScenariste.hasNext();)
		{
			scenariste = iScenariste.next();
			if(!scenariste.getId().getNom().equals(nom) && !scenariste.getId().getPrenom().equals(prenom))
			{
				scenariste = null;
			}
		}
		
		return scenariste;
	}
	
	/**
	 * Retourn la copie loue par le client correspondant aux nom et prenom passe en argument
	 * @param nom : le nom du client
	 * @param prenom : le prenom du client
	 * @param titre : le titre du film loue
	 * @return Copie copie
	 */
	public Copies RechercherCopieLoue(String nom, String prenom, String titre)
	{
		Copies copie = null;
		
		for(Iterator<?> iCopie = (Iterator<?>) ControlerClient.RechercherParNom(nom, prenom).getCopieses(); copie == null && iCopie.hasNext();)
		{
			copie = (Copies) iCopie.next();
			if(!copie.getFilms().getTitre().equals(titre))
			{
				copie = null;
			}
		}
		
		return copie;
	}
	
	/**
	 * Retourne une copie pouvant etre loue poue le film demande
	 * @param titre : le titre du film demande
	 * @return Copie louable
	 */
	private Copies PeutEtreLouer(String titre)
	{
		Copies louable = null;
		Films film = RechercherFilm(titre);
		
		for(Iterator<?> iCopie = (Iterator<?>) film.getCopieses().iterator(); louable == null && iCopie.hasNext();)
		{
			louable = (Copies) iCopie.next();
			if(louable.getClients() != null)
			{
				louable = null;
			}
		}
		
		return louable;
	}
	
	/**
	 * Supprime le film correspondant au titre passe en argument
	 * @param titre : le titre du film
	 */
	public void SupprimerFilm(String titre) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Films film = (Films) hbSession.createQuery("from Films where Films.TitreFilm = :titre").setParameter("titre", titre).list().iterator().next();
		hbSession.delete(film);
		Films.remove(RechercherFilm(titre));
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Supprime l'acteur correspondant aux nom et prenom passe en argument
	 * @param nom : le nom de l'acteur
	 * @param prenom : le prenom de l'acteur
	 */
	public void SupprimerRolesActeur(String nom, String prenom) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Rolesacteurs acteur = (Rolesacteurs) hbSession.createQuery("from RolesActeurs ra where ra.Nom = :nom and ra.Prenom = :prenom").setParameter("nom", nom).setParameter("prenom", prenom).list().iterator().next();
		hbSession.delete(acteur);
		RolesActeurs.remove(RechercherActeur(nom, prenom));
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	  * Supprime le scenariste correspondant aux nom et prenom passe en argument
	 * @param nom : le nom du scenariste
	 * @param prenom : le prenom du scenariste
	 */
	public void SupprimerScenariste(String nom, String prenom) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Personnes scenariste = (Personnes) hbSession.createQuery("from Scenaristes rea where rea.Nom = :nom and rea.Prenom = :prenom").setParameter("nom", nom).setParameter("prenom", prenom).list().iterator().next();
		hbSession.delete(scenariste);
		Scenaristes.remove(RechercherScenariste(nom, prenom));
		
		HibernateUtil.RealiserTransaction();
	}
}
