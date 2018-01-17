package com.example.russbell.transporte_oriterra.Class;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rusbell Gutierrez on 14/08/2017.
 */

public class Class_planilla implements Parcelable{
    private String planilla;
    private String fecha;

    public Class_planilla(String planilla,String fecha){
        this.planilla=planilla;
        this.fecha=fecha;
    }

    private Class_planilla(Parcel in){
        planilla=in.readString();
        fecha=in.readString();
    }

    public String getPlanilla() {
        return planilla;
    }

    public String getFecha() {
        return fecha;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(planilla);
        out.writeString(fecha);
    }

    public static final Parcelable.Creator<Class_planilla> CREATOR = new Parcelable.Creator<Class_planilla>() {
        public Class_planilla createFromParcel(Parcel in) {
            return new Class_planilla(in);
        }

        public Class_planilla[] newArray(int size) {
            return new Class_planilla[size];
        }
    };
}
