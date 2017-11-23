package tcd.sweng.barcodetracker.data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by cal on 16/11/16.
 */

public class Project implements Serializable
{
    int id;
    String name;
    long endDate;

    // HashSets containing the ID's (Use ResultSet to get the Objects!)
    HashSet<Integer> members = new HashSet<>();
    HashSet<Integer> thingies = new HashSet<>();

    public Project(int id, String name, long endDate)
    {
        this.id = id;
        this.name = name;
        this.endDate = endDate;
    }

    public void addIndividual(int id)
    {
        members.add(id);
    }

    public void addThingy(int id)
    {
        thingies.add(id);
    }

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public long getEndDate()
    {
        return endDate;
    }

    public HashSet<Integer> getMembers()
    {
        return members;
    }

    public HashSet<Integer> getThingies()
    {
        return thingies;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
