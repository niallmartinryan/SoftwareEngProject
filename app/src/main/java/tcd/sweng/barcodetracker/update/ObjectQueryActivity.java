package tcd.sweng.barcodetracker.update;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import tcd.sweng.barcodetracker.BarcodeTracker;
import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.data.Individual;
import tcd.sweng.barcodetracker.data.Project;
import tcd.sweng.barcodetracker.database.IndividualQueries;
import tcd.sweng.barcodetracker.database.ProjectQueries;
import tcd.sweng.barcodetracker.database.RequestActivity;
import tcd.sweng.barcodetracker.database.ThingyQueries;
import tcd.sweng.barcodetracker.list.PeopleListActivity;
import tcd.sweng.barcodetracker.list.ProjectListActivity;
import tcd.sweng.barcodetracker.list.ObjectsListActivity;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;

public class ObjectQueryActivity extends AppCompatActivity
{
    public enum Context
    {
        RECLAIM, ASSIGNMENT
    }

    Context currentContext = Context.RECLAIM;
    BarcodeTracker barcodeTracker;
    private final static int LOAD_PEOPLE = 1, SELECT_PEOPLE = 2;
    private final static int LOAD_OBJECTS = 100;
    private final static int LOAD_PROJECTS = 17, SELECT_PROJECT = 18;
    private boolean projectSelected;
    private boolean personSelected;
    Project project;
    Individual individual;

    Button pickProjectActivityButton, pickPeopleActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclaim);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentContext = (Context) getIntent().getExtras().get("context");

        barcodeTracker = (BarcodeTracker) getApplication();

        pickProjectActivityButton = (Button) findViewById(R.id.pickProjectActivityButton);
        pickProjectActivityButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        listAllProjects(individual);
                    }
                });
            }
        });
        pickPeopleActivityButton = (Button) findViewById(R.id.pickPersonActivityButton);

        pickPeopleActivityButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listAllPeople(project);
                    }
                });
            }
        });

        final Button pickProjectX = (Button) findViewById(R.id.pickProjectX);
        pickProjectX.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //// TODO: 09/12/16  
            }
        });
        final Button pickPeopleX = (Button) findViewById(R.id.pickPersonX);
        pickPeopleX.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //// TODO: 09/12/16  
            }
        });
        final CheckBox damagedCheckBox = (CheckBox) findViewById(R.id.damagedCheckBox);

        final DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker);

        Button searchButton = (Button) findViewById(R.id.searchReclaimActivityButton);
        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {   // fix date structure

                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        Calendar calendar = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        long date = calendar.getTimeInMillis();
                        String peopleSearch = (personSelected ? pickPeopleActivityButton.getText().toString() : null);
                        String projectSearch = (projectSelected ? pickProjectActivityButton.getText().toString() : null);
                        boolean damaged = damagedCheckBox.isChecked();
                        listObjects(date, projectSearch, peopleSearch, damaged);

                    }
                });

                Snackbar.make(view, "Going to pick a project", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == LOAD_OBJECTS)
        {
            launchReclaimListActivity((ResultSetOfCal) data.getExtras().get("results"));
        } else if (requestCode == LOAD_PROJECTS)
        {
            launchProjectList((ResultSetOfCal) data.getExtras().get("results"));
        } else if (requestCode == LOAD_PEOPLE)
        {
            launchPeopleList((ResultSetOfCal) data.getExtras().get("results"));
        } else if (requestCode == SELECT_PROJECT)
        {
            projectSelected = true;
            project = (Project) data.getExtras().get("project");
            System.out.println(project);
            pickProjectActivityButton.setText(project.getName());
            System.out.println("GOT DA PROJEC LK" + project.getName());
        } else if (requestCode == SELECT_PEOPLE)
        {
            personSelected = true;
            individual = (Individual) data.getExtras().get("individual");
            pickPeopleActivityButton.setText(individual.getName());
        }
    }

    public void listObjects(Long date, String project, String person, boolean damaged)
    {
        boolean checkAssigned = currentContext == Context.ASSIGNMENT;
        RequestActivity.queries = new ArrayList<>();
        if(currentContext == Context.RECLAIM)
            RequestActivity.queries.add(ThingyQueries.listObjects(barcodeTracker.getDatabase().getConnection(), date, project, person, damaged, checkAssigned));
        if(currentContext == Context.ASSIGNMENT)
            RequestActivity.queries.add(ThingyQueries.listObjects(barcodeTracker.getDatabase().getConnection(), date, project, person, damaged, checkAssigned));
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult(intent, LOAD_OBJECTS);
    }

    public void launchReclaimListActivity(ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, ObjectsListActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("results", resultSetOfCal);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void listAllProjects(Individual individual)
    {
        RequestActivity.queries = new ArrayList<>();
        if (individual == null)
        {
            RequestActivity.queries.add(ProjectQueries.getAllProjects(barcodeTracker.getDatabase().getConnection()));

        } else
        {
            RequestActivity.queries.add(ProjectQueries.getProjects(barcodeTracker.getDatabase().getConnection(), individual.getId()));
        }
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult(intent, LOAD_PROJECTS);
    }

    public void launchProjectList(ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, ProjectListActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("context", ProjectListActivity.Context.SINGLESELECT);
        extras.putSerializable("results", resultSetOfCal);
        intent.putExtras(extras);
        startActivityForResult(intent, SELECT_PROJECT);
    }

    public void listAllPeople(Project project)
    {
        RequestActivity.queries = new ArrayList<>();
        if (project == null)
        {
            RequestActivity.queries.add(IndividualQueries.listAllPeople(barcodeTracker.getDatabase().getConnection()));
        } else
        {
            RequestActivity.queries.add(IndividualQueries.listPeople(barcodeTracker.getDatabase().getConnection(), project.getId()));
        }
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult(intent, LOAD_PEOPLE);
    }

    public void launchPeopleList(ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, PeopleListActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("select", false);
        extras.putSerializable("context", PeopleListActivity.Context.SINGLESELECT);
        extras.putSerializable("results", resultSetOfCal);
        intent.putExtras(extras);
        startActivityForResult(intent, SELECT_PEOPLE);
    }
}
