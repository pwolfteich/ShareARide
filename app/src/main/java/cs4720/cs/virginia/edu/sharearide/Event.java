package cs4720.cs.virginia.edu.sharearide;

/**
 * Created by McNulty-PC on 9/29/2015.
 */
public class Event implements Comparable{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String name;
    public String date;
    public String location;
    public String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int id;

    public Event(){

    }
    public Event(int id, String name, String date, String location, String desc){
        this.id=id;
        this.name=name;
        this.date=date;
        this.location=location;
        this.description=desc;
    }

    @Override
    public int compareTo(Object another) {
        return this.name.compareTo(((Event)another).name);
    }
}
