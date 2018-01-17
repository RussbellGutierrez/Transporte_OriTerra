package com.example.russbell.transporte_oriterra.Threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Class.NClass_detalle;
import com.example.russbell.transporte_oriterra.Interfaces.NotifyingThreadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Russbell on 14/11/2017.
 */

public class ThreadDetalle extends AsyncTask<Boolean,String,Boolean> {

    private Context context;
    private ProgressDialog pd;
    private JSONObject response;
    private NotifyingThreadListener listener;
    private static final String TAG="ThreadDetalle";
    public static final ArrayList<NClass_detalle> tmpDetalle=new ArrayList<>();

    public ThreadDetalle(JSONObject response, NotifyingThreadListener listener, Context context){
        this.response=response;
        this.listener=listener;
        this.context=context;
        pd = new ProgressDialog(context);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd.setMessage("Download detalle...");
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Boolean... booleans) {
        boolean resultado;
        tmpDetalle.clear();
        try{
            JSONArray jadetalle=new JSONArray(response.getString("data"));

            for(int i=0;i<jadetalle.length();i++){
                JSONObject jdet = jadetalle.getJSONObject(i);
                NClass_detalle detalle=new NClass_detalle(jdet.getString("codprov"),jdet.getString("tipopla"),jdet.getString("seriepla"),
                        jdet.getString("nropla"),jdet.getString("fletero"),jdet.getString("idcliente"),jdet.getString("iddocumento"),
                        jdet.getString("serie"),jdet.getString("nrodoc"),jdet.getString("idlinea"),jdet.getString("codart"),
                        jdet.getString("cant"),jdet.getString("resto"),jdet.getString("unidades"),jdet.getString("precio"),
                        jdet.getString("impuesto"),jdet.getString("bruto"),jdet.getString("descuento"),jdet.getString("linea"),jdet.getString("fecha"));
                tmpDetalle.add(detalle);
                //Log.wtf(TAG,"Iteracion "+i);
            }
            resultado=true;
        }catch (JSONException e) {
            e.printStackTrace();
            resultado=false;
        }
        return resultado;
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (aBoolean){
            pd.dismiss();
            listener.AsyncCompleteListener(1);
        }else {
            pd.dismiss();
            Toast.makeText(context,"Error al guardar detalles de documentos",Toast.LENGTH_SHORT).show();
        }
    }
}
