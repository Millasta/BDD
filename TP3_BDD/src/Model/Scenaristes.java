package Model;

public class Scenaristes implements java.io.Serializable {

	private static final long serialVersionUID = -5470530741718287305L;
	
	/**
	 * Attributs de la classe correspondant aux champs de la table
	 */
	private ScenaristesId id;

	/**
	 * Constructeur par defaut
	 */
	public Scenaristes()
	{
	}
	
	/**
	 * Construit un scenariste avec son id
	 * @param id : l'id du scenariste
	 */
	public Scenaristes(ScenaristesId id)
	{
		this.id = id;
	}

	/**
	 * Getter de l'id
	 * @return ScenaristesId id
	 */
	public ScenaristesId getId()
	{
		return id;
	}

	/**
	 * Setter de l'id
	 * @param id : le nouvel id
	 */
	public void setId(ScenaristesId id)
	{
		this.id = id;
	}
}
