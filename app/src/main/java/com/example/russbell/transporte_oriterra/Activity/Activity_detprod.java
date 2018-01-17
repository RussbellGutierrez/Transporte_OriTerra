package com.example.russbell.transporte_oriterra.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Adapter.Adapter_cargadetalle;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.AdapterSize;
import com.example.russbell.transporte_oriterra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 23/08/2017.
 */

public class Activity_detprod extends AppCompatActivity
        implements View.OnClickListener,
        AdapterSize{

    private Adapter_cargadetalle adapter;

    @BindView(R.id.txt_camionerodet) TextView txt_camion;
    @BindView(R.id.txt_planilladet) TextView txt_planilla;
    @BindView(R.id.txt_cantbuena) TextView txt_buena;
    @BindView(R.id.txt_cantmala) TextView txt_mala;
    @BindView(R.id.recycler_detalle) RecyclerView recyclerView;
    @BindView(R.id.fab_listo) FloatingActionButton fab_listo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detprod);
        ButterKnife.bind(this);
        txt_camion.setText(Holder_data.codcamion);
        txt_planilla.setText(Holder_data.nropla);
        iniciarAdapter();
        fab_listo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(this,Activity_cuerpo.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(this,Activity_cuerpo.class);
        startActivity(intent);
        finish();
    }

    public void iniciarAdapter(){
        LinearLayoutManager linear=new LinearLayoutManager(this);
        adapter=new Adapter_cargadetalle(this,this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFinishCount(int buenas, int malas) {
        txt_buena.setText(String.valueOf(buenas));
        txt_mala.setText(String.valueOf(malas));
    }
}
