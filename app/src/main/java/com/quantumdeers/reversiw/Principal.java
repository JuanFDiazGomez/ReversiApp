package com.quantumdeers.reversiw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Principal extends AppCompatActivity {

    static int N = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creamos un Layout copia del creado en XML pero con el tablero
        RelativeLayout pantalla = creaTableroDinamico(this);

        // Mostrarmos nuestro nuevo Layout
        setContentView(pantalla);
    }

    private RelativeLayout creaTableroDinamico(Principal principal) {
        // Copiamos nuestro diseño del layout de la pantalla juego
        RelativeLayout pantalla =
                (RelativeLayout) LayoutInflater.from(principal).
                        inflate(R.layout.activity_juego, null, false);

        // Cojemos el layout que contendra el tablero de nuestra copia
        LinearLayout tablero = (LinearLayout) pantalla.findViewById(R.id.contenedor_tablero);

        // Creamos el tablero
        crearTablero(tablero);

        // Devolvemos nuestra nueva pantalla
        return pantalla;
    }

    private void crearTablero(LinearLayout tablero) {

        // Definimos la configuracion de nuestros elementos
        //LinearLayout.LayoutParams ParamsLayouts = configurarParamsLayouts();
        //LinearLayout.LayoutParams ParamsBotones = configurarParamsBotones();
        for(int i = 0; i < N; i++) {
            // Creamos la fila de botones
            LinearLayout contenedor_botones = new LinearLayout(this);
            contenedor_botones.setOrientation(LinearLayout.HORIZONTAL);
            contenedor_botones.setPadding(0,0,0,0);
            for(int j = 0; j < N ; j++){
                // Creamos el boton
                Button boton = new Button(this);
                // Intercambiamos el color a cada boton
                if((i+j) % 2 == 0) {
                    boton.setBackgroundColor(
                            getResources().getColor(R.color.cuadro_tablero_oscuro)
                    );
                }else{
                    boton.setBackgroundColor(
                            getResources().getColor(R.color.cuadro_tablero_claro)
                    );
                }
                // Configuramos el boton
                boton.setLayoutParams(configurarParamsBotones());

                // Añadimos cada boton al contenedor
                contenedor_botones.addView(boton);
            }
            // Configuramos la fila de botones
            contenedor_botones.setLayoutParams(configurarParamsLayouts());
            // Añadimos la fila de botones
            tablero.addView(contenedor_botones);
        }
    }

    private LinearLayout.LayoutParams configurarParamsBotones(){
        LinearLayout.LayoutParams ParamsBotones = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ParamsBotones.setMargins(0,0,0,0);

        // Le definimos el peso para que todos ocupen el mismo espacio
        ParamsBotones.weight=1;

        return ParamsBotones;
    }

    private LinearLayout.LayoutParams configurarParamsLayouts(){
        LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        paramsLayout.weight=1;

        return paramsLayout;
    }
}
