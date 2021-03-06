package com.example.safaralialisultanov.realmexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    final static String LOG_TAG = "myLogs";
    private static int id = 1;
    private FloatingActionButton fabAddPerson;
    private Realm myRealm;
    private ListView lvPersonNameList;
    private static ArrayList<PersonDetailsModel> personDetailsModelArrayList = new ArrayList<>();
    private PersonDetailsAdapter personDetailsAdapter;
    private static MainActivity instance;
    private AlertDialog.Builder subDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(LOG_TAG, "MainActivity.OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myRealm = Realm.getInstance(MainActivity.this);
        instance = this;
        getAllWidgets();
        bindWidgetsWithEvents();
        setPersonDetailsAdapter();
        getAllUsers();
    }
    public static MainActivity getInstance() {
        Log.e(LOG_TAG, "MainActivity.getInstance");
        return instance;
    }
    private void getAllWidgets() {
        Log.e(LOG_TAG, "MainActivity.getAllWidgets");
        fabAddPerson = (FloatingActionButton) findViewById(R.id.fabAddPerson);
        lvPersonNameList = (ListView) findViewById(R.id.lvPersonNameList);
    }
    private void bindWidgetsWithEvents() {
        Log.e(LOG_TAG, "MainActivity.bindWidgetsWithEvents");
        fabAddPerson.setOnClickListener(this);
        lvPersonNameList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e(LOG_TAG, "MainActivity.bindWidgetsWithEvents.onItemClick");
                Intent intent=new Intent(MainActivity.this,PersonDetailsActivity.class);
                intent.putExtra("PersonID", personDetailsModelArrayList.get(position).getId());
                startActivity(intent);
            }
        });
    }
    private void setPersonDetailsAdapter() {
        Log.e(LOG_TAG, "MainActivity.setPersonDetailsAdapter");
        personDetailsAdapter = new PersonDetailsAdapter(MainActivity.this, personDetailsModelArrayList);
        lvPersonNameList.setAdapter(personDetailsAdapter);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fabAddPerson:
                addOrUpdatePersonDetailsDialog(null,-1);
                break;
        }
    }
    public void addOrUpdatePersonDetailsDialog(final PersonDetailsModel model,final int position) {
//subdialog
        Log.e(LOG_TAG, "MainActivity.addOrUpdatePersonDetailsDialog");
        subDialog = new AlertDialog.Builder(MainActivity.this)
                .setMessage("Please enter all the details!!!")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dlg2, int which) {
                        dlg2.cancel();
                    }
                });
//maindialog
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.prompt_dialog, null);
        AlertDialog.Builder mainDialog = new AlertDialog.Builder(MainActivity.this);
        mainDialog.setView(promptsView);
        final EditText etAddPersonName = (EditText) promptsView.findViewById(R.id.etAddPersonName);
        final EditText etAddPersonEmail = (EditText) promptsView.findViewById(R.id.etAddPersonEmail);
        final EditText etAddPersonAddress = (EditText) promptsView.findViewById(R.id.etAddPersonAddress);
        final EditText etAddPersonAge = (EditText) promptsView.findViewById(R.id.etAddPersonAge);
        if (model != null) {
            etAddPersonName.setText(model.getName());
            etAddPersonEmail.setText(model.getEmail());
            etAddPersonAddress.setText(model.getAddress());
            etAddPersonAge.setText(String.valueOf(model.getAge()));
        }
        mainDialog.setCancelable(false)
                .setPositiveButton("Ok", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        final AlertDialog dialog = mainDialog.create();
        dialog.show();
        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Utility.isBlankField(etAddPersonName) && !Utility.isBlankField(etAddPersonEmail) && !Utility.isBlankField(etAddPersonAddress) && !Utility.isBlankField(etAddPersonAge)) {
                    PersonDetailsModel personDetailsModel = new PersonDetailsModel();
                    personDetailsModel.setName(etAddPersonName.getText().toString());
                    personDetailsModel.setEmail(etAddPersonEmail.getText().toString());
                    personDetailsModel.setAddress(etAddPersonAddress.getText().toString());
                    personDetailsModel.setAge(Integer.parseInt(etAddPersonAge.getText().toString()));
                    if (model == null)
                        addDataToRealm(personDetailsModel);
                    else
                        updatePersonDetails(personDetailsModel, position, model.getId());
                    dialog.cancel();
                } else {
                    subDialog.show();
                }
            }
        });
    }
    private void addDataToRealm(PersonDetailsModel model) {
        Log.e(LOG_TAG, "MainActivity.addDataToRealm");
        myRealm.beginTransaction();
        PersonDetailsModel personDetailsModel = myRealm.createObject(PersonDetailsModel.class);
        personDetailsModel.setId(id);
        personDetailsModel.setName(model.getName());
        personDetailsModel.setEmail(model.getEmail());
        personDetailsModel.setAddress(model.getAddress());
        personDetailsModel.setAge(model.getAge());
        personDetailsModelArrayList.add(personDetailsModel);
        myRealm.commitTransaction();
        personDetailsAdapter.notifyDataSetChanged();
        id++;
    }
    public void deletePerson(int personId, int position) {
        Log.e(LOG_TAG, "MainActivity.deletePerson");
        RealmResults<PersonDetailsModel> results = myRealm.where(PersonDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        results.remove(0);
        myRealm.commitTransaction();
        personDetailsModelArrayList.remove(position);
        personDetailsAdapter.notifyDataSetChanged();
    }
    public PersonDetailsModel searchPerson(int personId) {
        Log.e(LOG_TAG, "MainActivity.searchPerson");
        RealmResults<PersonDetailsModel> results = myRealm.where(PersonDetailsModel.class).equalTo("id", personId).findAll();
        myRealm.beginTransaction();
        myRealm.commitTransaction();
        return results.get(0);
    }
    public void updatePersonDetails(PersonDetailsModel model,int position,int personID) {
        Log.e(LOG_TAG, "MainActivity.updatePersonDetails");
        PersonDetailsModel editPersonDetails = myRealm.where(PersonDetailsModel.class).equalTo("id", personID).findFirst();
        myRealm.beginTransaction();
        editPersonDetails.setName(model.getName());
        editPersonDetails.setEmail(model.getEmail());
        editPersonDetails.setAddress(model.getAddress());
        editPersonDetails.setAge(model.getAge());
        myRealm.commitTransaction();
        personDetailsModelArrayList.set(position, editPersonDetails);
        personDetailsAdapter.notifyDataSetChanged();
    }
    private void getAllUsers() {
        Log.e(LOG_TAG, "MainActivity.getAllUsers");
        RealmResults<PersonDetailsModel> results = myRealm.where(PersonDetailsModel.class).findAll();
        myRealm.beginTransaction();
        for (int i = 0; i < results.size(); i++) {
            personDetailsModelArrayList.add(results.get(i));
        }
        if(results.size()>0)
            id = myRealm.where(PersonDetailsModel.class).max("id").intValue() + 1;
        myRealm.commitTransaction();
        personDetailsAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onDestroy() {
        Log.e(LOG_TAG, "MainActivity.onDestroy");
        super.onDestroy();
        personDetailsModelArrayList.clear();
        myRealm.close();
    }

}
