package cs4720.cs.virginia.edu.sharearide;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by harangju on 10/12/15.
 */
@ParseClassName("Ride")
public class Ride extends ParseObject {

    public ParseUser getDriver() {
        ParseUser driver = (ParseUser)get("driver");
        if (driver == null) {
            return this.driver;
        }
        return driver;
    }

    public void setDriver(ParseUser driver) {
        this.driver = driver;
        put("driver", driver);
    }

    public ArrayList<ParseUser> getPassengers() {
        ArrayList<ParseUser> passengers = (ArrayList<ParseUser>)get("passengers");
        if (passengers == null) {
            return this.passengers;
        }
        return passengers;
    }

    public void setPassengers(ArrayList<ParseUser> passengers) {
        this.passengers = passengers;
        put("passengers", passengers);
    }

    public Event getEvent() {
        Event event = (Event)get("event");
        if (event == null) {
            return this.event;
        }
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        put("event", event);
    }

    public void setSeats(int seats) {
        put("seats", seats);
    }

    public int getSeats() {
        return getInt("seats");
    }

    public ParseUser driver;
    public ArrayList<ParseUser> passengers;
    public Event event;

    public Ride() {
        //setPassengers(new ArrayList<ParseUser>());
    }

    public Ride(ParseUser driver, Event event) {
        setDriver(driver);
        setEvent(event);
        setPassengers(new ArrayList<ParseUser>());
    }

}
