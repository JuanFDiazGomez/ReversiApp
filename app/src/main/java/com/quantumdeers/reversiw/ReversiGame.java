package com.quantumdeers.reversiw;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by alumno on 21/10/16.
 */

public class ReversiGame {

    private LinearLayout pantalla;
    private int tamTablero;
    private Button[][] botonesTablero;
    private int casillasNoDisponibles;
    private int puntuacionJugador;
    private int puntuacionIA;

    public ReversiGame(Button[][] botonesTablero) {

        this.tamTablero = botonesTablero.length;
        this.botonesTablero = botonesTablero;
        this.casillasNoDisponibles = 0;
        this.puntuacionJugador = 0;
        this.puntuacionIA = 0;
    }


    public Button[][] getBotonesTablero() {
        return botonesTablero;
    }

    public int getCasillasNoDisponibles() {
        return casillasNoDisponibles;
    }

    public void setCasillasNoDisponibles(int casillasNoDisponibles) {
        this.casillasNoDisponibles = casillasNoDisponibles;
    }

    public int getPuntuacionJugador() {
        return puntuacionJugador;
    }

    public void setPuntuacionJugador(int puntuacionJugador) {
        this.puntuacionJugador = puntuacionJugador;
    }

    public int getPuntuacionIA() {
        return puntuacionIA;
    }

    public void setPuntuacionIA(int puntuacionIA) {
        this.puntuacionIA = puntuacionIA;
    }



}
