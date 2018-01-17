package com.example.russbell.transporte_oriterra.Volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Rusbell Gutierrez on 11/08/2017.
 */

public final class DatosSingleton {
    private static DatosSingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private DatosSingleton(Context context){
        DatosSingleton.context=context;
        requestQueue= getRequestQueue();
    }

    public static synchronized DatosSingleton getInstance(Context context){
        if(singleton==null){
            singleton=new DatosSingleton(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue(){
        if (requestQueue==null){
            requestQueue= Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public void addtoRequestQueue(Request request){
        getRequestQueue().add(request);
    }
}
