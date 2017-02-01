package com.quantumdeers.reversiw;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.Locale;

public class Puntuaciones extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntuaciones);
        mostrarPuntuaciones();
    }

    private void mostrarPuntuaciones() {
        ReversiDB reversiDB = new ReversiDB(getApplicationContext(),"ReversiDB",null,1);
        SQLiteDatabase db = reversiDB.getReadableDatabase();
        if(db != null){
            String sql = "SELECT nick AS _id,nick,score,boardSize, strftime('%d-%m-%Y', date) AS date FROM scores";
            Cursor cursor = db.rawQuery(sql,null);
            String[] campos = {"nick","score","boardSize","date"};
            int[] ids = {R.id.adapterNick,R.id.adapterPoints,R.id.adapterTotal,R.id.adapterDate};
            SimpleCursorAdapter mAdapter =
                    new SimpleCursorAdapter(
                            this,R.layout.adapter_scores, cursor, campos, ids
                    );
            ListView listaPuntuaciones = (ListView) findViewById(R.id.listPuntuaciones);
            listaPuntuaciones.setAdapter(mAdapter);
            db.close();
        }

    }
}
