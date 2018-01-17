package com.example.russbell.transporte_oriterra.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.russbell.transporte_oriterra.Adapter.Adapter_pager;
import com.example.russbell.transporte_oriterra.Dialogs.DlgInit;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.ComunicatorListener;
import com.example.russbell.transporte_oriterra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rusbell Gutierrez on 15/08/2017.
 */

public class Activity_cuerpo extends AppCompatActivity
        implements ComunicatorListener{

    private static String TAG="Debug_Actcuer";
    public static String POSITION = "POSITION";
    private SearchView searchView;

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.viewpager) ViewPager viewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuerpo);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        viewPager.setAdapter(new Adapter_pager(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setupWithViewPager(viewPager);

        Holder_data.newActivity(this);

        int position=0;
        Bundle extras=getIntent().getExtras();
        if (extras!=null){
            position=extras.getInt("viewposicion");
        }

        viewPager.setCurrentItem(position);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        new DlgInit().show(getFragmentManager(),"Dialog_init");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(POSITION, tabLayout.getSelectedTabPosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        viewPager.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    @Override
    public void OnFragmentListener(Bundle arg) {
    }
}
