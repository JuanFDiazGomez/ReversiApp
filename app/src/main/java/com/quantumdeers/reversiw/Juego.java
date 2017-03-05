package com.quantumdeers.reversiw;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Toast;

public class Juego extends AppCompatActivity {
    public ReversiDB db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameBoardCreator gameBoardCreator = new GameBoardCreator(this);
        setContentView(gameBoardCreator.getPantalla());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    
    public void abandonar_onClick(View view) {
        this.finish();
    }
}
