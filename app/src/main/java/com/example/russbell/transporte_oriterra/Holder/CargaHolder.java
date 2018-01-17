package com.example.russbell.transporte_oriterra.Holder;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Class.Feed_producto;
import com.example.russbell.transporte_oriterra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 21/08/2017.
 */

public class CargaHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.caja)  TextView caja;
    @BindView(R.id.resto) TextView resto;
    @BindView(R.id.total) TextView total;
    @BindView(R.id.unidad) TextView unidad;
    @BindView(R.id.cod_art) public TextView cod_art;
    @BindView(R.id.nom_art) public TextView nom_art;
    @BindView(R.id.observacion) public EditText observacion;
    @BindView(R.id.fab_obs) public FloatingActionButton fab_obs;
    @BindView(R.id.fab_edit) public FloatingActionButton fab_edit;

    public CargaHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void bind(Feed_producto feed){
        if (feed.getFeedobser().equals("Falta")){
            cod_art.setText(feed.getFeedcod());
            nom_art.setText(feed.getFeedart());
            caja.setText(feed.getFeedcaja());
            unidad.setText(feed.getFeedcaja());
            resto.setText(feed.getFeedresto());
            total.setText(feed.getFeedtotal());
        }else {
            cod_art.setText(feed.getFeedcod());
            nom_art.setText(feed.getFeedart());
            caja.setText(feed.getFeedcaja());
            unidad.setText(feed.getFeedcaja());
            resto.setText(feed.getFeedresto());
            total.setText(feed.getFeedtotal());
            observacion.setText(feed.getFeedobser());

            String darkred="#E63013";
            fab_edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(darkred)));
            fab_edit.setImageResource(R.drawable.close);
            fab_edit.setTag("mal");
        }
    }
}
