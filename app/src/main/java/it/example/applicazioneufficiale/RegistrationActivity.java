package it.example.applicazioneufficiale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class RegistrationActivity extends AppCompatActivity{
    //private TextView textBanner;
    private Button registrationButton;
    private EditText editNome, editCognome, editEmail, editPassword;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        progressBar = findViewById(R.id.progress);

        mAuth = FirebaseAuth.getInstance();

        registrationButton = (Button) findViewById(R.id.buttonRegistration);
        registrationButton.setOnClickListener(v -> {
            registerUser();
        });

        editNome = findViewById(R.id.editRegisterNome);
        editCognome = findViewById(R.id.editRegisterCognome);
        editEmail = findViewById(R.id.editRegisterEmail);
        editPassword = findViewById(R.id.editRegisterPassword);



    }

    private void registerUser() {
        String nome = editNome.getText().toString().trim();
        String cognome = editCognome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String password = editPassword.getText().toString().trim();

        if(nome.isEmpty()){
            editNome.setError(getString(R.string.name_warning));
            editNome.requestFocus();
            return;

        }

        if(cognome.isEmpty()) {
            editCognome.setError(getString(R.string.surname_warning));
            editCognome.requestFocus();
            return;
        }

        if(email.isEmpty()) {
            editEmail.setError(getString(R.string.email_warning));
            editEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editEmail.setError(getString(R.string.email_wrong));
            editEmail.requestFocus();
            return;
        }

        if(password.isEmpty()) {
            editPassword.setError(getString(R.string.password_warning));
            editPassword.requestFocus();
            return;
        }

        if(password.length() < 8){
            editPassword.setError(getString(R.string.password_length));
            editPassword.requestFocus();
            return;

        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            User user = new User (nome, cognome, email);

                            Toast.makeText(RegistrationActivity.this, R.string.registration_okay, Toast.LENGTH_LONG).show();

                            startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));

                            progressBar.setVisibility(View.GONE);



                            FirebaseDatabase.getInstance("https://auth-feae0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users")
                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user);



                        }else{
                            Toast.makeText(RegistrationActivity.this, R.string.registration_failed, Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


    }
}

