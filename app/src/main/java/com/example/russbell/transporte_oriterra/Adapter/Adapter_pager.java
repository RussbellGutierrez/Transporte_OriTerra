package com.example.russbell.transporte_oriterra.Adapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import com.example.russbell.transporte_oriterra.Fragment.Fragment_cliente;
import com.example.russbell.transporte_oriterra.Fragment.Fragment_producto;
import com.example.russbell.transporte_oriterra.Fragment.Fragment_ruta;
import com.example.russbell.transporte_oriterra.Fragment.Fragment_ruta_offline;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by Rusbell Gutierrez on 15/08/2017.
 */

public class Adapter_pager extends FragmentStatePagerAdapter
        implements TabLayout.OnTabSelectedListener{

    private static int NUM_ITEMS = 3;
    private String[] titulos=new String[]{"PRODUCTOS","CLIENTES","RUTAS"};

    public Adapter_pager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Fragment_producto();
            case 1:
                return new Fragment_cliente();
            case 2:
                return new Fragment_ruta_offline();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position){
        return titulos[position];
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        getItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        getItem(tab.getPosition());
    }
}
