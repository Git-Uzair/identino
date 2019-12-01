package com.tango.identino;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.tango.identino.model.courses;
import com.tango.identino.util.courseAdapter;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button loginbtn;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private EditText Email, Password;
    private ProgressBar loginProgressbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        loginbtn = findViewById(R.id.login_button);
        Email = findViewById(R.id.login_email_edit_text);
        Password = findViewById(R.id.login_password_edit_text);
        loginProgressbar=findViewById(R.id.login_progress_bar);


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Email.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_LONG).show();
                } else {
                    loginProgressbar.setVisibility(View.VISIBLE);


                    mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                loginProgressbar.setVisibility(View.INVISIBLE);
                                Intent intent = new Intent(getApplicationContext(), Post_login.class);
                                startActivity(intent);

                                finish();
                            }
                            else
                            {
                                loginProgressbar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_LONG).show();
                            }

                        }

                    });
                }


//
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();


    }
}
