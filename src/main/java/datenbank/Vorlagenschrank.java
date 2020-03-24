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
	private String maindokument;
	
	public String getMaindokument() {
		return maindokument;
	}

	public void setMaindokument(String mainDokument) {
		this.maindokument = mainDokument;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
