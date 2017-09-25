package net.asrulhadi.localesetting;

import android.content.Context;
import android.content.Intent;
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
        // get all the array
        String[] ha = getResources().getStringArray(R.array.hadis);
        for ( int i=0 ; i<ha.length; i++ ) {
            hw += "\n -> " + ha[i];
        }
        h.setText(hw);
    }

    public void shareText(View v) {
        String thisText = (String) ((TextView) findViewById(R.id.hello_world)).getText();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, thisText);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }
}
