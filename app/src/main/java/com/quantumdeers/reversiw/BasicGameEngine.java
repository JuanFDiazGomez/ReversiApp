package com.quantumdeers.reversiw;

import android.widget.Button;

import java.util.ArrayList;

/**
 * Created by alumno on 15/11/16.
 */

public abstract class BasicGameEngine {

    protected MiButton[][] matrizBotones; // El tablero en si
    protected static int TAM; // Variable que especifica el tamaño del tablero
    protected CoordenadasBusqueda coordenadas; // Clase que contiene las coordenada de busqueda
    protected ArrayList<Integer> casillasJugador; // Casillas en posesion de J1
    protected ArrayList<Integer> casillasIA; // Casillas en posesion de J2
    protected ArrayList<Integer> casillasLibres; // Casillas sin ocupar
    protected ArrayList<OrigenSeleccion> casillasDisponibles; // Casillas disponibles para su seleccion
    protected Turnos turnoJugadorActual; // Indica el turno actual - True si es J1 - False si es J2
    protected int casillasLibresDisponibles;

    public BasicGameEngine(int TAM, MiButton[][] matrizBotones){
        this.TAM = TAM;
        this.matrizBotones = matrizBotones;
        this.coordenadas = new CoordenadasBusqueda(this.TAM);
        this.casillasLibresDisponibles = TAM*TAM;

    }

    protected void jugada(Integer botonPulsado) {
        ArrayList<Integer> casillasPropias;
        if (turnoJugadorActual == Turnos.JUGADOR) {
            casillasPropias = casillasJugador;
        } else {
            casillasPropias = casillasIA;
        }
        casillasPropias.add(botonPulsado);
        casillasLibres.remove(botonPulsado);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void habilitarOpciones() {
        ArrayList<Integer> casillasContrarias;
        ArrayList<Integer> casillasPropias;
        if (turnoJugadorActual == Turnos.JUGADOR) {
            casillasPropias = casillasJugador;
            casillasContrarias = casillasIA;
        } else {
            casillasPropias = casillasIA;
            casillasContrarias = casillasJugador;
        }

        casillasDisponibles.clear();
        for (int tag : casillasPropias) {
            if (tag % TAM < (TAM - 1)) {
                if (casillasContrarias.indexOf(tag + coordenadas.E()) > -1) {
                    agregarDisponibles(tag, coordenadas.E(), casillasContrarias);
                }
                if (casillasContrarias.indexOf(tag + coordenadas.NE()) > -1) {
                    agregarDisponibles(tag, coordenadas.NE(), casillasContrarias);
                }
                if (casillasContrarias.indexOf(tag + coordenadas.SE()) > -1) {
                    agregarDisponibles(tag, coordenadas.SE(), casillasContrarias);
                }
            }
            if (tag % TAM != 0) {
                if (casillasContrarias.indexOf(tag + coordenadas.W()) > -1) {
                    agregarDisponibles(tag, coordenadas.W(), casillasContrarias);
                }
                if (casillasContrarias.indexOf(tag + coordenadas.NW()) > -1) {
                    agregarDisponibles(tag, coordenadas.NW(), casillasContrarias);
                }
                if (casillasContrarias.indexOf(tag + coordenadas.SW()) > -1) {
                    agregarDisponibles(tag, coordenadas.SW(), casillasContrarias);
                }
            }
            if (casillasContrarias.indexOf(tag + coordenadas.N()) > -1) {
                agregarDisponibles(tag, coordenadas.N(), casillasContrarias);
            }
            if (casillasContrarias.indexOf(tag + coordenadas.S()) > -1) {
                agregarDisponibles(tag, coordenadas.S(), casillasContrarias);
            }
        }
    }

    protected void agregarDisponibles(int origen, int coordenada, ArrayList<Integer> casillasContrarias) {
        int disponible = busqueda(origen + coordenada, coordenada, casillasContrarias);
        if (disponible > -1) {
            casillasDisponibles.add(new OrigenSeleccion(origen, disponible, coordenada));
            matrizBotones[disponible / TAM][disponible % TAM].setClickable(true);
        }
    }

    protected int busqueda(int tag, int coordenada, ArrayList<Integer> casillasContrarias) {
        int nuevoTag = tag + coordenada;
        if (Math.abs(coordenada) == 1) {
            if (nuevoTag / TAM != tag / TAM) {
                return -1;
            }
        } else {
            if (Math.abs((tag / TAM) - (nuevoTag / TAM)) != 1) {
                return -1;
            }
        }
        if (casillasLibres.indexOf(nuevoTag) > -1) {
            return nuevoTag;
        } else if (casillasContrarias.indexOf(nuevoTag) > -1) {
            return busqueda(nuevoTag, coordenada, casillasContrarias);
        } else {
            return -1;
        }
    }

    protected ArrayList<String> voltearCasillas(Integer seleccion) {
        ArrayList<String> casillasAGirar = new ArrayList<>(10);
        for (int index = 0; index < casillasDisponibles.size(); index++) {
            OrigenSeleccion os = casillasDisponibles.get(index);
            if (os.disponile == seleccion) {
                for (int bo = os.disponile - os.coordenada; bo != os.origen; bo -= os.coordenada) {
                    casillasAGirar.add(String.valueOf(bo));
                    if (turnoJugadorActual == Turnos.JUGADOR) {
                        casillasJugador.add(bo);
                        casillasIA.remove(Integer.valueOf(bo));
                    } else {
                        casillasIA.add(bo);
                        casillasJugador.remove(Integer.valueOf(bo));
                    }
                }
            }
        }
        return casillasAGirar;
    }

    protected void prepararSiguienteTurno() {
        int vueltas = 0;
        do {
            turnoJugadorActual = (turnoJugadorActual == Turnos.JUGADOR) ? Turnos.IA : Turnos.JUGADOR;
            habilitarOpciones();
            vueltas++;
        } while (casillasDisponibles.size() < 1 && vueltas != 2);
    }
}
