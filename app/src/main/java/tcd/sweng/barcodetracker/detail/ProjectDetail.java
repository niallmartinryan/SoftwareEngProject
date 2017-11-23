package tcd.sweng.barcodetracker.detail;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import tcd.sweng.barcodetracker.BarcodeTracker;
import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.data.Project;
import tcd.sweng.barcodetracker.database.IndividualQueries;
import tcd.sweng.barcodetracker.database.ProjectQueries;
import tcd.sweng.barcodetracker.database.RequestActivity;
import tcd.sweng.barcodetracker.list.PeopleListActivity;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;

public class ProjectDetail extends AppCompatActivity {

    private final static int LOAD_PEOPLE = 1, SELECT_PEOPLE = 2;

    Project project;
    BarcodeTracker barcodeTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        // Get the main application class
        barcodeTracker = (BarcodeTracker) getApplication();

        // Object representing the project being displayed
        project = (Project) getIntent().getExtras().get("project");

        setActionBarTitle(project.getName());
        setDate(project.getEndDate());
        final Button listPeople = (Button) findViewById(R.id.addPeopleButton);

        listPeople.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listPeople();
                    }
                });
            }
        });

    }

    public void listPeople()
    {
        RequestActivity.queries = new ArrayList<>();
        RequestActivity.queries.add(IndividualQueries.listAllPeople(barcodeTracker.getDatabase().getConnection()));
        RequestActivity.queries.add(ProjectQueries.getAllProjects(barcodeTracker.getDatabase().getConnection()));
        RequestActivity.queries.add(ProjectQueries.getAllAssignments(barcodeTracker.getDatabase().getConnection()));
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult(intent, LOAD_PEOPLE);
    }

    // Get barcode results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == LOAD_PEOPLE)
        {
            launchPeopleList((ResultSetOfCal) data.getExtras().get("results"));
        } else if (requestCode == SELECT_PEOPLE)
        {

        } else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void launchPeopleList(ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, PeopleListActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("results", resultSetOfCal);
        extras.putSerializable("context", PeopleListActivity.Context.MULTISELECT);
        extras.putInt("projectID", project.getId());
        intent.putExtras(extras);
        startActivityForResult(intent, SELECT_PEOPLE);
    }

    public void setActionBarTitle(String title)
    {
        getSupportActionBar().setTitle(title);
    }

    public String setDate(long dateLong)
    {
        Date date = new Date(dateLong);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }
}
