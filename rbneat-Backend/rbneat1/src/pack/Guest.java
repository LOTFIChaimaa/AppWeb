package pack;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Guest {
	
	@Id
	private int id;
	
	private int LoyaltyPoints;
	
	@OneToOne( fetch = FetchType.EAGER, cascade = CascadeType.MERGE )
	private User userGuest;
	
	@OneToOne( mappedBy = "guestReservation", fetch = FetchType.EAGER, cascade = CascadeType.MERGE )
	@JsonIgnore
	private Reservation reserv;
	
	
	/** Les getters and setters de ces variables*/
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUserGuest() {
		return userGuest;
	}

	public void setUserGuest(User userGuest) {
		this.userGuest = userGuest;
	}

	/**
	 * Set the loyalty points of the customer
	 * @param LP Amount of Loyalty Points
	 */
	public void setLoyaltyPoints(int LP) {
		this.LoyaltyPoints = LP;
	}
	
	/**
	 * Get the Loyalty Points of the customer
	 * @return LoyaltyPoints
	 */
	public int getLoyaltyPoints() {
		return this.LoyaltyPoints;
	}
	
	/**
	 * Add the loyalty points to LP existant
	 * @param LP Amount of Loyalty Points
	 */
	public void addLoyaltyPoints(int LP) {
		this.LoyaltyPoints += LP;
	}

}
