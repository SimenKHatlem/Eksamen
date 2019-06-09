package com.kandidat24.eksamen;

import androidx.fragment.app.FragmentActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * BarnehageKart tar inn en ArrayList med Barnehage elementer og bruker disse til å plassere markører på et google kart.
 * Mesteparten av koden som lager selve kartet ble generert av Android Studio
 */

public class BarnehageKart extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    ArrayList<Barnehage>  barnehager;  //Liste som holder BarnehageObjekt som brukes til å sette markere på kartet

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barnehage_kart);
        Intent intent = getIntent();
        barnehager = intent.getParcelableArrayListExtra("barnehager");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     *
     * Kjører barnehagelisten gjennom en for loop for å plassere markers på kartet.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        for(Barnehage barnehage: barnehager) {


            mMap.addMarker(new MarkerOptions().position(barnehage.getLatLng()).title(barnehage.navn));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(barnehage.getLatLng()));

        }


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(barnehager.get(0).getLatLng(), 15));
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
