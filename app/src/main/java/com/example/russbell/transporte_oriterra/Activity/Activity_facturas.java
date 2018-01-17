package com.example.russbell.transporte_oriterra.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Adapter.Adapter_detalle;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 27/08/2017.
 */

public class Activity_facturas extends AppCompatActivity
        implements View.OnClickListener{

    private Adapter_detalle adapter;

    @BindView(R.id.txt_nomcli) TextView txt_nomcli;
    @BindView(R.id.txt_numdocu) TextView txt_numdocu;
    @BindView(R.id.txt_importe) TextView txt_importe;
    @BindView(R.id.txt_plandocu) TextView txt_plani;
    @BindView(R.id.fab_close) FloatingActionButton fab_close;
    @BindView(R.id.recyclerDocumento) RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documento);
        Bundle extra=getIntent().getExtras();
        ButterKnife.bind(this);
        String numdocu="";
        String importe="";
        if (extra!=null){
            numdocu=extra.getString("numdocu");
            importe=extra.getString("importe");
            txt_numdocu.setText(numdocu);
            txt_importe.setText(importe);
        }
        txt_nomcli.setText(Holder_data.nombrecliente);
        txt_plani.setText(Holder_data.nropla);
        fab_close.setOnClickListener(this);
        iniciarAdapter(numdocu);
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,Activity_cuerpo.class);
        intent.putExtra("viewposicion",1);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,Activity_cuerpo.class);
        intent.putExtra("viewposicion",1);
        startActivity(intent);
        finish();
    }

    public void iniciarAdapter(String numdocu){
        LinearLayoutManager linear=new LinearLayoutManager(this);
        adapter=new Adapter_detalle(numdocu,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(adapter);
    }
}
