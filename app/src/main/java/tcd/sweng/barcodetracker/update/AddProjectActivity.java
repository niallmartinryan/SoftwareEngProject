package tcd.sweng.barcodetracker.update;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.sql.PreparedStatement;
import java.util.Calendar;
import java.util.GregorianCalendar;

import tcd.sweng.barcodetracker.BarcodeTracker;
import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.data.Project;
import tcd.sweng.barcodetracker.database.ProjectQueries;

public class AddProjectActivity extends AppCompatActivity
{
    BarcodeTracker barcodeTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        barcodeTracker = (BarcodeTracker) getApplication();

        final EditText editText2 = (EditText) findViewById(R.id.editText2);
        final DatePicker editText3 = (DatePicker) findViewById(R.id.endDate);

        Button addToProjectsButton = (Button) findViewById(R.id.addPeopleButton);
        addToProjectsButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Button addButton = (Button) findViewById(R.id.addProjectButton);
        addButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String nameToAdd = editText2.getText().toString();

                Calendar calendar = new GregorianCalendar(editText3.getYear(), editText3.getMonth(), editText3.getDayOfMonth());
                long endDateToAdd = calendar.getTimeInMillis();

                final Project project = new Project(-1, nameToAdd, endDateToAdd);
                AsyncTask.execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        PreparedStatement stmt = ProjectQueries.addProject(project, barcodeTracker.getDatabase().getConnection());
                        barcodeTracker.getDatabase().execute(stmt);
                        finish();
                    }
                });
            }
        });
    }

}
