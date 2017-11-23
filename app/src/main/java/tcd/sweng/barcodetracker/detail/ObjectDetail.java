package tcd.sweng.barcodetracker.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import tcd.sweng.barcodetracker.R;
import tcd.sweng.barcodetracker.data.Thingy;
import tcd.sweng.barcodetracker.generation.Barcode;
import tcd.sweng.barcodetracker.list.ObjectsListActivity;
import tcd.sweng.barcodetracker.results.ResultSetOfCal;
import tcd.sweng.barcodetracker.update.AddObjectActivity;

public class ObjectDetail extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_detail);

        ImageView qrCodeImage = (ImageView) findViewById(R.id.barcode);
        String data = (String) getIntent().getExtras().getString("data");
        ResultSetOfCal resultSetOfCal = (ResultSetOfCal) getIntent().getExtras().getSerializable("object");
        System.out.println("ResultSetOfCal - " + getIntent().getExtras().getSerializable("object"));
        ArrayList<Thingy> thingies = resultSetOfCal.getAllThingies();
        if (thingies.isEmpty())
        {
            // need to add as it is not in the database
            launchAddObjectActivity(data, resultSetOfCal);
        } else
        {
            System.out.println("Its in the database");
            // Its in the database.. move to another screen showing the object
            launchListObjects(data, resultSetOfCal);
        }

        Bitmap qrCodeMap = Barcode.generateAutoBarcode(getIntent().getExtras().getString("data"), getIntent().getExtras().getString("format"), 800, 500);
        qrCodeImage.setImageBitmap(qrCodeMap);

        TextView barcodeValue = (TextView) findViewById(R.id.barcodeValue);
        barcodeValue.setText(getIntent().getExtras().getString("data"));

        TextView barcodeFormat = (TextView) findViewById(R.id.barcodeFormat);
        barcodeFormat.setText(getIntent().getExtras().getString("format"));
    }

    public void launchAddObjectActivity(String data, ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, AddObjectActivity.class);
        Bundle extras = new Bundle();
        extras.putString("data", data);
        extras.putSerializable("results", resultSetOfCal);
        //extras.putSerializable("context", ProjectListActivity.Context.DISPLAY);
        intent.putExtras(extras);
        startActivity(intent);
    }

    public void launchListObjects(String data, ResultSetOfCal resultSetOfCal)
    {
        Intent intent = new Intent(this, ObjectsListActivity.class);
        Bundle extras = new Bundle();
        extras.putSerializable("results", resultSetOfCal);
        extras.putString("data", data);
        intent.putExtras(extras);
        startActivity(intent);
    }
}
