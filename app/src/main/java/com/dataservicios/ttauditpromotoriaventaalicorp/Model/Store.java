package com.dataservicios.ttauditpromotoriaventaalicorp.Model;



/**
 * Created by usuario on 10/01/2015.
 */
public class Store {
    private int id;
    private String fullname;
    private String code ;
    private String codCliente;
    private String address ;
    private String district;
    private String departamento;
    private String phone ;
    private String executive;
    private String giro ;
//    $company_id = $valoresPost['company_id'];
//    $fullname = $valoresPost['fullname'];
//    $distributor = $valoresPost['code'];
//    $telephone = $valoresPost['phone'];
//    $district = $valoresPost['distrito'];
//    $ubigeo = $valoresPost['departamento'];
//    $address = $valoresPost['address'];
//    $ejecutivo = $valoresPost['ejecutivo'];
//        if ($distributor=="Alicorp"){
//        $codclient = $valoresPost['codclient'];
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getExecutive() {
        return executive;
    }

    public void setExecutive(String executive) {
        this.executive = executive;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGiro() {
        return giro;
    }

    public void setGiro(String giro) {
        this.giro = giro;
    }
}
