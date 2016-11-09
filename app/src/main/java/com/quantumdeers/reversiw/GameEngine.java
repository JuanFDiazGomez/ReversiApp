package com.quantumdeers.reversiw;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class GameEngine {
    private Principal principal; // Guardamos en una variable la actividad principal
    private RelativeLayout pantalla; // Este es el layout principal
    private Button[][] matrizBotones; // El tablero en si
    private TextView TVPuntuacionJugador; // TextView donde se guarda la puntuacion del J1
    private TextView TVPuntuacionIA; // TextView donde se guarda la puntuacion del J2
    private static int TAM; // Variable que especifica el tamaño del tablero
    private boolean jugadorEmpieza; // Variable que especifica si el jugador empieza o la IA
    private Turnos turnoActual; // Indica el turno actual - True si es J1 - False si es J2
    private CoordenadasBusqueda coordenadas; // Clase que contiene las coordenada de busqueda
    private ArrayList<Integer> casillasJugador; // Casillas en posesion de J1
    private ArrayList<Integer> casillasIA; // Casillas en posesion de J2
    private ArrayList<Integer> casillasLibres; // Casillas sin ocupar
    private ArrayList<OrigenSeleccion> casillasDisponibles; // Casillas disponibles para su seleccion

    GameEngine(RelativeLayout pantalla, int TAM, Button[][] matrizBotones, Principal principal) {
        this.principal = principal;
        this.pantalla = pantalla;
        this.TAM = TAM;
        this.matrizBotones = matrizBotones;
        this.casillasJugador = new ArrayList<>(TAM / 2);
        this.casillasIA = new ArrayList<>(TAM / 2);
        this.casillasLibres = new ArrayList<>(TAM * TAM);
        this.casillasDisponibles = new ArrayList<>();
        this.jugadorEmpieza = true;
        this.coordenadas = new CoordenadasBusqueda(TAM);
        iniciarJuego();
        if (turnoActual == Turnos.IA) {
            this.getTareaAsincrona().execute((int) (Math.random() * casillasDisponibles.size()));
        }
    }

    private void iniciarJuego() {
        // Definimos de quien es el primer turno
        turnoActual = (jugadorEmpieza) ? Turnos.JUGADOR : Turnos.IA;
        // Agregamos todas las casillas a nuestra variable que guarda las libres
        for (int tag = 0; tag < TAM * TAM; tag++) {
            casillasLibres.add(tag);
        }
        // Nos aseguramos que todas las casillas esten desactivadas
        desactivarBotones(casillasLibres);
        // Definimos las variables para inicializar el centro del tablero
        int fila = (TAM - 1) / 2;
        int columna = (TAM) / 2;
        // Asignamos los TextView del layout a nuestras variables
        TVPuntuacionIA = (TextView) pantalla.findViewById(R.id.puntuacionIA);
        TVPuntuacionJugador = (TextView) pantalla.findViewById(R.id.puntuacionJugador);
        // Definimos las 4 casillas centrales
        matrizBotones[fila][fila].setText("X");
        casillasJugador.add(((fila * TAM) + fila));
        casillasLibres.remove((Integer) ((fila * TAM) + fila));

        matrizBotones[columna][columna].setText("X");
        casillasJugador.add(((columna * TAM) + columna));
        casillasLibres.remove((Integer) ((columna * TAM) + columna));

        matrizBotones[fila][columna].setText("O");
        casillasIA.add(((fila * TAM) + columna));
        casillasLibres.remove((Integer) ((fila * TAM) + columna));

        matrizBotones[columna][fila].setText("O");
        casillasIA.add(((columna * TAM) + fila));
        casillasLibres.remove((Integer) ((columna * TAM) + fila));
        // Ponemos la puntuacion iniciar el los TextView de cada jugador
        TVPuntuacionIA.setText(Integer.toString(casillasIA.size()));
        TVPuntuacionJugador.setText(Integer.toString(casillasJugador.size()));

        // Habilitamos las casillas disponibles para el jugador que tiene el turno inicial
        habilitarOpciones();
    }

    // Gracias a este metodo podremos crear la jugada
    public jugadaAsinkTask getTareaAsincrona() {
        return new jugadaAsinkTask();
    }

    /*
    Esta es la clase que hereda de AsynTask para poder ejecutarse en segundo plano las funciones
     */
    class jugadaAsinkTask extends AsyncTask<Integer, String, Void> {
        // Esto se ejecutara en segundo plano
        protected Void doInBackground(Integer... boton) {
            if(turnoActual == Turnos.IA){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
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
            actualizarPuntuaciones();
            if (comprobarJuegoFinalizado()) {
                tostadaResultado();
            } else {
                prepararSiguienteTurno();
                /*
                Si al terminar de preparar el siguiente turno ninguno de los dos puede mover
                el juego habrá finalizado
                 */
                if (casillasDisponibles.size() > 0) {
                    if (turnoActual == Turnos.JUGADOR) {
                        mostrarDisponibles();
                    } else {
                        getTareaAsincrona().execute(seleccionIA());
                    }
                } else {
                    tostadaResultado();
                }
            }
        }

        private void desactivarBotones(ArrayList<OrigenSeleccion> botonesADesactivar) {
            for (OrigenSeleccion tag : botonesADesactivar) {
                matrizBotones[tag.disponile / TAM][tag.disponile % TAM].setClickable(false);
                if (casillasLibres.indexOf(tag.disponile) > -1) {
                    publishProgress(Integer.toString(tag.disponile), "");
                }
            }
        }

        private void jugada(Integer botonPulsado) {
            String simbolo;
            ArrayList<Integer> casillasPropias;
            if (turnoActual == Turnos.JUGADOR) {
                simbolo = "X";
                casillasPropias = casillasJugador;
            } else {
                simbolo = "O";
                casillasPropias = casillasIA;
            }
            desactivarBotones(casillasDisponibles);
            publishProgress(botonPulsado.toString(), simbolo);
            matrizBotones[botonPulsado / TAM][botonPulsado % TAM].setClickable(false);
            casillasPropias.add(botonPulsado);
            casillasLibres.remove(botonPulsado);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            voltearCasillas(botonPulsado, simbolo);
        }

        private void voltearCasillas(Integer seleccion, String simbolo) {
            for (int index = 0; index < casillasDisponibles.size(); index++) {
                OrigenSeleccion os = casillasDisponibles.get(index);
                if (os.disponile == seleccion) {
                    for (int bo = os.disponile - os.coordenada; bo != os.origen; bo -= os.coordenada) {
                        publishProgress(Integer.toString(bo), simbolo);
                        if (turnoActual == Turnos.JUGADOR) {
                            casillasJugador.add(bo);
                            casillasIA.remove(Integer.valueOf(bo));
                        } else {
                            casillasIA.add(bo);
                            casillasJugador.remove(Integer.valueOf(bo));
                        }
                        try {
                            Thread.sleep(1000);
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            casillasIA.add(tag);
            casillasLibres.remove((Integer) tag);
            voltearCasillas(tag, "0");
        }
    }

    private void habilitarOpciones() {
        ArrayList<Integer> casillasContrarias;
        ArrayList<Integer> casillasPropias;
        String simbolo = "";
        if (turnoActual == Turnos.JUGADOR) {
            casillasPropias = casillasJugador;
            casillasContrarias = casillasIA;
            simbolo = "*";
        } else {
            casillasPropias = casillasIA;
            casillasContrarias = casillasJugador;
        }

        casillasDisponibles.clear();
        for (int tag : casillasPropias) {
            if (tag % TAM < (TAM - 1)) {
                if (casillasContrarias.indexOf(tag + coordenadas.E()) > -1) {
                    agregarDisponibles(tag, coordenadas.E(), simbolo, casillasContrarias);
                }
                if (casillasContrarias.indexOf(tag + coordenadas.NE()) > -1) {
                    agregarDisponibles(tag, coordenadas.NE(), simbolo, casillasContrarias);
                }
                if (casillasContrarias.indexOf(tag + coordenadas.SE()) > -1) {
                    agregarDisponibles(tag, coordenadas.SE(), simbolo, casillasContrarias);
                }
            }
            if (tag % TAM != 0) {
                if (casillasContrarias.indexOf(tag + coordenadas.W()) > -1) {
                    agregarDisponibles(tag, coordenadas.W(), simbolo, casillasContrarias);
                }
                if (casillasContrarias.indexOf(tag + coordenadas.NW()) > -1) {
                    agregarDisponibles(tag, coordenadas.NW(), simbolo, casillasContrarias);
                }
                if (casillasContrarias.indexOf(tag + coordenadas.SW()) > -1) {
                    agregarDisponibles(tag, coordenadas.SW(), simbolo, casillasContrarias);
                }
            }
            if (casillasContrarias.indexOf(tag + coordenadas.N()) > -1) {
                agregarDisponibles(tag, coordenadas.N(), simbolo, casillasContrarias);
            }
            if (casillasContrarias.indexOf(tag + coordenadas.S()) > -1) {
                agregarDisponibles(tag, coordenadas.S(), simbolo, casillasContrarias);
            }
        }
    }

    public void agregarDisponibles(int origen, int coordenada, String simbolo, ArrayList<Integer> casillasContrarias) {
        int disponible = busqueda(origen + coordenada, coordenada, casillasContrarias);
        if (disponible > -1) {
            casillasDisponibles.add(new OrigenSeleccion(origen, disponible, coordenada));
            matrizBotones[disponible / TAM][disponible % TAM].setText(simbolo);
            matrizBotones[disponible / TAM][disponible % TAM].setClickable(true);
        }
    }

    private int busqueda(int tag, int coordenada, ArrayList<Integer> casillasContrarias) {
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
        if (casillasLibres.indexOf(Integer.valueOf(nuevoTag)) > -1) {
            return nuevoTag;
        } else if (casillasContrarias.indexOf(nuevoTag) > -1) {
            return busqueda(nuevoTag, coordenada, casillasContrarias);
        } else {
            return -1;
        }
    }

    private int seleccionIA(){
        int index = (int) (Math.random() * casillasDisponibles.size());
        return casillasDisponibles.get(index).disponile;
    }

    private void desactivarBotones(ArrayList<Integer> botonesADesactivar) {
        for (Integer tag : botonesADesactivar) {
            matrizBotones[tag / TAM][tag % TAM].setClickable(false);
            matrizBotones[tag / TAM][tag % TAM].setText("");
        }
    }

    private void actualizarPuntuaciones() {
        TVPuntuacionIA.setText(String.valueOf(casillasIA.size()));
        TVPuntuacionJugador.setText(String.valueOf(casillasJugador.size()));
    }

    private boolean comprobarJuegoFinalizado() {
        if (casillasIA.size() + casillasJugador.size() == TAM * TAM) {
            Button botonAbandonar = (Button) pantalla.findViewById(R.id.botonAbandonar);
            botonAbandonar.setText(R.string.textoReiniciar);
            botonAbandonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View botonPulsado) {
                    reiniciar((Button) botonPulsado);
                }
            });
            return true;
        } else {
            return false;
        }
    }

    private void prepararSiguienteTurno() {
        int vueltas = 0;
        do {
            turnoActual = (turnoActual == Turnos.JUGADOR) ? Turnos.IA : Turnos.JUGADOR;
            habilitarOpciones();
            vueltas++;
        } while (casillasDisponibles.size() < 1 && vueltas != 2);
    }

    private void mostrarDisponibles() {
        for (OrigenSeleccion disponibles : casillasDisponibles) {
            matrizBotones[disponibles.disponile / TAM][disponibles.disponile % TAM].setText("*");
        }
    }

    private void reiniciar(Button botonAbandonar) {
        casillasJugador.clear();
        casillasIA.clear();
        casillasLibres.clear();
        iniciarJuego();
    }

    private void tostadaResultado() {
        String strResult = null;
        if (casillasIA.size() < casillasJugador.size()) {
            strResult = "****Has ganado****";
        } else if (casillasIA.size() > casillasJugador.size()) {
            strResult = "****Has perdido****";
        } else {
            strResult = "****Empate****";
        }
        Toast.makeText(principal, strResult, Toast.LENGTH_LONG).show();
    }
}
