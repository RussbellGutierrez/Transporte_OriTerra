package com.example.russbell.transporte_oriterra.Holder;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Class.Feed_cliente;
import com.example.russbell.transporte_oriterra.R;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 24/08/2017.
 */

public class DocumentoHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_codigo_cliente) TextView txt_codigo_cliente;
    @BindView(R.id.txt_nombre) TextView txt_nombre;
    @BindView(R.id.txt_direccion) TextView txt_direccion;
    @BindView(R.id.txt_celular) TextView txt_celular;
    @BindView(R.id.txt_estado) public TextView txt_estado;
    @BindView(R.id.fab_docu) public FloatingActionButton fab_docu;

    private static final String TAG="DocumentHolder";

    public DocumentoHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(Feed_cliente item){
        txt_codigo_cliente.setText(item.getFeedcodigo());
        txt_nombre.setText(item.getFeednombre());
        txt_direccion.setText(item.getFeeddireccion());
        txt_celular.setText(item.getFeedcelular());
        switch (item.getFeedestado()) {
            case "0":
                txt_estado.setText("Pendiente");
                txt_estado.setTextColor(Color.GRAY);
                Log.w(TAG,"El cliente "+item.getFeedcodigo()+" esta Pendiente");
                break;
            case "1":
                txt_estado.setText("Atendido");
                txt_estado.setTextColor(Color.parseColor("#1BBF18"));
                Log.w(TAG,"El cliente "+item.getFeedcodigo()+" esta Atendido");
                break;
            case "2":
                txt_estado.setText("Rechazado");
                txt_estado.setTextColor(Color.parseColor("#DA3535"));
                Log.w(TAG,"El cliente "+item.getFeedcodigo()+" esta Rechazado");
                break;
            //==========CUANDO SE GUARDA DATOS DE ESTADO SIN CONEXION, RECONOCE ESTOS STRINGS ==============
            case "Atendido":
                txt_estado.setText("Atendido");
                txt_estado.setTextColor(Color.parseColor("#1BBF18"));
                Log.w(TAG,"El cliente "+item.getFeedcodigo()+" esta Atendido");
                break;
            case "Rechazado":
                txt_estado.setText("Rechazado");
                txt_estado.setTextColor(Color.parseColor("#DA3535"));
                Log.w(TAG,"El cliente "+item.getFeedcodigo()+" esta Rechazado");
                break;
        }
    }
}
