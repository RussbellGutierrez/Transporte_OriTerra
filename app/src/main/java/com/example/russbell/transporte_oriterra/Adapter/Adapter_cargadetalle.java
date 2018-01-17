package com.example.russbell.transporte_oriterra.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.russbell.transporte_oriterra.Class.Feed_detalle;
import com.example.russbell.transporte_oriterra.Holder.DetalleHolder;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.AdapterSize;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_helper;

import java.util.ArrayList;

/**
 * Created by Russbell on 21/08/2017.
 */

public class Adapter_cargadetalle extends RecyclerView.Adapter<DetalleHolder>{

    private int mala=0;
    private int buena=0;
    private AdapterSize listener;
    private static final String TAG="Debug_Adapdet";
    private ArrayList<Feed_detalle> feed=new ArrayList<>();

    public Adapter_cargadetalle(Context context, AdapterSize listener){
        this.listener=listener;

        SQL_helper helper=new SQL_helper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select aCodart, aDescrip,total,observacion " +
                "from carga " +
                "where observacion not like 'Falta' " +
                "and nropla="+Holder_data.nropla+" "+
                "order by aProveedor,aLinea,aGenerico,aResto asc",null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                do {
                    Feed_detalle item=new Feed_detalle();
                    item.setCodigo(cursor.getString(0));
                    item.setDescripcion(cursor.getString(1));
                    item.setCantidad(cursor.getString(2));
                    item.setEstado(cursor.getString(3));
                    feed.add(item);
                    if (item.getEstado().equals("Completo")){
                        buena+=1;
                    }else {
                        mala+=1;
                    }
                    notifyDataSetChanged();
                }while (cursor.moveToNext());
            }
            listener.onFinishCount(buena,mala);
            cursor.close();
        }
    }

    @Override
    public DetalleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detprod,parent,false);
        return new DetalleHolder(view);
    }

    @Override
    public void onBindViewHolder(DetalleHolder holder, int position) {
        Feed_detalle item=feed.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
