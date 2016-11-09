package com.quantumdeers.reversiw;

enum Turnos{
    JUGADOR, IA
}


class OrigenSeleccion {
    public int origen;
    public int disponile;
    public int coordenada;

    public OrigenSeleccion(int origen, int disponile, int coordenada){
        this.origen = origen;
        this.disponile = disponile;
        this.coordenada = coordenada;
    }
}

public class CoordenadasBusqueda {
    private int TAM;

    public CoordenadasBusqueda(int TAM){
        this.TAM = TAM;
    }

    public int N(){ return -TAM; }

    public int S(){
        return TAM;
    }

    public int W(){
        return -1;
    }

    public int E(){
        return 1;
    }

    public int NW(){
        return N()+W();
    }

    public int NE(){
        return N()+E();
    }

    public int SW(){
        return S()+W();
    }

    public int SE(){
        return S()+E();
    }
}
