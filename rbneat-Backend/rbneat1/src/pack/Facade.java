package pack;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import javax.ejb.Singleton;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;


/**
 * Session Bean implementation class Facade
 */

@Singleton
@Path("/")
public class Facade {

    @PersistenceContext
    private EntityManager em;
    
    /*Récupérer la liste des utilisateurs en utilisant la methode GET*/
    @GET
    @Path("/user")
    @Produces({"application/json"}) 
    public Collection<User> getUsers() {
    	return em.createQuery("FROM User", User.class).getResultList();
    }
    
    /** Ajouter un utilisateur avec la méthode POST en donnant le json User
     * Depuis le front-end.
     * Nous avons ajouté la possibilité d'envoyer un mail de confirmation
     * pour vérifier que le mail donné est bien existant*/
    
    @POST
    @Path("/user")
    @Consumes({"application/json"})
    public void setUser(User i) throws AddressException, MessagingException {
    	em.persist(i);
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        /** créer une nouvelle session d'authenticator*/       
        Authenticator authenticator = new Authenticator() {      	
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("rbneat@gmail.com", "Lotfi1234");
            }
        };
        /**Créer  une nouvelle session*/
        Session session = Session.getInstance(properties, authenticator);
        /** Créer un nouveau message d'email*/
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("rbneat@gmail.com"));
        InternetAddress[] toAddresses = { new InternetAddress(i.getMail()) };
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        msg.setSubject("Mail de confirmation");
        msg.setSentDate(new Date());
        msg.setText("Bienvenue chez Rbneat ! \n \n \n "
        		+ "Votre inscription est bien prise en compte. \n "
        		+ "Vous pouvez désormais proposer des plats ou chercher des délicieux plats. \n "
        		+ "\n"
        		+ "\n "
        		+ "\n "
        		+ "A bientôt,\n "
        		+ "L'équipe Rbneat");
        
        /**l'envoie du mail*/
        Transport.send(msg);
    }
    
    /** Récupérer la liste des contacts déjà existant*/
    @GET
    @Path("/contact")
    @Produces({"application/json"})
    public Collection<Contact> getContact() {
    	return em.createQuery("FROM Contact", Contact.class).getResultList();
    }

    /** Ajouter un contact avec la méthode POST en donnant le json Contact
     * Depuis le front-end */
    @POST
    @Path("/contact")
    @Consumes({"application/json"})
    public void setContact(Contact c) {
    	em.persist(c);
    }
    
    
    /**Récupérer la liste des plats déjà enregistrés
     * */
    @GET
    @Path("/plat")
    @Produces({"application/json"}) 
    public Collection<Plat> getPlat() {
    	return em.createQuery("FROM Plat", Plat.class).getResultList();
    }
    
    /**Cette fonction sera utilisé dans la prochaine fonction
     * ça sert à vérifier si un hote est déjà enregistré dans le BD 
     * ou pas encore en donnant son id.*/
    public Boolean isNotAlreadyHost(int id) {  	 	
    	Query q = em.createQuery("select h from Host h where h.id=? ", Host.class);
    	@SuppressWarnings("unchecked")
    	List<Host> hosts = q.setParameter(1, id).getResultList();
    	return hosts.size() ==0;
    }
    
    /**Un utilisateur est soit un hote ou bien un invité, donc s'il propose un plat
     * sera ajouté dans la liste des hotes, si pour ça à l'ajout d'un plat par un utilisateur
     * on vérifie s'il est déjà existant, sinon on l'ajoute.*/
    
    /**On supprime les espaces lorsqu'on veut ajouter une description d'un plat, parce que 
     * le nombre de caractéres ne doit dépasser 250 caracteres.*/
    @POST
    @Path("/plat")
    @Consumes({"application/json"})
    public void setPlat(Plat p) {
    	em.persist(p);
    	String descp = p.getDescription().replaceAll("\\s", "");
    	p.setDescription(descp);
    	em.merge(p);
    	String mail = p.getMail();
    	Query q = em.createQuery("select u from User u where u.mail=? ", User.class);
    	User user = (User) q.setParameter(1, mail).getSingleResult();
    	int id = user.getId();
    	if(user !=null) {
	    	if (isNotAlreadyHost(id)) {
		    	Host h = new Host();
		    	h.setUserHost(user);
		    	h.setId(user.getId());
		    	h.setPlats(new HashSet<>());
		    	h.setComments(new HashSet<>());
		    	em.persist(h);
	    	}
	    	Host h = em.find(Host.class, user.getId());
	    	h.getPlats().add(p);
	    	em.merge(h);
	    	p.setHostPlat(h);
	    	em.merge(p);
    	}
    }
    
    /**Récupérer la liste des invités.*/
    @GET
    @Path("/guest")
    @Produces({"application/json"}) 
    public Collection<Guest> getGuest() {
    	return em.createQuery("FROM Guest", Guest.class).getResultList();
    }
    
    /** Ajouter un invité*/
    @POST
    @Path("/guest")
    @Consumes({"application/json"})
    public void setGuest(Guest g) {
    	em.persist(g);
    }
    
    /**Récupérer la liste des hotes*/
    @GET
    @Path("/host")
    @Produces({"application/json"}) 
    public Collection<Host> getHost() {
    	return em.createQuery("FROM Host", Host.class).getResultList();
    }
    
    /**Ajouter un hote avec une méthode POST*/
    @POST
    @Path("/host")
    @Consumes({"application/json"})
    public void setHost(Host h) {
    	em.persist(h);
    }
    
    /**Récupérer la liste des reservations avec une méthode GET*/
    @GET
    @Path("/reservation")
    @Produces({"application/json"}) 
    public Collection<Reservation> getMenu() {
    	return em.createQuery("SELECT i FROM Reservation i", Reservation.class).getResultList();
    }
    
    /**Envoie un mail de confirmation de commandes passé par l'invité.
     * Ce mail contient le nom de hote, avec les plats commandes, le prix et la date*/
    public void reservationMail(Reservation r) throws AddressException, MessagingException {
    
            Properties properties = new Properties();
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            /** créer une nouvelle session d'authenticator*/       
            Authenticator authenticator = new Authenticator() {      	
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("rbneat@gmail.com", "Lotfi1234");
                }
            };
            /**Créer  une nouvelle session*/
            Session session = Session.getInstance(properties, authenticator);
            /** Créer un nouveau message d'email*/
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("rbneat@gmail.com"));
            InternetAddress[] toAddresses = { new InternetAddress(r.getGuestReservation().getUserGuest().getMail()) };
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject("Mail de confirmation");
            msg.setSentDate(new Date());
            msg.setText("Merci pour vote commande chez Rbneat ! \n \n \n "
            		+ "Monsieur " + r.getGuestReservation().getUserGuest().getNom() + " " + r.getGuestReservation().getUserGuest().getPrenom()
            		+ "\nVotre réservation est: " + r.getId()+ "\n"
            		+ "Vous êtes un invité chez : " + r.getHostReservation().getUserHost().getNom() + " " + r.getHostReservation().getUserHost().getPrenom()
            		+ " Le " + r.getDate()
            		+ "Vous avez passer une coomande de " + r.getPrix()        		
            		+ "\n"
            		+ "\n "
            		+ "\n "
            		+ "A bientôt,\n "
            		+ "L'équipe Rbneat");
            /**l'envoie du mail*/
            Transport.send(msg);
        }
    
    /**Récupérer la liste des plats*/
    @GET
    @Path("/plats")
    @Produces({"application/json"}) 
    public Collection<Plat> getPlats() throws ClassNotFoundException{
         return em.createQuery("from Plat", Plat.class).getResultList();
     }
    
    
    /** Filtrage de plats par prix
     * On récupére la liste des plats qui ont un prix inférieur à celui passé en paramètre.*/
    @GET
    @Path("/platPrix/{prix}")
    @Produces({"application/json"}) 
    public Collection<Plat> getPlatByPrix(@PathParam("prix") String prix) throws ClassNotFoundException{
         return em.createQuery("select p from Plat p where p.prix <= ?", Plat.class).setParameter(1,prix).getResultList();
     }
    
    
    /*Filtrage de plats par pays
     * On récupére la liste des platsd'un pays passé en paramètre.
     */
    @GET
    @Path("/platPays/{pays}")
    @Produces({"application/json"}) 
    public Collection<Plat> getPlatByPays(@PathParam("pays") String pays) throws ClassNotFoundException{
         return em.createQuery("select p from Plat p where p.pays = ?", Plat.class).setParameter(1,pays).getResultList();
     }
    
    /*Filtrage de plats par pays et prix
     * On récupére la liste des plats qui ont un prix inférieur à celui passé en paramètre 1.
     * et de même pays que celui passé en paramètre 2
     * */
    @GET
    @Path("/platPaysPrix/{pays}/{prix}")
    @Produces({"application/json"}) 
    public Collection<Plat> getPlatByPaysPrix(@PathParam("pays") String pays, @PathParam("prix") String prix) throws ClassNotFoundException{
         return em.createQuery("select p from Plat p where p.pays = ? and p.prix < ?", Plat.class).setParameter(1,pays).setParameter(2,prix).getResultList();
     }
    
    /* Valider si le mail n'existe pas bien avant*/ 
    public boolean validateMail(User user) {
		Query q = em.createQuery("select u from User u where u.mail=?", User.class);		
		return q.setParameter(1, user.getMail()).getResultList().size()==0;
	}
    
    
    /* Voir si un untilisateur est inscrit avant */
    public boolean validateLogin(Login loginBean) throws ClassNotFoundException {	
		Query q = em.createQuery("select u from User u where u.mail=? and u.password=? ", User.class);
		q.setParameter(1, loginBean.getUsername());
		q.setParameter(2,loginBean.getPassword());
		return q.getResultList().size()==1;
	} 
    
    
    /* Puisque les id sont générés automatiquement et on n'a pas la notion
     * de session, à chaque fois lorsqu'on veut appliquer des fonctionnalités on donne
     * l'email et à partir de lui on récupére l'id
     * */
  
    public int getIdByMail(String mail){
    	int id = 0;
    	Query q = em.createQuery("select u from User u where u.mail=? ", User.class);
    	@SuppressWarnings("unchecked")
    	List<User> users = q.setParameter(1, mail).getResultList();
    	User u = users.get(0);
    	id = u.getId();
    	return id;
    }
    
    /**On récupére la liste des hotes**/
    @GET
    @Path("/hosts")
    @Produces({"application/json"}) 
    public Collection<Host> getHosts() throws ClassNotFoundException{
         return em.createQuery("select h from Host h", Host.class).getResultList();
    }
    
    /**On recupere la liste des plat associe à un hote donné par son mail*/
    @GET 
    @Path("/platHosts/{mail}")
    @Produces({"application/json"})
    /*Recuperer la liste des commentaires*/
    public Collection<Plat> getPlatHosts(@PathParam("mail") String mail) {
    	int id = getIdByMail(mail);
    	Host h = em.find(Host.class, id);
    	return h.getPlats();
    }
    
    /*Ajouter une commentaire à un hote à partir de son mail qui est donné comme 
     * un champs dans l*/
    @POST
    @Path("/addComment")
    @Consumes({"application/json"})
    public void addCommentaire( Commentaire comment ) {
   		em.persist(comment); 
   		String mail = comment.getMail();
   		int id = getIdByMail(mail);
   		Host h = em.find(Host.class, id);
   	    h.getComments().add(comment);
   	    comment.setHostComt(h);
		em.merge(h);
		em.merge(comment);
    }
    
    
    /* Récupérer la liste des commentaire associé à un hote à partir de son mail*/  
    @GET 
    @Path("/commentaire/{mail}")
    @Produces({"application/json"})
    /*Recuperer la liste des commentaires*/
    public Collection<Commentaire> getComentsByMail(@PathParam("mail") String mail) {
    	int id = getIdByMail(mail);
    	Host h = em.find(Host.class, id);
    	return h.getComments();
    }
	
    /**Puisque des fois on veut changer la photo associé à un plat
     * on récupére le plat à partir de son description et mail*/
    @PUT
    @Path("/update/{description}/{mail}/{photo}")
    @Consumes({"application/json"})
    public void associeIdPlat(@PathParam("description") String descp, @PathParam("mail") String mail, @PathParam("photo") String photo) {
    	Query q = em.createQuery("select p from Plat p where p.mail=? and p.description=? ", Plat.class);
		q.setParameter(1, mail);
		q.setParameter(2, descp);
		@SuppressWarnings("unchecked")
		List<Plat> plats = q.getResultList();
    	Plat p = plats.get(0);
    	p.setPhoto(photo);
    	em.merge(p);
    }
    
    /**A partir de la description d'un plat on récupére la hôte associe
     * Cette fonction va être utilise lorsqu'on veut ajouter une reservation.*/
    public Host getHostByDescription(String descp) {
    	Query q = em.createQuery("select p from Plat p where p.description=? ", Plat.class);
		q.setParameter(1, descp);
		@SuppressWarnings("unchecked")
		List<Plat> plats = q.getResultList();
    	Plat p = plats.get(0);
    	return p.getHostPlat();
    }
    
    /** Vérifie si un utilisateur est déjà ajouté comme un invité.*/
    public Boolean isNotAlreadyGuest(int id) {  	 	
    	Query q = em.createQuery("select g from Guest g where g.id=? ", Guest.class);
    	@SuppressWarnings("unchecked")
    	List<Guest> guests = q.setParameter(1, id).getResultList();
    	return guests.size() ==0;
    }
    
    /** Récupérer le plat associé à une description donnée en paramètre*/
    public Plat platByDescp(String descp) {
	    Query q = em.createQuery("select p from Plat p where p.description=? ", Plat.class);
		@SuppressWarnings("unchecked")
		List<Plat> plats = q.setParameter(1, descp).getResultList();
		return plats.get(0);
		
    }
    
    @POST
    @Path("/reservation/{description}/{mail}")
    @Consumes({"application/json"})
    public void reservation(@PathParam("description") String descp, @PathParam("mail") String mail) throws AddressException, MessagingException {
    	Query q = em.createQuery("select u from User u where u.mail=? ", User.class);
    	User user = (User) q.setParameter(1, mail).getSingleResult();
    	if(user !=null) {
    		int id = user.getId();
	    	if (isNotAlreadyGuest(id)) {
		    	Guest g = new Guest();
		    	g.setUserGuest(user);
		    	g.setId(user.getId());
		    	em.persist(g);
		    	Reservation r = new Reservation();
		    	r.setId(user.getId());
		    	Guest h = em.find(Guest.class, user.getId());
		    	r.setGuestReservation(h);
		    	r.setHostReservation(getHostByDescription(descp));
		        r.setPlats(new HashSet<>());
		 		em.persist(r);
		 		reservationMail(r);
	    	}
    	}
    	
    }
    
    
    /**Lorsque un invite ajoute le plat, on récupére le plat par sa description
     * et l'invité par son mail.*/
    @PUT
    @Path("/reservation1/{description}/{mail}")
    @Consumes({"application/json"})
    public void reservation1(@PathParam("description") String descp, @PathParam("mail") String mail) {
    	Query q = em.createQuery("select u from User u where u.mail=? ", User.class);
    	User user = (User) q.setParameter(1, mail).getSingleResult();
    	if(user !=null) {
    		int id = user.getId();
	    	if (!isNotAlreadyGuest(id)) {
		    	Reservation r = em.find(Reservation.class, id);
		    	r.getPlats().add(platByDescp(descp));
		 		em.persist(r);
	    	}
    	}
    }
    
    /** Récuperer la liste des plats pour une réservation donnée.**/
    @GET 
    @Path("/platReserv/{id}")
    @Produces({"application/json"})
    /*Recuperer la liste des commentaires*/
    public Collection<Plat> getPlatsReserv(@PathParam("id") int id) {
    	Reservation r = em.find(Reservation.class, id);
    	System.out.println(r.getPlats() + "3" + r.getId() + " 2 " + r.getGuestReservation());
    	r.setDate("hhhh");
    	return r.getPlats();
    }
    
}
