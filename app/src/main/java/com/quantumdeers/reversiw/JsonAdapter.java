package com.quantumdeers.reversiw;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by juanfdg on 17/02/17.
 */

public class JsonAdapter extends BaseAdapter {

    JSONArray noticias;
    Context contexto;
    Drawable[] drawables;

    public JsonAdapter(Context contexto, JSONArray noticias,Drawable[] drawables) {
        this.noticias = noticias;
        this.contexto = contexto;
        this.drawables = drawables;
    }

    @Override
    public int getCount() {
        return noticias.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return noticias.getJSONObject(position);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JSONObject noticia;
        try {
            noticia = noticias.getJSONObject(position);
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(contexto);
                convertView = inflater.inflate(R.layout.adapter_news, null);
            }

            TextView tv_title = (TextView) convertView.findViewById(R.id.title);
            tv_title.setText(noticia.getString("Title"));
            TextView tv_head = (TextView) convertView.findViewById(R.id.head);
            tv_head.setText(noticia.getString("Head"));
            TextView tv_body = (TextView) convertView.findViewById(R.id.body);
            tv_body.setText(noticia.getString("Body"));
            ImageView iv_image = (ImageView) convertView.findViewById(R.id.image);
            iv_image.setImageDrawable(drawables[position]);
            return convertView;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}