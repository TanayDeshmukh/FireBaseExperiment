package com.example.tanay.firebaseexp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends Activity {

    private EditText name,contact,address;
    private Button saveButton;
    private String displayName,uId;
    private User user;

    //private String url = "https://fir-exp-88f93.firebaseio.com/";

    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference usersRef = rootRef.child("users");

    public void saveButtonClick(View view){

        String uname = name.getText().toString().trim();
        String ucontact = contact.getText().toString().trim();
        String uaddr = address.getText().toString().trim();

        if(uname == null)
            uname = displayName;
        if(ucontact == null)
            ucontact = "no data";
        if(uaddr == null)
            uaddr = "no data";

        user = new User(uname,ucontact,uaddr);

        usersRef.child(uId).setValue(user);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayName = getIntent().getStringExtra("displayName");
        uId = getIntent().getStringExtra("uid");

        initUI();

    }

    private void initUI() {

        name = (EditText) findViewById(R.id.u_name);
        contact = (EditText) findViewById(R.id.user_contact);
        address = (EditText) findViewById(R.id.user_address);
        saveButton = (Button) findViewById(R.id.save_button);

    }

    /*@Override
    protected void onStart() {
        super.onStart();

        locationRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String newLocation = dataSnapshot.getValue(String.class);
                newLocationText.setText(newLocation);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String newLocation = dataSnapshot.getValue(String.class);
                newLocationText.setText(newLocation);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    //}
}
