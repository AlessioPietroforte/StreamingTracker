
package it.example.applicazioneufficiale.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import it.example.applicazioneufficiale.LoginActivity;
import it.example.applicazioneufficiale.R;
import it.example.applicazioneufficiale.User;


public class LogoutActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        Button logoutButton = findViewById(R.id.buttonLogout);

        logoutButton.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(LogoutActivity.this);
            //Setto title
            builder.setTitle("Logout");
            //Setto il messaggio
            builder.setMessage(R.string.logout_question);
            builder.setPositiveButton("YES", (dialog, which) -> {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LogoutActivity.this, LoginActivity.class));

            });

            //Risposta Negativa
            builder.setNegativeButton("NO", (dialog, which) -> {
                //Chide il pop up del logout
                dialog.dismiss();
            });
            //Mostra il pop up del dialogo
            builder.show();
        });


        user= FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance("https://auth-feae0-default-rtdb.europe-west1.firebasedatabase.app").getReference("users");
        userId = user.getUid();

        TextView nameTextView = findViewById(R.id.name);
        TextView surnameTextView = findViewById(R.id.surname);
        TextView emailTextView = findViewById(R.id.email);

        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String nome = userProfile.nome;
                    String cognome = userProfile.cognome;
                    String email = userProfile.email;

                    nameTextView.setText(nome);
                    surnameTextView.setText(cognome);
                    emailTextView.setText(email);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                Toast.makeText(LogoutActivity.this, R.string.genericError_message, Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();

        if(id == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }
}
