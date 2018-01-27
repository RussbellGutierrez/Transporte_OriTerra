package com.example.russbell.transporte_oriterra.Adapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ScrollingView;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.example.russbell.transporte_oriterra.Fragment.Fragment_cliente;
import com.example.russbell.transporte_oriterra.Fragment.Fragment_producto;
import com.example.russbell.transporte_oriterra.Fragment.Fragment_ruta;
import com.example.russbell.transporte_oriterra.Fragment.Fragment_ruta_offline;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rusbell Gutierrez on 15/08/2017.
 */

public class Adapter_pager extends FragmentStatePagerAdapter
        implements TabLayout.OnTabSelectedListener{

    private static int NUM_ITEMS = 3;
    private String[] titulos=new String[]{"PRODUCTOS","CLIENTES","RUTAS"};
    //NEW
    /*private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;*/

    public Adapter_pager(FragmentManager fm) {
        super(fm);
        /*mFragmentManager = fm;
        mFragmentTags = new HashMap<Integer, String>();*/
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

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    /*@Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object object = super.instantiateItem(container, position);
        if (object instanceof Fragment) {
            Fragment fragment = (Fragment) object;
            String tag = fragment.getTag();
            mFragmentTags.put(position, tag);
        }
        return object;
    }

    public Fragment getFragment(int position) {
        Fragment fragment = null;
        String tag = mFragmentTags.get(position);
        if (tag != null) {
            fragment = mFragmentManager.findFragmentByTag(tag);
        }
        return fragment;
    }*/
}
