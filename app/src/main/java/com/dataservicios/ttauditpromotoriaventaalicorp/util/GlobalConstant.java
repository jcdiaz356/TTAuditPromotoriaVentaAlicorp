package com.dataservicios.ttauditpromotoriaventaalicorp.util;
/**
 * Created by usuario on 11/11/2014.
 */
public final class GlobalConstant {
    public static String dominio = "http://ttaudit.com";
    //public static String dominio = "http://appfiliaibk.com";
    public static final String LOGIN_URL = dominio + "/loginUser";
    public static final String KEY_USERNAME = "username";
    public static String inicio,fin;
    public static  double latitude_open, longitude_open;
    public static  int global_close_audit =0;
    public static int company_id = 63;

    public static int[] poll_id = new int[]{
            879, //	0	Se encuentra Abierto el punto?
            880, //	1	Codigo Vendedor
            882, //	2	Codigo Cliente
            883, //	3	Cliente es "CLIENTE PERFECTO"
            884, //	4	Cliente es "CLIENTE PERFECTO" DE LAVANDERIA
            885, //	5	Productos encontrados
            901, //	6	Cliente permitio trabajar gondola
            961, //	7	Cliente Compró por categoría

    };


    public static int[] audit_id = new int[]{
            51, // 0 Promotorias Alicorp"
    } ;

   // public static String albunName = "AlicorpPhoto";
    //public static String directory_images = "/Pictures/" + albunName;
    public static String directory_images = "/Pictures/" ;
    public static String type_aplication = "android";

    public static final String JPEG_FILE_PREFIX = "_alicorpPromo_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";
}

