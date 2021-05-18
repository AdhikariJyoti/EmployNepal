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
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

public class SignUpActivity extends AppCompatActivity {
    UserHelper userHelper;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String gender ="";
    int i = 0;
    Vibrator v;
    private EditText inputFirstName, inputLastName, emailsignup, contactsignup, locationsignup, passwordsignup, confirmpasswordsignup;
    private Button btnsignup, btnback;
    private ProgressBar progressBar;
    private RadioGroup genderRg;
    private RadioButton radioButtonOptions;
    private RadioButton radioMale;
    private RadioButton radioFemale;
    private RadioButton radioOthers;
    private FirebaseAuth auth;
    private long userId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        auth = FirebaseAuth.getInstance();

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

        //radioButton = findViewById(radioGroup.getCheckedRadioButtonId());
        radioMale = findViewById(R.id.male_signup);
        radioFemale = findViewById(R.id.female_signup);
        radioOthers = findViewById(R.id.others_signup);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

//        genderRg = findViewById(R.id.radioGroupSignup);

//        genderRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                radioButtonOptions = genderRg.findViewById(i);
//                switch (i) {
//                    case R.id.male_signup:
//                    case R.id.female_signup:
//                    case R.id.others_signup:
//                        gender = radioButtonOptions.getText().toString();
//                        break;
//                    default:
//
//
//                }
//
////                gender = radioButtonOptions.getText().toString();
//
//
//            }
//        });

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getting the reference to firebase database
                rootNode = FirebaseDatabase.getInstance();
                reference = rootNode.getReference();

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

                final String firstName = inputFirstName.getText().toString().trim();
                final String lastName = inputLastName.getText().toString().trim();
                final String emailSignUp = emailsignup.getText().toString().trim();
                final String contactSignUp = contactsignup.getText().toString().trim();
//                String gender = radioButton.getText().toString();
//                String male = radioMale.getText().toString();
//                String female = radioFemale.getText().toString();
//                String others = radioOthers.getText().toString();


                final String locationSignUp = locationsignup.getText().toString().trim();
                final String passwordSignUp = passwordsignup.getText().toString().trim();
                final String confirmPasswordSignUp = confirmpasswordsignup.getText().toString().trim();

                //radiobuttons ko kam garna baki xa

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
                if (!(contactSignUp.length() == 10)) {
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
                if (radioFemale.isChecked()) {
                    gender = "Female";
                } else if (radioMale.isChecked()) {
                    gender = "Male";

                } else {
                    gender = "Others";
                }

                //radio button ko kam baki xa
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

//                genderRg = findViewById(R.id.radioGroupSignup);
//
//                genderRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                        radioButtonOptions = genderRg.findViewById(i);
//                        switch (i) {
//
//                            case R.id.female_signup:
//                                gender ="Female";
//                                break;
//                            case R.id.male_signup:
//                                gender ="Male";
//                                break;
//                            case R.id.others_signup:
//                                gender ="Others";
//                                break;
//
//                            default:
//
//
//                        }
//                    }
//                });

                //storing data to database
                //radio button ko lagi garna baki xa
                UserHelper userHelper = new UserHelper(firstName, lastName, emailSignUp, contactSignUp, gender, locationSignUp, passwordSignUp, confirmPasswordSignUp);
                //reference.setValue(userHelper);
                //reference.child(contactSignUp).setValue(userHelper);
                reference.child("users").child(String.valueOf(i)).child("firstName").setValue(firstName);
                reference.child("users").child(String.valueOf(i)).child("lastName").setValue(lastName);
                reference.child("users").child(String.valueOf(i)).child("email").setValue(emailSignUp);
                reference.child("users").child(String.valueOf(i)).child("contact").setValue(contactSignUp);
                reference.child("users").child(String.valueOf(i)).child("gender").setValue(gender);
                reference.child("users").child(String.valueOf(i)).child("location").setValue(locationSignUp);
                reference.child("users").child(String.valueOf(i)).child("password").setValue(passwordSignUp);
                reference.child("users").child(String.valueOf(i)).child("confirmPassword").setValue(confirmPasswordSignUp);



                //authentication with email and password
                progressBar.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(emailSignUp, passwordSignUp).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this, "Authentication failed" + task.getException(), Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));

                        }

                    }
                });


            }
        });
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(new Intent(SignUpActivity.this, LoginActivity.class)));
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