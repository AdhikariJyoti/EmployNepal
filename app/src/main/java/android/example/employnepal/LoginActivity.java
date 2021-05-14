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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    Vibrator v;
    private EditText emailLogin, passwordLogin;
    private Button rememberMeLogin, btnLoginJobProvider, btnLoginJobSeeker, btnCreateAccount, btnForgotPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        emailLogin = findViewById(R.id.email_login);
        passwordLogin = findViewById(R.id.password_login);
        rememberMeLogin = findViewById(R.id.remember_me_login);
        btnLoginJobProvider = findViewById(R.id.btn_login_as_job_provider);
        btnLoginJobSeeker = findViewById(R.id.btn_login_as_job_seeker);
        btnCreateAccount = findViewById(R.id.btn_create_account);
        btnForgotPassword = findViewById(R.id.btn_forgot_password);
        progressBar = findViewById(R.id.progressbar);
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        btnForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        btnLoginJobProvider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString().trim();
                final String password = passwordLogin.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    passwordLogin.setError("Please Enter password");
                    v.vibrate(100);
                    passwordLogin.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailLogin.setError("Please Enter password");
                    v.vibrate(100);
                    emailLogin.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        passwordLogin.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, DashboardJobProvider.class);
                                    startActivity(intent);
                                    finish();


                                }
                            }

                        });
            }


        });
        btnLoginJobSeeker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLogin.getText().toString().trim();
                final String password = passwordLogin.getText().toString().trim();
                if (TextUtils.isEmpty(password)) {
                    passwordLogin.setError("Please Enter password");
                    v.vibrate(100);
                    passwordLogin.requestFocus();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailLogin.setError("Please Enter password");
                    v.vibrate(100);
                    emailLogin.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (!task.isSuccessful()) {
                                    if (password.length() < 6) {
                                        passwordLogin.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Intent intent = new Intent(LoginActivity.this, DashboardJobSeeker.class);
                                    startActivity(intent);
                                    finish();


                                }
                            }

                        });
            }


        });

    }
}
