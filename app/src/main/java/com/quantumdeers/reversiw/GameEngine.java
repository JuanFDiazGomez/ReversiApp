package com.quantumdeers.reversiw;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

class GameEngine {
    private Principal principal;
    private RelativeLayout pantalla;
    private Button[][] matrizBotones;
    private TextView TVPuntuacionJugador;
    private TextView TVPuntuacionIA;
    private static int TAM;
    private boolean turnoJugador;
    private CoordenadasBusqueda coordenadas;
    private ArrayList<Integer> casillasJugador;
    private ArrayList<Integer> casillasIA;
    private ArrayList<Integer> casillasLibres;
    private ArrayList<origenSeleccion> casillasDisponibles;

    GameEngine(RelativeLayout pantalla, int TAM, Button[][] matrizBotones, Principal principal) {
        this.principal = principal;
        this.pantalla = pantalla;
        GameEngine.TAM = TAM;
        this.matrizBotones = matrizBotones;
        this.casillasJugador = new ArrayList<>(TAM / 2);
        this.casillasIA = new ArrayList<>(TAM / 2);
        this.casillasLibres = new ArrayList<>(TAM * TAM);
        this.casillasDisponibles = new ArrayList<>();
        this.turnoJugador = true;
        this.coordenadas = new CoordenadasBusqueda(TAM);
        iniciarJuego();
        if (!turnoJugador) {
            //this.getTareaAsincrona().execute(empiezaIA());
        }
    }

    private void iniciarJuego() {
        for (int tag = 0; tag < TAM * TAM; tag++) {
            casillasLibres.add(tag);
        }
        desactivarBotones(casillasLibres);
        int fila = (TAM - 1) / 2;
        int columna = (TAM) / 2;

        TVPuntuacionIA = (TextView) pantalla.findViewById(R.id.puntuacionIA);
        TVPuntuacionJugador = (TextView) pantalla.findViewById(R.id.puntuacionJugador);

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

        TVPuntuacionIA.setText(String.valueOf(casillasIA.size()));
        TVPuntuacionJugador.setText(String.valueOf(casillasJugador.size()));

        if (turnoJugador) {
            habilitarOpciones(turnoJugador);
        } else {
            habilitarOpciones(!turnoJugador);
        }
    }

    tareaAsincrona getTareaAsincrona() {
        return new tareaAsincrona();
    }

    private class tareaAsincrona extends AsyncTask<Integer, String, Void> {
        protected Void doInBackground(Integer... boton) {
            jugada(boton[0]);
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            int tag = Integer.parseInt(progress[0]);
            int fila = tag / TAM;
            int columna = tag % TAM;
            matrizBotones[fila][columna].setText(progress[1]);
        }

