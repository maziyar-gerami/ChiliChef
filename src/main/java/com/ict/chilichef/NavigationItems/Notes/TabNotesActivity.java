package com.ict.chilichef.NavigationItems.Notes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ict.chilichef.Adapter.ViewPagerAdapter;
import com.ict.chilichef.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class TabNotesActivity extends AppCompatActivity {

    private ArrayList<Bundle> arrayListBundle = new ArrayList<>();
    private TabLayout.Tab tab;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_notes);

        Bundle bundle = getIntent().getExtras();

        int cat = bundle.getInt("ItemID");
        String TITLE = bundle.getString("ItemName");
        int position = bundle.getInt("position");

        Toolbar toolbar = (Toolbar) findViewById(R.id.Toolbar_Public);
        setSupportActionBar(toolbar);
        ((TextView) findViewById(R.id.PublicTitle_toolbar)).setText(TITLE);

        back = (ImageButton) findViewById(R.id.PublicLeft_icon_id);
        back.setImageResource(R.drawable.ic_back);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TabNotesActivity.this.finish();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.Notes_viewPager);
        setupViewPager(viewPager, cat);
        int x = setupViewPager(viewPager, cat);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.Notes_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tab = tabLayout.getTabAt((x - 1) - position);

        if (tab != null) {

            tab.select();
        }

    }

    private int setupViewPager(ViewPager viewPager, int cat) {

        ViewPagerAdapter Adapter = new ViewPagerAdapter(getSupportFragmentManager());

        SQLiteDatabase myDatabase = getApplicationContext().openOrCreateDatabase(getString(R.string.databasePath), MODE_PRIVATE, null);

        Cursor cursor = myDatabase.rawQuery("SELECT * FROM categories WHERE Parent=+" + cat, null);

        cursor.moveToFirst();

        int counter = 0;     // a counter for counting tabs

        List<String> titles = new LinkedList<>();

        while (!cursor.isAfterLast()) {

            int id_tab = cursor.getInt(cursor.getColumnIndex("Id_Cat"));

            arrayListBundle.add(new Bundle());
            arrayListBundle.get(counter).putInt("Id_Cat", id_tab);
            titles.add(cursor.getString(cursor.getColumnIndex("Name")));

            cursor.moveToNext();
            counter++;
        }

        int i;

        for (i = counter - 1; i >= 0; i--) {

            NotesFragment notesFragment = new NotesFragment();
            notesFragment.setArguments(arrayListBundle.get(i));
            Adapter.addFragments(notesFragment, titles.get(i));

        }

        cursor.close();

        viewPager.setAdapter(Adapter);
        return counter;

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

}
