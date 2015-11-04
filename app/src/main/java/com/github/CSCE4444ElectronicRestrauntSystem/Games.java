package com.github.CSCE4444ElectronicRestrauntSystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Games extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        Button bTicTacToe = (Button) findViewById(R.id.bTicTacToe);
        Button bSnake = (Button) findViewById(R.id.bSnake);
        Button bRewards = (Button) findViewById(R.id.bRewardsGame);

        bTicTacToe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iTicTacToe = new Intent(Games.this, TicTacToe.class);
                startActivity(iTicTacToe);
            }
        });

        bSnake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iSnake = new Intent(Games.this, Snake.class);
                startActivity(iSnake);
            }
        });

        bRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRewardsGame = new Intent(Games.this, RewardsGame.class);
                startActivity(iRewardsGame);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_games, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
