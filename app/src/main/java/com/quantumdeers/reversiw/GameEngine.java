package com.quantumdeers.reversiw;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

class GameEngine extends AsyncTask<Button, String, Void> {
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
    private ArrayList<origenSeleccion> casillasDisponibles;

    GameEngine(RelativeLayout pantalla, int TAM, Button[][] matrizBotones, Principal principal, Boolean iniciar) {
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
        if(iniciar){
            this.iniciarJuego();
        }


    }
    GameEngine(GameEngine ge) {
        this.principal = ge.principal;
        this.pantalla = ge.pantalla;
        this.casillasOcupadas = ge.casillasOcupadas;
        this.puntuacionJugador = ge.puntuacionJugador;
        this.puntuacionIA = ge.puntuacionIA;
        this.TAM = ge.TAM;
        this.matrizBotones = ge.matrizBotones;
        this.casillasJugador = ge.casillasJugador;
        this.casillasIA = ge.casillasIA;
        this.casillasLibres = ge.casillasLibres;
        this.casillasDisponibles = ge.casillasDisponibles;
        this.casillasOrigen = ge.casillasOrigen;
        this.coordenadaOrigenSeleccion = ge.coordenadaOrigenSeleccion;
        this.jugadorEmpieza = ge.jugadorEmpieza;
        this.coordenadas = ge.coordenadas;
        /*if(iniciar){
            this.iniciarJuego();
        }*/


    }
    protected Void doInBackground(Button... boton) {
        jugada(boton[0]);
        /*cancel(true);
        if(isCancelled()){
            return null;
        }*/
        return null;
        //return null;
    }
    protected void onProgressUpdate(String... progress) {
        int tag = Integer.parseInt(progress[0]);
        int fila = tag/TAM;
        int columna = tag%TAM;
        matrizBotones[fila][columna].setText(progress[1]);
    }
    private void iniciarJuego() {
        for (int tag = 0; tag < TAM * TAM; tag++) {
            casillasLibres.add(tag);
        }
        //desactivarBotones(casillasLibres);
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

    private void desactivarBotones(ArrayList<origenSeleccion> botonesADesactivar) {
        for (origenSeleccion tag : botonesADesactivar) {
            matrizBotones[tag.seleccion / TAM][tag.seleccion % TAM].setClickable(false);
            if(casillasLibres.indexOf(tag.seleccion) > -1){
                publishProgress(Integer.toString(tag.seleccion),"");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //matrizBotones[tag.seleccion / TAM][tag.seleccion % TAM].setText("");
            }
        }
    }

    private void habilitarOpciones(boolean turnoJugador) {
        ArrayList<Integer> casillasContrarias = (turnoJugador) ? casillasIA : casillasJugador;
        ArrayList<Integer> casillasPropias = (turnoJugador) ? casillasJugador : casillasIA;
        desactivarBotones(casillasDisponibles);
        casillasDisponibles.clear();
        casillasOrigen.clear();
        coordenadaOrigenSeleccion.clear();
        int i = 0;
        Log.v("CrearDisponibles", "------------Turno "+ ++i +"------------");
        for (int tag : casillasPropias) {
            Log.v("CrearDisponibles", "soy: "+tag);
            Log.v("CrearDisponibles", "He realizado disponibles: ");
            if(tag%TAM < (TAM-1)){
                if (casillasContrarias.indexOf(tag + coordenadas.E()) > -1) {
                    int res = busqueda(tag + coordenadas.E(), coordenadas.E(), casillasContrarias);
                    if (res > -1) {

                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.E()));
                        /*casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.E());*/
                        publishProgress(Integer.toString(res),"*");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //matrizBotones[res/TAM][res%TAM].setText("*");
                        matrizBotones[res/TAM][res%TAM].setClickable(true);
                        Log.v("CrearDisponibles", "\t Este: "+res);
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.NE()) > -1) {
                    int res = busqueda(tag + coordenadas.NE(), coordenadas.NE(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.NE()));
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.NE());
                        publishProgress(Integer.toString(res),"*");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //matrizBotones[res/TAM][res%TAM].setText("*");
                        matrizBotones[res/TAM][res%TAM].setClickable(true);
                        Log.v("CrearDisponibles", "\t Noreste: "+res);
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.SE()) > -1) {
                    int res = busqueda(tag + coordenadas.SE(), coordenadas.SE(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.SE()));
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.SE());
                        publishProgress(Integer.toString(res),"*");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //matrizBotones[res/TAM][res%TAM].setText("*");
                        matrizBotones[res/TAM][res%TAM].setClickable(true);
                        Log.v("CrearDisponibles", "\t Sureste: "+res);
                    }
                }
            }
            if(tag%TAM != 0){
                if (casillasContrarias.indexOf(tag + coordenadas.W()) > -1) {
                    int res = busqueda(tag + coordenadas.W(), coordenadas.W(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.W()));
                        /*casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.W());*/
                        publishProgress(Integer.toString(res),"*");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //matrizBotones[res/TAM][res%TAM].setText("*");
                        matrizBotones[res/TAM][res%TAM].setClickable(true);
                        Log.v("CrearDisponibles", "\t Oeste: "+res);
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.NW()) > -1) {
                    int res = busqueda(tag + coordenadas.NW(), coordenadas.NW(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.NW()));
                        /*casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.NW());*/
                        publishProgress(Integer.toString(res),"*");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //matrizBotones[res/TAM][res%TAM].setText("*");
                        matrizBotones[res/TAM][res%TAM].setClickable(true);
                        Log.v("CrearDisponibles", "\t Noroeste: "+res);
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.SW()) > -1) {
                    int res = busqueda(tag + coordenadas.SW(), coordenadas.SW(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.SW()));
                        casillasOrigen.add(tag);
                        coordenadaOrigenSeleccion.add(coordenadas.SW());
                        publishProgress(Integer.toString(res),"*");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        //matrizBotones[res/TAM][res%TAM].setText("*");
                        matrizBotones[res/TAM][res%TAM].setClickable(true);
                        Log.v("CrearDisponibles", "\t Suroeste: "+res);
                    }
                }
            }

            if (casillasContrarias.indexOf(tag + coordenadas.N()) > -1) {
                int res = busqueda(tag + coordenadas.N(), coordenadas.N(), casillasContrarias);
                if (res > -1) {
                    casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.N()));
                    casillasOrigen.add(tag);
                    coordenadaOrigenSeleccion.add(coordenadas.N());
                    publishProgress(Integer.toString(res),"*");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //matrizBotones[res/TAM][res%TAM].setText("*");
                    matrizBotones[res/TAM][res%TAM].setClickable(true);
                    Log.v("CrearDisponibles", "\t Norte: "+res);
                }

            }
            if (casillasContrarias.indexOf(tag + coordenadas.S()) > -1) {
                int res = busqueda(tag + coordenadas.S(), coordenadas.S(), casillasContrarias);
                if (res > -1) {
                    casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.S()));
                    casillasOrigen.add(tag);
                    coordenadaOrigenSeleccion.add(coordenadas.S());
                    publishProgress(Integer.toString(res),"*");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //matrizBotones[res/TAM][res%TAM].setText("*");
                    matrizBotones[res/TAM][res%TAM].setClickable(true);
                    Log.v("CrearDisponibles", "\t Sur: "+res);
                }
            }
            Log.v("CrearDisponibles", "-----------------------------");
        }
    }

    private int busqueda(int tag, int coordenada, ArrayList<Integer> casillasContrarias) {
        if(tag > -1 && tag < TAM*TAM){
            int nuevoTag=tag+coordenada;
            if(Math.abs(coordenada)==1){
                if(nuevoTag/TAM == tag/TAM) {
                    if (casillasLibres.indexOf(nuevoTag) > -1) {
                        return nuevoTag;
                    }else if (casillasContrarias.indexOf(nuevoTag) > -1) {
                            busqueda(nuevoTag, coordenada, casillasContrarias);
                    }
                }
            }else{
                if(Math.abs((tag/TAM) - (nuevoTag/TAM)) == 1){
                    if (casillasLibres.indexOf(nuevoTag) > -1) {
                        return nuevoTag;
                    }else if (casillasContrarias.indexOf(nuevoTag) > -1) {
                            busqueda(nuevoTag, coordenada, casillasContrarias);
                    }
                }
            }

        }/* if(Math.abs(coordenada)==7){
                if(nuevoTag/TAM == (tag/TAM)-1 || ((nuevoTag/TAM)-1) == tag/TAM) {
                    if (casillasLibres.indexOf(nuevoTag) > -1) {
                        matrizBotones[nuevoTag / TAM][nuevoTag % TAM].setClickable(true);
                        matrizBotones[nuevoTag / TAM][nuevoTag % TAM].setText("*");
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
                    matrizBotones[nuevoTag / TAM][nuevoTag % TAM].setText("*");
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
        if (casillasIA.size()+casillasJugador.size() < TAM * TAM) {
            do{
                habilitarOpciones(false);
                if(casillasDisponibles.size()>0){
                    turnoIA();
                }
                habilitarOpciones(true);
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

    /*private void crearToast(String tag) {
        if (puntuacionJugador > puntuacionIA) {
            Toast.makeText(principal, "You Win!!!" + tag, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(principal, "You Lose!!!" + tag, Toast.LENGTH_LONG).show();
        }

    }*/

    private void turnoJugador(Button botonPulsado) {
        //botonPulsado.setText("X");
        publishProgress(((Integer) botonPulsado.getTag()).toString(),"X");
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        puntuacionJugador++;
        //TextView puntuacionJugadorTV =
        //        (TextView) pantalla.findViewById(R.id.puntuacionJugador);
        //puntuacionJugadorTV.setText(String.format(Locale.getDefault(), "%d", puntuacionJugador));
        botonPulsado.setClickable(false);
        casillasJugador.add((Integer) botonPulsado.getTag());
        casillasLibres.remove(botonPulsado.getTag());
        casillasOcupadas++;
        voltearCasillas((Integer)botonPulsado.getTag());
        //TODO definir este metodo correctamente
        //girarColindantes(botonPulsado, "X");
    }

    private void voltearCasillas(Integer tag) {
        String simbolo = (String) matrizBotones[tag / TAM][tag % TAM].getText();
        for(int index = 0; index < casillasDisponibles.size(); index++ ){
            origenSeleccion tagAux = casillasDisponibles.get(index);
            if(tagAux.seleccion == tag){
                /*int tagOrigen = casillasOrigen.get(index);
                int coordenada = coordenadaOrigenSeleccion.get(index);*/
                //coordenada*=-1;
                for(int botonOrigen = tagAux.origen + tagAux.coordenada; botonOrigen != tagAux.seleccion ; botonOrigen+=tagAux.coordenada){
                    publishProgress(Integer.toString(botonOrigen),"X");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //matrizBotones[botonOrigen/TAM][botonOrigen%TAM].setText(simbolo);
                    if(simbolo.equals("X")){
                        casillasIA.remove(Integer.valueOf(botonOrigen));
                        casillasJugador.add(botonOrigen);
                    }else{
                        casillasIA.add(botonOrigen);
                        casillasJugador.remove(Integer.valueOf(botonOrigen));
                    }
                }
            }
        }
    }

    private void turnoIA() {

        int index = (int) (Math.random() * casillasDisponibles.size());
        int tag = casillasDisponibles.get(index).seleccion;
        publishProgress(Integer.toString(tag),"O");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //matrizBotones[tag / TAM][tag % TAM].setText("O");

        casillasIA.add(tag);
        casillasLibres.remove((Integer) tag);
        puntuacionIA++;
        /*TextView puntuacionIaTV =
                (TextView) pantalla.findViewById(R.id.puntuacionIA);
        puntuacionIaTV.setText(String.format(Locale.getDefault(), "%d", puntuacionIA));*/
        casillasOcupadas++;
        voltearCasillas(tag);
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
