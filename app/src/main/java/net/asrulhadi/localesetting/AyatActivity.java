package net.asrulhadi.localesetting;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class AyatActivity extends AppCompatActivity {

    private int currentAyat = 1;
    private MediaPlayer player = null;
    FloatingActionButton fab = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // check status
                if ( player == null ) {
                    play();
                    // change the icon
                    AyatActivity.this.fab.setImageResource(android.R.drawable.ic_media_pause);
                } else {
                    if ( AyatActivity.this.player.isPlaying() ) {
                        Log.d("LocaleSetting", "Media Paused.");
                        // paused
                        AyatActivity.this.fab.setImageResource(android.R.drawable.ic_media_play);
                        AyatActivity.this.player.pause();
                    } else {
                        Log.d("LocaleSetting", "Media Resuming");
                        AyatActivity.this.player.start();
                        AyatActivity.this.fab.setImageResource(android.R.drawable.ic_media_pause);
                    }
                }
            }
        });

        TextView ayatView = (TextView) findViewById(R.id.ayatView);
        ayatView.setText(R.string.ayat);
    }

    private void play() {
        String pack = "net.asrulhadi.localesetting";
        // media from https://verses.quran.com/Alafasy/mp3/SSSAAA.mp3
        int ayatID = getResources().getIdentifier("a00100" + currentAyat, "raw", pack);
        player = MediaPlayer.create(AyatActivity.this, ayatID);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("LocaleSetting", "Media onCompletion called");
                AyatActivity.this.player.stop();
                currentAyat += 1;   // next audio
                releasePlayer();
                if ( currentAyat < 8 ) play();
                else {
                    Log.d("LocaleSetting", "Media Terminating");
                    // end of surah
                    AyatActivity.this.fab.setImageResource(android.R.drawable.ic_media_play);
                    currentAyat = 1;
                }
            }
        });
        Log.d("LocaleSetting", "Media Playing " + currentAyat);
        player.start();
    }

    public void releasePlayer() {
        Log.d("LocaleSetting", "Media releasing");
        if (player != null) {
            if ( player.isPlaying() ) player.stop();
            player.release();
        }
        player = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }
}
