package com.example.russbell.transporte_oriterra.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.R;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by Rusbell Gutierrez on 14/08/2017.
 */

public class DlgGoogle extends DialogFragment {

    private GoogleApiClient googleApiClient;

    public DlgGoogle(){
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState){
        return dialogGoogle();
    }

    public AlertDialog dialogGoogle(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.dlggoogle,null);
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
                GoogleApiClient api= Holder_data.googleApiClient;
                api.connect();
                dismiss();
            }
        });
        return builder.create();
    }
}
