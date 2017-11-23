package tcd.sweng.barcodetracker.data;

import java.io.Serializable;
import java.util.HashSet;

/**
 * Created by cal on 16/11/16.
 */

public class Individual implements Comparable<Individual>, Serializable
{
    int id;
    String name;

    // HashSets containing the ID's (Use ResultSet to get the Objects!)
    HashSet<Integer> projects = new HashSet<>();
    HashSet<Integer> thingies = new HashSet<>();

    public Individual(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    public void addProject(int id)
    {
        projects.add(id);
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

    public HashSet<Integer> getProjects()
    {
        return projects;
    }

    public HashSet<Integer> getThingies()
    {
        return thingies;
    }

    @Override
    public int compareTo(Individual individual)
    {
        return name.compareTo(individual.getName());
    }

    @Override
    public String toString()
    {
        return name;
    }
}
