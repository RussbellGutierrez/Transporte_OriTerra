package com.example.russbell.transporte_oriterra.Class;

/**
 * Created by Russbell on 10/11/2017.
 */

public class NClass_documento {
    private String sucursal;
    private String esquema;
    private String codprov;
    private String tipopla;
    private String seriepla;
    private String nropla;
    private String fletero;
    private String cliente;
    private String nomcli;
    private String dircli;
    private String telefono;
    private String negocio;
    private String XCoord;
    private String YCoord;
    private String documento;
    private String nrodoc;
    private String total;
    private String estado;
    private String fecha;
    private String ruta;
    private String tipopago;

    public NClass_documento(String sucursal, String esquema, String codprov,
                            String tipopla, String seriepla, String nropla,
                            String fletero, String cliente, String nomcli,
                            String dircli, String telefono, String negocio,
                            String XCoord, String YCoord, String documento,
                            String nrodoc, String total, String estado, String fecha, String ruta, String tipopago) {
        this.sucursal = sucursal;
        this.esquema = esquema;
        this.codprov = codprov;
        this.tipopla = tipopla;
        this.seriepla = seriepla;
        this.nropla = nropla;
        this.fletero = fletero;
        this.cliente = cliente;
        this.nomcli = nomcli;
        this.dircli = dircli;
        this.telefono = telefono;
        this.negocio = negocio;
        this.XCoord = XCoord;
        this.YCoord = YCoord;
        this.documento = documento;
        this.nrodoc = nrodoc;
        this.total = total;
        this.estado = estado;
        this.fecha = fecha;
        this.ruta = ruta;
        this.tipopago = tipopago;
    }

    public String getSucursal() {
        return sucursal;
    }

    public String getEsquema() {
        return esquema;
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

    public String getCliente() {
        return cliente;
    }

    public String getNomcli() {
        return nomcli;
    }

    public String getDircli() {
        return dircli;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getNegocio() {
        return negocio;
    }

    public String getXCoord() {
        return XCoord;
    }

    public String getYCoord() {
        return YCoord;
    }

    public String getDocumento() {
        return documento;
    }

    public String getNrodoc() {
        return nrodoc;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTotal() {
        return total;
    }

    public String getFecha() {
        return fecha;
    }

    public String getRuta() {
        return ruta;
    }

    public String getTipopago(){return tipopago;}
}
