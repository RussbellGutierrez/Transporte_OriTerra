package com.example.russbell.transporte_oriterra.Class;

/**
 * Created by Russbell on 10/11/2017.
 */

public class NClass_carga {
    private String codprov;
    private String seriepla;
    private String tipopla;
    private String nropla;
    private String fletero;
    private String aCodart;
    private String aDescrip;
    private String aResto;
    private String aAlmacen;
    private String aCodbarra;
    private String aCodbarrauni;
    private String aProveedor;
    private String aLinea;
    private String aGenerico;
    private String cant;
    private String resto;
    private String total;
    private String observacion;
    private String fecha;

    public NClass_carga(String codprov, String seriepla, String tipopla,
                        String nropla, String fletero, String aCodart,
                        String aDescrip, String aResto, String aAlmacen,
                        String aCodbarra, String aCodbarrauni, String aProveedor,
                        String aLinea, String aGenerico, String cant,
                        String resto, String total, String observacion, String fecha) {
        this.codprov = codprov;
        this.seriepla = seriepla;
        this.tipopla = tipopla;
        this.nropla = nropla;
        this.fletero = fletero;
        this.aCodart = aCodart;
        this.aDescrip = aDescrip;
        this.aResto = aResto;
        this.aAlmacen = aAlmacen;
        this.aCodbarra = aCodbarra;
        this.aCodbarrauni = aCodbarrauni;
        this.aProveedor = aProveedor;
        this.aLinea = aLinea;
        this.aGenerico = aGenerico;
        this.cant = cant;
        this.resto = resto;
        this.total = total;
        this.observacion = observacion;
        this.fecha = fecha;
    }

    public String getCodprov() {
        return codprov;
    }

    public String getSeriepla() {
        return seriepla;
    }

    public String getTipopla() {
        return tipopla;
    }

    public String getNropla() {
        return nropla;
    }

    public String getFletero() {
        return fletero;
    }

    public String getaCodart() {
        return aCodart;
    }

    public String getaDescrip() {
        return aDescrip;
    }

    public String getaResto() {
        return aResto;
    }

    public String getaAlmacen() {
        return aAlmacen;
    }

    public String getaCodbarra() {
        return aCodbarra;
    }

    public String getaCodbarrauni() {
        return aCodbarrauni;
    }

    public String getaProveedor() {
        return aProveedor;
    }

    public String getaLinea() {
        return aLinea;
    }

    public String getaGenerico() {
        return aGenerico;
    }

    public String getCant() {
        return cant;
    }

    public String getResto() {
        return resto;
    }

    public String getTotal() {
        return total;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFecha() {
        return fecha;
    }
}
