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
    private static int TAM;
    private boolean jugadorEmpieza;
    private int casillasOcupadas;
    private int puntuacionJugador;
    private int puntuacionIA;
    private CoordenadasBusqueda coordenadas;
    private ArrayList<Integer> casillasJugador;
    private ArrayList<Integer> casillasIA;
    private ArrayList<Integer> casillasLibres;
    private ArrayList<Integer> casillasOrigen;
    private ArrayList<Integer> coordenadaOrigenSeleccion;
    private ArrayList<Integer> casillasDisponibles;

    GameEngine(RelativeLayout pantalla, int TAM, Button[][] matrizBotones, Principal principal) {
        this.principal = principal;
        this.pantalla = pantalla;
        this.casillasOcupadas = 0;
        this.puntuacionJugador = 0;
        this.puntuacionIA = 0;
        this.TAM = TAM;
        this.matrizBotones = matrizBotones;
        this.casillasJugador = new ArrayList<>(TAM / 2);
        this.casillasIA = new ArrayList<>(TAM / 2);
        this.casillasLibres = new ArrayList<>(TAM * TAM);
        this.casillasDisponibles = new ArrayList<>();
        this.casillasOrigen = new ArrayList<>();
        this.coordenadaOrigenSeleccion = new ArrayList<>();
        this.jugadorEmpieza = true;
        this.coordenadas = new CoordenadasBusqueda(TAM);
        this.iniciarJuego();

    }

    private void iniciarJuego() {
        for (int tag = 0; tag < TAM * TAM; tag++) {
            casillasLibres.add(tag);
        }
        desactivarBotones(casillasLibres);
        int fila = (TAM - 1) / 2;
        int columna = (TAM) / 2;
        matrizBotones[fila][fila].setText("X");
        casillasJugador.add(((fila * TAM) + fila));
        casillasLibres.remove((Integer) ((fila * TAM) + fila));

        matrizBotones[fila][columna].setText("O");
        casillasIA.add(((fila * TAM) + columna));
        casillasLibres.remove((Integer) ((fila * TAM) + columna));

        matrizBotones[columna][fila].setText("O");
        casillasIA.add(((columna * TAM) + fila));
        casillasLibres.remove((Integer) ((columna * TAM) + fila));

        matrizBotones[columna][columna].setText("X");
        casillasJugador.add(((columna * TAM) + columna));
        casillasLibres.remove((Integer) ((columna * TAM) + columna));

        if (!jugadorEmpieza) {
            turnoIA();
        } else {
            habilitarOpciones(true);
        }
    }

    private void desactivarBotones(ArrayList<Integer> botonesADesactivar) {
        for (int tag : botonesADesactivar) {
            matrizBotones[tag / TAM][tag % TAM].setClickable(false);
            if(casillasLibres.indexOf(tag) > -1){matrizBotones[tag / TAM][tag % TAM].setText("");}
        }
    }

    private void habilitarOpciones(boolean turnoJugador) {
        ArrayList<Integer> casillasContrarias = (turnoJugador) ? casillasIA : casillasJugador;
        ArrayList<Integer> casillasPropias = (turnoJugador) ? casillasJugador : casillasIA;
        desactivarBotones(casillasDisponibles);
        casillasDisponibles.clear();
        casillasOrigen.clear();
        coordenadaOrigenSeleccion.clear();
        for (int tag : casillasPropias) {
            if(tag%TAM != (TAM-1)){
                if (casillasContrarias.indexOf(tag + coordenadas.E()) > -1) {
                    int res = busqueda(tag + coordenadas.E(), coordenadas.E(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(res);
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.E());
                        matrizBotones[res/TAM][res%TAM].setText("*");
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.NE()) > -1) {
                    int res = busqueda(tag + coordenadas.NE(), coordenadas.NE(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(res);
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.NE());
                        matrizBotones[res/TAM][res%TAM].setText("*");
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.SE()) > -1) {
                    int res = busqueda(tag + coordenadas.SE(), coordenadas.SE(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(res);
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.SE());
                        matrizBotones[res/TAM][res%TAM].setText("*");
                    }
                }
            }

            if(tag%TAM != 0){
                if (casillasContrarias.indexOf(tag + coordenadas.W()) > -1) {
                    int res = busqueda(tag + coordenadas.W(), coordenadas.W(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(res);
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.W());
                        matrizBotones[res/TAM][res%TAM].setText("*");
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.NW()) > -1) {
                    int res = busqueda(tag + coordenadas.NW(), coordenadas.NW(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(res);
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.NW());
                        matrizBotones[res/TAM][res%TAM].setText("*");
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.SW()) > -1) {
                    int res = busqueda(tag + coordenadas.SW(), coordenadas.SW(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(res);
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.SW());
                        matrizBotones[res/TAM][res%TAM].setText("*");
                    }
                }
            }

            if (casillasContrarias.indexOf(tag + coordenadas.N()) > -1) {
                int res = busqueda(tag + coordenadas.N(), coordenadas.N(), casillasContrarias);
                if (res > -1) {
                    casillasDisponibles.add(res);
                    casillasOrigen.add(tag);
                    coordenadaOrigenSeleccion.add(coordenadas.N());
                    matrizBotones[res/TAM][res%TAM].setText("*");
                }

            }
            if (casillasContrarias.indexOf(tag + coordenadas.S()) > -1) {
                int res = busqueda(tag + coordenadas.S(), coordenadas.S(), casillasContrarias);
                if (res > -1) {
                    casillasDisponibles.add(res);
                    casillasOrigen.add(tag);
                    coordenadaOrigenSeleccion.add(coordenadas.S());
                    matrizBotones[res/TAM][res%TAM].setText("*");
                }
            }
        }
    }

    private int busqueda(int tag, int coordenada, ArrayList<Integer> casillasContrarias) {
        if(tag > -1 && tag < TAM*TAM){
            int nuevoTag=tag+coordenada;
            if(Math.abs(coordenada)==1){
                if(nuevoTag/TAM == tag/TAM) {
                    if (casillasLibres.indexOf(nuevoTag) > -1) {
                        matrizBotones[nuevoTag / TAM][nuevoTag % TAM].setClickable(true);
                        return nuevoTag;
                    }
                    if (casillasContrarias.indexOf(nuevoTag) > -1) {
                        if (!(nuevoTag % TAM == 0 && (nuevoTag + coordenada) % TAM == (TAM - 1) ||
                                nuevoTag % TAM == (TAM - 1) && (nuevoTag + coordenada) % TAM == 0)) {
                            busqueda(nuevoTag + coordenada, coordenada, casillasContrarias);
                        }
                    }
                }
            }else{
                if (casillasLibres.indexOf(nuevoTag) > -1) {
                    matrizBotones[nuevoTag / TAM][nuevoTag % TAM].setClickable(true);
                    return nuevoTag;
                }
                if (casillasContrarias.indexOf(nuevoTag) > -1) {
                    if (!(nuevoTag % TAM == 0 && (nuevoTag + coordenada) % TAM == (TAM - 1) ||
                            nuevoTag % TAM == (TAM - 1) && (nuevoTag + coordenada) % TAM == 0)) {
                        busqueda(nuevoTag + coordenada, coordenada, casillasContrarias);
                    }
                }
            }

        }
        /*int nuevoTag = tag + coordenada;
        if (casillasLibres.indexOf(nuevoTag) > -1) {
            matrizBotones[nuevoTag/TAM][nuevoTag%TAM].setClickable(true);
            crearToast(Integer.toString(nuevoTag));
            return nuevoTag;
        } else if (casillasContrarias.indexOf(nuevoTag) > -1) {
            if(Math.abs(coordenada) != TAM) {
                if(nuevoTag%TAM != (TAM-1) && nuevoTag%TAM != 0) {
                    busqueda(nuevoTag, coordenada, casillasContrarias);
                }
            }else {
                busqueda(nuevoTag, coordenada, casillasContrarias);
            }
        }
        return -1;
        /*
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
        */
        return -1;

    }

    void jugada(Button botonPulsado) {
        turnoJugador(botonPulsado);
        if (casillasOcupadas < TAM * TAM) {
            do{
                habilitarOpciones(false);
                if(casillasDisponibles.size()>0){
                    turnoIA();
                }else{
                    habilitarOpciones(true);
                }
            }while(casillasDisponibles.size()<0);
        } else {
            Button botonAbandonar = (Button) pantalla.findViewById(R.id.botonAbandonar);
            //crearToast();
            botonAbandonar.setText(R.string.textoReiniciar);
            botonAbandonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View botonPulsado) {
                    reiniciar((Button) botonPulsado);
                }
            });
        }
    }

    private void crearToast(String tag) {
        if (puntuacionJugador > puntuacionIA) {
            Toast.makeText(principal, "You Win!!!" + tag, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(principal, "You Lose!!!" + tag, Toast.LENGTH_LONG).show();
        }

    }

    private void turnoJugador(Button botonPulsado) {
        botonPulsado.setText("X");
        voltearCasillas((Integer)botonPulsado.getTag());
        puntuacionJugador++;
        TextView puntuacionJugadorTV =
                (TextView) pantalla.findViewById(R.id.puntuacionJugador);
        puntuacionJugadorTV.setText(String.format(Locale.getDefault(), "%d", puntuacionJugador));
        botonPulsado.setClickable(false);
        casillasJugador.add((Integer) botonPulsado.getTag());
        casillasLibres.remove(botonPulsado.getTag());
        casillasOcupadas++;
        //TODO definir este metodo correctamente
        //girarColindantes(botonPulsado, "X");
    }

    private void voltearCasillas(Integer tag) {
        String simbolo = (String) matrizBotones[tag / TAM][tag % TAM].getText();
        for(int index = 0; index < casillasDisponibles.size(); index++ ){
            int tagAux = casillasDisponibles.get(index);
            if(tagAux == tag.intValue()){
                int tagOrigen = casillasOrigen.get(index);
                int coordenada = coordenadaOrigenSeleccion.get(index).intValue();
                coordenada*=-1;
                for(int x = tagAux+coordenada; x != tagOrigen ; x+=coordenada){
                    matrizBotones[x/TAM][x%TAM].setText(simbolo);
                    if(simbolo.equals("X")){
                        casillasIA.remove(new Integer(x));
                        casillasJugador.add(new Integer(x));
                    }else{
                        casillasIA.add(new Integer(x));
                        casillasJugador.remove(new Integer(x));
                    }
                }
            }
        }
    }

    private void turnoIA() {

        int index = (int) (Math.random() * casillasDisponibles.size());
        int tag = casillasDisponibles.get(index);
        matrizBotones[tag / TAM][tag % TAM].setText("O");
        voltearCasillas(tag);
        casillasIA.add(tag);
        casillasLibres.remove((Integer) tag);
        puntuacionIA++;
        TextView puntuacionIaTV =
                (TextView) pantalla.findViewById(R.id.puntuacionIA);
        puntuacionIaTV.setText(String.format(Locale.getDefault(), "%d", puntuacionIA));
        casillasOcupadas++;
        habilitarOpciones(true);
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
