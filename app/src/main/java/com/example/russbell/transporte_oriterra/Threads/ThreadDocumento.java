package com.example.russbell.transporte_oriterra.Threads;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.russbell.transporte_oriterra.Class.NClass_documento;
import com.example.russbell.transporte_oriterra.Interfaces.NotifyingThreadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Russbell on 14/11/2017.
 */

public class ThreadDocumento extends AsyncTask<Boolean,String,Boolean> {

    private Context context;
    private ProgressDialog pd;
    private JSONObject response;
    private NotifyingThreadListener listener;
    private static final String TAG="ThreadDocumento";
    public static final ArrayList<NClass_documento> tmpDocumento=new ArrayList<>();

    public ThreadDocumento(JSONObject response, NotifyingThreadListener listener, Context context){
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
        pd.setMessage("Download documento...");
        pd.show();
    }

    @Override
    protected Boolean doInBackground(Boolean... booleans) {
        boolean resultado;
        tmpDocumento.clear();
        try{
            JSONArray jadocumento=new JSONArray(response.getString("data"));

            for(int i=0;i<jadocumento.length();i++){
                JSONObject jdoc = jadocumento.getJSONObject(i);
                NClass_documento documento=new NClass_documento(jdoc.getString("sucursal"),jdoc.getString("esquema"),
                        jdoc.getString("codprov"),jdoc.getString("tipopla"),jdoc.getString("seriepla"),jdoc.getString("nropla"),
                        jdoc.getString("fletero"),jdoc.getString("cliente"),jdoc.getString("nomcli"),jdoc.getString("domicli"),
                        jdoc.getString("telefono"),jdoc.getString("negocio"),jdoc.getString("XCoord"),jdoc.getString("YCoord"),
                        jdoc.getString("documento"),jdoc.getString("nrodoc"),jdoc.getString("total"),jdoc.getString("estado"),jdoc.getString("fecha"),
                        jdoc.getString("ruta"),jdoc.getString("tipopago"));
                tmpDocumento.add(documento);
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
            Toast.makeText(context,"Error al guardar datos de los documentos",Toast.LENGTH_SHORT).show();
        }
    }
}
