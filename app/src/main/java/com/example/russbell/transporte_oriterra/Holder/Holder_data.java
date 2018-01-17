package com.example.russbell.transporte_oriterra.Holder;

import android.app.Activity;

import com.example.russbell.transporte_oriterra.Interfaces.InterfaceMap;
import com.example.russbell.transporte_oriterra.Interfaces.MultipleEvents;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import java.util.ArrayList;

/**
 * Created by Rusbell Gutierrez on 15/08/2017.
 */

public class Holder_data {

    public static String basedatos;
    public static String codcamion;
    public static String contrasena;
    public static String codprov;
    public static String seriepla;
    public static String tipopla;
    public static String nropla;
    public static String codigocliente;//codigo del cliente que se guarda al seleccionar su documento
    public static String nombrecliente;
    public static double longitud;
    public static double latitud;
    public static LatLng posicion;
    public static String titulo;
    public static int refreshing;
    public static int position;
    public static ArrayList<Marker> markerArray;
    public static Activity activity;
    public static DocumentoHolder holder;
    public static MultipleEvents listener;
    public static InterfaceMap mappistener;
    public static GoogleApiClient googleApiClient;
    public static MapboxMap mapboxMap;

    private Holder_data(String bd, String codcam,
                        String contra){
        basedatos =bd;
        codcamion =codcam;
        contrasena =contra;
    }

    private Holder_data(double x, double y){
        longitud =x;
        latitud =y;
    }

    private Holder_data(String provpla,String serpla,String tippla,String nopla){
        codprov =provpla;
        seriepla =serpla;
        tipopla =tippla;
        nropla =nopla;
    }

    private Holder_data(String cli,String nom){
        codigocliente =cli;
        nombrecliente=nom;
    }

    private Holder_data(LatLng latlng,String title){
        posicion=latlng;
        titulo=title;
    }

    private Holder_data(MultipleEvents events, DocumentoHolder hold, int posit){
        holder=hold;
        listener=events;
        position= posit;
    }

    private Holder_data(ArrayList<Marker> markers){
        markerArray=markers;
    }

    private Holder_data(Activity active){
        activity=active;
    }

    private Holder_data(GoogleApiClient api){
        googleApiClient =api;
    }

    private Holder_data(int refresh){
        refreshing=refresh;
    }

    private Holder_data(MapboxMap map){
        mapboxMap=map;
    }

    private Holder_data(InterfaceMap list){
        mappistener=list;
    }

    public static Holder_data newDataCamion(String basedatos, String codcamion, String contrasena){
        return new Holder_data(basedatos, codcamion, contrasena);
    }

    public static Holder_data newDataCoord(double longitud, double latitud){
        return new Holder_data(longitud,latitud);
    }

    public static Holder_data newDataPlanilla(String provpla,String serpla,String tippla,String nopla){
        return new Holder_data(provpla,serpla,tippla,nopla);
    }

    public static Holder_data newDataCliente(String cliente,String nombre){
        return new Holder_data(cliente,nombre);
    }

    public static Holder_data newGoogleApi(GoogleApiClient googleApiClient){
        return new Holder_data(googleApiClient);
    }

    public static Holder_data newMarkClient(LatLng ltlng, String title){
        return new Holder_data(ltlng,title);
    }

    public static Holder_data newListener(MultipleEvents listener, DocumentoHolder holder, int posit){
        return new Holder_data(listener,holder,posit);
    }

    public static Holder_data newActivity(Activity active){
        return new Holder_data(active);
    }

    public static Holder_data newMarkerArray(ArrayList<Marker> markers){
        return new Holder_data(markers);
    }

    public static Holder_data newRefreshAdapter(int refresh){
        return new Holder_data(refresh);
    }

    public static Holder_data newMap(MapboxMap map){
        return new Holder_data(map);
    }

    public static Holder_data newInerfaceMap(InterfaceMap maplt){
        return new Holder_data(maplt);
    }
}
