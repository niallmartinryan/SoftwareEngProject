package tcd.sweng.barcodetracker.database;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.sql.PreparedStatement;
import java.util.ArrayList;

import tcd.sweng.barcodetracker.BarcodeTracker;
import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;

public class RequestActivity extends AppCompatActivity
{
    BarcodeTracker barcodeTracker;
    ResultSetOfCal resultSet = new ResultSetOfCal();
    public static ArrayList<PreparedStatement> queries;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        barcodeTracker = (BarcodeTracker) getApplication();

        AsyncTask.execute(new Runnable()
        {
            @Override
            public void run()
            {
                for (final PreparedStatement stmt : queries)
                {
                    barcodeTracker.getDatabase().executeQuery(stmt, resultSet);
                }
                finishWithResult();
            }
        });
    }

    private void finishWithResult()
    {
        Bundle result = new Bundle();
        result.putSerializable("results", resultSet);
        Intent intent = new Intent();
        intent.putExtras(result);
        setResult(RESULT_OK, intent);
        finish();
    }
}
