package tcd.sweng.barcodetracker.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import tcd.sweng.barcodetracker.data.Individual;

/**
 * Created by cal on 17/11/16.
 */

public class IndividualQueries
{
    public static PreparedStatement addIndividual(Individual individual, Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO individuals (personName) VALUES (?)");
            stmt.setString(1, individual.getName());
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement listAllPeople(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM individuals");
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement listPeople(Connection conn, int projectID)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT personId, personName " +
                            "FROM individuals JOIN assignments " +
                            "ON personId = individualId " +
                    "WHERE projectId = (?)"
            );
            stmt.setInt(1, projectID);
            System.out.println("stmt = "+ stmt);
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement joinProject(Individual individual, Integer projectID, Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO assignments (individualId, projectId) VALUES (?,?)");
            stmt.setInt(1, individual.getId());
            stmt.setInt(2, projectID);
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
