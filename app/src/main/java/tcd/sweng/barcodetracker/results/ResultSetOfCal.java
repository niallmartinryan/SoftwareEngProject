package tcd.sweng.barcodetracker.results;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import tcd.sweng.barcodetracker.data.Individual;
import tcd.sweng.barcodetracker.data.Project;
import tcd.sweng.barcodetracker.data.Thingy;
import tcd.sweng.barcodetracker.database.IndividualQueries;
import tcd.sweng.barcodetracker.database.ProjectQueries;
import tcd.sweng.barcodetracker.database.ThingyQueries;

/**
 * Created by cal on 16/11/16.
 */

public class ResultSetOfCal implements Serializable
{
    // Hashmaps to map an ID to an Object
    Map<Integer, Individual> peoples = new HashMap<>();
    Map<Integer, Project> projects = new HashMap<>();
    Map<Integer, Thingy> thingies = new HashMap<>();

    public Map<Integer, Individual> getPeoples()
    {
        return peoples;
    }

    public Map<Integer, Project> getProjects()
    {
        return projects;
    }

    public Map<Integer, Thingy> getThingies()
    {
        return thingies;
    }

    public void addPerson(int id, String name)
    {
        peoples.put(id, new Individual(id, name));
    }

    public void addProject(int id, String name, long endDate)
    {
        projects.put(id, new Project(id, name, endDate));
    }

    public void addThing(int id, String name, int owner, Integer project, String barcode, boolean damaged, Long damagedDate, long attachedDate)
    {
        thingies.put(id, new Thingy(id, name, owner, project, barcode, damaged, damagedDate, attachedDate));
    }

    public void assignPersonToProject(int personId, int projectId)
    {
        if (peoples.get(personId) != null && projects.get(projectId) != null)
        {
            peoples.get(personId).addProject(projectId);
            projects.get(projectId).addIndividual(personId);
        }
    }
    public ArrayList<Thingy> getAllThingies()
    {
        ArrayList<Thingy> objects = new ArrayList<>(getThingies().values());
        return objects;
    }

    public ArrayList<Individual> getAllPeopleNames()
    {
        ArrayList<Individual> people = new ArrayList<>(getPeoples().values());
        Collections.sort(people);
        return people;
    }

    public ArrayList<Project> getAllProjects()
    {
        ArrayList<Project> projects = new ArrayList<>(getProjects().values());
        return projects;
    }

    public ArrayList<PreparedStatement> toSQLCreation(Connection conn)
    {
        ArrayList<PreparedStatement> SQL = new ArrayList<>();
        for (Project project : projects.values())
        {
            SQL.add(ProjectQueries.addProject(project, conn));
        }
        for (Individual individual : peoples.values())
        {
            SQL.add(IndividualQueries.addIndividual(individual, conn));
            for (Integer projectId : individual.getProjects())
            {
                SQL.add(IndividualQueries.joinProject(individual, projectId, conn));
            }
        }
        for (Thingy thingy : thingies.values())
        {
            SQL.add(ThingyQueries.addThingy(thingy, conn));
        }

        return SQL;
    }
}
