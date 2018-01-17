package com.example.russbell.transporte_oriterra.Class;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Russbell on 17/08/2017.
 */

public class Feed_detacli implements Parcelable {

    private String detdocumento;
    private String detimporte;
    private String detnegocio;

    public Feed_detacli(String detdocumento, String detimporte, String detnegocio) {
        this.detdocumento = detdocumento;
        this.detimporte = detimporte;
        this.detnegocio = detnegocio;
    }

    protected Feed_detacli(Parcel in){
        this.detdocumento=in.readString();
        this.detimporte=in.readString();
        this.detnegocio=in.readString();
    }

    public String getDetdocumento() {
        return detdocumento;
    }

    public String getDetimporte() {
        return detimporte;
    }

    public String getDetnegocio() {
        return detnegocio;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(detdocumento);
        out.writeString(detimporte);
        out.writeString(detnegocio);
    }

    public static final Parcelable.Creator<Feed_detacli> CREATOR = new Parcelable.Creator<Feed_detacli>() {
        @Override
        public Feed_detacli createFromParcel(Parcel source) {
            return new Feed_detacli(source);
        }

        @Override
        public Feed_detacli[] newArray(int size) {
            return new Feed_detacli[size];
        }
    };
}
