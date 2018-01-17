package com.example.russbell.transporte_oriterra.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Class.Class_planilla;
import com.example.russbell.transporte_oriterra.Class.Feed_detacli;
import com.example.russbell.transporte_oriterra.Interfaces.ViewClickListener;
import com.example.russbell.transporte_oriterra.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 24/08/2017.
 */

public class DetacliHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    @BindView(R.id.txt_documento) TextView txt_boletacliente;
    @BindView(R.id.txt_monto) TextView txt_montototal;
    @BindView(R.id.txt_tipo) TextView txt_tiponegocio;

    private ViewClickListener mListener;

    public DetacliHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
    }

    public void setInterface(ViewClickListener mListener){
        this.mListener=mListener;
    }

    public void bind(final Feed_detacli item){
        txt_boletacliente.setText(item.getDetdocumento());
        txt_montototal.setText(item.getDetimporte());
        txt_tiponegocio.setText(item.getDetnegocio());
    }

    @Override
    public void onClick(View v) {
        mListener.OnClickListenerView(v,getAdapterPosition());
    }
}
