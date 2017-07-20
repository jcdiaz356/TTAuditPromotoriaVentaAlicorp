package com.dataservicios.ttauditpromotoriaventaalicorp.Model;

/**
 * Created by jcdia on 23/03/2017.
 */

public class District {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDepartament_id() {
        return departament_id;
    }

    public void setDepartament_id(int departament_id) {
        this.departament_id = departament_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int departament_id ;
    private String name;


}
