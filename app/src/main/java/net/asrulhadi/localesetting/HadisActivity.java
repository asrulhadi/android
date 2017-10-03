package net.asrulhadi.localesetting;


import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class HadisActivity extends AppCompatActivity implements ActionBar.TabListener {
    private final static String TERJEMAHAN = "terjemah";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Terjemahan mTerjemah;
    private String translation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // restore translation
        if ( savedInstanceState != null ) {
            translation = savedInstanceState.getString(TERJEMAHAN);
            Log.d("LocaleSetting", " **** Restoring instance ==> " + translation);
        } else {
            translation = "";
        }

        setContentView(R.layout.activity_hadis);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //Log.d("LocaleSetting","From onPageSelected  " + (position+1));
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }

        // Terjemahan
        mTerjemah = new Terjemahan(this);
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface d, int id) {
                Log.d("LocaleSetting","onClick Dialog");
                showTerjemahanFragment(id);
            }
        };
        mTerjemah.setTerjemahan(new String[] {"Off","English","Bahasa Melayu"}, listener);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // need to put in manifest
        // android:configChanges="orientation|screenSize"
        Log.d("LocaleSetting", "New Config " + newConfig);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("LocaleSetting", " **** Saving Instance ==> " + translation);
        outState.putString(TERJEMAHAN, translation);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hadis, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.action_terjemahan:
                Log.d("LocaleSetting","klik menu terjemahan");
                mTerjemah.show();
                Log.d("LocaleSetting","Tamat show");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showTerjemahanFragment(int id) {
        switch (id) {
            case 1: translation = "en"; break;
            case 2: translation = "ms"; break;
            default: translation = "";
        }
        showTerjemahanFragment();
    }

    public void showTerjemahanFragment() {
        String msg = "Perlu tambah terjemahan ";
        Toast.makeText(getApplicationContext(), msg + translation, Toast.LENGTH_SHORT).show();

        int currentItemNo = mViewPager.getCurrentItem();
        Log.d("LocaleSetting","Calling getItem from showTerjemahan for " + (currentItemNo+1));

        // show translation for available view
        for (Fragment f: getSupportFragmentManager().getFragments()) {
            Log.d("LocaleSetting", "Fragment " + f);
            if ( f.getView() != null ) {
                boolean visible = "".equals(translation) ? false : true;
                HadisFragment hf = (HadisFragment)f;
                LinearLayout mView = (LinearLayout) f.getView();
                View tView = mView.findViewById(R.id.terjemahan);
                Log.d("LocaleSetting", "    View " + mView);
                hf.setTerjemahanText(translation, tView);
                hf.setTerjemahanVisibility(visible, mView);
                Log.d("LocaleSetting", "Setting for terjemahan " + mView.findViewById(R.id.sv_terjemah));
            }
        }
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        //Log.d("LocaleSetting","From onTabSelected  " + (tab.getPosition()+1));
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class HadisFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public HadisFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static HadisFragment newInstance(int sectionNumber) {
            HadisFragment fragment = new HadisFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);

            Log.d("LocaleSetting","From newInstance  " + sectionNumber);

            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_hadis, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

            // get the "ori" text
            int sectionNo = getArguments().getInt(ARG_SECTION_NUMBER);
            String sArabic = getLocalized(sectionNo-1, "ar");
            textView.setText(sArabic);
            Log.d("LocaleSetting","From onCreateVIew  " + sectionNo);

            // get translation
            String translate = ((HadisActivity) getActivity()).translation;
            /*if (savedInstanceState != null ) {
                translate = savedInstanceState.getString(TERJEMAHAN);
                Log.d("LocaleSetting", " **** Restoring from fragment " + translate);
            }*/
            setTerjemahanText(translate, rootView.findViewById(R.id.terjemahan));

            // translation: show or not
            setTerjemahanVisibility(! "".equals(translate), rootView.findViewById(R.id.hadis_layout));
            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            String translate = ((HadisActivity) getActivity()).translation;
            outState.putString(TERJEMAHAN, translate);
            super.onSaveInstanceState(outState);
        }

        private String getLocalized(int index, String locale) {
            // set config
            Configuration cfg = getResources().getConfiguration();
            Locale loc = Build.VERSION.SDK_INT>23 ? cfg.getLocales().get(0) : cfg.locale;
            // set to arabic
            cfg.setLocale(new Locale(locale));
            Context ctx = getContext().createConfigurationContext(cfg);
            // get the hadis
            String[] hw = ctx.getResources().getStringArray(R.array.hadis);
            // set to original
            cfg.setLocale(loc);
            getContext().createConfigurationContext(cfg);

            return ( index > hw.length ? "" : hw[index]);
        }

        public void setTerjemahanText(String locale, View t) {
            int index = getArguments().getInt(ARG_SECTION_NUMBER) - 1;
            switch (locale) {
                case "en":
                case "ms":
                    Log.d("LocaleSetting","Requesting " + locale + " translation untuk " + (index+1));
                    String translation = getLocalized(index, locale);
                    ((TextView)t).setText(translation);
                    break;
                default:
                    ((TextView)t).setText("DO NOT HAVE TRANSLATION");
            }
        }

        public void setTerjemahanVisibility(boolean show, View v) {
            // horizontal or vertical
            int limit = 480;
            int orientation = LinearLayout.VERTICAL;
            if (getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT) {
                // get screen size
                DisplayMetrics dm = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                int px=dm.widthPixels;
                int py=dm.heightPixels;
                Log.d("LocaleSetting","Display w:" + px + " h:" + py);
                if (px>limit) orientation = LinearLayout.HORIZONTAL;
            }
            Log.d("LocaleSetting", "HadisFragment v " + v);
            // get the scrollerview for terjemah
            LinearLayout ll = (LinearLayout) v;
            View vMain = ll.findViewById(R.id.sv_main);
            View vTerjemah = ll.findViewById(R.id.sv_terjemah);
            Log.d("LocaleSetting", "  View Terjemah " + vTerjemah);

            // set the correct order by switching the placement
            ll.removeView(vMain);
            ll.removeView(vTerjemah);
            if ( orientation == LinearLayout.VERTICAL) {
                ll.addView(vMain);
                ll.addView(vTerjemah);
            } else {
                ll.addView(vTerjemah);
                ll.addView(vMain);
            }
            ll.setOrientation(orientation);
            vTerjemah.setVisibility(show ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Log.d("LocaleSetting","onCreate called");
        }

        @Override
        public void onDestroyView() {
            Log.d("LocaleSetting", "View: " + getView());
            super.onDestroyView();
            Log.d("LocaleSetting","onDestroyView called " + getArguments().getInt(ARG_SECTION_NUMBER));

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a HadisFragment (defined as a static inner class below).
            Log.d("LocaleSetting","getItem " + (position+1) +" Called");
            return HadisFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            // return 3;
            return getResources().getStringArray(R.array.hadis).length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if ( position < 0 ) return null;
            if ( position > getResources().getStringArray(R.array.hadis).length ) return null;
            return getResources().getString(R.string.hadis) + " " + (position+1);
        }
    }
}
