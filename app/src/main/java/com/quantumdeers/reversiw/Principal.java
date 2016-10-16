package com.quantumdeers.reversiw;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.zip.Inflater;

public class Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int N = 5;
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout pantalla = (RelativeLayout) inflater.inflate(R.layout.activity_juego, null, false);

        LinearLayout tablero = (LinearLayout) pantalla.findViewById(R.id.contenedor_tablero);

        LinearLayout.LayoutParams configMatchParent = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        for(int i = 0; i < N; i++) {
            LinearLayout contenedor_botones = new LinearLayout(this);
            contenedor_botones.setOrientation(LinearLayout.HORIZONTAL);
            contenedor_botones.setLayoutParams(configMatchParent);
            for(int j = 0; j < N ; j++){
                Button boton = new Button(contenedor_botones.getContext());
                boton.setLayoutParams(configMatchParent);
                contenedor_botones.addView(boton);
            }
            tablero.addView(contenedor_botones);
        }
        setContentView(pantalla);
    }
}
