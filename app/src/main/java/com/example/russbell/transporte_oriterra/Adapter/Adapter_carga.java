package com.example.russbell.transporte_oriterra.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.example.russbell.transporte_oriterra.Class.Feed_producto;
import com.example.russbell.transporte_oriterra.Class.NClass_carga;
import com.example.russbell.transporte_oriterra.Holder.CargaHolder;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.AdapterComponents;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_helper;
import com.example.russbell.transporte_oriterra.SQL.SQL_sentences;
import com.example.russbell.transporte_oriterra.Threads.ThreadCarga;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Russbell on 21/08/2017.
 */

public class Adapter_carga extends RecyclerView.Adapter<CargaHolder>{

    private CardView card;
    private SQL_sentences sql;
    private Activity activity;
    private long lastClickTime=0;
    private AdapterComponents listener;
    private static final String TAG="Debug AdapProd";
    private ArrayList<Feed_producto> feed=new ArrayList<>();
    private HashMap<Integer,String> editTextList= new HashMap<>();

    public Adapter_carga(Activity activity, Context context, CardView card, AdapterComponents listener){
        this.card=card;
        this.activity=activity;
        this.listener=listener;
        sql=new SQL_sentences(context);

        if (!ThreadCarga.tmpCarga.isEmpty()){
            for(NClass_carga carga:ThreadCarga.tmpCarga){
                if (carga.getNropla().equals(Holder_data.nropla) &&
                        !carga.getObservacion().equals("Completo")){
                    Feed_producto item=new Feed_producto();
                    item.setFeedcod(carga.getaCodart());
                    item.setFeedart(carga.getaDescrip());
                    item.setFeedcaja(carga.getCant());
                    item.setFeedunidad(carga.getaResto());
                    item.setFeedresto(carga.getResto());
                    item.setFeedobser(carga.getObservacion());
                    item.setFeedtotal(carga.getTotal());
                    item.setFeedbarra(carga.getaCodbarra());
                    feed.add(item);
                    notifyDataSetChanged();
                }
            }
            listener.onFinishAdapter(feed);
        }else {
            drawCarga(context);
        }
    }

    private void drawCarga(Context context){
        SQL_helper helper=new SQL_helper(context);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select aCodart, aDescrip,cant,aResto,resto,observacion,total,aCodbarra " +
                "from carga " +
                "where observacion not like 'Completo' " +
                "and nropla="+Holder_data.nropla+" "+
                "order by aProveedor,aLinea,aGenerico,aResto desc",null);
        if (cursor!=null){
            if (cursor.moveToFirst()){
                do {
                    Feed_producto item=new Feed_producto();
                    item.setFeedcod(cursor.getString(0));
                    item.setFeedart(cursor.getString(1));
                    item.setFeedcaja(cursor.getString(2));
                    item.setFeedunidad(cursor.getString(3));
                    item.setFeedresto(cursor.getString(4));
                    item.setFeedobser(cursor.getString(5));
                    item.setFeedtotal(cursor.getString(6));
                    item.setFeedbarra(cursor.getString(7));
                    feed.add(item);
                    notifyDataSetChanged();
                }while (cursor.moveToNext());
            }
            listener.onFinishAdapter(feed);
            cursor.close();
        }
    }

    @Override
    public CargaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_producto,parent,false);
        return new CargaHolder(view);
    }

    @Override
    public void onBindViewHolder(final CargaHolder holder, int position) {
        Feed_producto item=feed.get(position);
        holder.bind(item);
        holder.fab_obs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Agregar funcionalidad al micro");
            }
        });
        holder.observacion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editTextList.put(holder.getAdapterPosition(),s.toString());
                String text=s.toString();
                String darkgreen="#0DB10D";
                String darkred="#E63013";

                if (text.length()==0){
                    holder.fab_edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(darkgreen)));
                    holder.fab_edit.setImageResource(R.drawable.check);
                    holder.fab_edit.setTag("ok");
                }else {
                    holder.fab_edit.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(darkred)));
                    holder.fab_edit.setImageResource(R.drawable.close);
                    holder.fab_edit.setTag("mal");
                }
            }
        });
        holder.fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag().equals("ok")){
                    if (SystemClock.elapsedRealtime()-lastClickTime<600){
                    }lastClickTime=SystemClock.elapsedRealtime();

                    String cod=holder.cod_art.getText().toString();

                    if (!ThreadCarga.tmpCarga.isEmpty()){
                        NClass_carga carga=ThreadCarga.tmpCarga.get(holder.getAdapterPosition());
                        carga.setObservacion("Completo");
                        ThreadCarga.tmpCarga.add(carga);
                    }

                    sql.saveObservacion(Holder_data.codcamion,cod,Holder_data.nropla,"Completo");
                    int newPosition=holder.getAdapterPosition();
                    remove(newPosition);
                }else if (v.getTag().equals("mal")){
                    if (SystemClock.elapsedRealtime()-lastClickTime<600){
                    }lastClickTime=SystemClock.elapsedRealtime();

                    int oldPosition=holder.getAdapterPosition();
                    int lastPosition=feed.size()-1;
                    String obs=holder.observacion.getText().toString();
                    String cod=holder.cod_art.getText().toString();

                    NClass_carga carga=ThreadCarga.tmpCarga.get(holder.getAdapterPosition());
                    carga.setObservacion(obs);
                    ThreadCarga.tmpCarga.add(carga);

                    sql.saveObservacion(Holder_data.codcamion,cod,Holder_data.nropla,obs);
                    moveLast(oldPosition,lastPosition,obs,holder);
                }

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

    private void remove(int newPosition){
        if (!ThreadCarga.tmpCarga.isEmpty()){
            ThreadCarga.tmpCarga.remove(newPosition);
        }
        feed.remove(newPosition);
        notifyItemRemoved(newPosition);
        notifyItemRangeChanged(newPosition,feed.size());
        if (newPosition==feed.size()){
            card.setVisibility(View.VISIBLE);
        }else {
            card.setVisibility(View.GONE);
        }
    }

    private void moveLast(int oldPosition,int lastPosition,String obs,CargaHolder holder){
        if (!ThreadCarga.tmpCarga.isEmpty()){
            ThreadCarga.tmpCarga.remove(oldPosition);
        }
        Feed_producto item=feed.get(oldPosition);
        item.setFeedobser(obs);
        feed.remove(oldPosition);
        feed.add(lastPosition,item);
        notifyItemRemoved(oldPosition);
        notifyItemRangeChanged(oldPosition,feed.size());
        postExecute(holder);
    }

    private void postExecute(CargaHolder holder){
        holder.observacion.setText(null);
        holder.observacion.clearFocus();
        InputMethodManager im=(InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(holder.observacion.getWindowToken(),0);
    }

    public void setFilter(ArrayList<Feed_producto> newFeed){
        feed=new ArrayList<>();
        feed.addAll(newFeed);
        notifyDataSetChanged();
    }

    public void setPositioning(String codart){
        boolean result=false;
        int i=0;
        do {
            if (feed.get(i).getFeedart().equals(codart)){
                Feed_producto scan=feed.get(i);
                feed.remove(i);
                feed.add(0,scan);
                notifyItemMoved(i,0);
                result=true;
            }i++;
        }while (!result);
    }
}
