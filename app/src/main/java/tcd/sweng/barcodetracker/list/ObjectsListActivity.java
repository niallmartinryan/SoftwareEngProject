package tcd.sweng.barcodetracker.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;

public class ObjectsListActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reclaim_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ResultSetOfCal resultSetOfCal = (ResultSetOfCal) getIntent().getExtras().get("results");
        System.out.println(resultSetOfCal.getAllThingies());
        System.out.println(resultSetOfCal.getAllProjects());
        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(new ArrayAdapter<>(this, R.layout.project_list_layout, android.R.id.text1, resultSetOfCal.getAllThingies()));

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

    }
}
