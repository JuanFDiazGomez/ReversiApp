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
    private int[] puntuacionesFinal;



    protected IAEngine(int profundidadMax, Button[][] matrizBotones) {
        super(TAM, matrizBotones);
        this.profundidadMax = profundidadMax;
        this.profundidadActual = 0;
        this.matrizDeEstados = new ArrayList[profundidadMax][3];
        this.arrayDeDisponibles = new ArrayList[profundidadMax];
        this.turnoJugadorActual = Turnos.IA;
        this.puntuacionesFinal = new int[2];
    }

    public int RecursivabuscarJugada(ArrayList<OrigenSeleccion> casillasDisponibles, ArrayList<Integer>... estados){
        inicializarIA();
        establecerEstadoInicial(casillasDisponibles, estados);
        RecursividadBuscarJugada();
        return arrayDeDisponibles[0].get(puntuacionesFinal[0]).disponile;
    }

    private void inicializarIA(){
        profundidadActual = 0;
        puntuacionesFinal[0] = -1;
        puntuacionesFinal[1] = -TAM*TAM;
    }

    private void RecursividadBuscarJugada(){
        for(int i = 0; i < casillasDisponibles.size(); i++){
            salvarEstados();
            profundidadActual++;
            juego(casillasDisponibles.get(i).disponile);
            if(casillasIA.size()-casillasJugador.size() > puntuacionesFinal[1]){
                puntuacionesFinal[0] = i;
            }
            profundidadActual--;
            restaurarEstado();
        }
    }

    private void juego(Integer juego){
        jugada(juego);
        // Preparar el siguiente turno solamente por la mejor jugada
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

    private void limpiarEstados(){
        for(ArrayList<Integer>[] estado : matrizDeEstados){
            estado[0].clear();
            estado[1].clear();
            estado[2].clear();
        }
        for(int i = 0; i < arrayDeDisponibles.length; i++){
            arrayDeDisponibles[i].clone();
        }
    }

    @Override
    protected void agregarDisponibles(int origen, int coordenada, ArrayList<Integer> casillasContrarias) {
        int disponible = busqueda(origen + coordenada, coordenada, casillasContrarias);
        if (disponible > -1) {
            casillasDisponibles.add(new OrigenSeleccion(origen, disponible, coordenada));
        }
    }
}

