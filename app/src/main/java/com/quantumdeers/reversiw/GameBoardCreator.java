package com.quantumdeers.reversiw;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

class GameBoardCreator {
    private Juego juego;
    private LinearLayout pantalla;
    private LinearLayout tablero;
    private final static int TAM = 10;
    private MiButton[][] matrizBotones;
    private GameEngine gameEngine;

    GameBoardCreator(Juego juego) {
        this.juego = juego;
        this.pantalla = crearPantalla();
        this.gameEngine = new GameEngine(pantalla, TAM, matrizBotones,this.juego);
    }

    LinearLayout getPantalla() {
        return pantalla;

    }

    private LinearLayout crearPantalla() {
        pantalla = (LinearLayout) LayoutInflater.from(juego).
                inflate(R.layout.activity_juego, new LinearLayout(juego), false);
        tablero = (LinearLayout) pantalla.findViewById(R.id.contenedor_tablero);
        Button botonAyuda = (Button) pantalla.findViewById(R.id.botonAyuda);
        botonAyuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gameEngine.mostrarDisponibles();
            }
        });
        modificarTablero();
        return pantalla;
    }

    private void modificarTablero() {
        LinearLayout[] arrayContenedorBotones = new LinearLayout[TAM];
        matrizBotones = new MiButton[TAM][TAM];
        for (int fila = 0; fila < TAM; fila++) {
            arrayContenedorBotones[fila] = crearContenedorBotones();
            for (int columna = 0; columna < TAM; columna++) {
                matrizBotones[fila][columna] = crearBoton(fila, columna);
                arrayContenedorBotones[fila].addView(matrizBotones[fila][columna]);
            }
            tablero.addView(arrayContenedorBotones[fila]);
        }
    }

    private LinearLayout crearContenedorBotones() {
        LinearLayout contenedor = new LinearLayout(juego);
        contenedor.setLayoutParams(crearLayoutParams());
        contenedor.setOrientation(LinearLayout.HORIZONTAL);
        contenedor.setPadding(0, 0, 0, 0);
        return contenedor;
    }

    private MiButton crearBoton(int fila, int columna) {
        MiButton boton = new MiButton(juego);
        boton.setLayoutParams(crearLayoutParams());
        boton.setClickable(false);
        boton.setText("");
        boton.setTag((fila * TAM) + columna);
        boton.setTextSize((float) (250 / TAM));
        boton.setPadding(0, 0, 0, 0);
        if (fila % 2 == 0) {
            if(columna % 2 == 0) {
                boton.setBackgroundColor(
                        juego.getResources().getColor(R.color.cuadro_tablero_oscuro));
            }else{
                boton.setBackgroundColor(
                        juego.getResources().getColor(R.color.cuadro_tablero_claro));
            }
        } else {
            if(columna % 2 != 0) {
                boton.setBackgroundColor(
                        juego.getResources().getColor(R.color.cuadro_tablero_oscuro));
            }else{
                boton.setBackgroundColor(
                        juego.getResources().getColor(R.color.cuadro_tablero_claro));
            }
        }

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View botonPulsado) {
                gameEngine.getTareaAsincrona().execute((Integer)(botonPulsado).getTag());
            }
        });
        return boton;
    }

    private LinearLayout.LayoutParams crearLayoutParams() {
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(0, 0, 0, 0);
        layoutParams.weight = 1;
        return layoutParams;
    }
}