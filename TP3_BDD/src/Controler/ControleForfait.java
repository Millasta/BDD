package Controler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hibernate.*;
import Model.Forfaits;

public class ControleForfait {
	
	/**
	 * Liste des forfaits
	 */
	private ArrayList<Forfaits> forfaits;

	/**
	 * Construit le controler des forfaits
	 * Initialise la list des forfaits avec la bdd
	 */
	public ControleForfait()
	{
		forfaits = new ArrayList<Forfaits>();
		Initialiser();
	}
	
	/**
	 * Getter de la liste des forfaits
	 * @return ArrayList<Forfait> Forfaits
	 */
	public ArrayList<Forfaits> getForfaits() 
	{
		return forfaits;
	}
	
	/**
	 * Initialise la liste des forfaits avec la bdd
	 */
	private void Initialiser()
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		List<?> lesForfaits = hbSession.createQuery("select f.typeforfait, f.cout, f.locationmax, f.dureemax from Forfaits f").list();
		
		for(Iterator<?> iForfait = (Iterator<?>) lesForfaits.iterator(); iForfait.hasNext();)
		{
			Object[] result = (Object[]) iForfait.next();
			Forfaits f = new Forfaits((String)result[0], (double)result[1], (byte)result[2], (short)result[3]);
			forfaits.add(f);
		}
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Cree un nouveau forfait
	 * @param typeForfait : le type du nouveau forfait
	 * @param cout : le cout du nouveau forfait
	 * @param locationMax : le nombre de location maximal du nouveau forfait
	 * @param dureeMax : la duree de location maximal du nouveau forfait
	 */
	public void Creer(String typeForfait, double cout, byte locationMax, short dureeMax) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Forfaits forfait = new Forfaits(typeForfait, cout, locationMax, dureeMax);
		
		hbSession.save(forfait);
		forfaits.add(forfait);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Modifie le forfait correspondant au type de forfait passe en argument
	 * @param typeForfait : le forfait a modifier
	 * @param cout : le nouveau cout du forfait
	 * @param locationMax : le nouveau nombre de location maximal du forfait
	 * @param dureeMax : la nouvelle duree de location maximal du forfait
	 */
	public void Modifier(String typeForfait, double cout, byte locationMax, short dureeMax) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Forfaits bdForfait = (Forfaits) hbSession.createQuery("from Forfaits where Forfaits.TypeForfait = :type").setParameter("type", typeForfait).list().iterator().next();
		Forfaits alForfait = Rechercher(typeForfait);
		
		bdForfait.setCout(cout);
		bdForfait.setLocationmax(locationMax);
		bdForfait.setDureemax(dureeMax);
		
		alForfait.setCout(cout);
		alForfait.setLocationmax(locationMax);
		alForfait.setDureemax(dureeMax);
		
		HibernateUtil.RealiserTransaction();
	}
	
	/**
	 * Retourne le forfait correspondant au type de forfait passe en argument
	 * @param typeForfait : le type de forfait recherche
	 * @return Forfait forfait ou null
	 */
	public Forfaits Rechercher(String typeForfait)
	{
		Forfaits forfait = null;
		
		for(Iterator<Forfaits> iForfait = forfaits.iterator(); forfait == null && iForfait.hasNext();)
		{
			forfait = iForfait.next();
			if(!forfait.getTypeforfait().equals(typeForfait))
			{
				forfait = null;
			}
		}
		
		return forfait;
	}
	
	/**
	 * Supprime le forfait correspondant au type de forfait passe en argument
	 * @param typeForfait : le type de forfait recherche
	 */
	public void Supprimer(String typeForfait) throws SQLException
	{
		Session hbSession = HibernateUtil.DemarerTransaction();
		
		Forfaits forfait = (Forfaits) hbSession.createQuery("from Forfaits where Forfaits.TypeForfait = :type").setParameter("type", typeForfait).list().iterator().next();
		hbSession.delete(forfait);
		forfaits.remove(Rechercher(typeForfait));
		
		HibernateUtil.RealiserTransaction();
	}
}
