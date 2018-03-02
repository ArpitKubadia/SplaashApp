package jain.star.yash.splaashapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;

    private DatabaseReference databaseReference;

    private EditText editTextName,editTextAddress;
    private Button buttonSave;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference= FirebaseDatabase.getInstance().getReference();

        editTextAddress=findViewById(R.id.editTextAddress);
        editTextName=findViewById(R.id.editTextName);
        buttonSave=findViewById(R.id.buttonSave);

        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.buttonLogout);

        //displaying logged in user name
        textViewUserEmail.setText("Welcome "+user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);
        buttonSave.setOnClickListener(this);
    }

    private void saveUserInformation(){
            String name=editTextName.getText().toString().trim();
            String address=editTextAddress.getText().toString().trim();

            UserInformation userInformation=new UserInformation(name,address);

            FirebaseUser user=firebaseAuth.getCurrentUser();

            databaseReference.child(user.getUid()).setValue(userInformation);
        Toast.makeText(this,"Information is saved in the database",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        //if logout is pressed
        if(view == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        if (view==buttonSave){
            saveUserInformation();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}