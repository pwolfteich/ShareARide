package cs4720.cs.virginia.edu.sharearide;

/**
 * Created by McNulty-PC on 9/29/2015.
 */

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

    public String name;
    public String date;
    public String location;
    public String description;
    public ArrayList<ParseUser> invitees = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
        this.expanded = false;
    }
    public String toString()
    {
        return name;
    }

    @Override
    public int compareTo(Object another) {
        return this.name.compareTo(((Event)another).name);
    }
}
