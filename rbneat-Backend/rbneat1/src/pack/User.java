package pack;

import java.io.Serializable;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User implements Serializable {

	/**
	 * Les attributs de cette classe sont communs pour l'invité et le hôte.
	 * Lorsqu'on ajoute un plat, on va créer cet utilisateur comme un hôte s'il
	 * n'est pas déjà enregistré.
	 * Lors d'une réservation, on ajoute l'utilisateur qui commande comme un invité
	 * s'il n'est pas déjà enregistré comme un guest.
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/** Nom d'utilisateur*/
	private String nom;
	
	/** Prenom d'utilisateur*/
	private String prenom;
	
	/** Adresse d'utilisateur*/
	private String adresse;
	
	/** Numero d'utilisateur*/
	private String numero;	

	/** Password de compte d'utilisateur*/
	private String password;
	
	/* Mail d'utilisateur*/
	private String mail;

	/** Les getters and setters de ces variables*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}


	
}