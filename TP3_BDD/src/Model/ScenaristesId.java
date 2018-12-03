package Model;

public class ScenaristesId implements java.io.Serializable {

	private static final long serialVersionUID = 2048149638108548137L;

	private Personnes personnes;
	private Films films;
	
	public ScenaristesId()
	{
	}
	
	public ScenaristesId(Personnes personnes, Films films)
	{
		this.personnes = personnes;
		this.films = films;
	}
	
	public Personnes getPersonnes()
	{
		return personnes;
	}

	public void setPersonnes(Personnes personnes)
	{
		this.personnes = personnes;
	}

	
	public Films getFilms()
	{
		return films;
	}

	public void setFilms(Films films)
	{
		this.films = films;
	}

}
