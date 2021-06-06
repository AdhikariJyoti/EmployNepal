package android.example.employnepal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.example.employnepal.models.UserHelper;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;


public class OtpActivity extends AppCompatActivity {
    String verificationCodeBySystem;
    private Button submitButton, resendButton, backButton;
    private PinView pinView;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String firstName, lastName, email, contact, location, gender, password, confirmPassword;
    String whatToDo, whatToDO;
    NestedScrollView scrollView;
    String country_code="977";
    String completePhoneNo;
    //for testing purpose only need to remove later
    String phoneNum = "+9779866548364";
    String testVerificationCode = "484698";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);



        submitButton = findViewById(R.id.button_submit);
        resendButton = findViewById(R.id.resend_btn);
        pinView = findViewById(R.id.pinView);
        progressBar = findViewById(R.id.progressbarOtp);
        backButton = findViewById(R.id.backButtonOtp);
        scrollView = findViewById(R.id.scrollView);
        progressBar.setVisibility(View.GONE);

        auth = FirebaseAuth.getInstance();



        //get all the data from intent
        Intent intent = new Intent(getApplicationContext(), OtpActivity.class);
        firstName = getIntent().getStringExtra("firstName");
        lastName = getIntent().getStringExtra("lastName");
        email = getIntent().getStringExtra("email");
        contact = getIntent().getStringExtra("contact");
        location = getIntent().getStringExtra("address");
        gender = getIntent().getStringExtra("gender");
        password = getIntent().getStringExtra("password");
        confirmPassword = getIntent().getStringExtra("confirmPassword");

        completePhoneNo = "+" + country_code + "" + contact;


        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View, String>(scrollView, "transition_OTP_screen");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(OtpActivity.this, pairs);
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
        //from forgetpasswordActivity
        whatToDo = getIntent().getStringExtra("whatToDO");
        //from signup
        whatToDO = getIntent().getStringExtra("whatTODo");


        //sendVerificationCodeToUser(completePhoneNo);
        String country_code = "977";
       String completePhoneNo = "+" + country_code + "" + contact;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(completePhoneNo)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(OtpActivity.this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


        //if the user has not given his phone no/or the same mobile device is not used
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = pinView.getText().toString();
                if (code.isEmpty() | code.length() < 6) {
                    pinView.setError("Wrong OTP!");
                    pinView.requestFocus();
                    return;
                }

                verifyCode(code);
                progressBar.setVisibility(View.VISIBLE);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OtpActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


    }

//    private void sendVerificationCodeToUser(String completePhoneNo) {
////        String country_code = "977";
////        String completePhoneNo = "+" + country_code + "" + phoneNo;
//        PhoneAuthOptions options =
//                PhoneAuthOptions.newBuilder(auth)
//                        .setPhoneNumber(completePhoneNo)       // Phone number to verify
//                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
//                        .setActivity(OtpActivity.this)                 // Activity (for callback binding)
//                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
//                        .build();
//        PhoneAuthProvider.verifyPhoneNumber(options);
//
//    }

//    private void sendVerificationCodeToUser(String phoneNo) {
//        String country_code = "977";
//        String completePhoneNo="+" + country_code + "" + phoneNo;
//
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                completePhoneNo,        // Phone number to verify
//                60,                 // Timeout duration
//                TimeUnit.SECONDS,   // Unit of timeout
//                TaskExecutors.MAIN_THREAD,// Activity (for callback binding)
//                mCallbacks);        // OnVerificationStateChangedCallbacks
//
//    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            //if not used, remove this
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    verificationCodeBySystem = s;
//
//                }
//            },10000);
            verificationCodeBySystem = s;


        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                progressBar.setVisibility(View.VISIBLE);
                pinView.setText(code);
                verifyCode(code);
            }

        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(OtpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, code);
        signInWithPhoneAuthCredential(credential);


    }

    /*private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, (task) -> {
                    if (task.isSuccessful()) {

//                        if ("updateData".equals(whatToDo)){
                        if (whatToDo!=null && whatToDo.equals("updateData")) {
                            updateOldUserData();
                        } else {
                            storeNewUserData();
                        }

                                *//*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);*//*


                    } else {
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            Toast.makeText(OtpActivity.this, "Verification not completed! Please try again", Toast.LENGTH_LONG).show();

                        }
                    }

                });
    }*/
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

//                        if ("updateData".equals(whatToDo)){
                            if (whatToDO != null && whatToDO.equals("storeData")) {
                                storeNewUserData();

                            } else if (whatToDo != null && whatToDo.equals("updateData")) {
                                updateOldUserData();
                            }

//                                *//*Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            startActivity(intent);*//*
                        } else {
                            // Sign in failed, display a message and update the UI

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(OtpActivity.this, "Verification not completed! Please try again", Toast.LENGTH_LONG).show();
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    private void storeNewUserData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users");

        //creating userhelper class and storing data to firebase
        UserHelper userHelper = new UserHelper(firstName, lastName, email, contact, location, gender, password, confirmPassword);
        reference.child(contact).setValue(userHelper);


    }

    private void updateOldUserData() {
        Intent intent = new Intent(getApplicationContext(), UpdatePasswordActivity.class);
        intent.putExtra("contact", contact);
        startActivity(intent);
        finish();
    }

    public void callNextScreenFromOtp(View view) {
        Intent intent = new Intent(OtpActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}