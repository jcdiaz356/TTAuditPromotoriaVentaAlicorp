package com.dataservicios.ttauditpromotoriaventaalicorp;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;

import com.dataservicios.ttauditpromotoriaventaalicorp.AlicorpPromotoria.NewStoreActivity;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.GPSTracker;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.GlobalConstant;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by usuario on 08/01/2015.
 */
public class FragmentRutas extends Fragment {

    private static final String LOG_TAG = FragmentRutas.class.getSimpleName();
    // Movies json url

    private ProgressDialog pDialog;

    Activity myActivity = (Activity) getActivity();
    private JSONObject params;
    private SessionManager session;
    private String email_user, id_user, name_user;
    private Button btNuevo;
    private GPSTracker gpsTracker;


    public FragmentRutas(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        session = new SessionManager(getActivity());
        // get user data from session
        HashMap<String, String> user = session.getUserDetails();
        // name
        name_user = user.get(SessionManager.KEY_NAME);
        // email
        email_user = user.get(SessionManager.KEY_EMAIL);
        // id
        id_user = user.get(SessionManager.KEY_ID_USER);
        //Añadiendo parametros para pasar al Json por metodo POST

        //gpsTracker = new GPSTracker(getActivity().getApplicationContext());
        gpsTracker = new GPSTracker(getActivity());

        if(gpsTracker.canGetLocation()){

            GlobalConstant.latitude_open = gpsTracker.getLatitude();
            GlobalConstant.longitude_open= gpsTracker.getLongitude();

        }else{

            gpsTracker.showSettingsAlert();
        }

        params = new JSONObject();
        try {
            params.put("id", id_user);
            params.put("company_id", GlobalConstant.company_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        View rootView = inflater.inflate(R.layout.fragment_rutas, container, false);
        btNuevo = (Button) rootView.findViewById(R.id.btNuevo);



        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage(getString(R.string.text_loading));
        pDialog.setCancelable(false);
        //cargaRutasAndPdvs();
        //cargaPdvs();
        //cargaRutasPdvs();
        // Creando objeto Json y llenando pdvs para auditar

        btNuevo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(getActivity().getApplicationContext(), NewStoreActivity.class);
                //intent.putExtras(argRuta);
                startActivity(intent);
            }
        });


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //hidePDialog();
    }



    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }




}
