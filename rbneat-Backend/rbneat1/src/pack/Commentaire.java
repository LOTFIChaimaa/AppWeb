package pack;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Commentaire {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/*Avis sur la personne*/
	private String commentaire;
	
	private String mail;
	
	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	@JsonIgnore
	private Host hostComt;
	
	public Host getHostComt() {
		return hostComt;
	}

	public void setHostComt(Host hostComt) {
		this.hostComt = hostComt;
	}

	private int noteService;

	/** Les getters and setters de ces variables*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNoteService() {
		return noteService;
	}

	public void setNoteService(int noteService) {
		this.noteService = noteService;
	}
	public String getCommentaire() {
		return commentaire;
	}

	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	
	

}
