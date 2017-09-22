package net.asrulhadi.localesetting;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // specifically set the locale
        Configuration config = getResources().getConfiguration();
        config.setLocale(new Locale("ms"));
        // set the view
        setContentView(R.layout.activity_main);
    }
}
