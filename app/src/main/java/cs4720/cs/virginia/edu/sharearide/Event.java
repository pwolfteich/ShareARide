package cs4720.cs.virginia.edu.sharearide;

/**
 * Created by McNulty-PC on 9/29/2015.
 */

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseUser;
import java.util.ArrayList;

@ParseClassName("Event")
public class Event extends ParseObject implements Comparable {

    // ParseObject

    public String getName() {
        String name = getString("name");
        if (name == null || name.equals("")) {
            return this.name;
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
        put("name", name);
    }

    public String getDate() {
        String date = getString("date");
        if (date == null || date.equals("")) {
            return this.date;
        }
        return date;
    }

    public void setDate(String date) {
        this.date = date;
        put("date", date);
    }

    public String getLocation() {
        String location = getString("location");
        if (location == null || location.equals("")) {
            return this.location;
        }
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
        put("location", location);
    }

    public String getDescription() {
        String description = getString("description");
        if (description == null || description.equals("")) {
            return this.description;
        }
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        put("description", description);
    }

    public ParseUser getHost() {
        ParseUser host = (ParseUser)get("host");
        return host;
    }

    public void setHost(ParseUser host) {
        put("host", host);
    }

    public ArrayList<ParseUser> getInvitees() {
        ArrayList<ParseUser> invitees = (ArrayList<ParseUser>)get("invitees");
        if (invitees == null) {
            return this.invitees;
        } else {
            return invitees;
        }
    }

    public void setInvitees(ArrayList<ParseUser> invitees) {
        this.invitees = invitees;
        put("invitees", invitees);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassengers(ArrayList<ParseUser> passengers) {
        put("passengers", passengers);
    }

    public ArrayList<ParseUser> getPassengers() {
        return (ArrayList<ParseUser>)get("passengers");
    }

    public void setDrivers(ArrayList<ParseUser> drivers) {
        put("drivers", drivers);
    }

    public ArrayList<ParseUser> getDrivers() {
        return (ArrayList<ParseUser>)get("drivers");
    }

    public void setPassengersWithRides(ArrayList<ParseUser> passengersWithRides) {
        put("passengersWithRides", passengersWithRides);
    }

    public ArrayList<ParseUser> getPassengersWithRides() {
        return (ArrayList<ParseUser>)get("passengersWithRides");
    }

    public String name;
    public String date;
    public String location;
    public String description;
    public ArrayList<ParseUser> invitees;

    public int id;

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public boolean expanded;

    public Event() {

    }

    public Event(int id, String name, String date, String location, String desc){
        this.id=id;
        setName(name);
        setDate(date);
        setLocation(location);
        setDescription(desc);
        invitees = new ArrayList<ParseUser>();
        setInvitees(new ArrayList<ParseUser>());
        setPassengers(new ArrayList<ParseUser>());
        setDrivers(new ArrayList<ParseUser>());
        setPassengersWithRides(new ArrayList<ParseUser>());
        this.expanded = false;
    }
    public String toString()
    {
        return name;
    }

    @Override
    /*
    public int compareTo(Object another) {
        return this.name.compareTo(((Event)another).name);
    }*/
    public int compareTo(Object another) {
        return this.getObjectId().compareTo(((Event)another).getObjectId());
    }
}
