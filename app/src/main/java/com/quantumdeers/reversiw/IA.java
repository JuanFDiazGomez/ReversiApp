package com.quantumdeers.reversiw;

import java.util.ArrayList;

/**
 * Created by alumno on 11/11/16.
 */

public class IA extends GameEngine {

    private int profundidadMax;
    private int profundidadActual;
    ArrayList<Integer>[][] matrizDeEstados;
    ArrayList<OrigenSeleccion>[] arrayDeDisponibles;


    protected IA(int profundidadMax) {
        this.profundidadMax = profundidadMax;
        this.profundidadActual = 0;
        this.matrizDeEstados = new ArrayList[profundidadMax][3];
        this.arrayDeDisponibles = new ArrayList[profundidadMax];
    }

    private int mejorOpcion(ArrayList<OrigenSeleccion> casillasDisponibles, ArrayList<Integer>... estados) {
        salvarEstados(casillasDisponibles, estados);
        //realizarJugadas();
        return 0;
    }

    @Override
    protected void jugada(Integer botonPulsado) {
        super.jugada(botonPulsado);
    }

    private void jugadas(int casilla) {
        int profundidadAux = ++profundidadActual;
        jugada(casilla);
        /*if (profundidadActual < profundidadMax) {
            salvarEstados();
            for (int)
            jugadas();
        }*/
        profundidadActual = profundidadAux;

    }

    private void salvarEstados(ArrayList<OrigenSeleccion> casillasDisponibles, ArrayList<Integer>... estados) {
        matrizDeEstados[profundidadActual][0] = (ArrayList<Integer>) estados[0].clone();
        matrizDeEstados[profundidadActual][1] = (ArrayList<Integer>) estados[1].clone();
        matrizDeEstados[profundidadActual][2] = (ArrayList<Integer>) estados[2].clone();
        arrayDeDisponibles[0] = (ArrayList<OrigenSeleccion>) casillasDisponibles.clone();
    }

    /*private void restaurarEstadoJuego() {
        turnoJugadorActual = turnoJugadorActualAux;
        turnoActual = turnoActualAux;
        casillasLibres = matrizDeEstados[0][0];
        casillasJugador = matrizDeEstados[0][1];
        casillasIA = matrizDeEstados[0][2];
        casillasDisponibles = arrayDeDisponibles[0];
    }*/
}

