package com.example.russbell.transporte_oriterra.Threads;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.russbell.transporte_oriterra.Interfaces.MapListener;
import com.example.russbell.transporte_oriterra.Volley.Peticiones;
import com.google.android.gms.maps.GoogleMap;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

/**
 * Created by Russbell on 9/11/2017.
 */

public class ThreadGetRoute extends AsyncTask<String,String,String> {
    private String URL;
    @SuppressLint("StaticFieldLeak")
    private Context context;
    private GoogleMap map;
    private MapboxMap mapoff;
    private Polyline polilinea;
    private MapListener mListener;
    private PolylineOptions polylineOptions;
    private LatLng destino;

    public ThreadGetRoute(LatLng destino,Context context
            , String URL, GoogleMap map, PolylineOptions polylineOptions
            , Polyline polilinea, MapListener mListener){
        this.context=context;
        this.URL=URL;
        this.map=map;
        this.polilinea=polilinea;
        this.mListener=mListener;
        this.polylineOptions=polylineOptions;
        this.destino=destino;
    }

    public ThreadGetRoute(LatLng destino, Context context
            , String URL, MapboxMap mapoff, PolylineOptions polylineOptions
            , Polyline polilinea, MapListener mListener){
        this.context=context;
        this.URL=URL;
        this.mapoff=mapoff;
        this.polilinea=polilinea;
        this.mListener=mListener;
        this.polylineOptions=polylineOptions;
        this.destino=destino;
    }

    private Peticiones pet=new Peticiones();
    private static String TAG="ThreadRoute";
    private ProgressDialog mProgressDialog;

    @Override
    protected void onPreExecute(){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Cargando rutas...");
        mProgressDialog.show();

        if (polilinea!=null){
            polilinea.remove();
        }
    }

    @Override
    protected String doInBackground(String... url) {
        String data = "";
        try {
            data=pet.connectionPath(context,URL);

        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        if (data.equals("")){
            data="";
        }else {
            Log.wtf(TAG,"Data: "+data);
        }
        return data;
        //}
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        mProgressDialog.dismiss();
        Log.wtf(TAG,"Result: "+result);
        if (!result.equals("")){
            //parsepath.execute(result);
            new ThreadParser(mapoff,polylineOptions,polilinea,mListener).execute(result);
            mListener.onTrace(destino);
        }else {
            Log.d(TAG, "Sin rutas");
        }
    }
}
