package Controler;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Session;

import Model.Clients;
import Model.Copies;
import Model.Films;
import Model.Personnes;
import Model.PersonnesId;
import Model.Rolesacteurs;
import Model.RolesacteursId;
import Model.ScenaristesId;
import Model.Scenaristes;


public class ControleFilm {

	/**
	 * Listes des films, acteurs, scenaristes et copies
	 */
	private ArrayList<Films> Films;
	private ArrayList<Rolesacteurs> RolesActeurs;
	private ArrayList<Scenaristes> Scenaristes;
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
		Scenaristes = new ArrayList<Scenaristes>();
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
		
		List<?> lesFilms = hbSession.createQuery("select f.titre, f.annee, f.duree, f.langue, f.resume, f.genres, f.paysproduction from Films f").list();
		
		for(Iterator<?> iFilm = lesFilms.iterator(); iFilm.hasNext();)
		{
			Object[] film = (Object[]) iFilm.next();
			Films f = new Films((String) film[0], (int) film[1], (short) film[2], (String) film[3], (String) film[4], (String) film[5], (String) film[6]);
			
			//TODO : faire comme les acteurs pour pouvoir permettre la location sans rechargement des clients
			List<?> lesScenaristes = hbSession.createQuery("select rea.id.personnes.id.nom, rea.id.personnes.id.prenom, rea.id.personnes.naissance, rea.id.personnes.lieunaissance, rea.id.personnes.biographie from Scenaristes rea where rea.id.films.titre = :titre").setParameter("titre", f.getTitre()).list();
			
			for(Iterator<?> iScenariste = (Iterator<?>) lesScenaristes.iterator(); iScenariste.hasNext();)
			{
				Object[] scenariste = (Object[]) iScenariste.next();
				
				PersonnesId pId = new PersonnesId((String) scenariste[0], (String) scenariste[1]);
				Personnes p = new Personnes(pId, (Date) scenariste[2], (String) scenariste[3], (String) scenariste[4]);
				ScenaristesId reaId = new ScenaristesId(p, f);
				Scenaristes rea = new Scenaristes(reaId);
				
				p.getFilmses().add(f);
				f.getScenaristes().add(rea);
				Scenaristes.add(rea);
			}
			
			List<?> lesActeurs = hbSession.createQuery("select ra.id.personnes.id.nom, ra.id.personnes.id.prenom, ra.id.personnes.naissance, ra.id.personnes.lieunaissance, ra.id.personnes.biographie, ra.nompersonnage from Rolesacteurs ra where ra.id.films.titre = :titre").setParameter("titre", f.getTitre()).list();
			
			for(Iterator<?> iActeur = (Iterator<?>) lesActeurs.iterator(); iActeur.hasNext();)
			{
				Object[] acteur = (Object[]) iActeur.next();
				
				PersonnesId pId = new PersonnesId((String) acteur[0], (String) acteur[1]);
				Personnes p = new Personnes(pId, (Date) acteur[2], (String) acteur[3], (String) acteur[4]);
				RolesacteursId raId = new RolesacteursId(p, f);
				Rolesacteurs ra = new Rolesacteurs(raId, (String) acteur[5]);
				
				p.getFilmses().add(f);
				f.getRolesacteurses().add(ra);
				RolesActeurs.add(ra);
			}
			
			List<?> lesCopies = hbSession.createQuery("select c.numerocopie, c.clients.id.utilisateurs.id.nom, c.clients.id.utilisateurs.id.prenom, c.datelocation from Copies c where c.films.titre = :titre").setParameter("titre", f.getTitre()).list();
			
			for(Iterator<?> iCopie = (Iterator<?>) lesCopies.iterator(); iCopie.hasNext();)
			{
				Object[] copie = (Object[]) iCopie.next();
				Copies c = new Copies((int) copie[0], f);
				
				Clients locataire = ControlerClient.RechercherParNom((String) copie[1], (String) copie[2]);
				
				if(locataire != null)
				{
					c.setClients(locataire);
					c.setDatelocation((Date) copie[3]);
				}
				
				f.getCopieses().add(c);
				Copies.add(c);
			}
			
			Films.add(f);
		}
		
		HibernateUtil.RealiserTransaction();
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
	 * Cree un nouveau film
	 * @param titre : le titre du nouveau film
	 * @param annee : l'annee du nouveau film
	 * @param duree : la duree du nouveau film
	 * @param langues : les langues du nouveau film
	 * @param resume : le resume du nouveau film
	 * @param genres : les genres du nouveau film
	 * @param paysProductions : les pays de productions du nouveau film
	 * @param nbCopie : le nombre de copie du nouveau film
	 * @throws Exception, SQLException
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
	 * @param role : r�le qu'il joue dans le film
	 * @throws SQLException, ParseException
	 */
	public void CreerRolesActeur(String titreFilm, String nom, String prenom, String dateNaissance, String lieuNaissance, String biographie, String role) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		Films film = RechercherFilm(titreFilm);
		PersonnesId idPersonne = new PersonnesId(nom, prenom);
		Personnes personne = new Personnes(idPersonne, StringToDate(dateNaissance), lieuNaissance, biographie);
		RolesacteursId idActeur = new RolesacteursId(personne, film); 
		Rolesacteurs acteur = new Rolesacteurs(idActeur, role);
		
		RechercherFilm(titreFilm).getRolesacteurses().add(acteur);
		
