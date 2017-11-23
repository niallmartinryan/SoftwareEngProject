package tcd.sweng.barcodetracker.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.data.Individual;
import tcd.sweng.barcodetracker.detail.PersonDetail;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;

public class PeopleListActivity extends AppCompatActivity
{
    public enum Context
    {
        DISPLAY,
        SINGLESELECT,
        MULTISELECT
    }

    Context currentContext = Context.DISPLAY;
    ListView list;
    ResultSetOfCal resultSetOfCal;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        currentContext = (Context) getIntent().getExtras().get("context");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (currentContext != Context.DISPLAY)
        {
            fab.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    finishWithResult();
                }
            });
        }

        resultSetOfCal = (ResultSetOfCal) getIntent().getExtras().get("results");
        int projectID = getIntent().getExtras().getInt("projectID");

        list = (ListView) findViewById(R.id.listview);

        if (currentContext == Context.SINGLESELECT)
        {
            list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, resultSetOfCal.getAllPeopleNames()));
            list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        } else if (currentContext == Context.MULTISELECT)
        {
            list.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, resultSetOfCal.getAllPeopleNames()));
            list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            for (int i = 0; i < resultSetOfCal.getAllPeopleNames().size(); i++)
            {
                list.setItemChecked(i, resultSetOfCal.getAllPeopleNames().get(i).getProjects().contains(projectID));
            }
        } else if (currentContext == Context.DISPLAY)
        {
            list.setAdapter(new ArrayAdapter<>(this, R.layout.people_list_layout, android.R.id.text1, resultSetOfCal.getAllPeopleNames()));
            list.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
                {
                    launchPeopleDetail(resultSetOfCal.getAllPeopleNames().get(i));
                }
            });
        }
    }

    public void launchPeopleDetail(Individual individual)
    {
        Intent intent = new Intent(this, PersonDetail.class);
        Bundle extras = new Bundle();
        extras.putSerializable("individual", individual);
        intent.putExtras(extras);
        startActivity(intent);
    }

    private void finishWithResult()
    {
        Bundle result = new Bundle();

        if (currentContext == Context.SINGLESELECT)
        {
            result.putSerializable("individual", resultSetOfCal.getAllPeopleNames().get(list.getCheckedItemPosition()));
        } else if (currentContext == Context.MULTISELECT)
        {

            ArrayList<Integer> selected = new ArrayList<>();
            int len = list.getCount();
            SparseBooleanArray checked = list.getCheckedItemPositions();
            for (int i = 0; i < len; i++)
            {
                if (checked.get(i))
                {
                    selected.add(resultSetOfCal.getAllPeopleNames().get(i).getId());
                }
            }
            result.putSerializable("selected", selected);
        }
        Intent intent = new Intent();
        intent.putExtras(result);
        setResult(RESULT_OK, intent);
        finish();
    }
}
