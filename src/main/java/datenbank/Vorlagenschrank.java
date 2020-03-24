package datenbank;

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
