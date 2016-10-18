package com.quantumdeers.reversiw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class Principal extends AppCompatActivity {

    static int N = 5;
    Button boton[][];
    LinearLayout contenedor_botones[];

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
        boton = new Button[N][N];
        contenedor_botones = new LinearLayout[N];
        for(int fila = 0; fila < N; fila++) {
            // Creamos la fila de botones
            contenedor_botones[fila] = new LinearLayout(this);
            contenedor_botones[fila].setOrientation(LinearLayout.HORIZONTAL);
            contenedor_botones[fila].setPadding(0,0,0,0);
            for(int columna = 0; columna < N ; columna++){
                // Creamos el boton
                boton[fila][columna] = new Button(this);
                // Intercambiamos el color a cada boton
                if((fila+columna) % 2 == 0) {
                    boton[fila][columna].setBackgroundColor(
                            getResources().getColor(R.color.cuadro_tablero_oscuro)
                    );
                }else{
                    boton[fila][columna].setBackgroundColor(
                            getResources().getColor(R.color.cuadro_tablero_claro)
                    );
                }
                // Configuramos el boton
                boton[fila][columna].setLayoutParams(configurarParamsBotones());
                boton[fila][columna].setTag((fila*N)+columna);
                boton[fila][columna].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Button boton = (Button) v;
                        boton.setText("X");
                        turnoIA();
                    }
                });

                // Añadimos cada boton al contenedor
                contenedor_botones[fila].addView(boton[fila][columna]);
            }
            // Configuramos la fila de botones
            contenedor_botones[fila].setLayoutParams(configurarParamsLayouts());

            // Añadimos la fila de botones
            tablero.addView(contenedor_botones[fila]);



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
    // TODO arreglar esto que no va
    public void turnoIA(){
        int fila;//= (int) Math.round(Math.random()*(N - 1));
        int columna;// = (int) Math.round(Math.random()*(N - 1));
        do{
            fila = (int) Math.random()*(N-1);
            columna  = (int) Math.random()*(N-1);
        }while(boton[fila][columna].getText().equals("X"));
        boton[fila][columna].setText("O");
    }
}
