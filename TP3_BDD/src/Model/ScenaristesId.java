package Model;

public class ScenaristesId implements java.io.Serializable {

	private static final long serialVersionUID = 2048149638108548137L;

	/**
	 * Attributs de la classe correspondant aux champs de la table
	 */
	private Personnes personnes;
	private Films films;
	
	/**
	 * Constructeur par defaut
	 */
	public ScenaristesId()
	{
	}
	
	/**
	 * Construit l'id d'un scenariste avec la personne et le film correspondant
	 * @param personnes : la personne correspondante
	 * @param films : le film correspondant
	 */
	public ScenaristesId(Personnes personnes, Films films)
	{
		this.personnes = personnes;
		this.films = films;
	}
	
	/**
	 * Getter de la personne
	 * @return Personnes personnes
	 */
	public Personnes getPersonnes()
	{
		return personnes;
	}

	/**
	 * Setter de la personnes
	 * @param personnes : la nouvelle personne
	 */
	public void setPersonnes(Personnes personnes)
	{
		this.personnes = personnes;
	}

	/**
	 * Getter du film
	 * @return Films films
	 */
	public Films getFilms()
	{
		return films;
	}

	/**
	 * Setter du film
	 * @param films : le nouveau film
	 */
	public void setFilms(Films films)
	{
		this.films = films;
	}

}
