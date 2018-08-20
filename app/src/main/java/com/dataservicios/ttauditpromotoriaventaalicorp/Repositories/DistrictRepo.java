package com.dataservicios.ttauditpromotoriaventaalicorp.Repositories;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dataservicios.ttauditpromotoriaventaalicorp.Model.Department;
import com.dataservicios.ttauditpromotoriaventaalicorp.Model.District;
import com.dataservicios.ttauditpromotoriaventaalicorp.SQLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DistrictRepo  extends DatabaseHelper {

    private static final String LOG = DistrictRepo.class.getSimpleName();

    public DistrictRepo(Context context) {
        super(context);
    }



    public List<District> getDistrictForDepartment(long department_id) {
        List<District> districts = new ArrayList<District>();
        String selectQuery = "SELECT  * FROM " + TABLE_DISTRICT+ " WHERE " + KEY_DEPARTMENT_ID + " = " + department_id;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                District pd = new District();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setDepartament_id(c.getInt((c.getColumnIndex(KEY_DEPARTMENT_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));

                // adding to todo list
                districts.add(pd);
            } while (c.moveToNext());
        }
        return districts;
    }





}