package com.quantumdeers.reversiw;

import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by alumno on 21/10/16.
 */

public class ReversiGame {
    private Principal principal;
    private RelativeLayout pantalla;
    private LinearLayout tablero;
    private final static int TAM = 6;
    private Button[][] matrizBotones;
    private int casillasOcupadas;
    private int puntuacionJugador;
    private int puntuacionIA;

    public ReversiGame(Principal principal) {
        this.principal = principal;
        this.casillasOcupadas = 0;
        this.puntuacionJugador = 0;
        this.puntuacionIA = 0;
        this.pantalla = crearPantalla();
    }

    public RelativeLayout getPantalla() {
        return pantalla;
    }

    private RelativeLayout crearPantalla() {
        pantalla = (RelativeLayout) LayoutInflater.from(principal).
                inflate(R.layout.activity_juego, new LinearLayout(principal), false);
        tablero = (LinearLayout) pantalla.findViewById(R.id.contenedor_tablero);
        modificarTablero();
        return pantalla;
    }

    private void modificarTablero() {
        LinearLayout[] arrayContenedorBotones = new LinearLayout[TAM];
        matrizBotones = new Button[TAM][TAM];
        for (int fila = 0; fila < TAM; fila++) {
            arrayContenedorBotones[fila] = crearContenedorBotones();
            for (int columna = 0; columna < TAM; columna++) {
                matrizBotones[fila][columna] = crearBoton(fila, columna);
                arrayContenedorBotones[fila].addView(matrizBotones[fila][columna]);
            }
            tablero.addView(arrayContenedorBotones[fila]);
        }
    }

    private LinearLayout crearContenedorBotones() {
        LinearLayout contenedor = new LinearLayout(principal);
        contenedor.setLayoutParams(crearLayoutParams());
        contenedor.setOrientation(LinearLayout.HORIZONTAL);
        contenedor.setPadding(0, 0, 0, 0);
        return contenedor;
    }

    private Button crearBoton(int fila, int columna) {
        Button boton = new Button(principal);
        boton.setLayoutParams(crearLayoutParams());
        boton.setTag((fila * TAM) + columna);
        boton.setTextSize((float) (250 / TAM));
        boton.setPadding(0, 0, 0, 0);
        if (fila % 2 == 0) {
            if(columna % 2 == 0) {
                boton.setBackgroundColor(
                        principal.getResources().getColor(R.color.cuadro_tablero_oscuro));
            }else{
                boton.setBackgroundColor(
                        principal.getResources().getColor(R.color.cuadro_tablero_claro));
            }
        } else {
            if(columna % 2 != 0) {
                boton.setBackgroundColor(
                        principal.getResources().getColor(R.color.cuadro_tablero_oscuro));
            }else{
                boton.setBackgroundColor(
                        principal.getResources().getColor(R.color.cuadro_tablero_claro));
            }
        }

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View botonPulsado) {
                jugada((Button) botonPulsado);
            }
        });

        return boton;
    }

    private LinearLayout.LayoutParams crearLayoutParams() {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        layoutParams.weight = 1;
        return layoutParams;
    }

    private void jugada(Button botonPulsado) {
        turnoJugador(botonPulsado);
        if (casillasOcupadas < TAM * TAM) {
            turnoIA();
        }
        if (casillasOcupadas == TAM * TAM) {
            Button botonAbandonar = (Button) pantalla.findViewById(R.id.botonAbandonar);
            crearToast();
            botonAbandonar.setText(R.string.textoReiniciar);
            botonAbandonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View botonPulsado) {
                    reiniciar((Button) botonPulsado);
                }
            });
        }
    }

    private void crearToast() {
        if (puntuacionJugador > puntuacionIA) {
            Toast.makeText(principal, "You Win!!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(principal, "You Lose!!!", Toast.LENGTH_LONG).show();
        }

    }

    private void turnoJugador(Button botonPulsado) {
        botonPulsado.setText("X");
        puntuacionJugador++;
        TextView puntuacionJugadorTV =
                (TextView) pantalla.findViewById(R.id.puntuacionJugador);
        puntuacionJugadorTV.setText(String.format(Locale.getDefault(), "%d", puntuacionJugador));
        botonPulsado.setClickable(false);
        casillasOcupadas++;
        //TODO definir este metodo correctamente
        //girarColindantes(botonPulsado, "X");
    }

    private void turnoIA() {
        int fila;//= (int) Math.round(Math.random()*(N - 1));
        int columna;// = (int) Math.round(Math.random()*(N - 1));
        do {
            fila = (int) (Math.random() * TAM);
            columna = (int) (Math.random() * TAM);
        } while (!matrizBotones[fila][columna].isClickable());
        matrizBotones[fila][columna].setText("O");
        puntuacionIA++;
        TextView puntuacionIaTV =
                (TextView) pantalla.findViewById(R.id.puntuacionIA);
        puntuacionIaTV.setText(String.format(Locale.getDefault(), "%d", puntuacionIA));
        matrizBotones[fila][columna].setClickable(false);
        casillasOcupadas++;
        //girarColindantes(botones[fila][columna], "O");
    }

    private void reiniciar(Button botonAbandonar) {
        for (int fila = 0; fila < TAM; fila++) {
            for (int columna = 0; columna < TAM; columna++) {
                matrizBotones[fila][columna].setClickable(true);
                matrizBotones[fila][columna].setText("");
                TextView puntuacionJugadorTV =
                        (TextView) pantalla.findViewById(R.id.puntuacionJugador);
                puntuacionJugadorTV.setText("0");
                TextView puntuacionIATV =
                        (TextView) pantalla.findViewById(R.id.puntuacionIA);
                puntuacionIATV.setText("0");
                puntuacionIA = 0;
                puntuacionJugador = 0;
                casillasOcupadas = 0;
                botonAbandonar.setText(R.string.boton_abandonar);
            }
        }
    }
    //TODO redefinir este metodo
    /*private void volteaColindantes(Button boton, String jugador) {
        int tag = (Integer) (boton.getTag());
        int fila = tag / N;
        int columna = tag - (fila * N);
        for (int i = -1; i < 2; i++) {
            if (fila + i < N && fila + i >= 0) {
                for (int j = -1; j < 2; j++) {
                    if (columna + j < N && columna + j >= 0) {
                        if (!(i == 0 && j == 0)) {
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
    }*/
}