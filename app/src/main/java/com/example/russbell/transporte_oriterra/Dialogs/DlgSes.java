package com.example.russbell.transporte_oriterra.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.russbell.transporte_oriterra.Activity.Activity_acceso;
import com.example.russbell.transporte_oriterra.R;

/**
 * Created by Rusbell Gutierrez on 14/08/2017.
 */

public class DlgSes extends DialogFragment{
    public DlgSes(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState){
        return sesionDialog();
    }

    public AlertDialog sesionDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.dlgses,null);
        builder.setView(v);
        Button ok=(Button)v.findViewById(R.id.ok);
        Button cancel=(Button)v.findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inicio=new Intent(getActivity(), Activity_acceso.class);
                startActivity(inicio);
                System.exit(0);
            }
        });
        return builder.create();
    }
}
