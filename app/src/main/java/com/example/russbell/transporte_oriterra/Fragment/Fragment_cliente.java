package com.example.russbell.transporte_oriterra.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
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

import com.example.russbell.transporte_oriterra.Adapter.Adapter_documento;
import com.example.russbell.transporte_oriterra.Class.Feed_cliente;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.AdapterComponents;
import com.example.russbell.transporte_oriterra.Interfaces.ComunicatorListener;
import com.example.russbell.transporte_oriterra.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Rusbell Gutierrez on 15/08/2017.
 */

public class Fragment_cliente extends Fragment
        implements AdapterComponents{

    private Adapter_documento adapter;
    private ComunicatorListener listener;
    private ArrayList<Feed_cliente> feed=new ArrayList<>();
    private static final String TAG="Frag_cliente";

    @BindView(R.id.rv_Client) RecyclerView recyclerView;
    @BindView(R.id.pb_cliente) ProgressBar progressBar;
    @BindView(R.id.txt_mensaje) TextView txt_mensaje;

    public Fragment_cliente(){
    }

    public static Fragment_cliente newCliente(Bundle arg){
        Fragment_cliente f=new Fragment_cliente();
        if (arg!=null){
            f.setArguments(arg);
        }
        return f;
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
        Log.w(TAG,"Attach on clientefrag");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup viewGroup=(ViewGroup)inflater.
                inflate(R.layout.fragment_cliente,container,false);
        ButterKnife.bind(this,viewGroup);
        iniciarAdapter();
        setHasOptionsMenu(true);

        if (Holder_data.refreshing==1){
            iniciarAdapter();
        }
        Log.w(TAG,"Createview on clientefrag");
        return viewGroup;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
        Log.w(TAG,"Detach on clientefrag");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_cliente,menu);
        MenuItem menuItem=menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setInputType(InputType.TYPE_CLASS_PHONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText=newText.toLowerCase();
                ArrayList<Feed_cliente> nuevofeed=new ArrayList<>();
                for (Feed_cliente feeder: feed){
                    String codcliente=feeder.getFeedcodigo().toLowerCase();
                    if (codcliente.contains(newText)){
                        nuevofeed.add(feeder);
                    }
                }
                if (nuevofeed.isEmpty()){
                    Toast.makeText(getContext(),"No existe el cliente",Toast.LENGTH_SHORT).show();
                }else {
                    adapter.setFilter(nuevofeed);
                }
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void iniciarAdapter(){
        LinearLayoutManager linear=new LinearLayoutManager(getActivity());
        adapter=new Adapter_documento(getFragmentManager(),getContext(),this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFinishAdapter(ArrayList<?> feed) {
        this.feed= (ArrayList<Feed_cliente>) feed;
        Log.w(TAG,"FinishAdapter on clientefrag");
    }
}
