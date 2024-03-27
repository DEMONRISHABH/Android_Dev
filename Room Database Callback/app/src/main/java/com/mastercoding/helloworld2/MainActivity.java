package com.mastercoding.helloworld2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.mastercoding.helloworld2.db.ContactsAppDatabase;
import com.mastercoding.helloworld2.db.entity.Contact;
import com.mastercoding.helloworld2.adapter.ContactsAdapter;


import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // ROOM Database Project


    // Variables
    private ContactsAdapter contactsAdapter;
    private ArrayList<Contact> contactArrayList  = new ArrayList<>();
    private RecyclerView recyclerView;
    private ContactsAppDatabase contactsAppDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Favorite Contacts");


        // RecyclerVIew
        recyclerView = findViewById(R.id.recycler_view_contacts);


        // Database
        contactsAppDatabase = Room.databaseBuilder(
                getApplicationContext(),
                ContactsAppDatabase.class,
                "ContactDB")
                .addCallback(myCallBack)
                .build();


        // Displaying All Contacts List
        DisplayAllContactInBackGround();


        contactsAdapter = new ContactsAdapter(this, contactArrayList,MainActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactsAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAndEditContacts(false, null, -1);
            }
        });
    }

    public void addAndEditContacts(final boolean isUpdated,final Contact contact,final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contact,null);

        AlertDialog.Builder alerDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alerDialogBuilder.setView(view);


        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!isUpdated ? "Add New Contact" : "Edit Contact");


        if (isUpdated && contact != null){
            newContact.setText(contact.getName());
            contactEmail.setText(contact.getEmail());
        }

        alerDialogBuilder.setCancelable(false)
                .setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("Delete",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (isUpdated){
                                    DeleteContact(contact, position);
                                }else{
                                    dialogInterface.cancel();
                                }
                            }
                        }
                );

        final AlertDialog alertDialog = alerDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(newContact.getText().toString())){
                    Toast.makeText(MainActivity.this, "Please Enter a Name", Toast.LENGTH_SHORT).show();

                    return;
                }else{
                    alertDialog.dismiss();
                }

                if (isUpdated && contact != null){
                    UpdateContact(newContact.getText().toString(), contactEmail.getText().toString(),position);

                }else{
                    CreateContact(newContact.getText().toString(), contactEmail.getText().toString());

                }

            }
        });

    }

    private void DeleteContact(Contact contact, int position) {

        contactArrayList.remove(position);
        contactsAppDatabase.getContactDAO().deleteContact(contact);
        contactsAdapter.notifyDataSetChanged();


    }

    private void UpdateContact(String name, String email, int position){
        Contact contact = contactArrayList.get(position);

        contact.setName(name);
        contact.setEmail(email);

        contactsAppDatabase.getContactDAO().updateContact(contact);

        contactArrayList.set(position, contact);
        contactsAdapter.notifyDataSetChanged();


    }

    private void CreateContact(String name, String email){

        long id = contactsAppDatabase.getContactDAO()
                .addContact(new Contact(name, email,0));

        Contact contact = contactsAppDatabase.getContactDAO().getContact(id);

        if (contact != null){
            contactArrayList.add(0, contact);
            contactsAdapter.notifyDataSetChanged();
        }

    }



    private void DisplayAllContactInBackGround(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {

                // Background Work
                contactArrayList.addAll(contactsAppDatabase.getContactDAO().getContacts());



                // Executed after the background work had finished
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        contactsAdapter.notifyDataSetChanged();
                    }
                });


            }
        });

    }



    // Menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // CallBacks
    RoomDatabase.Callback myCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);


            // These are 4 contacts already created in the app when installed (Built-In Contacts)
//            CreateContact("Bill Gates", "billgates@microsoft.com");
//            CreateContact("Nicolas Tesla", "nicolatesla@tesla.com");
//            CreateContact("Mark Zuker", "mark_zuker@facebook.com");
//            CreateContact("Satushi Namk","satushi@bitcoin.com");
//            DisplayAllContactInBackGround();
            Log.i("TAG","Database has been Created");
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);

            Log.i("TAG","Database has been Opened");
        }
    };


}