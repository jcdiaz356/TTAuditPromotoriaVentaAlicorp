package com.dataservicios.ttauditpromotoriaventaalicorp.Repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dataservicios.ttauditpromotoriaventaalicorp.Model.Department;
import com.dataservicios.ttauditpromotoriaventaalicorp.Model.User;
import com.dataservicios.ttauditpromotoriaventaalicorp.SQLite.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class DepartmentRepo extends DatabaseHelper {

    private static final String LOG = DepartmentRepo.class.getSimpleName();

    public DepartmentRepo(Context context) {
        super(context);
    }


    /**
     * Get all Department
     * @param id
     * @return
     */
    public Department getDepartament(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_DEPARTMENT + " WHERE " + KEY_ID + " = " + id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) c.moveToFirst();
        Department pd = new Department();
        pd.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));
        return pd;
    }



    /**
     * getting all User
     * */
    public List<Department> getAllDepartment() {
        List<Department> departments = new ArrayList<Department>();
        String selectQuery = "SELECT  * FROM " + TABLE_DEPARTMENT;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Department pd = new Department();
                pd.setId(c.getInt((c.getColumnIndex(KEY_ID))));
                pd.setName((c.getString(c.getColumnIndex(KEY_NAME))));

                // adding to todo list
                departments.add(pd);
            } while (c.moveToNext());
        }
        return departments;
    }

}