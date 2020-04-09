package main.dao;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//Entität der Vorlagenschrank-DB zur Darstellung n Hibernate
@Entity
@Table(name = "vorlagenschrank")
public class Vorlagenschrank{

	//id ist der Primärschlüssel
	@Id
	@Column(name = "id", unique = true)
	private String id;
	
	//der vorlagenpfad darf nicht leer sein
	@Column(name="vorlagenpfad", nullable = false)
	private String vorlagenpfad;
	
	public Vorlagenschrank(String id, String documentpath) {
		this.id=id;
		this.vorlagenpfad=documentpath;
	}

	//Getter und Setter für id und Vorlagenpfad
	public String getVorlagenpfad() {
		return vorlagenpfad;
	}

	public void setVorlagenpfad(String Vorlagenpfad) {
		this.vorlagenpfad = Vorlagenpfad;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
