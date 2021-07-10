package it.example.applicazioneufficiale.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import it.example.applicazioneufficiale.R;

public class MovieDetails extends AppCompatActivity {

    TextView movieName;
    ImageView movieImage;
    Button playButton;

    String mName, mImage, mId, mFileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        /*TODO inserire un floating button che permetta l' inserimento del film selezionato in watching
           watched, to watch. Se vogliamo semplificare possiamo aggiungere solo in preferiti,
           indipendentemente dal fatto che sia una serie o un film

         */
        movieImage = findViewById(R.id.movie_image);
        movieName = findViewById(R.id.movie_name);

        //bottone facoltativo se dobbiamo inserire il player per il trailer
        playButton = findViewById(R.id.play_button);

        mId = getIntent().getStringExtra("movieId");
        mName = getIntent().getStringExtra("movieName");
        mImage = getIntent().getStringExtra("movieImageUrl");
        mFileUrl = getIntent().getStringExtra("movieFile");

        Glide.with(this).load(mImage).into(movieImage);
        movieName.setText(mName);

    }
}