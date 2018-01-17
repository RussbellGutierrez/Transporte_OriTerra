package com.example.russbell.transporte_oriterra.Class;

/**
 * Created by Russbell on 10/11/2017.
 */

public class NClass_detalle {
    private String codprov;
    private String tipopla;
    private String seriepla;
    private String nropla;
    private String fletero;
    private String idcliente;
    private String iddocumento;
    private String serie;
    private String nrodoc;
    private String idlinea;
    private String codart;
    private String cant;
    private String resto;
    private String unidades;
    private String precio;
    private String impuesto;
    private String bruto;
    private String descuento;
    private String linea;
    private String fecha;

    public NClass_detalle(String codprov, String tipopla, String seriepla,
                          String nropla, String fletero, String idcliente,
                          String iddocumento, String serie, String nrodoc,
                          String idlinea, String codart, String cant, String resto,
                          String unidades, String precio, String impuesto, String bruto,
                          String descuento, String linea, String fecha) {
        this.codprov = codprov;
        this.tipopla = tipopla;
        this.seriepla = seriepla;
        this.nropla = nropla;
        this.fletero = fletero;
        this.idcliente = idcliente;
        this.iddocumento = iddocumento;
        this.serie = serie;
        this.nrodoc = nrodoc;
        this.idlinea = idlinea;
        this.codart = codart;
        this.cant = cant;
        this.resto = resto;
        this.unidades = unidades;
        this.precio = precio;
        this.impuesto = impuesto;
        this.bruto = bruto;
        this.descuento = descuento;
        this.linea = linea;
        this.fecha = fecha;
    }

    public String getCodprov() {
        return codprov;
    }

    public String getTipopla() {
        return tipopla;
    }

    public String getSeriepla() {
        return seriepla;
    }

    public String getNropla() {
        return nropla;
    }

    public String getFletero() {
        return fletero;
    }

    public String getIdcliente() {
        return idcliente;
    }

    public String getIddocumento() {
        return iddocumento;
    }

    public String getSerie() {
        return serie;
    }

    public String getNrodoc() {
        return nrodoc;
    }

    public String getIdlinea() {
        return idlinea;
    }

    public String getCodart() {
        return codart;
    }

    public String getCant() {
        return cant;
    }

    public String getResto() {
        return resto;
    }

    public String getUnidades() {
        return unidades;
    }

    public String getPrecio() {
        return precio;
    }

    public String getImpuesto() {
        return impuesto;
    }

    public String getBruto() {
        return bruto;
    }

    public String getDescuento() {
        return descuento;
    }

    public String getLinea() {
        return linea;
    }

    public String getFecha() {
        return fecha;
    }
}
