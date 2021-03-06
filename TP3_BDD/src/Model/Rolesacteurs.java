package Model;
// Generated Nov 30, 2018 9:19:15 AM by Hibernate Tools 5.3.6.Final

/**
 * Rolesacteurs generated by hbm2java
 */
public class Rolesacteurs implements java.io.Serializable {

	private static final long serialVersionUID = -1097276659634104426L;
	
	/**
	 * Attributs de la classe correspondant aux champs de la table
	 */
	private RolesacteursId id;
	private String nompersonnage;

	/**
	 * Constructeur par defaut
	 */
	public Rolesacteurs() 
	{
	}

	/**
	 * Construit un acteurs avec ses attributs
	 * @param id : l'id de la personne correspondante
	 * @param nomPersonnage : le nom du personnage dans le film
	 */
	public Rolesacteurs(RolesacteursId id, String nomPersonnage)
	{
		this.id = id;
		this.nompersonnage = nomPersonnage;
	}

	/**
	 * Getter de l'id
	 * @return RolesacteursId id
	 */
	public RolesacteursId getId() 
	{
		return id;
	}

	/**
	 * Setter de l'id
	 * @param id : le nouvel id
	 */
	public void setId(RolesacteursId id) 
	{
		this.id = id;
	}

	/**
	 * Getter du nom de personnage
	 * @return String nompersonnage
	 */
	public String getNompersonnage() 
	{
		return nompersonnage;
	}

	/**
	 * Setter du nom de personnage
	 * @param nomPersonnage
	 */
	public void setNompersonnage(String nomPersonnage) 
	{
		this.nompersonnage = nomPersonnage;
	}

}
