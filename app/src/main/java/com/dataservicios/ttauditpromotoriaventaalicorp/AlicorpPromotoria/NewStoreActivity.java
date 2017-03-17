package com.dataservicios.ttauditpromotoriaventaalicorp.AlicorpPromotoria;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dataservicios.ttauditpromotoriaventaalicorp.AlicorpPromotoria.CodigoVendedorActivity;
import com.dataservicios.ttauditpromotoriaventaalicorp.Model.Audit;
import com.dataservicios.ttauditpromotoriaventaalicorp.Model.PollDetail;
import com.dataservicios.ttauditpromotoriaventaalicorp.Model.Store;
import com.dataservicios.ttauditpromotoriaventaalicorp.R;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.AuditUtil;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.GPSTracker;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.GlobalConstant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewStoreActivity extends Activity {

    private Activity myActivity = this ;
    private Spinner spDistrito;
    private Spinner spDepartamento;
    private Spinner spGiro;
    private LinearLayout lyContent ;
    private EditText etOtros, etFullname, etAddress,etCodClient, etTelefono ;
    private Button btSave;

    private String strGiro ;
    int store_id;

    private  ArrayAdapter<String> adapter;
    private List<String> list;

    private ProgressDialog pDialog;

    private Store store ;
    private PollDetail pollDetail;
    private Audit mAudit;
    GPSTracker gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_store);
