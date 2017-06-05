package com.quantumdeers.reversiw;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by juanfdg on 19/02/17.
 */

public class NewsAsyncTask extends AsyncTask {
    private Context context;
    private String cadena = "";
    private JSONArray jsonArray;
    private Drawable[] drawables;
    private ProgressDialog pg;
    private Boolean conexionRealizada = false;
    private final String urlIP="10.0.2.2";

    public NewsAsyncTask(Context context) {

        this.context = context;
        pg = new ProgressDialog(context);
        pg.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pg.setMessage("Cargando noticias");
        pg.show();
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        BufferedReader reader = null;
        try {
            URL url = new URL("http://"+urlIP+"/infodb.php");
            HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            // Le establezco un tiempo de 3 segundos para que
            // en caso de que no haya conexión esta de un error
            // quizas el tiempo es muy corto pero asi te aseguras
            // no dejar el background en uso
            con.setConnectTimeout(3000);
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                cadena += line;
            }

            jsonArray = new JSONArray(cadena);
            drawables = new Drawable[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                url = new URL("http://"+urlIP+"/imagenes/" + jsonArray.getJSONObject(i).getString("image"));
                con = (HttpURLConnection) url
                        .openConnection();
                drawables[i] = Drawable.createFromStream(con.getInputStream(), "src name");
            }
            conexionRealizada = true;
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        if(conexionRealizada) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.prompt_news, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            ListView lv_news = (ListView) view.findViewById(R.id.newslist);
            lv_news.setAdapter(new JsonAdapter(context, jsonArray, drawables));
            builder.setCancelable(true);
            builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            builder.setView(view);
            builder.show();
            pg.cancel();
        }else{
            pg.cancel();
        }

    }
}
