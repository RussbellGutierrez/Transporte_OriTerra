package com.example.russbell.transporte_oriterra.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.russbell.transporte_oriterra.Activity.Activity_facturas;
import com.example.russbell.transporte_oriterra.Class.Feed_detacli;
import com.example.russbell.transporte_oriterra.Class.NClass_detalle;
import com.example.russbell.transporte_oriterra.Class.NClass_documento;
import com.example.russbell.transporte_oriterra.Holder.DetacliHolder;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.ViewClickListener;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_helper;
import com.example.russbell.transporte_oriterra.Threads.ThreadDetalle;
import com.example.russbell.transporte_oriterra.Threads.ThreadDocumento;

import java.util.ArrayList;

/**
 * Created by Russbell on 25/08/2017.
 */

public class Adapter_detdocumento extends RecyclerView.Adapter<DetacliHolder>{

    private static String TAG="Debug_adapdocu";
    private Activity activity;
    private ArrayList<Feed_detacli> feeddocu= new ArrayList<>();

    public Adapter_detdocumento(Activity activity){
        this.activity=activity;
        if (!ThreadDocumento.tmpDocumento.isEmpty()){
            for(NClass_documento documento:ThreadDocumento.tmpDocumento){
                if (documento.getNropla().equals(Holder_data.nropla) && documento.getCliente().equals(Holder_data.codigocliente)){

                        Feed_detacli item=new Feed_detacli(documento.getNrodoc(),
                                documento.getTotal(),documento.getNegocio());
                        feeddocu.add(item);
                        notifyDataSetChanged();
                }
            }
        }else {
            drawDetalle();
        }
    }

    private void drawDetalle(){
        SQL_helper helper=new SQL_helper(activity);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select cliente,nrodoc,total,negocio " +
                "from documentos " +
                "where cliente="+Holder_data.codigocliente+" " +
                "and nropla="+Holder_data.nropla,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                do {
                    Feed_detacli item=new Feed_detacli(cursor.getString(1),
                            cursor.getString(2),cursor.getString(3));
                    feeddocu.add(item);

                    notifyDataSetChanged();
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
    }

    @Override
    public DetacliHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detacli,parent,false);
        return new DetacliHolder(view);
    }

    @Override
    public void onBindViewHolder(final DetacliHolder holder, int position) {
        final Feed_detacli item=feeddocu.get(position);
        holder.bind(item);
        holder.setInterface(new ViewClickListener() {
            @Override
            public void OnClickListenerView(View view, int position) {
                Intent intent=new Intent(activity, Activity_facturas.class);
                intent.putExtra("numdocu",item.getDetdocumento());
                intent.putExtra("importe",item.getDetimporte());
                activity.startActivity(intent);
                //Toast.makeText(activity,"Funciona :D",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return feeddocu.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }
}
