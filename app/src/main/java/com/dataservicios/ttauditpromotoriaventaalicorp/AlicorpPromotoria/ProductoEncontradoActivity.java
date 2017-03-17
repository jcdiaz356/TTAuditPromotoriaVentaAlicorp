package com.dataservicios.ttauditpromotoriaventaalicorp.AlicorpPromotoria;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dataservicios.ttauditpromotoriaventaalicorp.Model.PollDetail;
import com.dataservicios.ttauditpromotoriaventaalicorp.R;
import com.dataservicios.ttauditpromotoriaventaalicorp.SQLite.DatabaseHelper;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.AuditUtil;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.GlobalConstant;
import com.dataservicios.ttauditpromotoriaventaalicorp.util.SessionManager;

import java.util.HashMap;

public class ProductoEncontradoActivity extends Activity {

    private Activity myActivity = this ;
    private static final String LOG_TAG = ProductoEncontradoActivity.class.getSimpleName();
    private SessionManager session;
    private Button bt_guardar;
    private LinearLayout lyContent;

    private String tipo,cadenaruc, fechaRuta, comentario="", type, region;

    private Integer user_id, company_id,store_id,rout_id,audit_id, poll_id;

    private DatabaseHelper db;

    private ProgressDialog pDialog;

    // private RadioGroup rgOpt1;
    private String opt1="";

    //private RadioButton[] radioButton1Array;
    private CheckBox[] checkBoxArray;

    private PollDetail pollDetail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_encontrado);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setTitle("Tienda");


        lyContent = (LinearLayout) findViewById(R.id.lyContent);

        bt_guardar = (Button) findViewById(R.id.btGuardar);


        Bundle bundle = getIntent().getExtras();
        company_id = GlobalConstant.company_id;
        store_id = bundle.getInt("store_id");

        audit_id = GlobalConstant.audit_id[0];
        //product_id =bundle.getInt("product_id");

        //poll_id = 562;
        poll_id = GlobalConstant.poll_id[5];


        pDialog = new ProgressDialog(myActivity);
        pDialog.setMessage(getString(R.string.text_loading));
        pDialog.setCancelable(false);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        // id
        user_id = Integer.valueOf(user.get(SessionManager.KEY_ID_USER)) ;

        createChecOptions();

        bt_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                opt1 = "" ;
                int contador = 0;
                for (int x = 0; x < checkBoxArray.length; x++) {
                    if(checkBoxArray[x].isChecked()) contador ++;
                }

