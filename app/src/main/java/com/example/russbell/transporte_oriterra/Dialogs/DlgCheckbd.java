package com.example.russbell.transporte_oriterra.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;

/**
 * Created by Russbell on 4/12/2017.
 */

public class DlgCheckbd extends DialogFragment {

    public DlgCheckbd(){
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState){
        return sesionDialog();
    }

    public AlertDialog sesionDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.dlgcheckbd,null);
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
                SQL_sentences sql=new SQL_sentences(getActivity());
                sql.truncateTB();
                dismiss();
            }
        });
        return builder.create();
    }
}
