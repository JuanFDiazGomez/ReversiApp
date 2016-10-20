package com.quantumdeers.reversiw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    static int N = 15;
    Button botones[][];
    LinearLayout contenedor_botones[];
    int casillasNoDisponibles = 0;
    int puntuacionJugador=0;
    int puntuacionIa=0;
    RelativeLayout pantalla;

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
        pantalla = (RelativeLayout) LayoutInflater.from(principal).
                inflate(R.layout.activity_juego, null, false);

        // Cojemos el layout que contendra el tablero de nuestra copia
        LinearLayout tablero = (LinearLayout) pantalla.findViewById(R.id.contenedor_tablero);

        // modificamos el tablero
        modificarTablero(tablero);

        // Devolvemos nuestra nueva pantalla
        return pantalla;
    }

    private void modificarTablero(LinearLayout tablero) {

        // Definimos la configuracion de nuestros elementos
        //LinearLayout.LayoutParams ParamsLayouts = configurarParamsLayouts();
        //LinearLayout.LayoutParams ParamsBotones = configurarParamsBotones();
        botones = new Button[N][N];
        contenedor_botones = new LinearLayout[N];
        for(int fila = 0; fila < N; fila++) {
            // Creamos la fila de botones
            contenedor_botones[fila] = new LinearLayout(this);
            contenedor_botones[fila].setOrientation(LinearLayout.HORIZONTAL);
            contenedor_botones[fila].setPadding(0,0,0,0);
            for(int columna = 0; columna < N ; columna++){
                // Creamos el boton
                botones[fila][columna] = new Button(this);
                // Intercambiamos el color a cada boton
                if((fila+columna) % 2 == 0) {
                    botones[fila][columna].setBackgroundColor(
                            getResources().getColor(R.color.cuadro_tablero_oscuro)
                    );
                }else{
                    botones[fila][columna].setBackgroundColor(
                            getResources().getColor(R.color.cuadro_tablero_claro)
                    );
                }
                // Configuramos el boton
                botones[fila][columna].setLayoutParams(configurarParamsBotones());
                botones[fila][columna].setTag(new Integer((fila*N)+columna));
                botones[fila][columna].setTextSize((float)(250/N));
                botones[fila][columna].setPadding(0,0,0,0);
                botones[fila][columna].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        turnoJugador(v);
                        if(casillasNoDisponibles < N*N)
                            turnoIA();
                        TextView puntuacionJugadorTV =
                                (TextView) pantalla.findViewById(R.id.puntuacionJugador);
                        puntuacionJugadorTV.setText(Integer.toString(puntuacionJugador));
                        TextView puntuacionIaTV =
                                (TextView) pantalla.findViewById(R.id.puntuacionIA);
                        puntuacionIaTV.setText(Integer.toString(puntuacionIa));
                        if(casillasNoDisponibles == N*N){
                            Button botonAbandonar = (Button) pantalla.findViewById(R.id.botonAbandonar);
                            if(puntuacionJugador>puntuacionIa){
                                botonAbandonar.setText("You WIN! - RESTART");
                            }else{
                                botonAbandonar.setText("You LOSE! - RESTART");
                            }
                            botonAbandonar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    reiniciar((Button)v);
                                }
                            });
                        }
                    }
                });

                // Añadimos cada boton al contenedor
                contenedor_botones[fila].addView(botones[fila][columna]);
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

    private void turnoJugador(View v){
        Button boton = (Button) v;
        Integer tag = (Integer)(boton.getTag());
        boton.setText("X");
        puntuacionJugador++;
        boton.setClickable(false);
        casillasNoDisponibles++;
        volteaColindantes(boton, "X");
    }

    private void turnoIA(){
        int fila;//= (int) Math.round(Math.random()*(N - 1));
        int columna;// = (int) Math.round(Math.random()*(N - 1));
        do{
            fila = (int) (Math.random()*N);
            columna  = (int) (Math.random()*N);
        }while(!botones[fila][columna].isClickable());
        botones[fila][columna].setText("O");
        puntuacionIa++;
        botones[fila][columna].setClickable(false);
        casillasNoDisponibles++;
        volteaColindantes(botones[fila][columna], "O");

    }
    // TODO hacer mas eficiente el bucle, separar las ocasiones en las que la fila es 0 o 5 y columna igual
    private void volteaColindantes(Button boton, String jugador){
        int tag = (Integer)(boton.getTag());
        int fila = (int)tag/N;
        int columna = tag-(fila*N);
        for(int i = -1; i < 2; i++){
            if(fila+i<N && fila+i>=0) {
                for (int j = -1; j < 2; j++) {
                    if(columna+j<N && columna+j>=0){
                        if(!(i==0 && j==0)) {
                            //TODO probar switch
                            if (botones[fila + i][columna + j].isClickable()) {
                                casillasNoDisponibles++;
                                botones[fila + i][columna + j].setClickable(false);
                                botones[fila + i][columna + j].setText(jugador);
                                if (jugador.equals("X")) {
                                    puntuacionJugador++;
                                } else {
                                    puntuacionIa++;
                                }
                                botones[fila + i][columna + j].setText(jugador);
                            } else {
                                if (!botones[fila + i][columna + j].getText().equals(jugador)) {
                                    if (jugador.equals("X")) {
                                        puntuacionJugador++;
                                        puntuacionIa--;
                                    } else {
                                        puntuacionIa++;
                                        puntuacionJugador--;
                                    }
                                    botones[fila + i][columna + j].setText(jugador);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void reiniciar(Button botonAbandonar){
        for (int fila = 0; fila < N; fila++){
            for(int columna = 0; columna < N; columna++){
                botones[fila][columna].setClickable(true);
                botones[fila][columna].setText("");
                TextView puntuacionJugadorTV =
                        (TextView) pantalla.findViewById(R.id.puntuacionJugador);
                puntuacionJugadorTV.setText("0");
                TextView puntuacionIaTV =
                        (TextView) pantalla.findViewById(R.id.puntuacionIA);
                puntuacionIaTV.setText("0");
                puntuacionIa=0;
                puntuacionJugador=0;
                casillasNoDisponibles=0;
                botonAbandonar.setText("ABANDONAR");
            }
        }
    }
}
