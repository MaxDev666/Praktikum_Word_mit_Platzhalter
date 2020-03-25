package datenbank;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//Entität der Vorlagenschrank-DB zur Darstellung n Hibernate
@Entity
@Table(name = "vorlagenschrank")
public class Vorlagenschrank implements Serializable{
	private static final long serialVersionUID = 1L;

	//id ist der Primärschlüssel
	@Id
	@Column(name = "id", unique = true)
	private String id;
	
	//der XMLPfad darf nicht leer sein
	@Column(nullable = false)
	private String xmlpfad;
	
	public String getXMLPfad() {
		return xmlpfad;
	}

	public void setXMLPfad(String XMLPfad) {
		this.xmlpfad = XMLPfad;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
