package com.example.russbell.transporte_oriterra.Dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.example.russbell.transporte_oriterra.R;

/**
 * Created by Rusbell Gutierrez on 11/08/2017.
 */

public class DlgBD  extends DialogFragment{
    private boolean estado=true;
    TextView basedatos;
    ImageView logobd;

    public DlgBD(){
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return createRadio();
    }

    public AlertDialog createRadio(){
        AlertDialog.Builder build=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();

        View v=inflater.inflate(R.layout.dlgbd,null);
        RadioGroup rg=(RadioGroup)v.findViewById(R.id.radio_group);
        basedatos=(TextView)getActivity().findViewById(R.id.txt_basedatos);
        logobd=(ImageView)getActivity().findViewById(R.id.logo_bd);
        build.setView(v);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                RadioButton rb1=(RadioButton)group.findViewById(R.id.rb_oriunda);
                RadioButton rb2=(RadioButton)group.findViewById(R.id.rb_terranorte);

                if(rb1.isChecked()){
                    estado=true;
                }else if(rb2.isChecked()) {
                    estado=false;
                }
            }
        });

        Button ok=(Button)v.findViewById(R.id.btn_ok);
        Button cancel=(Button)v.findViewById(R.id.btn_cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado){
                    basedatos.setText("oriunda");
                    logobd.setImageResource(R.drawable.ori_logo);
                    dismiss();
                }else {
                    basedatos.setText("terranorte");
                    logobd.setImageResource(R.drawable.terra_logo);
                    dismiss();
                }
            }
        });

        return build.create();
    }
}
