package com.quantumdeers.reversiw;

enum opcionCoordenadas{
    N,S,W,E,NW,SW,NE,SE
}
class origenSeleccion{
    public int origen;
    public int seleccion;

    public origenSeleccion(int origen, int seleccion){
        this.origen = origen;
        this.seleccion = seleccion;
    }
}

public class CoordenadasBusqueda {
    private int TAM;

    public CoordenadasBusqueda(int TAM){
        this.TAM = TAM;
    }
    public int N(){
        return -TAM;
    }

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
