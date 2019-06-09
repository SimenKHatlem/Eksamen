package com.kandidat24.eksamen;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import java.util.ArrayList;


/**
 * Barnehageinfo aktiviteten viser informasjon om en enkelt barnehage. Den får inn nsrID (barnehagens ID) og sender den, context og TextViewene som må fylles ut til Hjelpefunksjoner.volleyBarnehageInfo.
 */

public class BarnehageInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barnehage_info);

        Intent intent = getIntent();

        String nsrId = intent.getStringExtra("nsrId");  //barnehagens nsrId


        ArrayList<View> TWListe = new ArrayList<>(); //Liste med TextView feltene som må fylles ut.
        TWListe.add(findViewById(R.id.barnehageNavn));
        TWListe.add(findViewById(R.id.BHIAlderSvar));
        TWListe.add(findViewById(R.id.BHIKommuneSvar));
        TWListe.add(findViewById(R.id.BHIAdresseSvar));
        TWListe.add(findViewById(R.id.BHIApningstidSvar));
        TWListe.add(findViewById(R.id.BHIAntBarnSvar));
        TWListe.add(findViewById(R.id.BHIPedProfilSvar));
        TWListe.add(findViewById(R.id.BHInettsideLenkeSvar));
        TWListe.add(findViewById(R.id.BHIEpost));
        TWListe.add(findViewById(R.id.BHIRing));

        HjelpeFunksjoner hjelpeFunksjoner = new HjelpeFunksjoner();

        //Sender kontekst, nsrId, listen med TextViews, og to bilder som skal få onClick metoder
        hjelpeFunksjoner.volleyBarnehageInfo(
                BarnehageInfo.this, nsrId, TWListe, (ImageView)findViewById(R.id.mailBilde), (ImageView) findViewById(R.id.ringBilde)
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