        protected void onPostExecute(Void nada) {
            turnoJugador = !turnoJugador;
            TVPuntuacionIA.setText(String.valueOf(casillasIA.size()));
            TVPuntuacionJugador.setText(String.valueOf(casillasJugador.size()));
            if (casillasIA.size() + casillasJugador.size() == TAM * TAM) {
                Button botonAbandonar = (Button) pantalla.findViewById(R.id.botonAbandonar);
                botonAbandonar.setText(R.string.textoReiniciar);
                botonAbandonar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View botonPulsado) {
                        reiniciar((Button) botonPulsado);
                    }
                });
            }else{
                if(!turnoJugador){
                    int botonIA = (int) (Math.random() * casillasDisponibles.size());
                    getTareaAsincrona().execute(botonIA);
                }
            }
        }




        void jugada(Integer botonPulsado) {

            if (turnoJugador) {
                turnoJugador(botonPulsado);
                habilitarOpciones(false);
            }else {
                if (casillasDisponibles.size() > 0) {
                    turnoIA();
                    habilitarOpciones(true);
                }
            }
        }

        private void desactivarBotones(ArrayList<origenSeleccion> botonesADesactivar) {
            for (origenSeleccion tag : botonesADesactivar) {
                matrizBotones[tag.disponile / TAM][tag.disponile % TAM].setClickable(false);
                if (casillasLibres.indexOf(tag.disponile) > -1) {
                    publishProgress(Integer.toString(tag.disponile), "");
                }
            }
        }

        private void habilitarOpciones(boolean habilitarJugador) {
            ArrayList<Integer> casillasContrarias = (habilitarJugador) ? casillasIA : casillasJugador;
            ArrayList<Integer> casillasPropias = (habilitarJugador) ? casillasJugador : casillasIA;
            casillasDisponibles.clear();
            for (int tag : casillasPropias) {
                if (tag % TAM < (TAM - 1)) {
                    if (casillasContrarias.indexOf(tag + coordenadas.E()) > -1) {
                        int res = busqueda(tag + coordenadas.E(), coordenadas.E(), casillasContrarias);
                        if (res > -1) {
                            casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.E()));
                            publishProgress(Integer.toString(res), "*");
                            matrizBotones[res / TAM][res % TAM].setClickable(true);
                        }
                    }

                    if (casillasContrarias.indexOf(tag + coordenadas.NE()) > -1) {
                        int res = busqueda(tag + coordenadas.NE(), coordenadas.NE(), casillasContrarias);
                        if (res > -1) {
                            casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.NE()));
                            publishProgress(Integer.toString(res), "*");
                            matrizBotones[res / TAM][res % TAM].setClickable(true);
                        }
                    }

                    if (casillasContrarias.indexOf(tag + coordenadas.SE()) > -1) {
                        int res = busqueda(tag + coordenadas.SE(), coordenadas.SE(), casillasContrarias);
                        if (res > -1) {
                            casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.SE()));
                            publishProgress(Integer.toString(res), "*");
                            matrizBotones[res / TAM][res % TAM].setClickable(true);
                        }
                    }
                }
                if (tag % TAM != 0) {
                    if (casillasContrarias.indexOf(tag + coordenadas.W()) > -1) {
                        int res = busqueda(tag + coordenadas.W(), coordenadas.W(), casillasContrarias);
                        if (res > -1) {
                            casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.W()));
                            publishProgress(Integer.toString(res), "*");
                            matrizBotones[res / TAM][res % TAM].setClickable(true);
                        }
                    }

                    if (casillasContrarias.indexOf(tag + coordenadas.NW()) > -1) {
                        int res = busqueda(tag + coordenadas.NW(), coordenadas.NW(), casillasContrarias);
                        if (res > -1) {
                            casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.NW()));
                            publishProgress(Integer.toString(res), "*");
                            matrizBotones[res / TAM][res % TAM].setClickable(true);
                        }
                    }

                    if (casillasContrarias.indexOf(tag + coordenadas.SW()) > -1) {
                        int res = busqueda(tag + coordenadas.SW(), coordenadas.SW(), casillasContrarias);
                        if (res > -1) {
                            casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.SW()));
                            publishProgress(Integer.toString(res), "*");
                            matrizBotones[res / TAM][res % TAM].setClickable(true);
                        }
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.N()) > -1) {
                    int res = busqueda(tag + coordenadas.N(), coordenadas.N(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.N()));
                        publishProgress(Integer.toString(res), "*");
                        matrizBotones[res / TAM][res % TAM].setClickable(true);
                    }

                }
                if (casillasContrarias.indexOf(tag + coordenadas.S()) > -1) {
                    int res = busqueda(tag + coordenadas.S(), coordenadas.S(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.S()));
                        publishProgress(Integer.toString(res), "*");
                        matrizBotones[res / TAM][res % TAM].setClickable(true);
                    }
                }
            }
        }

        private void turnoJugador(Integer botonPulsado) {
            desactivarBotones(casillasDisponibles);
            publishProgress(botonPulsado.toString(), "X");
            matrizBotones[botonPulsado/TAM][botonPulsado%TAM].setClickable(false);
            casillasJugador.add(botonPulsado);
            casillasLibres.remove(botonPulsado);
            voltearCasillas(botonPulsado);
        }

        private void voltearCasillas(Integer tag) {
            String simbolo = (String) matrizBotones[tag / TAM][tag % TAM].getText();
            for (int index = 0; index < casillasDisponibles.size(); index++) {
                origenSeleccion tagAux = casillasDisponibles.get(index);
                if (tagAux.disponile == tag) {
                    for (int botonOrigen = tagAux.origen + tagAux.coordenada; botonOrigen != tagAux.disponile; botonOrigen += tagAux.coordenada) {
                        publishProgress(Integer.toString(botonOrigen), simbolo);
                        if (turnoJugador) {
                            casillasIA.remove(Integer.valueOf(botonOrigen));
                            casillasJugador.add(botonOrigen);
                        } else {
                            casillasIA.add(botonOrigen);
                            casillasJugador.remove(Integer.valueOf(botonOrigen));
                        }
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private void turnoIA() {
            desactivarBotones(casillasDisponibles);
            int index = (int) (Math.random() * casillasDisponibles.size());
            int tag = casillasDisponibles.get(index).disponile;
            publishProgress(Integer.toString(tag), "O");
            casillasIA.add(tag);
            casillasLibres.remove((Integer) tag);
            voltearCasillas(tag);
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
                    botonAbandonar.setText(R.string.boton_abandonar);
                }
            }
        }
    }

    private void habilitarOpciones(boolean habilitarJugador) {
        ArrayList<Integer> casillasContrarias = (habilitarJugador) ? casillasIA : casillasJugador;
        ArrayList<Integer> casillasPropias = (habilitarJugador) ? casillasJugador : casillasIA;
        casillasDisponibles.clear();
        for (int tag : casillasPropias) {
            if (tag % TAM < (TAM - 1)) {
                if (casillasContrarias.indexOf(tag + coordenadas.E()) > -1) {
                    int res = busqueda(tag + coordenadas.E(), coordenadas.E(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.E()));
                        matrizBotones[res / TAM][res % TAM].setText("*");
                        matrizBotones[res / TAM][res % TAM].setClickable(true);
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.NE()) > -1) {
                    int res = busqueda(tag + coordenadas.NE(), coordenadas.NE(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.NE()));
                        matrizBotones[res / TAM][res % TAM].setText("*");
                        matrizBotones[res / TAM][res % TAM].setClickable(true);
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.SE()) > -1) {
                    int res = busqueda(tag + coordenadas.SE(), coordenadas.SE(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.SE()));
                        matrizBotones[res / TAM][res % TAM].setText("*");
                        matrizBotones[res / TAM][res % TAM].setClickable(true);
                    }
                }
            }
            if (tag % TAM != 0) {
                if (casillasContrarias.indexOf(tag + coordenadas.W()) > -1) {
                    int res = busqueda(tag + coordenadas.W(), coordenadas.W(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.W()));
                        matrizBotones[res / TAM][res % TAM].setText("*");
                        matrizBotones[res / TAM][res % TAM].setClickable(true);
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.NW()) > -1) {
                    int res = busqueda(tag + coordenadas.NW(), coordenadas.NW(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.NW()));
                        matrizBotones[res / TAM][res % TAM].setText("*");
                        matrizBotones[res / TAM][res % TAM].setClickable(true);
                    }
                }

                if (casillasContrarias.indexOf(tag + coordenadas.SW()) > -1) {
                    int res = busqueda(tag + coordenadas.SW(), coordenadas.SW(), casillasContrarias);
                    if (res > -1) {
                        casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.SW()));
                        matrizBotones[res / TAM][res % TAM].setText("*");
                        matrizBotones[res / TAM][res % TAM].setClickable(true);
                    }
                }
            }

            if (casillasContrarias.indexOf(tag + coordenadas.N()) > -1) {
                int res = busqueda(tag + coordenadas.N(), coordenadas.N(), casillasContrarias);
                if (res > -1) {
                    casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.N()));
                    matrizBotones[res / TAM][res % TAM].setText("*");
                    matrizBotones[res / TAM][res % TAM].setClickable(true);
                }

            }
            if (casillasContrarias.indexOf(tag + coordenadas.S()) > -1) {
                int res = busqueda(tag + coordenadas.S(), coordenadas.S(), casillasContrarias);
                if (res > -1) {
                    casillasDisponibles.add(new origenSeleccion(tag, res, coordenadas.S()));
                    matrizBotones[res / TAM][res % TAM].setText("*");
                    matrizBotones[res / TAM][res % TAM].setClickable(true);
                }
            }
        }
    }

    private int busqueda(int tag, int coordenada, ArrayList<Integer> casillasContrarias) {
        if (tag > -1 && tag < TAM * TAM) {
            int nuevoTag = tag + coordenada;
            if (Math.abs(coordenada) == 1) {
                if (nuevoTag / TAM == tag / TAM) {
                    if (casillasLibres.indexOf(nuevoTag) > -1) {
                        return nuevoTag;
                    } else if (casillasContrarias.indexOf(nuevoTag) > -1) {
                        return busqueda(nuevoTag, coordenada, casillasContrarias);
                    }else{
                        return -1;
                    }
                }else{
                    return -1;
                }
            } else {
                if (Math.abs((tag / TAM) - (nuevoTag / TAM)) == 1) {
                    if (casillasLibres.indexOf(nuevoTag) > -1) {
                        return nuevoTag;
                    } else if (casillasContrarias.indexOf(nuevoTag) > -1) {
                        return busqueda(nuevoTag, coordenada, casillasContrarias);
                    }else{
                        return -1;
                    }
                }else{
                    return -1;
                }
            }
        }else{
            return -1;
        }
    }

    private void desactivarBotones(ArrayList<Integer> botonesADesactivar) {
        for (Integer tag : botonesADesactivar) {
            matrizBotones[tag / TAM][tag % TAM].setClickable(false);
        }
    }
}
