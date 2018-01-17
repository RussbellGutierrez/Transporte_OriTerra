package com.example.russbell.transporte_oriterra.Interfaces;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.annotations.Polyline;

import java.util.ArrayList;

/**
 * Created by Russbell on 9/11/2017.
 */

public interface MapListener {
    void onSetPolyline(Polyline polyline);
    void onTrace(LatLng destino);
}
