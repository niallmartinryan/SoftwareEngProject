package tcd.sweng.barcodetracker.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

import tcd.sweng.barcodetracker.data.Individual;
import tcd.sweng.barcodetracker.data.Project;
import tcd.sweng.barcodetracker.data.Thingy;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;

/**
 * Created by cal on 17/11/16.
 */

public class ExampleDatabase
{
    public static void createTables(Connection conn)
    {
        System.out.println("Making sure all tables exist...");
        if (!tableExists("projects", conn))
        {
            try
            {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE projects " +
                        "(`projectId` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "`projectName` VARCHAR(255) NOT NULL," +
                        "`enddate` LONG NOT NULL," +
                        "PRIMARY KEY (projectId))";
                stmt.executeUpdate(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (!tableExists("individuals", conn))
        {
            try
            {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE individuals " +
                        "(`personId` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "`personName` VARCHAR(255) NOT NULL," +
                        "PRIMARY KEY (personId))";
                stmt.executeUpdate(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (!tableExists("assignments", conn))
        {
            try
            {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE assignments " +
                        "(`individualId` INT NOT NULL," +
                        "`projectId` INT NOT NULL," +
                        "PRIMARY KEY (individualId, projectId))";
                stmt.executeUpdate(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        if (!tableExists("objects", conn))
        {
            try
            {
                Statement stmt = conn.createStatement();
                String sql = "CREATE TABLE objects " +
                        "(`objectId` INT NOT NULL AUTO_INCREMENT PRIMARY KEY," +
                        "`objectName` VARCHAR(255) NOT NULL," +
                        "`individualId` INT NOT NULL, " +
                        "`projectId` INT NOT NULL, " +
                        "`damaged` BOOLEAN NOT NULL default 0," +
                        "`damagedDate` LONG," +
                        "`barcode` VARCHAR(255) NOT NULL," +
                        "`attachedDate` LONG NOT NULL," +
                        "PRIMARY KEY (objectId))";
                stmt.executeUpdate(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println("Table creation finished!");
    }

    public static void dropAllTables(Connection conn, boolean areYouSure, boolean areYouREALLYSure)
    {
        if (areYouSure && areYouREALLYSure)
        {
            try
            {
                Statement stmt = conn.createStatement();
                String sql = "DROP TABLE projects, individuals, assignments, objects";
                stmt.execute(sql);
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void populateExampleData(Connection conn)
    {
        ResultSetOfCal exampleData = new ResultSetOfCal();

        Map<Integer, Project> projects = exampleData.getProjects();
        projects.put(1, new Project(1, "The Dark Lord", Long.MAX_VALUE));
        projects.put(5, new Project(5, "Tis merely a scratch", Long.MAX_VALUE));
        projects.get(5).addIndividual(3);
        projects.get(5).addIndividual(1);
        projects.get(1).addIndividual(1);

        Map<Integer, Individual> individuals = exampleData.getPeoples();
        individuals.put(1, new Individual(1, "Tom Riddle"));
        individuals.get(1).addProject(1);
        individuals.put(2, new Individual(2, "Albus Dumbledore"));

        Map<Integer, Thingy> thingies = exampleData.getThingies();
        thingies.put(1, new Thingy(1, "Diary", 1, 1, "123", false, null, 1482192000000l));
        thingies.put(2, new Thingy(2, "Ring", 3, 2, "123", false, null, 1482192000000l));
        thingies.put(3, new Thingy(3, "Cup", 3, 2, "123", false, null, 1482192000000l));
        thingies.put(4, new Thingy(4, "Locket", 1, 2, "123", true, 1482192000000l, 1482192000000l));
        thingies.put(5, new Thingy(5, "Diadem", 2, 1, "123", false, null, 1482192000000l));
        thingies.put(6, new Thingy(6, "Mr Potter", 2, 1, "123", true, null, 1482192000000l));
        thingies.put(7, new Thingy(7, "Prof Quirrell", 1, 1, "123", true, 1482192000000l, 1482192000000l));
        thingies.put(8, new Thingy(8, "Nagini", 1, 1, "123", false, null, 1482192000000l));
        projects.get(5).addThingy(2);
        projects.get(5).addThingy(3);
        projects.get(5).addThingy(4);
        for (int i = 1; i < 9; i++)
        {
            projects.get(1).addThingy(i);
            individuals.get(1).addThingy(i);
        }

        ArrayList<PreparedStatement> statements = exampleData.toSQLCreation(conn);
        for (PreparedStatement statement : statements)
        {
            try
            {
                statement.execute();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }

    private static boolean tableExists(String table, Connection conn)
    {
        try
        {
            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM " + table + " LIMIT 1";
            stmt.executeQuery(sql);
            System.out.println(table + " exists!");
            return true;
        } catch (SQLException ex)
        {
            System.out.println(table + " doesn't exist!");
            return false;
        }
    }
}
