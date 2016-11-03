package com.quantumdeers.reversiw;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

class GameBoardCreator {
    private Principal principal;
    private RelativeLayout pantalla;
    private LinearLayout tablero;
    private final static int TAM = 6;
    private Button[][] matrizBotones;
    private GameEngine gameEngine;

    GameBoardCreator(Principal principal) {
        this.principal = principal;
        this.pantalla = crearPantalla();
        this.gameEngine = new GameEngine(pantalla, TAM, matrizBotones,this.principal);
    }

    RelativeLayout getPantalla() {
        return pantalla;

    }

    private RelativeLayout crearPantalla() {
        pantalla = (RelativeLayout) LayoutInflater.from(principal).
                inflate(R.layout.activity_juego, new LinearLayout(principal), false);
        tablero = (LinearLayout) pantalla.findViewById(R.id.contenedor_tablero);
        modificarTablero();
        return pantalla;
    }

    private void modificarTablero() {
        LinearLayout[] arrayContenedorBotones = new LinearLayout[TAM];
        matrizBotones = new Button[TAM][TAM];
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
        LinearLayout contenedor = new LinearLayout(principal);
        contenedor.setLayoutParams(crearLayoutParams());
        contenedor.setOrientation(LinearLayout.HORIZONTAL);
        contenedor.setPadding(0, 0, 0, 0);
        return contenedor;
    }

    private Button crearBoton(int fila, int columna) {
        Button boton = new Button(principal);
        boton.setLayoutParams(crearLayoutParams());
        boton.setText("");
        boton.setTag((fila * TAM) + columna);
        boton.setTextSize((float) (250 / TAM));
        boton.setPadding(0, 0, 0, 0);
        if (fila % 2 == 0) {
            if(columna % 2 == 0) {
                boton.setBackgroundColor(
                        principal.getResources().getColor(R.color.cuadro_tablero_oscuro));
            }else{
                boton.setBackgroundColor(
                        principal.getResources().getColor(R.color.cuadro_tablero_claro));
            }
        } else {
            if(columna % 2 != 0) {
                boton.setBackgroundColor(
                        principal.getResources().getColor(R.color.cuadro_tablero_oscuro));
            }else{
                boton.setBackgroundColor(
                        principal.getResources().getColor(R.color.cuadro_tablero_claro));
            }
        }

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View botonPulsado) {
                gameEngine.jugada((Button) botonPulsado);
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