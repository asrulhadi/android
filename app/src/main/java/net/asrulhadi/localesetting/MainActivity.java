package net.asrulhadi.localesetting;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // specifically set the locale
        Configuration config = getResources().getConfiguration();
        config.setLocale(new Locale("jp"));
        // set the view
        setContentView(R.layout.activity_main);
    }

    public void tukarBahasa(View v) {
        TextView h = (TextView) findViewById(R.id.hello_world);
        Configuration cfg = getResources().getConfiguration();
        cfg.setLocale(new Locale("ms"));
        Context ctx = createConfigurationContext(cfg);
        String hw = ctx.getResources().getString(R.string.hello);
        h.setText(R.string.hello);
    }
}
