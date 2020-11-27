package ds.gae.entities;

import java.util.Date;
import java.util.Objects;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.PathElement;

public class Reservation extends Quote {

    private int carId;
    
    Datastore datastore=DatastoreOptions.getDefaultInstance().getService();
	
    Key resKey=null;
    
    /***************
     * CONSTRUCTOR *
     ***************/
     
    public Reservation(Quote quote, int carId) {
        this(
                quote.getRenter(),
                quote.getStartDate(),
                quote.getEndDate(),
                quote.getRentalCompany(),
                quote.getCarType(),
                quote.getRentalPrice()
        );
        
    	//this.carId = carId;
        KeyFactory keyFactory= datastore.newKeyFactory().addAncestors(PathElement.of("Car", carId)).setKind("Reservation");
        Key resKey= datastore.allocateId(keyFactory.newKey());
           
        Entity r = Entity.newBuilder(resKey)
        		.set("carId", carId)
    			.set("Quote", quote.getKey())
    			.build();
    	datastore.put(r);
    }
    
    public Key getKey() {
    	return resKey;
    }

    private Reservation(
            String renter,
            Date start,
            Date end,
            String rentalCompany,
            String carType,
            double rentalPrice) {
        super(renter, start, end, rentalCompany, carType, rentalPrice);
    }

    /******
     * ID *
     ******/

    public int getCarId() {
        return carId;
    }

    /*************
     * TO STRING *
     *************/

    @Override
    public String toString() {
        return String.format(
                "Reservation for %s from %s to %s at %s\nCar type: %s\tCar: %s\nTotal price: %.2f",
                getRenter(),
                getStartDate(),
                getEndDate(),
                getRentalCompany(),
                getCarType(),
                getCarId(),
                getRentalPrice()
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getCarId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }
        Reservation other = (Reservation) obj;
        if (getCarId() != other.getCarId()) {
            return false;
        }
        return true;
    }
}