		hbSession.save(personne);
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
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public void CreerScenariste(String titreFilm, String nom, String prenom, String dateNaissance, String lieuNaissance, String biographie) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		PersonnesId idPersonne = new PersonnesId(nom, prenom);
		Personnes personne = new Personnes(idPersonne, StringToDate(dateNaissance), lieuNaissance, biographie);
		Films film = RechercherFilm(titreFilm);
		ScenaristesId idScenariste = new ScenaristesId(personne, film);
		Scenaristes scenariste = new Scenaristes(idScenariste);
	
		film.getScenaristes().add(scenariste);
		
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
	 * @throws SQLException
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
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public void ModifierRolesActeur(String nom, String prenom, String titreFilm, String dateNaissance, String lieuNaissance, String biographie) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Rolesacteurs bdActeur = (Rolesacteurs) hbSession.createQuery("from RolesActeurs ra where ra.Nom = :nom and ra.Prenom = :prenom and ra.TitreFilm = :titre").setParameter("nom", nom).setParameter("prenom", prenom).setParameter("titre", titreFilm).list().iterator().next();
		Rolesacteurs alActeur = RechercherActeur(nom, prenom);
		
		bdActeur.getId().getPersonnes().setNaissance(StringToDate(dateNaissance));
		bdActeur.getId().getPersonnes().setLieunaissance(lieuNaissance);
		bdActeur.getId().getPersonnes().setBiographie(biographie);
		
		alActeur.getId().getPersonnes().setNaissance(StringToDate(dateNaissance));
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
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public void ModifierScenariste(String nom, String prenom, String titreFilm, String dateNaissance, String lieuNaissance, String biographie) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Scenaristes bdScenariste = (Scenaristes) hbSession.createQuery("from Scenaristes rea where rea.Nom = :nom and rea.Prenom = :prenom and rea.TitreFilm = :titre").setParameter("nom", nom).setParameter("prenom", prenom).setParameter("titre", titreFilm).list().iterator().next();
		Scenaristes alScenariste = RechercherScenariste(nom, prenom);
		
		bdScenariste.getId().getPersonnes().setNaissance(StringToDate(dateNaissance));
		bdScenariste.getId().getPersonnes().setLieunaissance(lieuNaissance);
		bdScenariste.getId().getPersonnes().setBiographie(biographie);
		
		alScenariste.getId().getPersonnes().setNaissance(StringToDate(dateNaissance));
		alScenariste.getId().getPersonnes().setLieunaissance(lieuNaissance);
		alScenariste.getId().getPersonnes().setBiographie(biographie);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Permet au client demandant de louer le film demande
	 * @param nom : le nom du client demandant
	 * @param prenom : le prenom du client demandant
	 * @param titre : le titre du film demande
	 * @return boolean louable
	 * @throws SQLException
	 * @throws ParseException 
	 */
	public boolean Louer(String nom, String prenom, String titre) throws SQLException, ParseException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Copies bdALouer = null;
		
		boolean louable = false;
		Clients loueur = ControlerClient.RechercherParNom(nom, prenom);
		Copies alALouer = PeutEtreLouer(titre);
		
		if(alALouer != null && loueur.getCopieses().size() < loueur.getForfaits().getLocationmax())
		{
			bdALouer = (Copies) hbSession.createQuery("from Copies c where c.numerocopie = :num").setParameter("num", alALouer.getNumerocopie()).list().iterator().next();
			
			bdALouer.setClients(loueur);
			bdALouer.setDatelocation(new SimpleDateFormat("yyyy-mm-dd").parse(LocalDate.now().toString()));
			
			alALouer.setClients(loueur);
			alALouer.setDatelocation(new SimpleDateFormat("yyyy-mm-dd").parse(LocalDate.now().toString()));
			
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
	 * @throws SQLException
	 */
	public void Rendre(String nom, String prenom, String titre) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Clients loueur = ControlerClient.RechercherParNom(nom, prenom);
		Copies alARendre = RechercherCopieLoue(nom, prenom, titre);
		
		Copies bdARendre = (Copies) hbSession.createQuery("from Copies where Copies.NumeroCopie = :num").setParameter("num", alARendre.getNumerocopie()).list().iterator().next();
		
		bdARendre.setClients(null);
		bdARendre.setDatelocation(null);
		
		alARendre.setClients(null);
		alARendre.setDatelocation(null);
		
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
	 * Retourne la liste des films dont le titre contient la s�quence passe en argument
	 * @param partTitre : la s�quence a rechercher
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
			nom = new String(((Scenaristes) tmpFilm.getScenaristes()).getId().getPersonnes().getId().getPrenom() + " " + ((Scenaristes) tmpFilm.getScenaristes()).getId().getPersonnes().getId().getNom());
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
	public Scenaristes RechercherScenariste(String nom, String prenom)
	{
		Scenaristes scenariste = null;
		
		for(Iterator<Scenaristes> iScenariste = Scenaristes.iterator(); scenariste == null && iScenariste.hasNext();)
		{
			scenariste = iScenariste.next();
			if(!scenariste.getId().getPersonnes().getId().getNom().equals(nom) && !scenariste.getId().getPersonnes().getId().getPrenom().equals(prenom))
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
		
		for(Iterator<?> iCopie = (Iterator<?>) ControlerClient.RechercherParNom(nom, prenom).getCopieses().iterator(); copie == null && iCopie.hasNext();)
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
	 * @throws SQLException
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
	 * @throws SQLException
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
	 * @throws SQLException
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
