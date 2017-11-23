package tcd.sweng.barcodetracker.database;

import android.os.AsyncTask;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import tcd.sweng.barcodetracker.results.ResultSetOfCal;

/**
 * Created by cal on 15/11/16.
 * <p>
 * Database Tables:
 * <p>
 * =projects=
 * [projectId,projectName,enddate]
 * <p>
 * =individuals=
 * [personId,personName]
 * <p>
 * =assignments=
 * [individualId,projectId]
 * <p>
 * =objects=
 * [objectId,objectName,individualId,projectId,barcode,damaged,damagedDate]
 */

public class Database
{
    private boolean connected = false;
    private Connection conn;

    public void connect()
    {
        new ConnectTask().execute("jdbc:h2:tcp://149.56.225.34:993/~/database.db");
    }

    public Connection getConnection()
    {
        return conn;
    }

    private class ConnectTask extends AsyncTask<String, Void, Boolean>
    {
        protected Boolean doInBackground(String... urls)
        {
            connect(urls[0]);
            return true;
        }

        protected void onPostExecute(Boolean result)
        {
            connected = result;
        }
    }

    private void connect(String url)
    {
        if (connected)
            return;
        try
        {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        try
        {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        System.out.println("Successfully connected to local database!");
//        ExampleDatabase.dropAllTables(conn, true, true);
//        ExampleDatabase.createTables(conn);
//        ExampleDatabase.populateExampleData(conn);
    }

    public void execute(PreparedStatement stmt)
    {
        try
        {
            stmt.execute();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    public void executeQuery(PreparedStatement stmt, ResultSetOfCal resultSetOfCal)
    {
        try
        {
            ResultSet resultSet = stmt.executeQuery();
            while (resultSet.next())
            {
                try // People
                {
                    int id = resultSet.getInt("personId");
                    String name = resultSet.getString("personName");
                    resultSetOfCal.addPerson(id, name);
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
                try // Projects
                {
                    int id = resultSet.getInt("projectId");
                    String name = resultSet.getString("projectName");
                    long enddate = resultSet.getLong("enddate");
                    resultSetOfCal.addProject(id, name, enddate);
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
                try // Things
                {
                    int id = resultSet.getInt("objectId");
                    String name = resultSet.getString("objectName");
                    int ownerId = resultSet.getInt("individualId");
                    Integer projectId = resultSet.getInt("projectId");
                    String barcode = resultSet.getString("barcode");
                    boolean damaged = resultSet.getBoolean("damaged");
                    Long damagedDate = null;
                    try
                    {
                        damagedDate = resultSet.getLong("damagedDate");
                    } catch (Exception e)
                    {

                    }
                    long date = resultSet.getLong("attachedDate");
                    resultSetOfCal.addThing(id, name, ownerId, projectId, barcode, damaged, damagedDate, date);
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
                try // People/Project Relationships
                {
                    int personId = resultSet.getInt("individualId");
                    int projectId = resultSet.getInt("projectId");
                    resultSetOfCal.assignPersonToProject(personId, projectId);
                } catch (SQLException e)
                {
                    e.printStackTrace();
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void disconnect()
    {
        if (connected)
        {
            try
            {
                conn.close();
            } catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
    }
}
