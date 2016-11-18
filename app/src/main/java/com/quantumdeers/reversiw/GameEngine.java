package com.quantumdeers.reversiw;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

class GameEngine extends BasicGameEngine{
	private Principal principal; // Guardamos en una variable la actividad principal
	private RelativeLayout pantalla; // Este es el layout principal
	private TextView TVPuntuacionJugador; // TextView donde se guarda la puntuacion del J1
	private TextView TVPuntuacionIA; // TextView donde se guarda la puntuacion del J2
	private boolean jugadorEmpieza; // Variable que especifica si el jugador empieza o la IA
	private Button botonAyuda;
	private boolean ayudaVisible;
	private int turnoActual;
	private IAEngine IA;

	GameEngine(RelativeLayout pantalla, int TAM, Button[][] matrizBotones, Principal principal) {
		super(TAM,matrizBotones);
		this.principal = principal;
		this.pantalla = pantalla;
		this.casillasJugador = new ArrayList<>(TAM / 2);
		this.casillasIA = new ArrayList<>(TAM / 2);
		this.casillasLibres = new ArrayList<>(TAM * TAM);
		this.casillasDisponibles = new ArrayList<>();
		this.jugadorEmpieza = true;
		this.ayudaVisible = false;
		this.botonAyuda = (Button) pantalla.findViewById(R.id.botonAyuda);
		iniciarJuego();
		if (turnoJugadorActual == Turnos.IA) {
			this.getTareaAsincrona().execute((int) (Math.random() * casillasDisponibles.size()));
		}
		this.IA = new IAEngine(2, matrizBotones);
	}

	private void iniciarJuego() {
		turnoActual = 0;
		// Definimos de quien es el primer turno
		turnoJugadorActual = (jugadorEmpieza) ? Turnos.JUGADOR : Turnos.IA;
		// Agregamos todas las casillas a nuestra variable que guarda las libres
		for (int tag = 0; tag < TAM * TAM; tag++) {
			casillasLibres.add(tag);
			matrizBotones[tag / TAM][tag % TAM].setClickable(false);
			matrizBotones[tag / TAM][tag % TAM].setText("");
		}

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
		TVPuntuacionIA.setText(String.valueOf(casillasIA.size()));
		TVPuntuacionJugador.setText(String.valueOf(casillasJugador.size()));

		// Habilitamos las casillas disponibles para el jugador que tiene el turno inicial
		habilitarOpciones();
        //IAEngine.jugada(12);
	}

	// Gracias a este metodo podremos crear la jugada
	jugadaAsinkTask getTareaAsincrona() {
		return new jugadaAsinkTask();
	}

	protected class jugadaAsinkTask extends AsyncTask<Integer, String, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			botonAyuda.setClickable(false);
			desactivarBotones(casillasDisponibles);
			ayudaVisible = false;

		}

		@Override
		protected Void doInBackground(Integer... boton) {
			turnoActual++;
			String simbolo = (turnoJugadorActual == Turnos.IA) ? "O" : "X";
			jugada(boton[0]);
			publishProgress(String.valueOf(boton[0]), simbolo);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (String tagBoton : voltearCasillas(boton[0])) {
				publishProgress(tagBoton, simbolo);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			super.onProgressUpdate();
			int tag = Integer.parseInt(values[0]);
			int fila = tag / TAM;
			int columna = tag % TAM;
			matrizBotones[fila][columna].setText(values[1]);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
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
					botonAyuda.setClickable(Boolean.TRUE);
					if (turnoJugadorActual == Turnos.IA) {
						getTareaAsincrona().execute(IA.RecursivabuscarJugada(
								(ArrayList<OrigenSeleccion>) casillasDisponibles.clone(),
								(ArrayList<Integer>) casillasLibres.clone(),
								(ArrayList<Integer>)casillasJugador.clone(),
								(ArrayList<Integer>) casillasIA.clone()));
					}
				} else {
					tostadaResultado();
				}
			}
		}
	}

	private int seleccionIA() {
		int index = (int) (Math.random() * casillasDisponibles.size());
		return casillasDisponibles.get(index).disponile;
	}

	protected void desactivarBotones(ArrayList<OrigenSeleccion> botonesADesactivar) {
		for (OrigenSeleccion tag : botonesADesactivar) {
			matrizBotones[tag.disponile / TAM][tag.disponile % TAM].setClickable(false);
			matrizBotones[tag.disponile / TAM][tag.disponile % TAM].setText("");
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
					reiniciar();
				}
			});
			return true;
		} else {
			return false;
		}
	}

	void mostrarDisponibles() {
		ayudaVisible = !ayudaVisible;
		String simbolo = (ayudaVisible) ? "*" : "";
		for (OrigenSeleccion disponibles : casillasDisponibles) {
			matrizBotones[disponibles.disponile / TAM][disponibles.disponile % TAM].setText(simbolo);
		}
	}

	private void reiniciar() {
		casillasJugador.clear();
		casillasIA.clear();
		casillasLibres.clear();
		botonAyuda.setClickable(Boolean.TRUE);
		ayudaVisible = Boolean.FALSE;
		iniciarJuego();
	}

	private void tostadaResultado() {
		String strResult;
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
