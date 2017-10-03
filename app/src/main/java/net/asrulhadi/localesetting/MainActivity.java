package net.asrulhadi.localesetting;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private GestureDetector mGestureDetector;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // specifically set the locale
        Configuration config = getResources().getConfiguration();
        config.setLocale(new Locale("jp"));
        // set the view
        setContentView(R.layout.activity_main);

        // set long press on imageView
        ImageView img = (ImageView) findViewById(R.id.imageView);
        img.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("LocaleSetting","Drpd OnLongClickListener");
                return ambilGambar();
            }
        });

        // setting double click
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDoubleTap(MotionEvent event) {
                Log.d("LocaleSetting","Drpd SimpleOnGestureListener");
                System.out.println("Double Table dari Gesture");
                return ambilGambar();
            }
        });

        // also apply to image
        img.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return mGestureDetector.onTouchEvent(motionEvent);
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView img = (ImageView) findViewById(R.id.imageView);
            img.setImageBitmap(imageBitmap);
        }
    }

    private boolean ambilGambar() {
        // open camera and capture the image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
        // get the thumbnail

        //File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        //imageUri = Uri.fromFile(photo);
        return true;
    }

    public void tukarBahasa(View v) {
        TextView h = (TextView) findViewById(R.id.hello_world);
        Configuration cfg = getResources().getConfiguration();
        cfg.setLocale(new Locale("ms"));
        Context ctx = createConfigurationContext(cfg);
        String hw = ctx.getResources().getString(R.string.hello);
        // get all the array
        String[] ha = ctx.getResources().getStringArray(R.array.hadis);
        for (String hai: ha) {
            hw += "\n ==> " + hai;
        }
        h.setText(hw);
    }

    public void tunjukHadis(View v) {
        Intent intent = new Intent(this, HadisActivity.class);
        startActivity(intent);
    }

    public void tunjukAyat(View v) {
        Intent intent = new Intent(this, AyatActivity.class);
        startActivity(intent);
    }

    public void shareText(View v) {
        tukarBahasa(v);
        String thisText = (String) ((TextView) findViewById(R.id.hello_world)).getText();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, thisText);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }
}
