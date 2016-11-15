package com.quantumdeers.reversiw;

import java.util.ArrayList;

/**
 * Created by alumno on 11/11/16.
 */

public class IAEngine extends BasicGameEngine {

    private int profundidadMax;
    private int profundidadActual;
    private ArrayList<Integer>[][] matrizDeEstados;
    private ArrayList<OrigenSeleccion>[] arrayDeDisponibles;
    private int[] puntuacionesFinal = {-1,-1};



    protected IAEngine(int profundidadMax) {
        this.profundidadMax = profundidadMax;
        this.profundidadActual = 0;
        this.matrizDeEstados = new ArrayList[profundidadMax][3];
        this.arrayDeDisponibles = new ArrayList[profundidadMax];
    }

    private int RecursivabuscarJugada(ArrayList<OrigenSeleccion> casillasDisponibles, ArrayList<Integer>... estados){
        establecerEstadoInicial(0)
        salvarEstados(casillasDisponibles, estados[0], estados[1], estados[2]);
        RecursivabuscarJugada(-1, casillasDisponibles, estados[0], estados[1], estados[2]);
        // despues de realizar la busquedad tendremos el tag con la mayor puntuacion
        // El tag con mayor puntuacion esta en en el index 0 del array puntuacionesFinal

        return 0;
    }

    private void RecursivabuscarJugada(int posicionOrigen, ArrayList<OrigenSeleccion> casillasDisponibles, ArrayList<Integer>... estados){
        salvarEstados(casillasDisponibles, estados[0], estados[1], estados[2]);
        for(int i = 0; i < this.casillasDisponibles)
        // Hacemos un bucle por cada uno de las casillas disponibles
            // hacemos la jugada en esas casillas disponibles
            //Comprobamos que no estemos en la profundidad
            // una vez realizada las jugadas volvemos a llamar la funcion que continue
            // aumentando la profundidad en 1
        // Tras aumentar la profundidad, cuando vaya hacia atras tambien la reduciremos
    }



    private void salvarEstados() {
        matrizDeEstados[profundidadActual][0] = (ArrayList<Integer>) casillasLibres.clone();
        matrizDeEstados[profundidadActual][1] = (ArrayList<Integer>) casillasJugador.clone();
        matrizDeEstados[profundidadActual][2] = (ArrayList<Integer>) casillasIA.clone();
        arrayDeDisponibles[profundidadActual] = (ArrayList<OrigenSeleccion>) casillasDisponibles.clone();
    }
}

