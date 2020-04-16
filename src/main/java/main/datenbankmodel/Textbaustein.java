package main.datenbankmodel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//Entit�t der Textbaustein-DB zur Darstellung in Hibernate
@Entity
@Table(name = "textbaustein")
public class Textbaustein{

	public Textbaustein() {
		
	}
	//id ist der Prim�rschl�ssel
	@Id
	@Column(name = "id", unique = true)
	private String id;
	
	//der Text darf nicht leer sein
	@Column(name="text", nullable = false)
	private String text;
	
	public Textbaustein(String id, String text) {
		this.id="tb_"+id;
		this.text=text;
	}

	//Getter und Setter f�r id und Vorlagenpfad
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = "tb_"+id;
	}
}
