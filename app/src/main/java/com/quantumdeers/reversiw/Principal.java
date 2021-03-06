package com.quantumdeers.reversiw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //Abrimos la base dedatos
        ReversiDB reversiDB = new ReversiDB(this, "ReversiDB", null, 1);
        new NewsAsyncTask(this).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_configuracion:
                cambiarPantalla(Preferences.class);
                break;
            case R.id.menu_acerca_de:
                cambiarPantalla(Acerca_de.class);
                break;
            case R.id.menu_salir:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void principal_CambiarPantalla(View view) {
        switch (view.getId()) {
            case R.id.btn_MW_Jugar:
                cambiarPantalla(Juego.class);
                break;
            case R.id.btn_MW_Instrucciones:
                cambiarPantalla(Instrucciones.class);
                break;
            case R.id.btn_MW_Puntuaciones:
                cambiarPantalla(Puntuaciones.class);
                break;
            default:
                break;
        }

    }
    //
    private void cambiarPantalla(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
