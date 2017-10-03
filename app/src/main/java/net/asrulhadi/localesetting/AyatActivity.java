package net.asrulhadi.localesetting;

import android.media.AudioManager;
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

    private MediaPlayer create(int id) {
        return MediaPlayer.create(AyatActivity.this, id);
    }

    private MediaPlayer create(String url) {
        MediaPlayer p = new MediaPlayer();
        p.setAudioStreamType(AudioManager.STREAM_MUSIC);
        p.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                Log.d("LocaleSetting", "MediaPlayer: Async error : " + i + " " + i1);
                return false;
            }
        });
        try {
            Log.d("LocaleSetting", "MediaPlayer: setting data source");
            p.setDataSource(url);
            Log.d("LocaleSetting", "MediaPlayer: preparing async");
            p.prepareAsync();
        } catch (Exception e) {
            Log.e("LocaleSetting", "Error: " + e);
            p.release();
            return null;
        }
        Log.d("LocaleSetting", "MediaPlayer: ready to play");
        return p;
    }

    private void play() {
        String pack = getApplicationInfo().packageName; //"net.asrulhadi.localesetting";
        // media from https://verses.quran.com/Alafasy/mp3/SSSAAA.mp3
        int audio = getResources().getIdentifier("a00100" + currentAyat, "raw", pack);
        //String audio = "https://verses.quran.com/Alafasy/mp3/00100" + currentAyat + ".mp3";
        // URL need <uses-permission android:name="android.permission.INTERNET" />
        player = create(audio);
        Log.d("LocaleSetting", "MediaPlayer: creating audio for " + currentAyat);

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.d("LocaleSetting", "MediaPlayer: onCompletion called");
                AyatActivity.this.player.stop();
                currentAyat += 1;   // next audio
                releasePlayer();
                if ( currentAyat < 8 ) play();
                else {
                    Log.d("LocaleSetting", "MediaPlayer: Terminating");
                    // end of surah
                    AyatActivity.this.fab.setImageResource(android.R.drawable.ic_media_play);
                    currentAyat = 1;
                }
            }
        });
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.d("LocaleSetting", "MediaPlayer: Playing " + currentAyat);
                mediaPlayer.start();
            }
        });
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