//        getActionBar().setDisplayHomeAsUpEnabled(true);
//        getActionBar().setTitle("Nueva Tienda");

        Spinner spinner;
        spDistrito = (Spinner) findViewById(R.id.spDistrito) ;
        spDepartamento = (Spinner) findViewById(R.id.spDepartamento) ;
        spGiro = (Spinner) findViewById(R.id.spGiro) ;
        lyContent = (LinearLayout)  findViewById(R.id.lyContent);
        etOtros= (EditText) findViewById(R.id.etOtros) ;
        etFullname = (EditText) findViewById(R.id.etFullname);
        etAddress = (EditText) findViewById(R.id.etAddress) ;
        etCodClient = (EditText) findViewById(R.id.etCodClient) ;
        etTelefono = (EditText) findViewById(R.id.etTelefono) ;
        btSave = (Button) findViewById(R.id.btSave);

        gpsTracker = new GPSTracker(myActivity);

        pDialog = new ProgressDialog(myActivity);
        pDialog.setMessage(getString(R.string.text_loading));
        pDialog.setCancelable(false);

        store = new Store();


        loadDepartamento();
        loadDistrito();
        loadGiro();


        spGiro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String label = parent.getItemAtPosition(position).toString();

                //Toast.makeText(myActivity,label,Toast.LENGTH_LONG).show();

                if(label.equals("OTROS")){
                    lyContent.setVisibility(View.VISIBLE);
                    etOtros.setText("");
                } else {
                    lyContent.setVisibility(View.INVISIBLE);
                    etOtros.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if(etFullname.getText().toString().equals("")) {
                    Toast.makeText(myActivity,R.string.text_requiere_fullname,Toast.LENGTH_LONG).show();
                    etFullname.requestFocus();
                    return;
                }

                if(etAddress.getText().toString().equals("")) {
                    Toast.makeText(myActivity,R.string.text_requiere_address,Toast.LENGTH_LONG).show();
                    etAddress.requestFocus();
                    return;
                }

                if(spGiro.getSelectedItem().toString().equals("OTROS")) {

                    if(etOtros.getText().toString().equals("")) {
                        Toast.makeText(myActivity,R.string.text_requiere_commet,Toast.LENGTH_LONG).show();
                        etOtros.requestFocus();
                        return;
                    }

                    strGiro= etOtros.getText().toString();
                } else {

                    strGiro = spGiro.getSelectedItem().toString();
                }


                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                GlobalConstant.inicio = strDate;

                if(gpsTracker.canGetLocation()){
                    GlobalConstant.latitude_open = gpsTracker.getLatitude();
                    GlobalConstant.longitude_open= gpsTracker.getLongitude();
                }else{
                    gpsTracker.showSettingsAlert();
                    return;
                }


                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                builder.setTitle(R.string.message_save);
                builder.setMessage(R.string.message_save_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        store.setFullname(etFullname.getText().toString());
                        store.setCode("Alicorp");
                        store.setAddress(etAddress.getText().toString());
                        store.setCodCliente(etCodClient.getText().toString());
                        store.setDistrict(spDistrito.getSelectedItem().toString());
                        store.setDepartamento(spDistrito.getSelectedItem().toString());
                        store.setGiro(strGiro.toString());
                        store.setPhone(etTelefono.getText().toString());
                        store.setExecutive("");


                        new loadPoll().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
                builder.setCancelable(false);



            }
        });
    }

    class loadPoll extends AsyncTask<Void , Integer , Integer> {
        /**
         * Antes de comenzar en el hilo determinado, Mostrar progresión
         * */
        boolean failure = false;
        @Override
        protected void onPreExecute() {

            pDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub

            store_id = AuditUtil.newStore(GlobalConstant.company_id, store);
            if (store_id > 0) {
                AuditUtil.saveLatLongStore(store_id,GlobalConstant.latitude_open, GlobalConstant.longitude_open);
            }
            return store_id;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Integer result) {
            // dismiss the dialog once product deleted

            if (result == 0){

                Toast.makeText(myActivity , R.string.message_no_save_data, Toast.LENGTH_LONG).show();

            } else {
                Bundle argRuta = new Bundle();
                argRuta.clear();
                argRuta.putInt("store_id", store_id);

                Intent intent;
                intent = new Intent(myActivity, CodigoVendedorActivity.class);
                intent.putExtras(argRuta);
                startActivity(intent);
                finish();
            }
            hidepDialog();
        }
    }


    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void loadDistrito() {
        list = new ArrayList<String>();
        //list.clear();
        list.add("Lima");
        list.add("Ate");
        list.add("Barranci");
        list.add("Breña");
        list.add("Bellavista");
        list.add("Callao");
        list.add("Carmen de la Legua");
        list.add("Comas");
        list.add("Chorrillos");
        list.add("Chosica");
        list.add("El Agustino");
        list.add("Jesús María");
        list.add("La Punta");
        list.add("La Perla");
        list.add("La Molina");
        list.add("La Victoria");
        list.add("Lince");
        list.add("Lurigancho");
        list.add("Los Olivos");
        list.add("Magdalena del Mar");
        list.add("Manchay");
        list.add("Miraflores");
        list.add("Pueblo Libre");
        list.add("Puente Piedra");
        list.add("Rimac");
        list.add("San Isidro");
        list.add("Independencia");
        list.add("San Juan de Miraflores");
        list.add("San Luis");
        list.add("San Martin de Porres");
        list.add("San Miguel");
        list.add("Santiago de Surco");
        list.add("Surquillo");
        list.add("Villa María del Triunfo");
        list.add("San Juan de Lurigancho");
        list.add("Santa Rosa");
        list.add("San Borja");
        list.add("Villa El Savador");
        list.add("Santa Anita");
        list.add("Ventanilla");

        //adapterDistrito = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, list);
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
        //adapter.clear();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spDistrito.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void loadDepartamento() {
        list = new ArrayList<String>();
        //list.clear();
        list.add("Lima");
        list.add("Amazonas");
        list.add("Ancash");
        list.add("Apurimac");
        list.add("Arequipa");
        list.add("Ayacucho");
        list.add("Cajamarca");
        list.add("Callao");
        list.add("Cusco");
        list.add("Huancavelica");
        list.add("Huanuco");
        list.add("Ica");
        list.add("Junin");
        list.add("La Libertad");
        list.add("Lambayeque");
        list.add("Loreto");
        list.add("Madre De Dios");
        list.add("Moquegua");
        list.add("Pasco");
        list.add("Piura");
        list.add("Puno");
        list.add("San Martin");
        list.add("Tacna");
        list.add("Tumbes");
        list.add("Ucayali");


        //adapterDistrito = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, list);
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, list);
        //adapter.clear();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //
        //adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spDepartamento.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void loadGiro() {

        list = new ArrayList<String>();
        list.clear();
        list.add("OTROS");
        list.add("LIBRERÍA");
        list.add("LOCUTORIO");
        list.add("BAR");
        list.add("RESTAURANTE");
        list.add("FARMACIA");
        list.add("CASA DE CAMBIO");
        list.add("GRIFO");
        list.add("BODEGA");
        list.add("MINI MARKET");
        list.add("PUESTO DE MERCADO");
        list.add("CENTRO DE COBRANZA");
        list.add("TIENDAS COMERCIALES");
        list.add("AGENCIA DE VIAJES");
        list.add("MAYORISTAS");
        list.add("CADENAS");
        list.add("AUTO SERVICIOS");
        list.add("MINICADENAS");
        list.add("SUBDISTRIBUIDOR");
        list.add("MERCADO");

        //adapterDistrito = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, list);
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, list);
        //adapter.clear();
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);

//        TextView spinnerText = (TextView) spGiro.getChildAt(0);
//        spinnerText.setTextColor(Color.RED);

        spGiro.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



}
