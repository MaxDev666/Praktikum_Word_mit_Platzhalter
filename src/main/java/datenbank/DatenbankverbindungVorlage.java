package datenbank;

import java.util.List;

import javax.management.Query;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DatenbankverbindungVorlage {
	//es wird eine EntityManagerFactory erstellt, mit der die einzelnen Entitymanager erzeugt werden
	private static EntityManagerFactory emf = Persistence
			.createEntityManagerFactory("WordPlatzhalter");

	/*	Methode dient dem Hinzufügen einer Vorlage in die Datenbank, 
	 *  dazu wird als erstes ein Entitymanager und eine Transaction erstellt
		Diese Transaction wird gestartet, eine neue Vorlage instanziert und 
		die Spalten der Tabelle mit den Parameterwerten ausgefüllt
		Danach wird die Transaction und somit die Befehle committet. 
		Beim Fehlerfall werden die Befehle zurück gesetzt*/
	public static String addVorlage(String id, String vorlagenpfad) {
		
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = null;
		String rueckgabe = null;
		try {
			et = em.getTransaction();
			et.begin();
			Vorlagenschrank vorlage = new Vorlagenschrank();
			vorlage.setId(id);
			vorlage.setVorlagenpfad(vorlagenpfad);
			em.persist(vorlage);
			et.commit();
			rueckgabe="Hinzufügen erfolgreich "+vorlage.getVorlagenpfad();
		} catch (Exception e) {
			if (et != null) {
				et.rollback();
			}
			rueckgabe = "Hinzufügen nicht erfolgreich.";
		} finally {
			em.close();
		}
		return rueckgabe;
	}

	/* Vorlage anhand übergebener ID aus Tabelle auswählen.
	 * Dazu wird nach dem Entitymanager ein query aufgebaut, 
	 * dass eine Vorlage anhand der als Parameter übergebenen ID auswählt.
	 * Von dieser Vorlage wird dann der XML-Pfad an den Aufrufer zurückgegeben.
	 * Wenn der Datenbankeintrag nicht vorhanden ist, wird ein Fehler zurückgegeben.
	 */
	public static String getVorlage(String id) throws NoResultException{
		EntityManager em = emf.createEntityManager();
		String query = "SELECT vorlage FROM Vorlagenschrank vorlage WHERE vorlage.id=:ID";

		TypedQuery<Vorlagenschrank> tq = em.createQuery(query, Vorlagenschrank.class);
		tq.setParameter("ID", id);
		Vorlagenschrank vorlage = null;
		vorlage = tq.getSingleResult();
		String rueckgabe = vorlage.getVorlagenpfad();
		em.close();
		return rueckgabe;
	}

	/* Frage, ob benötigt!
	 * Alle Vorlagen auswählen
	 * Es werden alle Vorlagen durch ein Query ausgewählt und in einer Arrayliste gespeichert.
	 * Von jeder Vorlage wird der Pfad zurückgegeben
	 */
	public static List<Vorlagenschrank> getVorlagen() {
		EntityManager em = emf.createEntityManager();
		String strQuery = "SELECT vorlage FROM Vorlagenschrank vorlage WHERE vorlage.id IS NOT NULL";
		TypedQuery<Vorlagenschrank> tq = em.createQuery(strQuery, Vorlagenschrank.class);
		List<Vorlagenschrank> vorlagen = null;
		try {
			vorlagen = tq.getResultList();
			//vorlagen.forEach(vorlage -> System.out.println(vorlage.getVorlagenpfad()));
			
		} catch (NoResultException e) {
			e.printStackTrace();
		} finally {
			em.close();
		}
		return vorlagen;
	}
	
	/* Ändern der Vorlage mit übegebener ID durch den übergebenen XMLPfad
	 * Es wird nach der Vorlage mit der als Parameter übergebenen ID gesucht und bei dieser der Documentpath ersetzt 
	 */
	public static String changeVorlage(String id, String vorlagenpfad) {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = null;
		Vorlagenschrank vorlage = null;
		String rueckgabe = null;
		try {
			et = em.getTransaction();
			et.begin();
			vorlage = em.find(Vorlagenschrank.class, id);
			vorlage.setVorlagenpfad(vorlagenpfad);
			em.persist(vorlage);
			et.commit();
			rueckgabe = "Ändern erfolgreich.";
		} catch (Exception e) {
			if (et != null) {
				et.rollback();
			}
			rueckgabe = "Ändern nicht erfolgreich.";
		} finally {
			em.close();
		}
		return rueckgabe;
	}
	
	/* Löschen einer Vorlage
	 * Es wird anhand der ID nach einer Vorlage gesucht und dieser Datensatz dann gelöscht. 
	 * Dies muss durch flush() und clear() der Entity mitgeteilt werden
	 */
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
	
	public static void deleteVorlagen() {
		EntityManager em = emf.createEntityManager();
		EntityTransaction et = null;
		javax.persistence.Query q = em.createQuery("DELETE FROM Vorlagenschrank");
		try {
			et = em.getTransaction();
			et.begin();
			q.executeUpdate();
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
