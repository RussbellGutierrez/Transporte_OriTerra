package com.example.russbell.transporte_oriterra.SQL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.russbell.transporte_oriterra.SQL.SQL_columns.detalle;
import static com.example.russbell.transporte_oriterra.SQL.SQL_columns.documentos;
import static com.example.russbell.transporte_oriterra.SQL.SQL_columns.carga;

/**
 * Created by Russbell on 11/04/2017.
 */

public class SQL_helper extends SQLiteOpenHelper {

    //Base de Datos
    private static final String db_name="movilBD";

    public SQL_helper(Context context){
        super(context,db_name,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //==========================CREACION DE LAS TABLAS EN SQLITE=================================
        db.execSQL("create table "+detalle.table_name+" (" +
                detalle.codprov+" text," +
                detalle.tipopla+" text," +
                detalle.seriepla+" text," +
                detalle.nropla+" text," +
                detalle.fletero+" text," +
                detalle.idcliente+" text," +
                detalle.iddocumento+" text," +
                detalle.serie+" text," +
                detalle.nrodoc+" text," +
                detalle.idlinea+" integer," +
                detalle.codart+" text," +
                detalle.cant+" integer," +
                detalle.resto+" integer," +
                detalle.unidades+" double," +
                detalle.precio+" double," +
                detalle.impuesto+" double," +
                detalle.bruto+" double," +
                detalle.descuento+" double," +
                detalle.linea+" double," +
                detalle.fecha+" text)");

        db.execSQL("create table "+documentos.table_name+" (" +
                documentos.sucursal+" text," +
                documentos.esquema+" text," +
                documentos.codprov+" text," +
                documentos.tipopla+" text," +
                documentos.seriepla+" text," +
                documentos.nropla+" text," +
                documentos.fletero+" text," +
                documentos.cliente+" text," +
                documentos.nomcli+" text," +
                documentos.dircli+" text," +
                documentos.telefono+" text," +
                documentos.negocio+" text," +
                documentos.XCoord+" double," +
                documentos.YCoord+" double," +
                documentos.documento+" text," +
                documentos.nrodoc+" text," +
                documentos.total+" text," +
                documentos.estado+" text," +
                documentos.fecha+" text," +
                documentos.ruta+" text," +
                documentos.tipopago+" text)");

        db.execSQL("create table "+carga.table_name+" (" +
                carga.codprov+" text," +
                carga.seriepla+" text," +
                carga.tipopla+" text," +
                carga.nropla+" text," +
                carga.fletero+" text," +
                carga.aCodart+" text," +
                carga.aDescrip+" text," +
                carga.aResto+" integer," +
                carga.aAlmacen+" text," +
                carga.aCodbarra+" text," +
                carga.aCodbarrauni+" text," +
                carga.aProveedor+" text," +
                carga.aLinea+" integer," +
                carga.aGenerico+" integer," +
                carga.cant+" integer," +
                carga.resto+" integer," +
                carga.total+" text," +
                carga.observacion+" text," +
                carga.fecha+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*db.execSQL("drop table if exists "+Persona.TABLE_NAME);
        db.execSQL("drop table if exists "+Cliente.TABLE_NAME);
        db.execSQL("drop table if exists "+Camion.TABLE_NAME);
        db.execSQL("drop table if exists "+Articulo.TABLE_NAME);
        db.execSQL("drop table if exists "+Documento.TABLE_NAME);
        db.execSQL("drop table if exists "+Carga.TABLE_NAME);
        db.execSQL("drop table if exists "+notaCredito.TABLE_NAME);
        db.execSQL("drop table if exists "+Detalle.TABLE_NAME);
        db.execSQL("drop table if exists "+productoCredito.TABLE_NAME);*/

        //==================DROP DE NUEVAS TABLAS====================
        db.execSQL("drop table if exists "+detalle.table_name);
        db.execSQL("drop table if exists "+documentos.table_name);
        db.execSQL("drop table if exists "+carga.table_name);
    }
}
