package com.quantumdeers.reversiw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by juanfdg on 22/01/17.
 */
//TODO agregar un campo al boton que sea un enum de tipo vacio jugador o makina
public class MiButton extends Button{
    Paint miPincel;

    public MiButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        crearPincel();
    }

    private void crearPincel() {
        miPincel = new Paint(Paint.ANTI_ALIAS_FLAG);
        miPincel.setStyle(Paint.Style.FILL);
        miPincel.setColor(Color.TRANSPARENT);
        this.getDrawingCache(true);
    }

    public MiButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        crearPincel();
    }

    public MiButton(Context context) {
        super(context);
        crearPincel();
    }

    public Paint getMiPincel(){
        return miPincel;
    }

    public void setMiPincel(Paint miPincel){
        this.miPincel=miPincel;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(miPincel.getColor()!=Color.TRANSPARENT){
            canvas.drawCircle(this.getWidth()/2F, this.getHeight()/2F, this.getHeight()/3F, miPincel);
        }
    }
}
