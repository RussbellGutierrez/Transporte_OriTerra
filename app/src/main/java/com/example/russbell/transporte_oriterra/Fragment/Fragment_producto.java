package com.example.russbell.transporte_oriterra.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Activity.Activity_detprod;
import com.example.russbell.transporte_oriterra.Adapter.Adapter_carga;
import com.example.russbell.transporte_oriterra.Class.Feed_producto;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.AdapterComponents;
import com.example.russbell.transporte_oriterra.Interfaces.ComunicatorListener;
import com.example.russbell.transporte_oriterra.Interfaces.UpdateFragment;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rusbell Gutierrez on 15/08/2017.
 */

public class Fragment_producto extends Fragment
        implements View.OnClickListener,
        AdapterComponents{

    private SQL_sentences sql;
    private Adapter_carga adapter;
    private ComunicatorListener listener;
    private static String TAG="Debug_fragprod";
    private ArrayList<Feed_producto> feed=new ArrayList<>();

    @BindView(R.id.card) CardView card;
    @BindView(R.id.texto) TextView texto;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.fab_scan) FloatingActionButton fab_scan;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    public Fragment_producto(){
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ComunicatorListener){
            listener=(ComunicatorListener) context;
        }else {
            throw new RuntimeException(context.toString()
                    +" debe implementar ComunicatorListener");
        }
    }

    /*public static Fragment_producto newInstance() {
        return new Fragment_producto();
    }*/

    /*public static Fragment_producto newProducto(Bundle arg){
        Fragment_producto f=new Fragment_producto();
        if (arg!=null){
            f.setArguments(arg);
        }
        return f;
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup viewGroup=(ViewGroup)inflater.
                inflate(R.layout.fragment_producto,container,false);
        ButterKnife.bind(this,viewGroup);
        iniciaAdapter();
        fab_scan.setOnClickListener(this);
        setHasOptionsMenu(true);
        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_producto,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<Feed_producto> nuevofeed=new ArrayList<>();
                for (Feed_producto feeder: feed){
                    String name=feeder.getFeedart().toLowerCase();
                    if (name.contains(newText)){
                        nuevofeed.add(feeder);
                    }
                }
                if (nuevofeed.isEmpty()){
                    Toast.makeText(getContext(),"No existe el producto",Toast.LENGTH_SHORT).show();
                }else {
                    adapter.setFilter(nuevofeed);
                }
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_det:
                Intent intent=new Intent(getActivity(), Activity_detprod.class);
                startActivity(intent);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFinishAdapter(ArrayList<?> feed) {
        this.feed= (ArrayList<Feed_producto>) feed;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        sql=new SQL_sentences(getContext());
        IntentResult scanResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        String scanContent=scanResult.getContents();
        String scanFormat=scanResult.getFormatName();
        if (scanContent==null && scanFormat==null){
            Toast.makeText(getContext(),"No se escaneo producto",Toast.LENGTH_SHORT).show();
        }else {
            String codart=sql.searchScan(scanContent,Holder_data.nropla);
            if (!codart.equals("")){
                selecItem(codart);
            }
        }
    }

    private void iniciaAdapter(){
        LinearLayoutManager linear=new LinearLayoutManager(getActivity());
        adapter=new Adapter_carga(getActivity(),getContext(),card,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(adapter);
    }

    private void selecItem(String codigo){
        int i=0;
        for (Feed_producto feeder:feed){
            String codart=feeder.getFeedart().toLowerCase();
            if (codart.contains(codigo)){
                adapter.setPositioning(codart);
                break;
            }else {
                if (i==feed.size()){
                    Toast.makeText(getContext(),"Producto no encontrado",Toast.LENGTH_SHORT).show();
                }
            }i++;
        }
    }

    /*@Override
    public void update() {
        Log.i(TAG,"Fragment producto is execute update");
    }*/
}
