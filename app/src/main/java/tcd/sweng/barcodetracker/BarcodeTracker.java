package tcd.sweng.barcodetracker;

import android.app.Application;

import tcd.sweng.barcodetracker.database.Database;

/**
 * Created by cal on 15/11/16.
 */

public class BarcodeTracker extends Application
{
    private Database database;

    public BarcodeTracker()
    {
        database = new Database();
    }

    public Database getDatabase()
    {
        return database;
    }
}
