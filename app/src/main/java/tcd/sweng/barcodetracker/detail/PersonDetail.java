package tcd.sweng.barcodetracker.detail;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import tcd.sweng.barcodetracker.BarcodeTracker;
import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.data.Individual;
import tcd.sweng.barcodetracker.database.ProjectQueries;
import tcd.sweng.barcodetracker.database.RequestActivity;
import tcd.sweng.barcodetracker.list.PeopleListActivity;
import tcd.sweng.barcodetracker.list.ProjectListActivity;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;

public class PersonDetail extends AppCompatActivity
{
    Individual person;
    BarcodeTracker barcodeTracker;
    private final static int LOAD_PROJECTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get the main application class
        barcodeTracker = (BarcodeTracker) getApplication();

        // Object representing the person being displayed
        person = (Individual) getIntent().getExtras().get("individual");

        setContentView(R.layout.activity_person);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setActionBarTitle(person.getName());

        final Button listProjects = (Button) findViewById(R.id.projectButton);
        listProjects.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listProjects();
                    }
                });
            }
        });

    }

    public void listProjects()
    {
        RequestActivity.queries = new ArrayList<>();
        RequestActivity.queries.add(ProjectQueries.getProjects(barcodeTracker.getDatabase().getConnection(), person.getId()));
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult(intent, LOAD_PROJECTS);

    }

    // Get barcode results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == LOAD_PROJECTS)
        {
            launchProjectList((ResultSetOfCal) data.getExtras().get("results"));
        } else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void launchProjectList(ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, ProjectListActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("results", resultSetOfCal);
        extras.putSerializable("context", ProjectListActivity.Context.MULTISELECT);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_person, menu);
        return true;
    }

    public void setActionBarTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }
}
