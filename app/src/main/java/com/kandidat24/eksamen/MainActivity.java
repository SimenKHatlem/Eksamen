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

import java.util.ArrayList;
import java.util.Collections;

/**
 * Main Activity er startpunktet for appen. Fremsiden viser en liste over fylkene som brukeren kan velge mellom for å komme til kommunelisten.
 */

public class MainActivity extends AppCompatActivity {

    //URLer
    public static final String fylkerURL = "https://www.barnehagefakta.no/api/Fylker";
    public static final String locationKommuneURL = "https://www.barnehagefakta.no/api/Location/kommune/";
    public static final String kommuneURL = "https://www.barnehagefakta.no/api/Kommune/";
    public static final String barnehageURL = "https://www.barnehagefakta.no/api/Barnehage/";
    public static final String locationRadius = "https://www.barnehagefakta.no/api/Location/radius/";
    public static final String legacyKommuner = "https://data-nxr-fellestjeneste.udir.no/api/legacy/kommuner";
    HjelpeFunksjoner hjelpeFunksjoner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        hjelpeFunksjoner = new HjelpeFunksjoner();

        ArrayList<SorteringsObjekt> sort = new ArrayList<>();
        sort.add(new SorteringsObjekt("bbb", "bbb"));
        sort.add(new SorteringsObjekt("aaa", "aaa"));
        Collections.sort(sort);
        Toast.makeText(this, sort.get(0).getNokkel(), Toast.LENGTH_SHORT).show();

        //volleyFylkesliste tar inn konteksten og ListViewet som skal vise fylkene
        hjelpeFunksjoner.volleyFylkesliste(
                MainActivity.this, (ListView) findViewById(R.id.forsideListe)
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
                    startIntent = new Intent(MainActivity.this, KommuneInfo.class);
                    startIntent.putExtra("KOMMUNE_NUMMER", kommune);
                }
                else{
                    Toast.makeText(this, "Kommune ikke valgt", Toast.LENGTH_LONG);
                }

                break;
            case R.id.instillingerKnapp:
                startIntent = new Intent(MainActivity.this, Instillinger.class);
                MainActivity.this.startActivity(startIntent);
                break;
            case R.id.gpsKnapp:
                startIntent = new Intent(MainActivity.this, GPSListe.class);
                MainActivity.this.startActivity(startIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    }