//                if (contador == 0){
//                    Toast.makeText(myActivity, R.string.message_select_options, Toast.LENGTH_LONG).show();
//                    return;
//                } else{
                    for (int x = 0; x < checkBoxArray.length; x++) {
                        if(checkBoxArray[x].isChecked())  {
                            opt1 = opt1 + poll_id.toString() + checkBoxArray[x].getTag() + "|";

                        }
                    }

               // }


                AlertDialog.Builder builder = new AlertDialog.Builder(myActivity);
                builder.setTitle(R.string.save);
                builder.setMessage(R.string.message_save_information);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        pollDetail = new PollDetail();
                        pollDetail.setPoll_id(poll_id);
                        pollDetail.setStore_id(store_id);
                        pollDetail.setSino(0);
                        pollDetail.setOptions(1);
                        pollDetail.setLimits(0);
                        pollDetail.setMedia(0);
                        pollDetail.setComment(0);
                        pollDetail.setResult(0);
                        pollDetail.setLimite(0);
                        pollDetail.setComentario("");
                        pollDetail.setAuditor(user_id);
                        pollDetail.setProduct_id(0);
                        pollDetail.setPublicity_id(0);
                        pollDetail.setCompany_id(GlobalConstant.company_id);
                        pollDetail.setCategory_product_id(0);
                        pollDetail.setCommentOptions(0);
                        pollDetail.setSelectdOptions(opt1);
                        pollDetail.setSelectedOtionsComment("");
                        pollDetail.setPriority("0");

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



    class loadPoll extends AsyncTask<Void, Integer , Boolean> {
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
        protected Boolean doInBackground(Void... params) {
            // TODO Auto-generated method stub

            if(!AuditUtil.insertPollDetail(pollDetail)) return false;


            return true;
        }
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(Boolean result) {
            // dismiss the dialog once product deleted

            if (result){

                Bundle argPDV = new Bundle();
                argPDV.putInt("store_id", Integer.valueOf(store_id));
                Intent intent = new Intent(myActivity, ClienteGondolaActivity.class);
                intent.putExtras(argPDV);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(myActivity , R.string.message_select_options, Toast.LENGTH_LONG).show();
            }
            hidepDialog();
        }
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
//                this.finish();
//                Intent a = new Intent(this,PanelAdmin.class);
//                //a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(a);
//                overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
        //return super.onOptionsItemSelected(item);
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            //Toast.makeText(myActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar pongase en contácto con el administrador", Toast.LENGTH_LONG).show();
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(myActivity, "No se puede volver atras, los datos ya fueron guardado, para modificar póngase en contácto con el administrador", Toast.LENGTH_LONG).show();
//        super.onBackPressed();
//        this.finish();
//
//        overridePendingTransition(R.anim.anim_slide_in_right,R.anim.anim_slide_out_right);
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void createChecOptions(){

        checkBoxArray = new CheckBox[12];

        checkBoxArray[0] = new CheckBox(this);
        checkBoxArray[0].setText("Ariel (cualquier presentación)");
        checkBoxArray[0].setTag("a");
        lyContent.addView(checkBoxArray[0]);

        checkBoxArray[1] = new CheckBox(this);
        checkBoxArray[1].setText("Bolivar (cualquier presentación)");
        checkBoxArray[1].setTag("b");
        lyContent.addView(checkBoxArray[1]);

        checkBoxArray[2] = new CheckBox(this);
        checkBoxArray[2].setText("Ace (cualquier presentación)");
        checkBoxArray[2].setTag("c");
        lyContent.addView(checkBoxArray[2]);

        checkBoxArray[3] = new CheckBox(this);
        checkBoxArray[3].setText("Opal (cualquier presentación)");
        checkBoxArray[3].setTag("d");
        lyContent.addView(checkBoxArray[3]);

        checkBoxArray[4] = new CheckBox(this);
        checkBoxArray[4].setText("Sapalio (cualquier presentación)");
        checkBoxArray[4].setTag("e");
        lyContent.addView(checkBoxArray[4]);

        checkBoxArray[5] = new CheckBox(this);
        checkBoxArray[5].setText("Marsella (cualquier presentación)");
        checkBoxArray[5].setTag("f");
        lyContent.addView(checkBoxArray[5]);


        checkBoxArray[6] = new CheckBox(this);
        checkBoxArray[6].setText("Suavizante Bolivar (sachets)");
        checkBoxArray[6].setTag("g");
        lyContent.addView(checkBoxArray[6]);

        checkBoxArray[7] = new CheckBox(this);
        checkBoxArray[7].setText("Suavizante Bolivar (frascos)");
        checkBoxArray[7].setTag("h");
        lyContent.addView(checkBoxArray[7]);


        checkBoxArray[8] = new CheckBox(this);
        checkBoxArray[8].setText("Downy (sachets)");
        checkBoxArray[8].setTag("i");
        lyContent.addView(checkBoxArray[8]);

        checkBoxArray[9] = new CheckBox(this);
        checkBoxArray[9].setText("Downy (frascos)");
        checkBoxArray[9].setTag("j");
        lyContent.addView(checkBoxArray[9]);

        checkBoxArray[10] = new CheckBox(this);
        checkBoxArray[10].setText("Suavitel (sachets)");
        checkBoxArray[10].setTag("k");
        lyContent.addView(checkBoxArray[10]);

        checkBoxArray[11] = new CheckBox(this);
        checkBoxArray[11].setText("Suavitel (frascos)");
        checkBoxArray[11].setTag("l");
        lyContent.addView(checkBoxArray[11]);
    }



}

