package com.example.russbell.transporte_oriterra.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.russbell.transporte_oriterra.Class.Feed_cliente;
import com.example.russbell.transporte_oriterra.Class.NClass_documento;
import com.example.russbell.transporte_oriterra.Dialogs.DlgDoc;
import com.example.russbell.transporte_oriterra.Dialogs.DlgObs;
import com.example.russbell.transporte_oriterra.Holder.DocumentoHolder;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.AdapterComponents;
import com.example.russbell.transporte_oriterra.Interfaces.MultipleEvents;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_helper;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;
import com.example.russbell.transporte_oriterra.Threads.ThreadDocumento;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;

import java.util.ArrayList;

/**
 * Created by Russbell on 21/08/2017.
 */

public class Adapter_documento extends RecyclerView.Adapter<DocumentoHolder>
        implements MultipleEvents {

    private Context context;
    private String lastcodcliente="";
    private String newcodcliente="";
    private AdapterComponents listener;
    private FragmentManager fragmentManager;
    private ArrayList<Feed_cliente> feed=new ArrayList<>();
    private static final String TAG="Adap_documento";

    public Adapter_documento(FragmentManager fragmentManager, Context context, AdapterComponents listener) {
        this.context=context;
        this.listener=listener;
        this.fragmentManager=fragmentManager;
        if (!ThreadDocumento.tmpDocumento.isEmpty()){
            for(NClass_documento documento:ThreadDocumento.tmpDocumento){
                if (documento.getNropla().equals(Holder_data.nropla)){
                    newcodcliente=documento.getCliente();
                    if (!lastcodcliente.equals(newcodcliente)){
                        lastcodcliente=newcodcliente;
                        Feed_cliente item=new Feed_cliente();
                        item.setFeedcodigo(documento.getCliente());
                        item.setFeednombre(documento.getNomcli());
                        item.setFeeddireccion(documento.getDircli());
                        item.setFeedcelular(documento.getTelefono());
                        item.setFeedestado(documento.getEstado());
                        feed.add(item);
                        notifyDataSetChanged();
                    }
                }
            }
            listener.onFinishAdapter(feed);
        }else {
            drawDocumento(context);
        }
    }

    private void drawDocumento(Context context){
        if (!ThreadDocumento.tmpDocumento.isEmpty()){
            ThreadDocumento.tmpDocumento.clear();
        }

        SQL_sentences sql = new SQL_sentences(context);
        SQL_helper helper=new SQL_helper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select distinct cliente,nomcli," +
                "dircli,telefono,estado,negocio,ruta,sucursal,esquema,codprov,tipopla,seriepla," +
                "nropla,fletero,XCoord,YCoord,documento,nrodoc,total,fecha,tipopago " +
                "from documentos " +
                "where nropla="+Holder_data.nropla,null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                do {
                    Feed_cliente item=new Feed_cliente();
                    item.setFeedcodigo(cursor.getString(0));
                    item.setFeednombre(cursor.getString(1));
                    item.setFeeddireccion(cursor.getString(2));
                    item.setFeedcelular(cursor.getString(3));
                    item.setFeedestado(cursor.getString(4));
                    feed.add(item);
                    notifyDataSetChanged();

                    NClass_documento docu=new NClass_documento(cursor.getString(7),cursor.getString(8),
                            cursor.getString(9),cursor.getString(10),cursor.getString(11),cursor.getString(12),
                            cursor.getString(13),cursor.getString(0),cursor.getString(1),cursor.getString(2),
                            cursor.getString(3),cursor.getString(5),cursor.getString(14),cursor.getString(15),
                            cursor.getString(16),cursor.getString(17),cursor.getString(18),cursor.getString(4),cursor.getString(19),
                            cursor.getString(6),cursor.getString(20));

                    ThreadDocumento.tmpDocumento.add(docu);
                }while (cursor.moveToNext());
            }
            listener.onFinishAdapter(feed);
            cursor.close();
        }
    }

    @Override
    public DocumentoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cliente,parent,false);
        return new DocumentoHolder(view);
    }

    @Override
    public void onBindViewHolder(final DocumentoHolder holder, int position) {
        //Log.i(TAG,"BindviewHolder is execute!!!");
        Holder_data.newListener(Adapter_documento.this,holder,holder.getAdapterPosition());
        final Feed_cliente item=feed.get(position);
        holder.bind(item);
        holder.fab_docu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Holder_data.newDataCliente(item.getFeedcodigo(),item.getFeednombre());
                new DlgDoc().show(fragmentManager,"Dlg_docu");
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Holder_data.newListener(Adapter_documento.this,holder,holder.getAdapterPosition());
                Holder_data.newRefreshAdapter(1);
                Holder_data.newDataCliente(item.getFeedcodigo(),item.getFeednombre());
                new DlgObs().show(fragmentManager,"Dlg_obs");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return feed.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void setFilter(ArrayList<Feed_cliente> newFeed){
        feed=new ArrayList<>();
        feed.addAll(newFeed);
        notifyDataSetChanged();
    }

    @Override
    public void CustomEvents(String event) {
        Log.i(TAG,"Customevents from adap");
        switch (event){
            case "Atendido":
                Holder_data.holder.txt_estado.setText("Atendido");
                Holder_data.holder.txt_estado.setTextColor(Color.parseColor("#1BBF18"));
                changeStateClient("1");
                Holder_data.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Marker marker: Holder_data.markerArray){
                            String codcliente=marker.getSnippet();
                            Log.i(TAG,"Compare snippet "+codcliente+" with code "+Holder_data.codigocliente);
                            if (codcliente.equals(Holder_data.codigocliente)){
                                marker.setIcon(changeMarker("verde"));
                                Log.i(TAG,"The marker from "+codcliente+" is reachable!!!");
                                break;
                            }else{
                                Log.i(TAG,"No marker exist :c");
                            }
                        }
                    }
                });
                break;
            case "Rechazado":
                Holder_data.holder.txt_estado.setText("Rechazado");
                Holder_data.holder.txt_estado.setTextColor(Color.parseColor("#DA3535"));
                changeStateClient("2");
                Holder_data.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        for(Marker marker: Holder_data.markerArray){
                            String codcliente=marker.getSnippet();
                            Log.i(TAG,"Compare snippet "+codcliente+" with code "+Holder_data.codigocliente);
                            if (codcliente.equals(Holder_data.codigocliente)){
                                marker.setIcon(changeMarker("rojo"));
                                Log.i(TAG,"The marker from "+codcliente+" is reachable!!!");
                                break;
                            }else{
                                Log.i(TAG,"No marker exist :c");
                            }
                        }
                    }
                });
                break;
        }
    }

    private Icon changeMarker(String marcador){
        IconFactory iconFactory = IconFactory.getInstance(context);
        Icon icon = null;
        switch(marcador){
            case "camionero":
                icon= iconFactory.fromResource(R.drawable.marktruckmini);
                break;
            case "meta":
                icon= iconFactory.fromResource(R.drawable.markstart);
                break;
            case "verde":
                icon= iconFactory.fromResource(R.drawable.pin_llego);
                break;
            case "rojo":
                icon= iconFactory.fromResource(R.drawable.pin_rechazo);
                break;
            case "gris":
                icon= iconFactory.fromResource(R.drawable.pin_nuevo_falta);
                break;
        }
        return icon;
    }

    private void changeStateClient(String estado){
        Log.d(TAG,"Reach state change");

        Feed_cliente item=feed.get(Holder_data.position);
        Log.d(TAG,"Antes el cliente "+item.getFeedcodigo()+" tiene estado "+item.getFeedestado()+" en Feed_cliente");
        item.setFeedestado(estado);
        feed.set(Holder_data.position,item);
        //TODO:Prueba de que guardo el dato
        Feed_cliente item2=feed.get(Holder_data.position);
        Log.d(TAG,"Ahora el cliente "+item2.getFeedcodigo()+" tiene estado "+item2.getFeedestado()+" en Feed_cliente");

        int i=0;
        for (NClass_documento docu: ThreadDocumento.tmpDocumento){
            if (docu.getCliente().equals(Holder_data.codigocliente)){
                NClass_documento documento=ThreadDocumento.tmpDocumento.get(i);
                Log.d(TAG,"Antes el cliente "+documento.getCliente()+" tiene estado "+documento.getEstado());
                documento.setEstado(estado);
                ThreadDocumento.tmpDocumento.set(i,documento);
                NClass_documento documento2=ThreadDocumento.tmpDocumento.get(i);
                Log.d(TAG,"Ahora el cliente "+documento2.getCliente()+" tiene estado "+documento2.getEstado());
                Holder_data.newRefreshAdapter(1);
                //break;
            }
            i++;
        }
    }
}
