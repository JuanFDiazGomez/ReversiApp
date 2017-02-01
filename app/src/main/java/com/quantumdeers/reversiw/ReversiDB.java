package com.quantumdeers.reversiw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by juanfdg on 27/01/17.
 */

public class ReversiDB extends SQLiteOpenHelper {
    String sqlCreate = "CREATE TABLE scores(" +
            "nombre TEXT," +
            "puntuacion NUM," +
            "total_casillas NUM," +
            "fecha DATE" +
            ")";
    public ReversiDB(Context contexto, String nombre, SQLiteDatabase.CursorFactory factory, int version){
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO mejorar la actualizacion de la tabla, portar los datos
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS scores");
        sqLiteDatabase.execSQL(sqlCreate);
    }
}
