package com.example.safaralialisultanov.realmexample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewDebug;
import android.widget.TextView;

/**
 * Created by safarali.alisultanov on 09.07.2016.
 */
public class PersonDetailsActivity extends AppCompatActivity {

    final static String LOG_TAG = "myLogs";
    private TextView tvPersonDetailId,tvPersonDetailName,tvPersonDetailEmail,tvPersonDetailAddress,tvPersonDetailAge;
    private PersonDetailsModel personDetailsModel=new PersonDetailsModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOG_TAG, "PersonDetailsActivity.onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getAllWidgets();
        getDataFromPreviousActivity();
        setDataInWidgets();
    }
    private void getAllWidgets()
    {
        Log.e(LOG_TAG, "PersonDetailsActivity.getAllWidgets");
        tvPersonDetailId= (TextView) findViewById(R.id.tvPersonDetailID);
        tvPersonDetailName= (TextView) findViewById(R.id.tvPersonDetailName);
        tvPersonDetailEmail= (TextView) findViewById(R.id.tvPersonDetailEmail);
        tvPersonDetailAddress= (TextView) findViewById(R.id.tvPersonDetailAddress);
        tvPersonDetailAge= (TextView) findViewById(R.id.tvPersonDetailAge);
    }
    private void getDataFromPreviousActivity()
    {

        int personID = getIntent().getIntExtra("PersonID", -1);
        Log.e(LOG_TAG, "PersonDetailsActivity.getDataFromPreviousActivity: " + String.valueOf(personID));
        personDetailsModel=MainActivity.getInstance().searchPerson(personID);
    }
    private void setDataInWidgets()
    {
        Log.e(LOG_TAG, "PersonDetailsActivity.setDataInWidgets");
        tvPersonDetailId.setText(getString(R.string.person_id,String.valueOf(personDetailsModel.getId())));
        tvPersonDetailName.setText(getString(R.string.person_name,personDetailsModel.getName()));
        tvPersonDetailEmail.setText(getString(R.string.person_email,personDetailsModel.getEmail()));
        tvPersonDetailAddress.setText(getString(R.string.person_address,personDetailsModel.getAddress()));
        tvPersonDetailAge.setText(getString(R.string.person_age, String.valueOf(personDetailsModel.getAge())));
    }
}
