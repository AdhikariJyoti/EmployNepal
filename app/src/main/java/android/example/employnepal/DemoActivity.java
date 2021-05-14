package android.example.employnepal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DemoActivity extends AppCompatActivity {
    UserHelper userHelper;
    FirebaseDatabase database;
    DatabaseReference reference;
    int i = 0;
    Vibrator v;
    private EditText inputFirstName, inputLastName, emailsignup, contactsignup, locationsignup, passwordsignup, confirmpasswordsignup;
    private Button btnsignup, btnback;
    private ProgressBar progressBar;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private RadioButton radioOthers;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

        userHelper = new UserHelper();

        inputFirstName = findViewById(R.id.firstName_signup);
        inputLastName = findViewById(R.id.lastName_signup);
        emailsignup = findViewById(R.id.email_signup);
        contactsignup = findViewById(R.id.contact_signup);
        locationsignup = findViewById(R.id.location_signup);
        passwordsignup = findViewById(R.id.password_signup);
        confirmpasswordsignup = findViewById(R.id.confirm_password_signup);
        btnsignup = findViewById(R.id.btnSignUp);
        btnback = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progressbar);

        radioMale = findViewById(R.id.male_signup);
        radioFemale = findViewById(R.id.female_signup);
        radioOthers = findViewById(R.id.others_signup);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    i = (int) snapshot.getChildrenCount();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                ///

            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstName = inputFirstName.getText().toString().trim();
                String lastName = inputLastName.getText().toString().trim();
                String emailSignUp = emailsignup.getText().toString().trim();
                String contactSignUp = contactsignup.getText().toString().trim();
                String locationSignUp = locationsignup.getText().toString().trim();
                String male = radioMale.getText().toString();
                String female = radioFemale.getText().toString();
                String others = radioOthers.getText().toString();
                String passwordSignUp = passwordsignup.getText().toString().trim();
                String confirmPasswordSignUp = confirmpasswordsignup.getText().toString().trim();


                if (TextUtils.isEmpty(firstName)) {
                    inputFirstName.setError("Please Enter your first name");
                    v.vibrate(100);
                    inputFirstName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(lastName)) {
                    inputLastName.setError("Please Enter your last name");
                    v.vibrate(100);
                    inputLastName.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(emailSignUp)) {
                    emailsignup.setError("Please Enter your email address");
                    v.vibrate(100);
                    emailsignup.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(contactSignUp)) {
                    contactsignup.setError("Please Enter your phone number");
                    v.vibrate(100);
                    contactsignup.requestFocus();
                    return;
                }
                if (contactSignUp.length() < 10) {
                    contactsignup.setError("Please Enter 10 digits");
                    v.vibrate(100);
                    contactsignup.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(locationSignUp)) {
                    locationsignup.setError("Please Enter your address");
                    v.vibrate(100);
                    locationsignup.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(passwordSignUp)) {
                    passwordsignup.setError("Please Enter password");
                    v.vibrate(100);
                    passwordsignup.requestFocus();
                    return;
                }

                if (passwordSignUp.length() < 6) {
                    passwordsignup.setError("Password too short, enter minimum 6 characters!");
                    v.vibrate(100);
                    passwordsignup.requestFocus();
                    return;
                }
                if (!passwordSignUp.equals(confirmPasswordSignUp)) {
                    confirmpasswordsignup.setError("Passwords didn't match");
                    v.vibrate(100);
                    confirmpasswordsignup.requestFocus();
                    return;
                }

                //setting value to database
                userHelper.setFirstName(firstName);
                reference.child(String.valueOf(i + 1)).setValue(userHelper);
                userHelper.setLastName(lastName);
                reference.child(String.valueOf(i + 1)).setValue(userHelper);
                userHelper.setEmail(emailSignUp);
                reference.child(String.valueOf(i + 1)).setValue(userHelper);
                userHelper.setContact(contactSignUp);
                reference.child(String.valueOf(i + 1)).setValue(userHelper);
                userHelper.setLocation(locationSignUp);
                reference.child(String.valueOf(i + 1)).setValue(userHelper);

                if (radioMale.isChecked()) {
                    userHelper.setGender(male);
                    reference.child(String.valueOf(i + 1)).setValue(userHelper);
                } else if (radioFemale.isChecked()) {
                    userHelper.setGender(female);
                    reference.child(String.valueOf(i + 1)).setValue(userHelper);
                } else {
                    userHelper.setGender(others);
                    reference.child(String.valueOf(i + 1)).setValue(userHelper);
                }
                userHelper.setPassword(passwordSignUp);
                reference.child(String.valueOf(i + 1)).setValue(userHelper);
                userHelper.setConfirmPassword(confirmPasswordSignUp);
                reference.child(String.valueOf(i + 1)).setValue(userHelper);

                progressBar.setVisibility(View.VISIBLE);
                //authentication with email and password
                auth.createUserWithEmailAndPassword(emailSignUp, passwordSignUp).addOnCompleteListener(DemoActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(DemoActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(DemoActivity.this, "Authentication failed" + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(DemoActivity.this, LoginActivity.class));

                        }
                    }
                });


            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(new Intent(DemoActivity.this, LoginActivity.class)));
                finish();
            }

        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


}
