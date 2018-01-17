package com.example.russbell.transporte_oriterra.Threads;

import android.graphics.Color;
import android.os.AsyncTask;

import com.example.russbell.transporte_oriterra.Class.Path_route;
import com.example.russbell.transporte_oriterra.Interfaces.MapListener;
import com.google.android.gms.maps.GoogleMap;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.annotations.Polyline;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.maps.MapboxMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Russbell on 9/11/2017.
 */

public class ThreadParser extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
    private GoogleMap map;
    private MapboxMap mapoff;
    private List<List<HashMap<String, String>>> routes= Collections.emptyList();
    private Polyline polilinea;
    private MapListener mListener;
    private PolylineOptions polyOpt;

    public ThreadParser(GoogleMap map,PolylineOptions polyOpt,Polyline polilinea,MapListener mListener){
        this.map=map;
        this.polilinea=polilinea;
        this.mListener=mListener;
        this.polyOpt=polyOpt;
    }

    public ThreadParser(MapboxMap mapoff,PolylineOptions polyOpt, Polyline polilinea, MapListener mListener){
        this.mapoff=mapoff;
        this.polilinea=polilinea;
        this.mListener=mListener;
        this.polyOpt=polyOpt;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(
            String... jsonData) {

        if (routes!=null){
            routes.clear();
        }

        JSONObject jObject;

        try {
            jObject = new JSONObject(jsonData[0]);
            Path_route parser = new Path_route();
            routes = parser.parse(jObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> routes) {
        ArrayList<LatLng> points = null;

        if (polilinea!=null){
            polilinea.remove();
        }
        // traversing through routes
        for (int i = 0; i < routes.size(); i++) {
            points = new ArrayList<LatLng>();
            //polylineOptions = new PolylineOptions();
            List<HashMap<String, String>> path = routes.get(i);

            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            polyOpt.addAll(points);
            polyOpt.width(2);
            polyOpt.color(Color.BLUE);
        }
        polilinea = mapoff.addPolyline(polyOpt);
        mListener.onSetPolyline(polilinea);
    }
}
