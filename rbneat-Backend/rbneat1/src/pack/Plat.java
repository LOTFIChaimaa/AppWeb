package pack;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;


/** Un plat proposé à reserver*/
@Entity
public class Plat {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	/** Nom de plat*/
	private String nom;
	/** Pays d'origine de ce plat*/
	private String pays;
	
	/** Mail de l'hôte qui a proposé cet plat*/
	private String mail;

	/* Prix de plat*/
	private String prix;
	
	/* Image de plat	*/
	@Column(length = 500000000, name = "photo")
	private String photo;
	
	/** Nb maximum d'invités qui peuvent être venir pour ce plat */
	private int nbInvite;
	
	/** L'adresse de l'hôte qui a proposé ce plat*/
	private String adresse;

	/*Description de plat*/
	private String description;
	
	/*Date et l'heure de disponibilité de ce plat*/
	private String date;	
	private String heure;
	
	
	/** Les plats associé à une reservation, plusieurs plats peuvent être associé au même reservation*/
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	@JsonIgnore
	private Reservation reservation;
	
	/** L'hote proposé ce plat, on le récupére en utilisant son mail*/
	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	@JsonIgnore
	private Host hostPlat;
	
	
	/** Les getters and setters de ces variables*/
	public Host getHostPlat() {
		return hostPlat;
	}

	public void setHostPlat(Host hostPlat) {
		this.hostPlat = hostPlat;
	}
	
	
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
	
	public String getMail() {
		return mail;
	}

	public void setMail(String email) {
		this.mail = email;
	}

	public String getPays() {
		return pays;
	}

	public void setPays(String pays) {
		this.pays = pays;
	}

	public String getPrix() {
		return prix;
	}

	public void setPrix(String prix) {
		this.prix = prix;
	}

	@Column(length = 500000000, name = "photo")
	public String getPhoto() {
		return photo;
	}

	@Column(length = 500000000, name = "photo")
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getNbInvite() {
		return nbInvite;
	}

	public void setNbInvite(int nbInvite) {
		this.nbInvite = nbInvite;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getHeure() {
		return heure;
	}

	public void setHeure(String heure) {
		this.heure = heure;
	}

	public String getDescription() {
		return description;
	}

	public Reservation getReservation() {
		return reservation;
	}

	public void setReservation(Reservation reservation) {
		this.reservation = reservation;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}


	public String getAdresse() {
		return adresse;
	}

	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
}


