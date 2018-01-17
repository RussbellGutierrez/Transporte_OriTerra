package com.example.russbell.transporte_oriterra.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Class.Feed_detdocu;
import com.example.russbell.transporte_oriterra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 28/08/2017.
 */

public class DetdocuHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.txt_codigo) TextView txt_codigo;
    @BindView(R.id.txt_descripcion) TextView txt_descripcion;
    @BindView(R.id.txt_cantidad) TextView txt_cantidad;
    @BindView(R.id.txt_imp) TextView txt_imp;

    public DetdocuHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(Feed_detdocu item){
        txt_imp.setText(item.getFeedimporte());
        txt_codigo.setText(item.getFeedcodart());
        txt_cantidad.setText(item.getFeedcantart());
        txt_descripcion.setText(item.getFeeddesart());
    }
}
