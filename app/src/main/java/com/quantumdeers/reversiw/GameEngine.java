package com.quantumdeers.reversiw;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

class GameEngine {
    private Principal principal;
    private RelativeLayout pantalla;
    private Button[][] matrizBotones;
    private int casillasOcupadas;
    private int puntuacionJugador;
    private int puntuacionIA;
    private static int TAM;
    private ArrayList<Integer> casillasJugador;
    private ArrayList<Integer> casillasIA;
    private ArrayList<Integer> casillasDisponibles;
    private static ArrayList<Integer> casillasSinOcupar;
    private CoordenadasBusqueda[] arrayCoordenadas;
    private boolean jugadorEmpieza;

    GameEngine(RelativeLayout pantalla, int TAM, Button[][] matrizBotones, Principal principal){
        this.principal = principal;
        this.pantalla = pantalla;
        this.casillasOcupadas = 0;
        this.puntuacionJugador = 0;
        this.puntuacionIA = 0;
        this.TAM = TAM;
        this.matrizBotones = matrizBotones;
        this.casillasJugador = new ArrayList<>(TAM/2);
        this.casillasIA = new ArrayList<>(TAM/2);
        this.casillasDisponibles = new ArrayList<>(10);
        this.jugadorEmpieza=true;
        this.crearCoordenadas();
        this.iniciarJuego();

    }
    private void crearCoordenadas(){
        int index = 0;
        arrayCoordenadas = new CoordenadasBusqueda[8];
        arrayCoordenadas[index] = new CoordenadasBusqueda(opcionCoordenadas.N);
        arrayCoordenadas[++index] = new CoordenadasBusqueda(opcionCoordenadas.S);
        arrayCoordenadas[++index] = new CoordenadasBusqueda(opcionCoordenadas.W);
        arrayCoordenadas[++index] = new CoordenadasBusqueda(opcionCoordenadas.E);
        arrayCoordenadas[++index] = new CoordenadasBusqueda(opcionCoordenadas.NW);
        arrayCoordenadas[++index] = new CoordenadasBusqueda(opcionCoordenadas.NE);
        arrayCoordenadas[++index] = new CoordenadasBusqueda(opcionCoordenadas.SW);
        arrayCoordenadas[++index] = new CoordenadasBusqueda(opcionCoordenadas.SE);
        casillasSinOcupar = new ArrayList<>(TAM*TAM);
        for(int tag = 0; tag < TAM*TAM; tag++){
            casillasSinOcupar.add(tag);
        }
    }
    private void iniciarJuego(){
        for(int fila = 0; fila < TAM; fila++){
            for(int columna = 0; columna < TAM; columna++){
                matrizBotones[fila][columna].setClickable(false);
            }
        }
        int A = (TAM-1)/2;
        int B = (TAM)/2;
        matrizBotones[A][A].setText("X");
        casillasJugador.add(((A*TAM)+A));
        casillasSinOcupar.remove(((A*TAM)+A));

        matrizBotones[A][B].setText("O");
        casillasIA.add(((A*TAM)+B));
        casillasSinOcupar.remove(((A*TAM)+B));

        matrizBotones[B][A].setText("O");
        casillasIA.add(((B*TAM)+A));
        casillasSinOcupar.remove(((B*TAM)+A));

        matrizBotones[B][B].setText("X");
        casillasJugador.add(((B*TAM)+B));
        casillasSinOcupar.remove(((B*TAM)+B));
        if(!jugadorEmpieza){
            turnoIA();
        }else{
            habilitarOpciones("O");
        }
    }

    private void habilitarOpciones(String simboloContrario){
        for (int tag : casillasJugador){
            int fila = tag/TAM;
            int columna = tag%TAM;
            for(int index = 0 ; index < 6 ; index++){
                int modFila = arrayCoordenadas[index].getFila();
                int modColumna = arrayCoordenadas[index].getColumna();
                int tagAux = ((fila+modFila)*TAM)+(columna+modColumna);
                if(tagAux >= 0 && tagAux < TAM*TAM){
                    int filaAux = tagAux/TAM;
                    int colAux = tagAux%TAM;
                    if(matrizBotones[filaAux][colAux].getText().equals(simboloContrario)){
                        busqueda(filaAux, colAux, arrayCoordenadas[index], simboloContrario);
                    }
                }
            }
        }
    }

