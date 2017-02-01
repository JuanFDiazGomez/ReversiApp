package com.quantumdeers.reversiw;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        //Siempre ponemos la dificultad inicial en facil
        this.findViewById(R.id.btn_MW_Dificultad).setTag(Integer.valueOf("0"));
        //Abrimos la base dedatos
        ReversiDB reversiDB = new ReversiDB(this,"ReversiDB",null,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }
    //TODO cambiar la funcionalidad de este menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_configuracion:
                tostadaNoConfiguracion();
                break;
            case R.id.menu_acerca_de:
                tostadaAcercaDe();
                break;
            case R.id.menu_salir:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void tostadaNoConfiguracion() {
        Toast.makeText(this, "Configuración aun no disponible", Toast.LENGTH_LONG).show();
    }
    // TODO esto hacerlo como tostada o como pantalla?
    private void tostadaAcercaDe() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_acerca_de, null);
        //layout.setBackgroundColor(Color.TRANSPARENT);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    public void principal_CambiarPantalla(View view) {
        switch(view.getId()){
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

    private void cambiarPantalla(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    public void principal_CambiarDificultad(View view) {
        Button boton = (Button) view;
        int dificultadActual = (Integer) boton.getTag();
        dificultadActual = (dificultadActual<2) ? dificultadActual + 1 : 0;
        boton.setText(
                (getResources().
                        getStringArray(R.array.btn_dificultadArray))[dificultadActual]
        );
        boton.setTag(dificultadActual);
    }
}
