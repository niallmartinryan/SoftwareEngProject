package tcd.sweng.barcodetracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import tcd.sweng.barcodetracker.database.IndividualQueries;
import tcd.sweng.barcodetracker.database.ProjectQueries;
import tcd.sweng.barcodetracker.database.RequestActivity;
import tcd.sweng.barcodetracker.database.ThingyQueries;
import tcd.sweng.barcodetracker.detail.ObjectDetail;
import tcd.sweng.barcodetracker.list.PeopleListActivity;
import tcd.sweng.barcodetracker.list.ProjectListActivity;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;
import tcd.sweng.barcodetracker.update.AddPersonActivity;
import tcd.sweng.barcodetracker.update.AddProjectActivity;
import tcd.sweng.barcodetracker.update.ObjectQueryActivity;

public class MainActivity extends AppCompatActivity
{

    BarcodeTracker barcodeTracker;
    private final static int LOAD_PEOPLE = 1;
    private final static int LOAD_PROJECTS = 17;
    private final static int LOAD_RECLAIM = 100;
    private final static int LOAD_SCANNED_OBJECT = 31;       //CHECKS IF CURRENTLY SCANNED OBJECT IS IN DATABASE
    private IntentResult resultScan ;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Get the main application class
        barcodeTracker = (BarcodeTracker) getApplication();
        barcodeTracker.getDatabase().connect();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button scanButton = (Button) findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        launchBarcodeScanner();
                    }
                });

            }
        });

        final Button listAllPeople = (Button) findViewById(R.id.peopleButton);
        listAllPeople.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listAllPeople();
                    }
                });
            }
        });

        final Button listAllProjects = (Button) findViewById(R.id.projectButton);
        listAllProjects.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listAllProjects();
                    }
                });
            }
        });
        Button reclaimActivityButton = (Button) findViewById(R.id.toReclaimActivityButton);
        reclaimActivityButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                launchReclaimActivity();
            }
        });

        Button checkAssignmentsButton = (Button) findViewById(R.id.checkAssignmentsButton);
        checkAssignmentsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                launchCheckAssignmentsActivity();
            }
        });

        Button tempAddPersonButton = (Button) findViewById(R.id.tempAddPersonButton);
        tempAddPersonButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                launchTempPersonActivity();
            }
        });

        Button tempAddProjectButton = (Button) findViewById(R.id.tempAddProjectButton);
        tempAddProjectButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                launchTempProjectActivity();
            }
        });
    }

    public void listAllPeople()
    {
        RequestActivity.queries = new ArrayList<>();
        RequestActivity.queries.add(IndividualQueries.listAllPeople(barcodeTracker.getDatabase().getConnection()));
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult(intent, LOAD_PEOPLE);
    }


    public void listAllProjects()
    {
        RequestActivity.queries = new ArrayList<>();
        RequestActivity.queries.add(ProjectQueries.getAllProjects(barcodeTracker.getDatabase().getConnection()));
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult(intent, LOAD_PROJECTS);
    }

    public void launchReclaimActivity()
    {
        Intent intent = new Intent(this, ObjectQueryActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("context", ObjectQueryActivity.Context.RECLAIM);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void launchCheckAssignmentsActivity()
    {
        Intent intent = new Intent(this, ObjectQueryActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("context", ObjectQueryActivity.Context.ASSIGNMENT);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void launchPeopleList(ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, PeopleListActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("results", resultSetOfCal);
        extras.putSerializable("context", PeopleListActivity.Context.DISPLAY);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void launchProjectList(ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, ProjectListActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("results", resultSetOfCal);
        extras.putSerializable("context", ProjectListActivity.Context.DISPLAY);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void launchBarcodeScanner()
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    //Use the temp button to debug stuff
    public void launchTempPersonActivity()
    {
        Intent intent = new Intent(this, AddPersonActivity.class);
        startActivity(intent);
    }

    //Use another temp button to debug stuff
    public void launchTempProjectActivity()
    {
        Intent intent = new Intent(this, AddProjectActivity.class);
        startActivity(intent);
    }

    // Get barcode results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null)
        {
            if (result.getContents() == null)
            {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else
            {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                // Sample Object Detail
                System.out.println("Format: " + result.getFormatName());
                // Here I need to access the database and check if the barcode is in the database..
                resultScan = result;
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        listObjectsOfBarcode(resultScan.getContents(), resultScan.getFormatName());
                    }
                });

                //displayObjectDetail(result.getContents(), result.getFormatName());
            }
        } else if (requestCode == LOAD_PEOPLE)
        {
            launchPeopleList((ResultSetOfCal) data.getExtras().get("results"));
        } else if (requestCode == LOAD_PROJECTS)
        {
            launchProjectList((ResultSetOfCal) data.getExtras().get("results"));
        } else if (requestCode == LOAD_SCANNED_OBJECT)
        {
            displayObjectDetail((ResultSetOfCal) data.getExtras().get("results"), resultScan.getContents(),resultScan.getFormatName());
        } else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void displayObjectDetail(ResultSetOfCal resultSetOfCal,String data, String format)
    {
        Intent intent = new Intent(this, ObjectDetail.class);
        Bundle extras = new Bundle();
        extras.putSerializable("object", resultSetOfCal);
        extras.putString("data", data);
        extras.putString("format", format);
        intent.putExtras(extras);
        startActivity(intent);
    }
    public void listObjectsOfBarcode(String data, String format)
    {
        RequestActivity.queries = new ArrayList<>();
        RequestActivity.queries.add(ThingyQueries.isScannedThingyInDatabase(barcodeTracker.getDatabase().getConnection(),data));
        Intent intent = new Intent(this, RequestActivity.class);
        startActivityForResult(intent, LOAD_SCANNED_OBJECT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
