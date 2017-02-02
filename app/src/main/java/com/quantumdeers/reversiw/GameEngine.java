package com.quantumdeers.reversiw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

class GameEngine extends BasicGameEngine{
	private Juego juego; // Guardamos en una variable la actividad juego
	private LinearLayout pantalla; // Este es el layout juego
	private TextView TVPuntuacionJugador; // TextView donde se guarda la puntuacion del J1
	private TextView TVPuntuacionIA; // TextView donde se guarda la puntuacion del J2
	private boolean jugadorEmpieza; // Variable que especifica si el jugador empieza o la IA
	private Button botonAyuda;
	private boolean ayudaVisible;
	private int turnoActual;
	private IAEngine IA;
	private ReversiDB db;
	GameEngine(LinearLayout pantalla, int TAM, MiButton[][] matrizBotones, Juego juego) {
		super(TAM,matrizBotones);
		this.juego = juego;
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
		this.IA = new IAEngine(1, matrizBotones);
		db = new ReversiDB(juego,"ReversiDB",null,1);
	}

	private void iniciarJuego() {
		turnoActual = 0;
		// Definimos de quien es el primer turno
		turnoJugadorActual = (jugadorEmpieza) ? Turnos.JUGADOR : Turnos.IA;
		// Agregamos todas las casillas a nuestra variable que guarda las libres
		for (int tag = 0; tag < TAM * TAM; tag++) {
			casillasLibres.add(tag);
			matrizBotones[tag / TAM][tag % TAM].setClickable(false);
			matrizBotones[tag / TAM][tag % TAM].getMiPincel().setColor(Color.TRANSPARENT);
			matrizBotones[tag / TAM][tag % TAM].invalidate();
		}

		// Definimos las variables para inicializar el centro del tablero
		int fila = (TAM - 1) / 2;
		int columna = (TAM) / 2;
		// Asignamos los TextView del layout a nuestras variables
		TVPuntuacionIA = (TextView) pantalla.findViewById(R.id.puntuacionIA);
		TVPuntuacionJugador = (TextView) pantalla.findViewById(R.id.puntuacionJugador);
		// Definimos las 4 casillas centrales

		matrizBotones[fila][fila].getMiPincel().setColor(juego.getResources().getColor(R.color.ficha_jugador1));
		matrizBotones[fila][fila].invalidate();
		casillasJugador.add(((fila * TAM) + fila));
		casillasLibres.remove((Integer) ((fila * TAM) + fila));
		casillasLibresDisponibles--;

		matrizBotones[columna][columna].getMiPincel().setColor(juego.getResources().getColor(R.color.ficha_jugador1));
		matrizBotones[columna][columna].invalidate();
		casillasJugador.add(((columna * TAM) + columna));
		casillasLibres.remove((Integer) ((columna * TAM) + columna));
		casillasLibresDisponibles--;

		matrizBotones[fila][columna].getMiPincel().setColor(juego.getResources().getColor(R.color.ficha_jugador2));
		matrizBotones[fila][columna].invalidate();
		casillasIA.add(((fila * TAM) + columna));
		casillasLibres.remove((Integer) ((fila * TAM) + columna));
		casillasLibresDisponibles--;

		matrizBotones[columna][fila].getMiPincel().setColor(juego.getResources().getColor(R.color.ficha_jugador2));
		matrizBotones[columna][fila].invalidate();
		casillasIA.add(((columna * TAM) + fila));
		casillasLibres.remove((Integer) ((columna * TAM) + fila));
		casillasLibresDisponibles--;

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
			casillasLibresDisponibles--;

		}

