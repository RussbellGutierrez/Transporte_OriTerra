package com.example.russbell.transporte_oriterra.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Adapter.Adapter_detdocumento;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 25/08/2017.
 */

public class DlgDoc extends DialogFragment{

    @BindView(R.id.txt_nomcliente) TextView txt_nomcliente;
    @BindView(R.id.recycler_cliente) RecyclerView recyclerView;
    @BindView(R.id.btn_listo) Button btn_listo;

    public DlgDoc(){
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDocDialog();
    }

    public AlertDialog createDocDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.dlgdet,null);
        ButterKnife.bind(this,v);
        txt_nomcliente.setText(Holder_data.nombrecliente);
        mostrarDocumento();
        builder.setView(v);
        btn_listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return builder.create();
    }

    public void mostrarDocumento(){
        LinearLayoutManager linear=new LinearLayoutManager(getActivity());
        Adapter_detdocumento adapter=new Adapter_detdocumento(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linear);
        recyclerView.setAdapter(adapter);
    }
}
