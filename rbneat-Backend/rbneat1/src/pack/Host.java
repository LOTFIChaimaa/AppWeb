package pack;


import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import com.fasterxml.jackson.annotation.JsonIgnore;

/** Les utilisateurs hotes qui propose des plats*/
@Entity
public class Host {
	
	/** L'id n'est pas géré automatiquement.*/
	@Id
	private int id;
	@OneToOne( fetch = FetchType.EAGER, cascade = CascadeType.MERGE )
	private User userHost;
		
	@OneToOne( mappedBy = "hostReservation", fetch = FetchType.EAGER, cascade = CascadeType.MERGE )
	@JsonIgnore
	private Reservation reserv;
	
	@OneToMany(mappedBy = "hostComt", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	private Set<Commentaire> comments;
	
	@OneToMany(mappedBy = "hostPlat", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	private Set<Plat> plats;
	
	
	/** Les getters and setters de ces variables*/
	public Set<Plat> getPlats() {
		return plats;
	}
	public void setPlats(Set<Plat> plats) {
		this.plats = plats;
	}
	public Reservation getReserv() {
		return reserv;
	}
	public void setReserv(Reservation reserv) {
		this.reserv = reserv;
	}
	public Set<Commentaire> getComments() {
		return comments;
	}
	public void setComments(Set<Commentaire> comment) {
		this.comments = comment;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public User getUserHost() {
		return userHost;
	}
	public void setUserHost(User userHost) {
		this.userHost = userHost;
	}
	

}
