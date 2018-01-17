package com.example.russbell.transporte_oriterra.Holder;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Class.Feed_detalle;
import com.example.russbell.transporte_oriterra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 24/08/2017.
 */

public class DetalleHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.txt_codigo) TextView txt_codigo;
    @BindView(R.id.txt_descripcion) TextView txt_descripcion;
    @BindView(R.id.txt_cantidad) TextView txt_cantidad;
    @BindView(R.id.img_estado) ImageView img_estado;

    public DetalleHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(Feed_detalle det){
        String colorString="#E63013";
        txt_codigo.setText(det.getCodigo());
        txt_descripcion.setText(det.getDescripcion());
        txt_cantidad.setText(det.getCantidad());
        if (!det.getEstado().equals("Completo")){
            img_estado.setImageResource(R.drawable.close);
            img_estado.setBackgroundColor(Color.parseColor(colorString));
        }
    }
}
