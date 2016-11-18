package com.quantumdeers.reversiw;

import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by alumno on 11/11/16.
 */

public class IAEngine extends BasicGameEngine {

    private int profundidadMax;
    private int profundidadActual;
    private ArrayList<Integer>[][] matrizDeEstados;
    private ArrayList<OrigenSeleccion>[] arrayDeDisponibles;
    private int[][] puntuacionesFinal;



    protected IAEngine(int profundidadMax, Button[][] matrizBotones) {
        super(TAM, matrizBotones);
        this.profundidadMax = profundidadMax;
        this.profundidadActual = -1;
        this.matrizDeEstados = new ArrayList[profundidadMax][3];
        this.arrayDeDisponibles = new ArrayList[profundidadMax];
        this.turnoJugadorActual = Turnos.IA;
    }

    public int RecursivabuscarJugada(ArrayList<OrigenSeleccion> casillasDisponibles, ArrayList<Integer>... estados){
        inicializarIA();
        puntuacionesFinal = new int[casillasDisponibles.size()][3];
        establecerEstadoInicial(casillasDisponibles, estados);
        //salvarEstados(casillasDisponibles, estados[0], estados[1], estados[2]);
        RecursividadBuscarJugada();
        // despues de realizar la busquedad tendremos el tag con la mayor puntuacion
        // El tag con mayor puntuacion esta en en el index 0 del array puntuacionesFinal
        int seleccion = puntuacionesFinal[0][0];
        int []caminos = new int[casillasDisponibles.size()];
        for(int i = 0; i < caminos.length ; i++){
            caminos[i] = i;
        }
        for(int loquesea = 0; loquesea < puntuacionesFinal.length-1; loquesea++){
            if(puntuacionesFinal[loquesea][2] < puntuacionesFinal[loquesea+1][2]){
                seleccion=puntuacionesFinal[loquesea+1][0];
            }
        }
        return arrayDeDisponibles[0].get(seleccion).disponile;
    }

    private void inicializarIA(){
        profundidadActual = -1;
    }
    /*private void RecursivabuscarJugada(int camino){
        profundidadActual++;
        salvarEstados();
        for(int i = 0; i < this.casillasDisponibles.size(); i++){
            if(profundidadActual == 0){camino = i;}
            juego(casillasDisponibles.get(i).disponile);
            if(profundidadActual < profundidadMax-1){
                RecursivabuscarJugada(camino);
                profundidadActual--;
            }else{
                puntuacionesFinal[camino][0]=camino;
                puntuacionesFinal[camino][1]=casillasJugador.size();
                puntuacionesFinal[camino][2]=casillasIA.size();

            }
        }
        // Hacemos un bucle por cada uno de las casillas disponibles
            // hacemos la jugada en esas casillas disponibles
            //Comprobamos que no estemos en la profundidad
            // una vez realizada las jugadas volvemos a llamar la funcion que continue
            // aumentando la profundidad en 1
        // Tras aumentar la profundidad, cuando vaya hacia atras tambien la reduciremos
    }*/
    private void RecursividadBuscarJugada(){
        for(int i = 0; i < casillasDisponibles.size(); i++){
            salvarEstados();
            profundidadActual = 0;
            restaurarEstado();
            jugada(casillasDisponibles.get(i).disponile);
            profundidadActual = 1;
            salvarEstados();
            for(int j = 0; j < casillasDisponibles.size(); j++){
                restaurarEstado();
                jugada(casillasDisponibles.get(i).disponile);
                salvarEstados();

            }
        }
    }

    private void juego(Integer juego){
        jugada(juego);
        prepararSiguienteTurno();
    }

    private void salvarEstados() {
        matrizDeEstados[profundidadActual][0] = (ArrayList<Integer>) casillasLibres.clone();
        matrizDeEstados[profundidadActual][1] = (ArrayList<Integer>) casillasJugador.clone();
        matrizDeEstados[profundidadActual][2] = (ArrayList<Integer>) casillasIA.clone();
        arrayDeDisponibles[profundidadActual] = (ArrayList<OrigenSeleccion>) casillasDisponibles.clone();
    }

    private void restaurarEstado(){
        casillasLibres = matrizDeEstados[profundidadActual][0];
        casillasJugador = matrizDeEstados[profundidadActual][1];
        casillasIA = matrizDeEstados[profundidadActual][2];
        casillasDisponibles = arrayDeDisponibles[profundidadActual];
    }

    private void establecerEstadoInicial(ArrayList<OrigenSeleccion> casillasDisponibles, ArrayList<Integer>[] estados){
        this.casillasLibres = estados[0];
        this.casillasJugador = estados[1];
        this.casillasIA = estados[2];
        this.casillasDisponibles = casillasDisponibles;
    }
}

