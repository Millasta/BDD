package Model;
// Generated Nov 30, 2018 9:19:15 AM by Hibernate Tools 5.3.6.Final

/**
 * Rolesacteurs generated by hbm2java
 */
public class Rolesacteurs implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1097276659634104426L;
	private RolesacteursId id;
	private String nompersonnage;

	public Rolesacteurs() {
	}

	public Rolesacteurs(RolesacteursId id, String nompersonnage) {
		this.id = id;
		this.nompersonnage = nompersonnage;
	}

	public RolesacteursId getId() {
		return this.id;
	}

	public void setId(RolesacteursId id) {
		this.id = id;
	}

	public String getNompersonnage() {
		return this.nompersonnage;
	}

	public void setNompersonnage(String nompersonnage) {
		this.nompersonnage = nompersonnage;
	}

}
