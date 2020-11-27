package ds.gae.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.google.cloud.datastore.*;
import com.google.datastore.v1.PropertyFilter;

public class Car {

	/*
    private int id;
    private CarType carType;
    private Set<Reservation> reservations;
	*/
	
    Datastore datastore=DatastoreOptions.getDefaultInstance().getService();
    Key carKey=null;
    
    /***************
     * CONSTRUCTOR *
     ***************/
    		

    public Car(int uid, CarType type) {
        //this.id = uid;
    	//this.carType = carType;
    	//this.reservations = new HashSet<Reservation>();
    	
        Key carKey= datastore.newKeyFactory()
        		.addAncestors(PathElement.of("CarType", type.getName()))
        		.setKind("car")
        		.newKey(uid);
   
        
        Entity c = Entity.newBuilder(carKey)
        		.set("uid", uid)
    			.build();
    	datastore.put(c);
    	
        
    }
    
    public Key getKey() {
    	
    	return carKey;
    }

    /******
     * ID *
     ******/

    public int getId() {
    	Entity c= datastore.get(carKey);
    	
        return  c.getValue("uid");
    }

    /************
     * CAR TYPE *
     ************/

    public CarType getType() {
    	
    	
        return carType;
    }

    /****************
     * RESERVATIONS *
     ****************/

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public boolean isAvailable(Date start, Date end) {
        if (!start.before(end)) {
            throw new IllegalArgumentException("Illegal given period");
        }

        for (Reservation reservation : getReservations()) {
            if (reservation.getEndDate().before(start) || reservation.getStartDate().after(end)) {
                continue;
            }
            return false;
        }
        return true;
    }

    public void addReservation(Reservation res) {
        reservations.add(res);
    }

    public void removeReservation(Reservation reservation) {
        reservations.remove(reservation);
    }
}
