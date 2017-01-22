package com.quantumdeers.reversiw;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Toast;

public class Juego extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameBoardCreator gameBoardCreator = new GameBoardCreator(this);
        setContentView(gameBoardCreator.getPantalla());
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

    private void tostadaAcercaDe() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.activity_acerca_de, (ViewGroup) findViewById(R.id.activity_principal));
        layout.setBackgroundColor(Color.TRANSPARENT);
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}
