package net.asrulhadi.localesetting;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

import static java.security.AccessController.getContext;

/**
 * Created by hadi on 27/09/17.
 */

public final class Terjemahan {

    private Dialog dialog;
    private Context context;
    private AlertDialog.Builder builder;

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public Terjemahan(Context ctx) {
        context = ctx;

        // build the dialog
        builder = new AlertDialog.Builder(context);

        // Chain together various setter methods to set the dialog characteristics
        //builder.setMessage(R.string.terjemahan_dialog_message)
        builder.setTitle(R.string.terjemahan_dialog_title);

        // set buttons
        /*builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int id) {
                // do nothing
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int id) {
                // do nothing
            }
        });*/

    }

    // set OK Button
    public void setOKButton(CharSequence text, DialogInterface.OnClickListener listener) {
        builder.setPositiveButton(text, listener);
    }

    public void setTerjemahan(String[] terjemahan, DialogInterface.OnClickListener listener) {
        //Toast.makeText(context, "Cancel Clicked 1", Toast.LENGTH_SHORT).show();
        builder.setItems(terjemahan, listener);
    }

    public void show() {
        //  Get the AlertDialog from create()
        dialog = builder.create();
        // show the dialog
        dialog.show();
    }

    public String terjemah(String locale, int index) {
        String res = "";

        // set the language
        Configuration cfg = context.getResources().getConfiguration();
        Locale oldlocale = Build.VERSION.SDK_INT < 24 ? cfg.locale : cfg.getLocales().get(0);

        Log.d("LocaleSetting","Original Config: " + oldlocale);

        cfg.setLocale(new Locale(locale));

        Log.d("LocaleSetting","     Old Locale: " + oldlocale);
        Log.d("LocaleSetting","     New Locale: " + (Build.VERSION.SDK_INT < 24?cfg.locale:cfg.getLocales().get(0)));
        Context ctx = context.createConfigurationContext(cfg);

        // get the hadis with new locale
        String[] hadis = ctx.getResources().getStringArray(R.array.hadis);
        if ( index < hadis.length ) res = hadis[index];
        // set back to defaults
        cfg.setLocale(oldlocale);
        Log.d("LocaleSetting","   Reset Config: " + cfg);

        return res;
    }
}
