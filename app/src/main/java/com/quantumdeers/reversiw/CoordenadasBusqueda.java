package com.quantumdeers.reversiw;

enum opcionCoordenadas{
    N,S,W,E,NW,SW,NE,SE
}

public class CoordenadasBusqueda {
    private int fila;
    private int columna;

    public CoordenadasBusqueda(opcionCoordenadas opcion){
        switch(opcion){
            case N:
                this.fila = -1;
                this.columna = 0;
                break;
            case S:
                this.fila = 1;
                this.columna = 0;
                break;
            case W:
                this.fila = 0;
                this.columna = -1;
                break;
            case E:
                this.fila = 0;
                this.columna = 1;
                break;
            case NW:
                this.fila = -1;
                this.columna = -1;
                break;
            case SW:
                this.fila = 1;
                this.columna = -1;
                break;
            case NE:
                this.fila = -1;
                this.columna = 1;
                break;
            case SE:
                this.fila = 1;
                this.columna = 1;
                break;
        }
    }

    public int getFila() {
        return fila;
    }

    public int getColumna() {
        return columna;
    }


}
