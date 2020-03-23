package jpa_entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "vorlagenschrank")
public class Vorlagenschrank implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true)
	private String id;
	
	@Column(name = "mainDokument", nullable = false)
	private String dokumentinhalt;
	
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getWord() {
		return dokumentinhalt;
	}

	public void setWord(String word) {
		this.dokumentinhalt = word;
	}
    
    
}
