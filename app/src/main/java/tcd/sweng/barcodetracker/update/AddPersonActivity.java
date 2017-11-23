package tcd.sweng.barcodetracker.update;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.sql.PreparedStatement;

import tcd.sweng.barcodetracker.BarcodeTracker;
import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.data.Individual;
import tcd.sweng.barcodetracker.database.IndividualQueries;

public class AddPersonActivity extends AppCompatActivity
{
    BarcodeTracker barcodeTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        barcodeTracker = (BarcodeTracker) getApplication();

        final EditText editText = (EditText) findViewById(R.id.editText);


        Button addToProjectsButton = (Button) findViewById(R.id.button);
        addToProjectsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button addButton = (Button) findViewById(R.id.button2);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String nameToAdd = editText.getText().toString();
                final Individual individual = new Individual(-1, nameToAdd);
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        PreparedStatement stmt = IndividualQueries.addIndividual(individual, barcodeTracker.getDatabase().getConnection());
                        barcodeTracker.getDatabase().execute(stmt);
                        finish();
                    }
                });
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
