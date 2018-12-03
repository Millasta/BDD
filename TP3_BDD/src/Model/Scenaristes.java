package Model;

public class Scenaristes implements java.io.Serializable {

	private static final long serialVersionUID = -5470530741718287305L;
	
	private ScenaristesId id;

	public Scenaristes()
	{
	}
	
	public Scenaristes(ScenaristesId id)
	{
		this.id = id;
	}

	public ScenaristesId getId()
	{
		return id;
	}

	public void setId(ScenaristesId id)
	{
		this.id = id;
	}
}
