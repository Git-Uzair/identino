package com.tango.identino;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tango.identino.model.instructor;

import java.util.zip.Inflater;

import static android.view.View.VISIBLE;


public class MainActivity extends AppCompatActivity {

    private Button loginbtn;
    private Button forgotPasswordReset;
    private Button forgotPasswordClosePopup;
    private Button forgotPassword;
    private EditText forgotPasswordEmail;
    private PopupWindow forgotPasswordPopup;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText Email, Password;
    private ProgressBar loginProgressbar, forgotPasswordProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        loginbtn = findViewById(R.id.login_button);
        Email = findViewById(R.id.login_email_edit_text);
        Password = findViewById(R.id.login_password_edit_text);
        loginProgressbar = findViewById(R.id.login_progress_bar);
        forgotPassword=findViewById(R.id.login_forgot_password_button);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater popupInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                forgotPasswordPopup=new PopupWindow(popupInflater.inflate(R.layout.forgot_password_popup,null,false),1000,1000,true);

                forgotPasswordPopup.showAtLocation(MainActivity.this.findViewById(R.id.main_activity_layout), Gravity.CENTER,0,0);
                View myPopup=forgotPasswordPopup.getContentView();
                forgotPasswordEmail= myPopup.findViewById(R.id.forgot_password_email_edit_text);
                forgotPasswordReset= myPopup.findViewById(R.id.forgot_password_reset_button);
                forgotPasswordProgressBar= myPopup.findViewById(R.id.forgot_password_progress_bar);
                forgotPasswordReset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        forgotPasswordProgressBar.setVisibility(VISIBLE);
                        FirebaseAuth.getInstance().sendPasswordResetEmail(forgotPasswordEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                forgotPasswordProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Email sent successfully",Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                forgotPasswordProgressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                });
                forgotPasswordClosePopup=(Button) myPopup.findViewById(R.id.forgot_password_close_popup_button);
                forgotPasswordClosePopup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        forgotPasswordPopup.dismiss();
                    }
                });
            }
        });


        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Email.getText().toString().isEmpty() || Password.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all the fields", Toast.LENGTH_LONG).show();
                } else {
                    loginProgressbar.setVisibility(VISIBLE);


                    mAuth.signInWithEmailAndPassword(Email.getText().toString(), Password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                loginProgressbar.setVisibility(View.INVISIBLE);
                                final Intent intent = new Intent(getApplicationContext(), Post_login.class);
                                db.collection("instructor").document(Email.getText().toString().trim()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        instructor INS = documentSnapshot.toObject(instructor.class);
                                        intent.putExtra("name", INS.getName());
                                        startActivity(intent);
                                        finish();
                                    }
                                });


                            } else {
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
