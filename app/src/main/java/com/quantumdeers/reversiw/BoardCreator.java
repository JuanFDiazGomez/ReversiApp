package com.quantumdeers.reversiw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by alumno on 28/10/16.
 */

public class BoardCreator {
    static int N;
    Principal v;
    RelativeLayout pantalla;
    Button botones[][];
    LinearLayout contenedor_botones[];
    int casillasNoDisponibles = 0;
    int puntuacionJugador=0;
    int puntuacionIa=0;

    public BoardCreator(Principal v, int TAM) {
        this.v=v;
        this.pantalla = creaTableroDinamico(this.v);
        this.N = TAM;

    }

    public RelativeLayout getPantalla() {
        return pantalla;
    }

    public Button[][] getBotones() {
        return botones;
    }

    public LinearLayout[] getContenedor_botones() {
        return contenedor_botones;
    }

    private RelativeLayout creaTableroDinamico(Principal principal) {
        pantalla = (RelativeLayout) LayoutInflater.from(principal).
                inflate(R.layout.activity_juego, null, false);
        LinearLayout tablero = (LinearLayout) pantalla.findViewById(R.id.contenedor_tablero);
        modificarTablero(tablero);
        return pantalla;
    }

    private void modificarTablero(LinearLayout tablero) {
        botones = new Button[N][N];
        contenedor_botones = new LinearLayout[N];
        for(int fila = 0; fila < N; fila++) {
            contenedor_botones[fila] = new LinearLayout(v);
            contenedor_botones[fila].setOrientation(LinearLayout.HORIZONTAL);
            contenedor_botones[fila].setPadding(0,0,0,0);
            contenedor_botones[fila].setLayoutParams(configurarParamsLayouts());
            for(int columna = 0; columna < N ; columna++){
                botones[fila][columna]=crearBoton(fila, columna);
                contenedor_botones[fila].addView(botones[fila][columna]);
            }
            tablero.addView(contenedor_botones[fila]);
        }
    }

    private Button crearBoton(int fila, int columna){
        Button boton = new Button(v);
        // Configuramos el boton
        boton.setLayoutParams(configurarParamsBotones());
        boton.setTag(new Integer((fila*N)+columna));
        boton.setTextSize((float)(250/N));
        boton.setPadding(0,0,0,0);
        // Intercambiamos el color a cada boton
        if(fila%2==0){
            if(columna % 2 == 0) {
                boton.setBackgroundColor(v.getResources().getColor(R.color.cuadro_tablero_oscuro)
                );
            }else{
                boton.setBackgroundColor(
                        v.getResources().getColor(R.color.cuadro_tablero_claro)
                );
            }
        }else{
            if(columna % 2 == 0) {
                boton.setBackgroundColor(
                        v.getResources().getColor(R.color.cuadro_tablero_claro)
                );
            }else{
                boton.setBackgroundColor(
                        v.getResources().getColor(R.color.cuadro_tablero_oscuro)
                );
            }
        }

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jugada(v);
            }
        });
        boton.setClickable(true);
        return boton;
    }

    private void jugada(View v){
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
            crearToast();
            botonAbandonar.setText("Reiniciar");
            botonAbandonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reiniciar((Button)v);
                }
            });
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

    private void crearToast() {
        if(puntuacionJugador>puntuacionIa){
            Toast.makeText(v, "You Win!!!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(v, "You Lose!!!", Toast.LENGTH_LONG).show();
        }

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