    private void busqueda(int fila, int columna, CoordenadasBusqueda coordenada, String simboloContrario){
        int modFila = coordenada.getFila();
        int modColumna = coordenada.getColumna();
        int tagAux = ((fila+modFila)*TAM)+(columna+modColumna);
        if(tagAux >= 0 && tagAux < TAM*TAM){
            int filaAux = tagAux/TAM;
            int colAux = tagAux%TAM;
            if(matrizBotones[filaAux][colAux].getText().equals(simboloContrario)){
                busqueda(filaAux, colAux, coordenada, simboloContrario);
            } else if (matrizBotones[filaAux][colAux].getText().equals("")){
                matrizBotones[filaAux][colAux].setClickable(true);
            }
        }
    }
    void jugada(Button botonPulsado){
        turnoJugador(botonPulsado);
        if (casillasOcupadas < TAM * TAM) {
            turnoIA();
        }else{
            Button botonAbandonar = (Button) pantalla.findViewById(R.id.botonAbandonar);
            crearToast();
            botonAbandonar.setText(R.string.textoReiniciar);
            botonAbandonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View botonPulsado) {
                    reiniciar((Button) botonPulsado);
                }
            });
        }
    }

    private void crearToast() {
        if (puntuacionJugador > puntuacionIA) {
            Toast.makeText(principal, "You Win!!!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(principal, "You Lose!!!", Toast.LENGTH_LONG).show();
        }

    }

    private void turnoJugador(Button botonPulsado) {
        botonPulsado.setText("X");
        puntuacionJugador++;
        TextView puntuacionJugadorTV =
                (TextView) pantalla.findViewById(R.id.puntuacionJugador);
        puntuacionJugadorTV.setText(String.format(Locale.getDefault(), "%d", puntuacionJugador));
        botonPulsado.setClickable(false);
        casillasJugador.add((Integer)botonPulsado.getTag());
        casillasOcupadas++;
        //TODO definir este metodo correctamente
        //girarColindantes(botonPulsado, "X");
    }

    private void turnoIA() {
        int fila;
        int columna;
        do {
            fila = (int) (Math.random() * TAM);
            columna = (int) (Math.random() * TAM);
        } while (!matrizBotones[fila][columna].isClickable());
        matrizBotones[fila][columna].setText("O");
        puntuacionIA++;
        TextView puntuacionIaTV =
                (TextView) pantalla.findViewById(R.id.puntuacionIA);
        puntuacionIaTV.setText(String.format(Locale.getDefault(), "%d", puntuacionIA));
        matrizBotones[fila][columna].setClickable(false);
        casillasOcupadas++;
        habilitarOpciones("O");
        //girarColindantes(botones[fila][columna], "O");
    }

    private void reiniciar(Button botonAbandonar) {
        for (int fila = 0; fila < TAM; fila++) {
            for (int columna = 0; columna < TAM; columna++) {
                matrizBotones[fila][columna].setClickable(true);
                matrizBotones[fila][columna].setText("");
                TextView puntuacionJugadorTV =
                        (TextView) pantalla.findViewById(R.id.puntuacionJugador);
                puntuacionJugadorTV.setText("0");
                TextView puntuacionIATV =
                        (TextView) pantalla.findViewById(R.id.puntuacionIA);
                puntuacionIATV.setText("0");
                puntuacionIA = 0;
                puntuacionJugador = 0;
                casillasOcupadas = 0;
                botonAbandonar.setText(R.string.boton_abandonar);
            }
        }
    }

    //TODO redefinir este metodo
    /*private void volteaColindantes(Button boton, String jugador) {
        int tag = (Integer) (boton.getTag());
        int fila = tag / N;
        int columna = tag - (fila * N);
        for (int i = -1; i < 2; i++) {
            if (fila + i < N && fila + i >= 0) {
                for (int j = -1; j < 2; j++) {
                    if (columna + j < N && columna + j >= 0) {
                        if (!(i == 0 && j == 0)) {
                            //TODO probar switch
                            if (botones[fila + i][columna + j].isClickable()) {
                                casillasNoDisponibles++;
                                botones[fila + i][columna + j].setClickable(false);
                                botones[fila + i][columna + j].setText(jugador);
                                if (jugador.equals("X")) {
                                    puntuacionJugador++;
                                } else {
                                    puntuacionIa++;
                                }
                                botones[fila + i][columna + j].setText(jugador);
                            } else {
                                if (!botones[fila + i][columna + j].getText().equals(jugador)) {
                                    if (jugador.equals("X")) {
                                        puntuacionJugador++;
                                        puntuacionIa--;
                                    } else {
                                        puntuacionIa++;
                                        puntuacionJugador--;
                                    }
                                    botones[fila + i][columna + j].setText(jugador);
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/
}