		@Override
		protected Void doInBackground(Integer... boton) {
			turnoActual++;
			String simbolo = (turnoJugadorActual == Turnos.IA) ? "O" : "X";
			jugada(boton[0]);
			//TODO cambiar lo k envia al hilo de la UI
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
			int color = juego.getResources()
					.getColor(
							(turnoJugadorActual == Turnos.JUGADOR) ?
									R.color.ficha_jugador1 : R.color.ficha_jugador2
					);
			int tag = Integer.parseInt(values[0]);
			int fila = tag / TAM;
			int columna = tag % TAM;
			Paint pincel = matrizBotones[fila][columna].getMiPincel();
			pincel.setColor(color);
			matrizBotones[fila][columna].setMiPincel(pincel);
			matrizBotones[fila][columna].invalidate();
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			actualizarPuntuaciones();
			if (comprobarJuegoFinalizado()) {
				tostadaResultado();
				guardarPuntuaciones();
			} else {
				prepararSiguienteTurno();
				/*
				Si al terminar de preparar el siguiente turno ninguno de los dos puede mover
                el juego habrá finalizado
                 */
				if (casillasDisponibles.size() > 0) {
					botonAyuda.setClickable(Boolean.TRUE);
					if (turnoJugadorActual == Turnos.IA) {
						/*getTareaAsincrona().execute(IA.RecursivabuscarJugada(
								(ArrayList<OrigenSeleccion>) casillasDisponibles.clone(),
								(ArrayList<Integer>) casillasLibres.clone(),
								(ArrayList<Integer>)casillasJugador.clone(),
								(ArrayList<Integer>) casillasIA.clone()));*/
						getTareaAsincrona().execute(seleccionIA());
					}
				} else {
					tostadaResultado();
				}
			}
		}
	}

	private void guardarPuntuaciones() {
		ReversiDB reversiDB = new ReversiDB(juego,"ReversiDB",null,1);
		SQLiteDatabase db = reversiDB.getWritableDatabase();
		LayoutInflater inflater = LayoutInflater.from(juego);
		View view = inflater.inflate(R.layout.prompt_dialog_nickname, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(juego);
		builder.setTitle(R.string.input_dialog_nickname);
		final EditText txtNickname = (EditText) view.findViewById(R.id.prompt_nickname);
		builder.setCancelable(true);
		builder.setPositiveButton(R.string.guardarPuntuaciones, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				String nickName = txtNickname.getText().toString();
				ReversiDB reversiDB = new ReversiDB(juego,"ReversiDB",null,1);
				SQLiteDatabase db = reversiDB.getWritableDatabase();
				String sql = "INSERT INTO scores (nick,score,boardSize,date) VALUES ('"
						+nickName+"',"
						+casillasJugador.size()+","
						+TAM*TAM+",'"
						+new java.sql.Date(new Date().getTime())+"')";
				db.execSQL(sql);
				db.close();
			}
		});
		builder.setNegativeButton("No guardar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.cancel();
			}
		});

		builder.setView(view);
		builder.show();


	}

	private int seleccionIA() {
		int index = (int) (Math.random() * casillasDisponibles.size());
		return casillasDisponibles.get(index).disponile;
	}

	protected void desactivarBotones(ArrayList<OrigenSeleccion> botonesADesactivar) {
		for (OrigenSeleccion tag : botonesADesactivar) {
			matrizBotones[tag.disponile / TAM][tag.disponile % TAM].setClickable(false);
			Paint pincel = matrizBotones[tag.disponile / TAM][tag.disponile % TAM].getMiPincel();
			pincel.setColor(Color.TRANSPARENT);
			matrizBotones[tag.disponile / TAM][tag.disponile % TAM].setMiPincel(pincel);
			matrizBotones[tag.disponile / TAM][tag.disponile % TAM].invalidate();
		}
	}

	private void actualizarPuntuaciones() {
		TVPuntuacionIA.setText(String.valueOf(casillasIA.size()));
		TVPuntuacionJugador.setText(String.valueOf(casillasJugador.size()));
	}

	private boolean comprobarJuegoFinalizado() {
		if (casillasLibresDisponibles == 0) {
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
		//AyudaVisible inicialmente esta a false
		ayudaVisible = !ayudaVisible;
		int color = (ayudaVisible) ? juego.getResources().getColor(R.color.ficha_jugador1) : Color.TRANSPARENT;
		for (OrigenSeleccion disponibles : casillasDisponibles) {
			Paint pincel = matrizBotones[disponibles.disponile / TAM][disponibles.disponile % TAM].getMiPincel();
			pincel.setColor(color);
			pincel.setAlpha((ayudaVisible) ? 70 : 0);
			matrizBotones[disponibles.disponile / TAM][disponibles.disponile % TAM].setMiPincel(pincel);
			matrizBotones[disponibles.disponile / TAM][disponibles.disponile % TAM].invalidate();
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
		Toast.makeText(juego, strResult, Toast.LENGTH_LONG).show();
	}
}
