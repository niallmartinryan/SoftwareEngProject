package tcd.sweng.barcodetracker.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import tcd.sweng.barcodetracker.data.Project;

/**
 * Created by cal on 17/11/16.
 */

public class ProjectQueries
{
    public static PreparedStatement addProject(Project project, Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO projects (projectName, endDate) VALUES (?,?)");
            stmt.setString(1, project.getName());
            stmt.setLong(2, project.getEndDate());
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement getProjects(Connection conn, int personID)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM projects WHERE projectId = (?)");
            stmt.setInt(1, personID);
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement getAllProjects(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM projects");
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement getAllAssignments(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM assignments");
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
