package pack;
import java.util.Collection;
import javax.persistence.*;


/** Chaque réservation a un invité, un hote, les plats choisi, le prix avec la date*/

@Entity
public class Reservation {

	@Id
	private int id;
	
	/** l'hote pour cette reservation*/
	@OneToOne( fetch = FetchType.EAGER, cascade = CascadeType.MERGE )
	private Host hostReservation;
	
	/** l'invité de cettte reservation*/
	@OneToOne( fetch = FetchType.EAGER, cascade = CascadeType.MERGE )
	private Guest guestReservation;
	
	/** La date de reservation**/
	private String Date;
	
	/** Le prix total*/
	private String Prix;
	
	/**Ensemble des plats choisi par l'invité chez cet hote*/
	@OneToMany(mappedBy = "reservation", fetch = FetchType.EAGER, cascade = CascadeType.ALL )
	private Collection<Plat> plats;
	
	
	/** Les getters and setters de ces variables*/
	public int getId() {
		return id;
	}

	public Collection<Plat> getPlats() {
		return plats;
	}
	public void setPlats(Collection<Plat> plats) {
		this.plats = plats;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public Host getHostReservation() {
		return hostReservation;
	}

	public void setHostReservation(Host hostReservation) {
		this.hostReservation = hostReservation;
	}

	public Guest getGuestReservation() {
		return guestReservation;
	}

	public void setGuestReservation(Guest guestReservation) {
		this.guestReservation = guestReservation;
	}

	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
	}
	public String getPrix() {
		return Prix;
	}
	public void setPrix(String prix) {
		Prix = prix;
	}
	
	
}