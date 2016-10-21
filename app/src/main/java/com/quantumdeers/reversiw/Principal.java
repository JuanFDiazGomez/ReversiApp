package com.quantumdeers.reversiw;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    static int tamTablero = 15;
    Button botones[][];
    LinearLayout contenedor_botones[];
    int casillasNoDisponibles = 0;
    int puntuacionJugador = 0;
    int puntuacionIa = 0;
    boolean turnoJugador;
    RelativeLayout pantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Creamos un Layout copia del creado en XML pero con el tablero
        pantalla = creaTableroDinamico(this);

        //ReversiGame game = new ReversiGame(botones);

        // Mostrarmos nuestro nuevo Layout
        setContentView(pantalla);

    }

    private RelativeLayout creaTableroDinamico(Principal principal) {
        // Copiamos nuestro diseño del layout de la pantalla juego
        pantalla = (RelativeLayout) LayoutInflater.from(principal).
                inflate(R.layout.activity_juego, null, false);

        // Cojemos el layout que contendra el tablero de nuestra copia
        LinearLayout tablero = (LinearLayout) pantalla.findViewById(R.id.contenedor_tablero);

        // modificamos el tablero
        modificarTablero(tablero);

        // Devolvemos nuestra nueva pantalla
        return pantalla;
    }

    private void crearContenedorBotones(LinearLayout contenedor) {
        contenedor = new LinearLayout(this);
        contenedor.setOrientation(LinearLayout.HORIZONTAL);
        contenedor.setPadding(0, 0, 0, 0);
    }

    private Button crearBoton(Button boton, Integer tag) {
        // Creamos el boton
        boton = new Button(this);
        boton.setLayoutParams(configurarParamsBotones());
        boton.setTag(tag);
        boton.setTextSize((float) (250 / tamTablero));
        boton.setPadding(0, 0, 0, 0);
        // Intercambiamos el color a cada boton
        if (tag.intValue() % 2 == 0) {
            boton.setBackgroundColor(
                    getResources().getColor(R.color.cuadro_tablero_oscuro)
            );
        } else {
            boton.setBackgroundColor(
                    getResources().getColor(R.color.cuadro_tablero_claro)
            );
        }
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                turnoJugador(v);
                if (casillasNoDisponibles < tamTablero * tamTablero)
                    turnoIA();
                actualizarPuntuaciones();
                comprobarJuegoFinalizado();

            }
        });
        return boton;
    }

    private void comprobarJuegoFinalizado(){
        if (casillasNoDisponibles == tamTablero * tamTablero) {
            Button botonAbandonar = (Button) pantalla.findViewById(R.id.botonAbandonar);
            if (puntuacionJugador > puntuacionIa) {
                botonAbandonar.setText("You WIN! - RESTART");
            } else {
                botonAbandonar.setText("You LOSE! - RESTART");
            }
            botonAbandonar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reiniciar((Button) v);
                }
            });
        }
    }
    private void actualizarPuntuaciones() {
        if (turnoJugador) {
            TextView puntuacionTV =
                    (TextView) pantalla.findViewById(R.id.puntuacionJugador);
            puntuacionTV.setText(Integer.toString(puntuacionJugador));
        } else {
            TextView puntuacionIaTV =
                    (TextView) pantalla.findViewById(R.id.puntuacionIA);
            puntuacionIaTV.setText(Integer.toString(puntuacionIa));
        }


    }

    private void modificarTablero(LinearLayout tablero) {

        botones = new Button[tamTablero][tamTablero];
        contenedor_botones = new LinearLayout[tamTablero];

        for (int fila = 0; fila < tamTablero; fila++) {
            // Creamos la fila de botones
            crearContenedorBotones(contenedor_botones[fila]);
            for (int columna = 0; columna < tamTablero; columna++) {
                Integer tag = new Integer((fila * tamTablero) + columna);
                botones[fila][columna] = crearBoton(botones[fila][columna], tag);
                contenedor_botones[fila].addView(botones[fila][columna]);
            }
            // Configuramos la fila de botones
            contenedor_botones[fila].setLayoutParams(configurarParamsLayouts());

            // Añadimos la fila de botones
            tablero.addView(contenedor_botones[fila]);
        }
    }

    private LinearLayout.LayoutParams configurarParamsBotones() {
        LinearLayout.LayoutParams ParamsBotones = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ParamsBotones.setMargins(0, 0, 0, 0);

        // Le definimos el peso para que todos ocupen el mismo espacio
        ParamsBotones.weight = 1;

        return ParamsBotones;
    }

    private LinearLayout.LayoutParams configurarParamsLayouts() {
        LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        paramsLayout.weight = 1;

        return paramsLayout;
    }

    private void turnoJugador(View v) {
        Button boton = (Button) v;
        Integer tag = (Integer) (boton.getTag());
        boton.setText("X");
        puntuacionJugador++;
        boton.setClickable(false);
        casillasNoDisponibles++;
        volteaColindantes(boton, "X");
    }

    private void turnoIA() {
        int fila;//= (int) Math.round(Math.random()*(tamTablero - 1));
        int columna;// = (int) Math.round(Math.random()*(tamTablero - 1));
        do {
            fila = (int) (Math.random() * tamTablero);
            columna = (int) (Math.random() * tamTablero);
        } while (!botones[fila][columna].isClickable());
        botones[fila][columna].setText("O");
        puntuacionIa++;
        botones[fila][columna].setClickable(false);
        casillasNoDisponibles++;
        volteaColindantes(botones[fila][columna], "O");

    }

    // TODO hacer mas eficiente el bucle, separar las ocasiones en las que la fila es 0 o 5 y columna igual
    private void volteaColindantes(Button boton, String jugador) {
        int tag = (Integer) (boton.getTag());
        int fila = (int) tag / tamTablero;
        int columna = tag - (fila * tamTablero);
        for (int i = -1; i < 2; i++) {
            if (fila + i < tamTablero && fila + i >= 0) {
                for (int j = -1; j < 2; j++) {
                    if (columna + j < tamTablero && columna + j >= 0) {
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
    }

    private void reiniciar(Button botonAbandonar) {
        for (int fila = 0; fila < tamTablero; fila++) {
            for (int columna = 0; columna < tamTablero; columna++) {
                botones[fila][columna].setClickable(true);
                botones[fila][columna].setText("");
                TextView puntuacionJugadorTV =
                        (TextView) pantalla.findViewById(R.id.puntuacionJugador);
                puntuacionJugadorTV.setText("0");
                TextView puntuacionIaTV =
                        (TextView) pantalla.findViewById(R.id.puntuacionIA);
                puntuacionIaTV.setText("0");
                puntuacionIa = 0;
                puntuacionJugador = 0;
                casillasNoDisponibles = 0;
                botonAbandonar.setText("ABANDONAR");
            }
        }
    }
}
