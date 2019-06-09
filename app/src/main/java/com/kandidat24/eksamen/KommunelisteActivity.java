package com.kandidat24.eksamen;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;


/**
 * KommunelisteActivity tar inn posisjonen fylket var i fylkeslisten og bruker den til å hente kommunenavnene i det fylke og vise dem
 */
public class KommunelisteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kommuneliste);

        Intent intent = getIntent();



        int posisjon = intent.getIntExtra("POSISJON", 0);

        HjelpeFunksjoner hjelpeFunksjoner = new HjelpeFunksjoner();
        //volleyKommuneliste tar inn konteksten, ListViewet som skal vise kommunennavnene og posisjonen fylket det er en del av var
        hjelpeFunksjoner.volleyKommuneliste(
                KommunelisteActivity.this, (ListView) findViewById(R.id.kommuneListe), posisjon
        );
    }

    /**
     * Setter navigation_meny som meny for denne aktiviteten
     * @param menu
     * @return
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("");
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation_meny, menu);
        SharedPreferences sharedBarn = PreferenceManager.getDefaultSharedPreferences(this);
        String kommune = sharedBarn.getString("kommune", "Ikke valgt");
        if(kommune.equals("Ikke valgt")) {
            MenuItem item = menu.findItem(R.id.favKnapp);
            item.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);


    }

    /**
     * Lytter etter klikk på knappene i menyen. favKnapp fungerer ikke.
     * @param item
     * @return
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent startIntent;
        switch (item.getItemId()) {
            case R.id.favKnapp:
                SharedPreferences sharedBarn = PreferenceManager.getDefaultSharedPreferences(this);
                String kommune = sharedBarn.getString("kommune", "Ikke valgt");
                if(!kommune.equals("Ikke valgt")) {
                    Toast.makeText(this, kommune, Toast.LENGTH_LONG);
                    startIntent = new Intent(this, KommuneInfo.class);
                    startIntent.putExtra("KOMMUNE_NUMMER", kommune);
                }
                else{
                    Toast.makeText(this, "Kommune ikke valgt", Toast.LENGTH_LONG);
                }

                break;
            case R.id.instillingerKnapp:
                startIntent = new Intent(this, Instillinger.class);
                this.startActivity(startIntent);
                break;
            case R.id.gpsKnapp:
                startIntent = new Intent(this, GPSListe.class);
                this.startActivity(startIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
