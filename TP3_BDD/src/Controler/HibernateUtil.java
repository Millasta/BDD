package Controler;

import org.hibernate.*;
import org.hibernate.cfg.*;

public final class HibernateUtil {
	
	private static SessionFactory sessionFactory;
	private static Session session;
	private static Transaction transaction;
	
	static {
		try {
			sessionFactory = new Configuration().configure().buildSessionFactory();
	    } 
		catch (Throwable ex) {
	       throw new ExceptionInInitializerError(ex);
	    }
	}
	
	public static Session DemarerTransaction() 
	{
		session = sessionFactory.openSession();
		transaction = session.beginTransaction();
		return session;
	}
	
	public static void RealiserTransaction() 
	{
		transaction.commit();
		session.close();
	}
	
	public static void shutdown()
	{
		sessionFactory.close();
	}
	
	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
}
