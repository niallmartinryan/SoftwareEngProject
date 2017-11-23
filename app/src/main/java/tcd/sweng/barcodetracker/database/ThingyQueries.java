package tcd.sweng.barcodetracker.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import tcd.sweng.barcodetracker.data.Thingy;

/**
 * Created by cal on 17/11/16.
 */

public class ThingyQueries
{
    public static PreparedStatement addThingy(Thingy thingy, Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO objects (objectId, objectName, individualId, projectId, barcode, damaged, damagedDate, attachedDate) VALUES (?,?,?,?,?,?,?,?)");
            stmt.setInt(1, thingy.getId());
            stmt.setString(2, thingy.getName());
            stmt.setInt(3, thingy.getOwner());
            if (thingy.getProject() != null)
                stmt.setInt(4, thingy.getProject());
            else
                stmt.setNull(4, Types.INTEGER);
            stmt.setString(5, thingy.getBarcode());
            stmt.setBoolean(6, thingy.isDamaged());
            if (thingy.getDamagedDate() != null)
                stmt.setLong(7, thingy.getDamagedDate());
            else
                stmt.setNull(7, Types.INTEGER);
            stmt.setLong(8, thingy.getAttachedDate());
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement allObjects(Connection conn)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM objects");
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement listObjects(Connection conn, Long date, String project, String person, boolean damaged, boolean checkAssigned)
    {
        String statementToAdd = "SELECT * FROM OBJECTS, PROJECTS, INDIVIDUALS WHERE OBJECTS.PROJECTID = PROJECTS.PROJECTID ";
        if (date != null)
        {
            if (damaged)
            {
                statementToAdd += "AND OBJECTS.DAMAGEDDATE <= " + date + " ";
            } else if (checkAssigned)
            {
                statementToAdd += "AND OBJECTS.ATTACHEDDATE >= " + date + " ";
            } else
            {
                statementToAdd += "AND PROJECTS.ENDDATE <= " + date + " ";
            }
        }
        if (project != null)
        {
            statementToAdd += "AND PROJECTS.PROJECTNAME is '" + project + "' ";
        }
        if (person != null)
        {
            statementToAdd += "AND OBJECTS.INDIVIDUALID = INDIVIDUALS.PERSONID AND INDIVIDUALS.PERSONNAME is '" + person + "' ";
        }
        statementToAdd += "AND DAMAGED=" + damaged + "";
        System.out.println(statementToAdd);
        try
        {
            PreparedStatement stmt = conn.prepareStatement(statementToAdd);
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static PreparedStatement isScannedThingyInDatabase(Connection conn, String barcode)
    {
        try
        {
            PreparedStatement stmt = conn.prepareStatement("SELECT * from OBJECTS " +
                    "WHERE '" + barcode + "' is OBJECTS.BARCODE ");
            System.out.println("statement - " + stmt);
            return stmt;
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return null;
    }


}
