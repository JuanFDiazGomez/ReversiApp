package com.quantumdeers.reversiw;

enum Turnos{
    JUGADOR, IA
}

/**
 * Esta clase nos permite almacenar que casilla
 * es la que nos habilito una para que al pulsarla
 * cambiemos todas las fichas intermedias
 */
class OrigenSeleccion {
    int origen;
    int disponile;
    int coordenada;

    OrigenSeleccion(int origen, int disponile, int coordenada){
        this.origen = origen;
        this.disponile = disponile;
        this.coordenada = coordenada;
    }
}

/**
 * Con esta clase creamos las variacion que tiene que seguir
 * segun el tamaño escogido del tablero
 */
class CoordenadasBusqueda {
    private int TAM;

    CoordenadasBusqueda(int TAM){
        this.TAM = TAM;
    }

    int N(){ return -TAM; }

    int S(){
        return TAM;
    }

    int W(){
        return -1;
    }

    int E(){
        return 1;
    }

    int NW(){
        return N()+W();
    }

    int NE(){
        return N()+E();
    }

    int SW(){
        return S()+W();
    }

    int SE(){
        return S()+E();
    }
}
