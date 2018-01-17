package com.example.russbell.transporte_oriterra.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.russbell.transporte_oriterra.Activity.Activity_cuerpo;
import com.example.russbell.transporte_oriterra.Class.Class_planilla;
import com.example.russbell.transporte_oriterra.Class.NClass_carga;
import com.example.russbell.transporte_oriterra.Holder.ClickHolder;
import com.example.russbell.transporte_oriterra.Holder.Holder_data;
import com.example.russbell.transporte_oriterra.Interfaces.ViewClickListener;
import com.example.russbell.transporte_oriterra.R;
import com.example.russbell.transporte_oriterra.SQL.SQL_helper;
import com.example.russbell.transporte_oriterra.Threads.ThreadCarga;
import com.example.russbell.transporte_oriterra.Threads.ThreadUnique;

import java.util.ArrayList;

/**
 * Created by Rusbell Gutierrez on 14/08/2017.
 */

public class Adapter_planilla extends RecyclerView.Adapter<ClickHolder>{

    private static String TAG="Debug_adapplan";
    private Activity activity;
    private ArrayList<Class_planilla> feedplan= new ArrayList<>();

    public Adapter_planilla(Activity activity){
        this.activity=activity;
        if (!ThreadCarga.tmpPlanilla.isEmpty()){
            for(Class_planilla item:ThreadCarga.tmpPlanilla){
                Class_planilla planilla=new Class_planilla(item.getPlanilla(),item.getFecha());
                feedplan.add(planilla);
                notifyDataSetChanged();
            }
        }else {
            drawPlanilla();
        }
    }

    private void drawPlanilla(){
        SQL_helper helper=new SQL_helper(activity);
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select codprov, seriepla, tipopla, nropla, fecha from carga group by nropla",null);
        if (cursor!=null && cursor.getCount()!=0){
            //Log.wtf(TAG,"In cursor count is "+cursor.getCount());
            if (cursor.moveToFirst()){
                do{
                    //Log.wtf(TAG,"In cursor nropla is "+cursor.getString(0)+" and fecha is "+cursor.getString(1));
                    String nropla=cursor.getString(0)+"-"+cursor.getString(1)+"-"+cursor.getString(2)+"-"+cursor.getString(3);
                    Class_planilla planilla=new Class_planilla(nropla,
                            cursor.getString(4));
                    feedplan.add(planilla);
                    notifyDataSetChanged();
                }while (cursor.moveToNext());
            }
            cursor.close();
        }else {
            Log.d(TAG,"Data base is null!!");
        }
    }

    @Override
    public ClickHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_planilla,parent,false);
        return new ClickHolder(view);
    }

    @Override
    public void onBindViewHolder(ClickHolder holder, int position) {
        final Class_planilla item=feedplan.get(position);
        holder.bind(item);
        holder.setInterface(new ViewClickListener() {
            @Override
            public void OnClickListenerView(View view, int position) {
                //int planilla=Integer.valueOf(item.getPlanilla());
                String[] arg=item.getPlanilla().split("-");
                Holder_data.newDataPlanilla(arg[0],arg[1],arg[2],arg[3]);
                Intent in=new Intent(activity,Activity_cuerpo.class);
                activity.startActivity(in);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedplan.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }
}
