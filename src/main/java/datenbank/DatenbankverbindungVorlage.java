package datenbank;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DatenbankverbindungVorlage {
	private static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("Word_mit_Platzhalter");

	public static void addVorlage(String id, String maindokument) {

		EntityManager em = emf.createEntityManager();
		EntityTransaction et = null;
		try {
			et = em.getTransaction();
			et.begin();
			Vorlagenschrank vorlage = new Vorlagenschrank();
			vorlage.setId(id);
			vorlage.setMaindokument(maindokument);
			em.persist(vorlage);
			et.commit();
		} catch (Exception e) {
			if (et != null) {
				et.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	public static void getVorlage(String id) {
		EntityManager em = emf.createEntityManager();
		String query = "SELECT vorlage FROM Vorlagenschrank vorlage WHERE vorlage.id=:ID";

		TypedQuery<Vorlagenschrank> tq = em.createQuery(query, Vorlagenschrank.class);
		tq.setParameter("ID", id);
		Vorlagenschrank vorlage = null;
		try {
			vorlage = tq.getSingleResult();
			System.out.println(vorlage.getMaindokument());
		} catch (NoResultException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}

	}

	public static void getVorlagen() {
		EntityManager em = emf.createEntityManager();
		String strQuery = "SELECT vorlage FROM Vorlagenschrank vorlage WHERE vorlage.id IS NOT NULL";
		TypedQuery<Vorlagenschrank> tq = em.createQuery(strQuery, Vorlagenschrank.class);
		List<Vorlagenschrank> vorlagen;
		try {
			vorlagen = tq.getResultList();
			vorlagen.forEach(vorlage -> System.out.println(vorlage.getMaindokument()));

		} catch (NoResultException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	public static void changeVorlage(String id, String maindokument) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = null;
		Vorlagenschrank vorlage = null;
		try {
			et = em.getTransaction();
			et.begin();
			vorlage = em.find(Vorlagenschrank.class, id);
			vorlage.setMaindokument(maindokument);
			em.persist(vorlage);
			et.commit();
		} catch (Exception e) {
			if (et != null) {
				et.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
	
	public static void deleteVorlage(String id) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = null;
		Vorlagenschrank vorlage = null;
		try {
			et = em.getTransaction();
			et.begin();
			vorlage = em.find(Vorlagenschrank.class, id);
			em.remove(vorlage);
			em.flush();
			em.clear();

			et.commit();
		} catch (Exception e) {
			if (et != null) {
				et.rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
	}
}
