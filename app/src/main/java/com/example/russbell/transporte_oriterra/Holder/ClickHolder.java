package com.example.russbell.transporte_oriterra.Holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.russbell.transporte_oriterra.Class.Class_planilla;
import com.example.russbell.transporte_oriterra.Interfaces.ViewClickListener;
import com.example.russbell.transporte_oriterra.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Russbell on 12/05/2017.
 */

public class ClickHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener{

    @BindView(R.id.reparto) TextView reparto;
    @BindView(R.id.fecha) TextView fecha;

    private ViewClickListener mListener;

    public ClickHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this,itemView);
        itemView.setOnClickListener(this);
    }

    public void setInterface(ViewClickListener mListener){
        this.mListener=mListener;
    }

    public void bind(final Class_planilla plan){
        reparto.setText(plan.getPlanilla());
        fecha.setText(plan.getFecha());
    }

    @Override
    public void onClick(View v) {
        mListener.OnClickListenerView(v,getAdapterPosition());
    }
}

