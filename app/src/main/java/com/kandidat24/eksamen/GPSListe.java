package com.kandidat24.eksamen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

/**
 * GPSListe viser en liste over barnehager nær GPS lokasjonen til brukeren. Brukeren kan endre radiusen på søkeområdet, og klikke søk.
 * Brukeren kan også klikke på en knapp nederst på siden for å se elementene vist i listen i et kart.
 * Koden er bygd opp på prinsipper vist i fårelesningsfoiler av Jon Kvisli
 */
public class GPSListe extends AppCompatActivity {

    public final static int MY_REQUEST_LOCATION = 1;
    private LocationManager locationManager;
    private String locationProvider = LocationManager.GPS_PROVIDER;
    private Location myLocation;  //Brukerens lokasjon
    Context context;
    String padding; //hvor mye radiusen skal utvides med
    String lat; //Breddegrad
    String lng; //Lengdegrad
    ListView gpsListe; //ListViewet som lister opp barnehagene

HjelpeFunksjoner hjelpeFunksjoner = new HjelpeFunksjoner();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpsliste);

        context = this;
        locationManager = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        final EditText editPadding = findViewById(R.id.editPadding);
        padding = editPadding.getText().toString();
        gpsListe =  findViewById(R.id.gspListe);

        Button paddingKnapp = findViewById(R.id.paddingKnapp);

        //når den klikkes søker den med den samme lokasjonen, men henter padding fra TextEdit feltet padding
        paddingKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                padding = editPadding.getText().toString();
                hjelpeFunksjoner.volleyGpsListe(context, gpsListe, lat, lng, padding, (Button) findViewById(R.id.gpsKartKnapp));

            }
        });

        if (!locationManager.isProviderEnabled(locationProvider)) {
            Toast.makeText(this, "Aktiver "+locationProvider+" under Location i Settings", Toast.LENGTH_LONG).show();
        } else {
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED)
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_LOCATION);
            else { // Appen har allerede fått tillatelse
                myLocation = locationManager.getLastKnownLocation(locationProvider);
                if (myLocation != null) {
                    lat = Double.toString(myLocation.getLatitude());
                    lng = Double.toString(myLocation.getLongitude());

                    hjelpeFunksjoner.volleyGpsListe(context, gpsListe, lat, lng, padding, (Button) findViewById(R.id.gpsKartKnapp));
                }
                else{
                    Toast.makeText(this, "Kunne ikke finne lokasjon", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_REQUEST_LOCATION) {
// Sjekk om bruker har gitt tillatelsen.
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    myLocation = locationManager.getLastKnownLocation(locationProvider);
                    if (myLocation != null) {
                        lat = Double.toString(myLocation.getLatitude());
                        lng = Double.toString(myLocation.getLongitude());
                        hjelpeFunksjoner.volleyGpsListe(context, gpsListe, lat, lng, padding, (Button) findViewById(R.id.gpsKartKnapp));

                    }
                    else{
                        Toast.makeText(this, "Kunne ikke finne lokasjon", Toast.LENGTH_LONG).show();
                    }
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
            } else // Bruker avviste spørsmål om tillatelse. Kan ikke lese posisjon
                Toast.makeText(this, "Kan ikke vise posisjon uten brukertillatelse.", Toast.LENGTH_LONG).show();
        }
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

