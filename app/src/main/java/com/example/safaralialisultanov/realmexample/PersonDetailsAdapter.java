package com.example.safaralialisultanov.realmexample;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by safarali.alisultanov on 09.07.2016.
 */
public class PersonDetailsAdapter extends BaseAdapter {

    private ArrayList<PersonDetailsModel> personDetailsArrayList;
    private Context context;
    private LayoutInflater inflater;

    public PersonDetailsAdapter(Context context, ArrayList<PersonDetailsModel> personDetailsArrayList) {
        this.context = context;
        this.personDetailsArrayList = personDetailsArrayList;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return personDetailsArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return personDetailsArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Holder holder;
        if (v == null) {
            v = inflater.inflate(R.layout.inflate_list_item, null);
            holder = new Holder();
            holder.tvPersonName = (TextView)v.findViewById(R.id.tvPersonName);
            holder.ivEditPersonDetail = (ImageView)v.findViewById(R.id.ivEditPersonDetail);
            holder.ivDeletePerson = (ImageView)v.findViewById(R.id.ivDeletePerson);
            v.setTag(holder);
        }
        else {
            holder = (Holder)v.getTag();
        }
        holder.tvPersonName.setText(personDetailsArrayList.get(position).getName());
        holder.ivEditPersonDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonDetailsModel dataToEditModel = MainActivity.getInstance().searchPerson(personDetailsArrayList.get(position).getId());
                MainActivity.getInstance().addOrUpdatepersonDetailsDialog(dataToEditModel, position);
            }
        });
        holder.ivDeletePerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowConfirmDialog(context, personDetailsArrayList.get(position).getId(), position);
            }
        });

        return v;
    }

    class Holder {
        TextView tvPersonName;
        ImageView ivDeletePerson, ivEditPersonDetail;
    }

    public static void ShowConfirmDialog(Context context, final int personId, final int position) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setMessage("Are you sure you want to delete this record?")
                .setCancelable(true)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.getInstance().deletePerson(personId, position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
