package Model;
// Generated Nov 30, 2018 9:19:15 AM by Hibernate Tools 5.3.6.Final

import java.util.HashSet;
import java.util.Set;

/**
 * Films generated by hbm2java
 */
public class Films implements java.io.Serializable {

	private static final long serialVersionUID = 1694271170317920677L;
	
	/**
	 * Attributs de la classe correspondant aux champs de la table
	 */
	private String titre;
	private int annee;
	private short duree;
	private String langue;
	private String resume;
	private String genres;
	private String paysproduction;
	private int copies;

	/**
	 * Liste des acteurs du film
	 */
	private Set<Object> rolesacteurses = new HashSet<Object>(0);
	
	/**
	 * Liste des scenaristes du film
	 */
	private Set<Object> scenaristes = new HashSet<Object>(0);
	
	/**
	 * Liste des copies du film
	 */
	private Set<Object> copieses = new HashSet<Object>(0);

	/**
	 * Constructeur par defaut
	 */
	public Films()
	{
	}

	/**
	 * Construit un films avec ses attributs
	 * @param titre : le titre du film
	 * @param annee : l'anne de production du film
	 * @param duree : la duree du film
	 * @param langue : la langue original du film
	 * @param resume : le resume du film
	 * @param genres : les genres du film
	 * @param paysProduction : le pays de production du film
	 */
	public Films(String titre, int annee, short duree, String langue, String resume, String genres, String paysProduction)
	{
		this.titre = titre;
		this.annee = annee;
		this.duree = duree;
		this.langue = langue;
		this.resume = resume;
		this.genres = genres;
		this.paysproduction = paysProduction;
	}

	/**
	 * Construit un films avec ses attributs, ses acteurs, ses scenariste et ses copies
	 * @param titre : le titre du film
	 * @param annee : l'anne de production du film
	 * @param duree : la duree du film
	 * @param langue : la langue original du film
	 * @param resume : le resume du film
	 * @param genres : les genres du film
	 * @param paysProduction : le pays de production du film
	 * @param rolesActeurs : les acteurs jouant dans le film
	 * @param personnes : les scenaristes du film
	 * @param copies : les copies du film
	 */
	public Films(String titre, int annee, short duree, String langue, String resume, String genres, String paysProduction, Set<Object> rolesActeurs, Set<Object> scenaristes, Set<Object> copies)
	{
		this.titre = titre;
		this.annee = annee;
		this.duree = duree;
		this.langue = langue;
		this.resume = resume;
		this.genres = genres;
		this.paysproduction = paysProduction;
		this.rolesacteurses = rolesActeurs;
		this.scenaristes = scenaristes;
		this.copieses = copies;
	}

	/**
	 * Getter du titre
	 * @return String titre
	 */
	public String getTitre()
	{
		return titre;
	}

	/**
	 * Setter du titre
	 * @param titre : le nouveau titre
	 */
	public void setTitre(String titre)
	{
		this.titre = titre;
	}

	/**
	 * Getter de l'annee
	 * @return int annee
	 */
	public int getAnnee()
	{
		return annee;
	}

	/**
	 * Setter de l'annee
	 * @param annee : la nouvelle annee
	 */
	public void setAnnee(int annee)
	{
		this.annee = annee;
	}

	/**
	 * Getter de la duree
	 * @return short duree
	 */
	public short getDuree()
	{
		return duree;
	}

	/**
	 * Setter de la duree
	 * @param duree : la nouvelle duree
	 */
	public void setDuree(short duree)
	{
		this.duree = duree;
	}

	/**
	 * Getter de la langue original
	 * @return String langue
	 */
	public String getLangue()
	{
		return langue;
	}

	/**
	 * Setter de la langue original
	 * @param langue : la nouvelle langue
	 */
	public void setLangue(String langue)
	{
		this.langue = langue;
	}

	/**
	 * Getter du resume
	 * @return String resume
	 */
	public String getResume() 
	{
		return resume;
	}

	/**
	 * Setter du resume
	 * @param resume : le nouveau resume
	 */
	public void setResume(String resume)
	{
		this.resume = resume;
	}

	/**
	 * Getter des genres
	 * @return String genres
	 */
	public String getGenres()
	{
		return genres;
	}

	/**
	 * Setter des genres
	 * @param genres : les nouveaux genres
	 */
	public void setGenres(String genres) 
	{
		this.genres = genres;
	}

	public int getCopies()
	{
		return copies;
	}

	public void setCopies(int copies)
	{
		this.copies = copies;
	}
	
	/**
	 * Getter des pays de production
	 * @return String paysproduction
	 */
	public String getPaysproduction()
	{
		return paysproduction;
	}

	/**
	 * Setter des pays de production
	 * @param paysproduction : les nouveaux pays de production
	 */
	public void setPaysproduction(String paysProduction)
	{
		this.paysproduction = paysProduction;
	}

	/**
	 * Getter des acteurs
	 * @return Set<Object> rolesacteurses
	 */
	public Set<Object> getRolesacteurses() 
	{
		return rolesacteurses;
	}

	/**
	 * Setter des acteurs
	 * @param rolesacteurs : les nouveaux acteurs
	 */
	public void setRolesacteurses(Set<Object> rolesActeurs)
	{
		this.rolesacteurses = rolesActeurs;
	}

	/**
	 * Getter des scenaristes
	 * @return Set<Object> personneses
	 */
	public Set<Object> getScenaristes()
	{
		return scenaristes;
	}

	/**
	 * Setter des scenaristes
	 * @param personnes : les nouveaux scenaristes
	 */
	public void setScenaristes(Set<Object> personnes) 
	{
		this.scenaristes = personnes;
	}

	/**
	 * Getter des copies
	 * @return Set<Object> copieses
	 */
	public Set<Object> getCopieses()
	{
		return copieses;
	}

	/**
	 * Setter des copies
	 * @param copies : les nouvelles copies
	 */
	public void setCopieses(Set<Object> copies) 
	{
		this.copieses = copies;
	}

}
