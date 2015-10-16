package com.github.CSCE4444ElectronicRestrauntSystem;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class CustomerMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        Button bPayBill = (Button) findViewById(R.id.bPayBill);
        Button bViewMenu = (Button) findViewById(R.id.bCustomerViewMenu);
        Button bGames = (Button) findViewById(R.id.bGames);
        Button bRewardsClub = (Button) findViewById(R.id.bRewardsClub);
        Button bCallServer = (Button) findViewById(R.id.bCallServer);

        bPayBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iPayBill = new Intent(CustomerMain.this, PayBill.class);
                startActivity(iPayBill);
            }
        });

        bViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iMenu = new Intent(CustomerMain.this, MenuMain.class);
                startActivity(iMenu);
            }
        });

        bGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iGames = new Intent(CustomerMain.this, Games.class);
                startActivity(iGames);
            }
        });

        bRewardsClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iRewards = new Intent(CustomerMain.this, RewardsLogin.class);
                startActivity(iRewards);
            }
        });

        bCallServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iCallServer = new Intent(CustomerMain.this, CallServer.class);
                startActivity(iCallServer);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_main, menu);
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
