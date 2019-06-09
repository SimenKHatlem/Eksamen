package com.kandidat24.eksamen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


public class HjelpeFunksjoner {


    /**
     * volleyFylkesliste tar inn kontekst fra MainActivity og et ListView. Den bruker Volley til å hente et JSONObject som den går igjennom og henter ut fylkesnavnene fra en database, sorterer dem på fylkesnavn og legger dem i ListViewet
     * Noe kode hentet fra https://developer.android.com/training/volley/simple
     * @param kontekst Kontekst fra MainActivity
     * @param lw ListView i MainActivity som skal ta imot fylkesnavnene
     */

    public void volleyFylkesliste(final Context kontekst, final ListView lw){
        final ArrayList<SorteringsObjekt> sortObjListe = new ArrayList();  //holder navnene på fylken
        final ArrayList<String> fylkesnavnSortert = new ArrayList(); //Holder navnet på alle fylkene sortert

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(kontekst);



// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MainActivity.fylkerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject JSONSvar = new JSONObject(response);  //konverterer responsen fra databasen til et JSONObject
                            JSONArray svarArray = JSONSvar.getJSONArray("fylker");

                            //Går igjennom alle fylkene og legger dem sortObjListe
                            for (int i = 0; i < svarArray.length(); i++) {

                                JSONObject fylkesobjekt = svarArray.getJSONObject(i);

                                 sortObjListe.add(new SorteringsObjekt(fylkesobjekt.getString("fylkesnavn"), ""));



                            }

                            Collections.sort(sortObjListe);//sorterer listen på kommunenavn

                            //putter de sorterte verdiene i fylkesnavnSortert
                            for(SorteringsObjekt sortObj: sortObjListe){
                                fylkesnavnSortert.add(sortObj.getNokkel());
                            }


                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(kontekst,android.R.layout.simple_list_item_1,fylkesnavnSortert); //putter elementene i sortObjListe i standard layout simple_list_item_1
                            lw.setAdapter(arrayAdapter);

                            //Setter onItemClickListener som åpner KommunelisteActivity og sender med posisjonen hvor den ble klikket
                            lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent startIntent = new Intent(kontekst, KommunelisteActivity.class);
                                    startIntent.putExtra("POSISJON", position);
                                    kontekst.startActivity(startIntent);
                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(kontekst, e.toString() + " JSONException", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(kontekst, error.toString(), Toast.LENGTH_LONG).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    /**
     * volleyKommuneliste tar inn kontekst fra KommuneListeActivity og et ListView. Den bruker Volley til å hente et JSONObject som den går igjennom og henter ut kommunenavnene fra en database og legger dem sortert i ListViewet
     * Noe kode hentet fra https://developer.android.com/training/volley/simple
     * @param kontekst  Kontekst fra KommuneListeActivity
     * @param lw ListView i KommuneListeActivity som skal holde kommunenavnene
     * @param posisjon Posisjonen elementet som ble valgt i MainActivity var
     */
    public void volleyKommuneliste(final Context kontekst, final ListView lw, final int posisjon){
        final ArrayList<SorteringsObjekt> sortObjListe = new ArrayList();  //lager en liste med usorterte kommunenavn og kommunenummer
        final ArrayList<String> kommunesnavn = new ArrayList();  //liste med sorterte kommunenavn

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(kontekst);


// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MainActivity.fylkerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject JSONSvar = new JSONObject(response);  //konverterer responsen fra databasen til et JSONObject
                            JSONArray svarArray = JSONSvar.getJSONArray("fylker");  //henter ut JSONArray med fylker
                            JSONObject kommuneObjekt = svarArray.getJSONObject(posisjon);  //henter JSONObjectet av fylket som ble klikket
                            JSONArray kommuneListe = kommuneObjekt.getJSONArray("kommuner"); //henter listen med kommuner fra fylket

                            //Går igjennom kommuneListe og legger kommunenavnet og kommunenummeret i sortObjListe
                            for (int i = 0; i < kommuneListe.length(); i++) {

                                JSONObject fylkesobjekt = kommuneListe.getJSONObject(i);
                                sortObjListe.add(new SorteringsObjekt(fylkesobjekt.getString("kommunenavn"), fylkesobjekt.getString("kommunenummer")));



                            }


                            Collections.sort(sortObjListe);//sorterer listen på kommunenavn

                            //putter de sorterte verdiene i kommunenavn

                            for(SorteringsObjekt sortObj: sortObjListe){
                                kommunesnavn.add(sortObj.getNokkel());

                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(kontekst,android.R.layout.simple_list_item_1,kommunesnavn); //putter elementene i kommunenavn i standard layout simple_list_item_1
                            lw.setAdapter(arrayAdapter);

                            //Setter onItemClickListener som åpner KommuneInfo og sender med kommunenummeret til kommunen som ble klikket
                            lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                                    Intent startIntent = new Intent(kontekst, KommuneInfo.class);
                                    startIntent.putExtra("KOMMUNE_NUMMER", sortObjListe.get( position).getVerdi());
                                    kontekst.startActivity(startIntent);


                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(kontekst, e.toString() + " JSONException", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(kontekst, error.toString(), Toast.LENGTH_LONG).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    /**
     * volleyKommuneliste tar inn kontekst fra KommuneInfo, et RecyclerView, et TextView og et kommuneNummer.
     * Den begynner med å fylle bildeListe og overskriftListe med hardkodede resurser. Den henter så data fra databasen med hjelp av kommuneNummer og legger det i tekstListe.
     * Data fra ArrayListene, kontekst og kommuneNummer puttes i en RecyclerViewAdapter som så settes som adapter for rw.
     * Noe kode hentet fra https://developer.android.com/training/volley/simple og https://developer.android.com/guide/topics/ui/layout/recyclerview
     * @param kontekst  Kontekst fra KommuneInfo
     * @param rw RecyclerView i KommuneInfo som skal holde informsjonen om kommunen
     * @param tw TextView som skal vise kommunenavnet som overskrift på siden
     * @param kommuneNummer kommunenummeret til kommunen valgt i KommunelisteActivity
     */
    public void volleyKommuneInfo(final Context kontekst, final RecyclerView rw, final TextView tw, final String kommuneNummer){

         final ArrayList<Integer> bildeListe = new ArrayList<>(); //Liste med bildene som skal vises
         final ArrayList<String> overskiftListe = new ArrayList<>(); //Liste med overskriftene som skal vises
         final ArrayList<String> tekstListe = new ArrayList<>(); //Liste med teksten som skal vises

        //Legger til bildene
        bildeListe.add(R.drawable.liste);
        bildeListe.add(R.drawable.antallbarn);
        bildeListe.add(R.drawable.antallperansatt);
        bildeListe.add(R.drawable.lekeogopphold);
        bildeListe.add(R.drawable.antalllerer);
        bildeListe.add(R.drawable.pedagognormen);
        bildeListe.add(R.drawable.link);


        //Legger til overskriftene
        overskiftListe.add("");
        overskiftListe.add(kontekst.getResources().getString(R.string.antallBarniBH));
        overskiftListe.add(kontekst.getResources().getString(R.string.antallBarnPAnsatt));
        overskiftListe.add(kontekst.getResources().getString(R.string.arealPBarn));
        overskiftListe.add(kontekst.getResources().getString(R.string.andelBHLererutdanning));
        overskiftListe.add(kontekst.getResources().getString(R.string.andelPedNorm));
        overskiftListe.add(kontekst.getResources().getString(R.string.komLenke));



        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(kontekst);



// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MainActivity.kommuneURL + kommuneNummer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject JSONSvar = new JSONObject(response);
                            tw.setText(JSONSvar.getString("navn"));//setter kommunens navn som tekst til tw

                            String kommuneNavn = JSONSvar.getJSONObject("kommune").getString("kommunenavn");  //navnet på kommunen

                            JSONObject indikatorData = JSONSvar.getJSONObject("indikatorDataKommune");//alle indikatorData for kommunen

                            overskiftListe.set(2, overskiftListe.get(2) + " " + kommuneNavn);

                            //legger elementer fra databasen i tekstListe
                            tekstListe.add("Klikk her for å se liste over barnehager i " + kommuneNavn);
                            if(!sjekkNull(indikatorData.getString("antallBarn"))){
                                tekstListe.add(indikatorData.getString("antallBarn"));
                            }
                            else{
                                tekstListe.add("ikke satt");
                            }

                            if(!sjekkNull(indikatorData.getString("antallBarnPerAnsatt"))) {
                                tekstListe.add(indikatorData.getString("antallBarnPerAnsatt"));
                            }
                            else{
                                tekstListe.add("ikke satt");
                            }

                            if(!sjekkNull(indikatorData.getString("lekeOgOppholdsarealPerBarn"))){
                            tekstListe.add(indikatorData.getString("lekeOgOppholdsarealPerBarn"));
                            }
                            else{
                                tekstListe.add("ikke satt");
                            }

                            if(!sjekkNull(indikatorData.getString("andelAnsatteBarnehagelarer"))){
                            tekstListe.add(indikatorData.getString("andelAnsatteBarnehagelarer") + "%");
                            }
                            else{
                                tekstListe.add("ikke satt");
                            }

                            if(!sjekkNull(indikatorData.getString("andelBarnehagerSomOppfyllerPedagognormen"))){
                            tekstListe.add(indikatorData.getString("andelBarnehagerSomOppfyllerPedagognormen") + "%");
                            }
                            else{
                                tekstListe.add("ikke satt");
                            }

                            if(!sjekkNull(JSONSvar.getString("urlTilSoknadOmBarnehageplass"))){
                            tekstListe.add(JSONSvar.getString("urlTilSoknadOmBarnehageplass"));
                            }
                            else{
                                tekstListe.add("ikke satt");
                            }

                            //Sender kontekst, ArrayListene og kommuneNummeret til RecyclerViewAdapter og setter den for rw.
                            RecyclerViewAdapter adapter = new RecyclerViewAdapter(kontekst, bildeListe, overskiftListe, tekstListe, kommuneNummer);
                            rw.setAdapter(adapter);
                            rw.setLayoutManager(new LinearLayoutManager(kontekst));


                        } catch (JSONException e) {
                            Toast.makeText(kontekst, e.toString() + " JSONException", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }






                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(kontekst, error.toString(), Toast.LENGTH_LONG).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    /**
     * volleyBarnehageliste tar inn kontekst fra BarnehagelisteActivity, et ListView og kommuneNummer. Den bruker Volley til å hente et JSONObject som den går igjennom og henter ut barnehagenavnene fra en database ved hjelp av kommuneNummer og legger dem i ListViewet
     * Noe kode hentet fra https://developer.android.com/training/volley/simple
     * @param kontekst  Kontekst fra barnehagelisteActivity
     * @param lw ListView i volleyBarnehageliste som skal holde barnehagenavnene
     * @param kommuneNummer kommunenummeret for kommunen valgt
     */

    public void volleyBarnehageliste(final Context kontekst, final ListView lw, String kommuneNummer, final Button visIKart){
        final ArrayList<SorteringsObjekt> sortObjListe = new ArrayList();  //Liste med SorteringsObjekt som skal sorteres før de legges i barnehageNavn og nsrId
        final ArrayList<String> barnehageNavn = new ArrayList();  //Liste med sorterte barnehagenavnene

        final ArrayList<Barnehage> barnehager = new ArrayList<>();

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(kontekst);



// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MainActivity.locationKommuneURL + kommuneNummer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray JSONSvarArray = new JSONArray(response); //konverterer svaret fra databasen til en JSONArray

                            //går igjennom JSONSvarArray og sjekker om bruker har valgt en alder på barnet, hvis ja, legger den kun til barnehager som tilbyr tjenester til det barnet i listen
                            for (int i = 0; i < JSONSvarArray.length(); i++) {

                                JSONObject barnehageObjekt = JSONSvarArray.getJSONObject(i);  //JSONObject som holder informasjonen om barnehagen
                                String navn = barnehageObjekt.getString("navn");
                                LatLng latLng = new LatLng(barnehageObjekt.getJSONArray("koordinatLatLng").getDouble(0),barnehageObjekt.getJSONArray("koordinatLatLng").getDouble(1));
                                barnehager.add(new Barnehage(latLng, navn ));
                                String alder = barnehageObjekt.getString("alder");

                                SharedPreferences sharedBarn = PreferenceManager.getDefaultSharedPreferences(kontekst);
                                String barneAlder = sharedBarn.getString("alder", "Ikke valgt");


                                if (barneAlder.equals("Ikke valgt")) {
                                    sortObjListe.add(new SorteringsObjekt(navn, barnehageObjekt.getString("nsrId")));

                                } else if (!alder.equals("null") && !alder.equals("")) {
                                    if (barnehageObjekt.getString("alder").length() == 5) {
                                        if (Character.getNumericValue(alder.charAt(0)) <= Integer.parseInt(barneAlder) && Character.getNumericValue(alder.charAt(4)) >= Integer.parseInt(barneAlder)) {
                                            sortObjListe.add(new SorteringsObjekt(navn, barnehageObjekt.getString("nsrId")));

                                        }
                                    } else {
                                        if (Integer.parseInt(alder) == Integer.parseInt(barneAlder)) {
                                            sortObjListe.add(new SorteringsObjekt(navn, barnehageObjekt.getString("nsrId")));

                                        }
                                    }

                                }
                            }


                            Collections.sort(sortObjListe);//sorterer listen på barnehagenavn

                            //putter de sorterte verdiene i barnehageNavn
                            for(SorteringsObjekt sortObj: sortObjListe) {
                                barnehageNavn.add(sortObj.getNokkel());

                            }

                            //Sender kontekst, android.R.layout.simple_list_item_1 og barnehageNavn til arrayAdapter og setter den for lw.
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(kontekst,android.R.layout.simple_list_item_1,barnehageNavn);
                            lw.setAdapter(arrayAdapter);

                            //Hvis et element klikkes åpner den en BarnehageInfo aktivity og sender med nsrId
                            lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent startIntent = new Intent(kontekst, BarnehageInfo.class);
                                    startIntent.putExtra("nsrId", sortObjListe.get(position).getVerdi());
                                    kontekst.startActivity(startIntent);
                                }
                            });

                            //Hvis knappen klikkes så starter den visIKart som viser kommunens barnehager i et kart.
                            visIKart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    visIKart(kontekst, barnehager);

                                }
                            });

                        } catch (JSONException e) {
                            Toast.makeText(kontekst, e.toString() + " JSONException", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }





                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(kontekst, error.toString(), Toast.LENGTH_LONG).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    /**
     * volleyBarnehageInfo tar inn kontekst fra BarnehageInfo, en Arraylist med ListView, og to ImageView.
     * Den bruker volley for å hente data fra en database med bruk av barnehageNummer. Den går så igjennom listen med TextViews og puttet informasjon fra spørringen i TextView elementene
     * Noe kode hentet fra https://developer.android.com/training/volley/simple
     * @param kontekst  Kontekst fra BarnehageInfo
     * @param TWListe ListView i volleyBarnehageliste som skal holde barnehagenavnene
     * @param epostBilde kommunenummeret for kommunen valgt,
     * @param ringBilde kommunenummeret for kommunen valgt
     */

    public void volleyBarnehageInfo(final Context kontekst, String barnehageNummer, final ArrayList<View> TWListe, final ImageView epostBilde, final ImageView ringBilde){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(kontekst);



// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MainActivity.barnehageURL + barnehageNummer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int i= 0;

                        TextView TW = (TextView) TWListe.get(i++);
                        try {

                            final JSONObject JSONSvar = new JSONObject(response); //Omgjør responsen til et JSONObject

                            //setter overskriften til barnehagens navn
                            TW.setText(JSONSvar.getString("navn"));



                            JSONObject indikatorDataBarnehage = JSONSvar.getJSONObject("indikatorDataBarnehage"); //Holder indikatordataene til den valgte barnehagen

                            //Setter alder i TextView
                            TW = (TextView) TWListe.get(i++);
                            TW.setText(JSONSvar.getString("alder"));

                            //Setter kommunenavn i TextView
                            TW = (TextView) TWListe.get(i++);
                            TW.setText(JSONSvar.getJSONObject("kommune").getString("kommunenavn"));

                            //Setter den fulle adressen i TextView
                            TW = (TextView) TWListe.get(i++);
                            JSONObject besoksAdresse = JSONSvar.getJSONObject("kontaktinformasjon").getJSONObject("besoksAdresse");
                            String adresse = besoksAdresse.getString("adresselinje");
                            adresse+= ", " + besoksAdresse.getString("postnr");
                            adresse+= " " + besoksAdresse.getString("poststed");
                            TW.setText(adresse);

                            //Setter sammen åpningstid og puttder den i TextView
                            TW = (TextView) TWListe.get(i++);
                            String apningstid = JSONSvar.getString("apningstidFra");
                            apningstid += " - " + JSONSvar.getString("apningstidTil");
                            TW.setText(apningstid);
                            TW = (TextView) TWListe.get(i++);
                            TW.setText(indikatorDataBarnehage.getString("antallBarn"));

                            //Stripper om barnehagen har en pedagogisk profil, hvis ja stripper av [ og ", og setter det i TextView
                            TW = (TextView) TWListe.get(i++);
                            if(sjekkNull(JSONSvar.getString("pedagogiskProfil"))) {
                                TW.setText("Ikke satt");
                            }
                            else {
                                TW.setText(JSONSvar.getString("pedagogiskProfil").replaceAll("\\[", "").replaceAll("\"", "").replaceAll("\\]", ""));
                            }
                            //Setter URLen til søknad om barnehageplass i Text
                            TW = (TextView) TWListe.get(i++);
                            TW.setText(JSONSvar.getString("urlTilSoknadOmBarnehageplass"));
                            TW.setPaintFlags(TW.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                            //setter en lytter på lenken, hvis den klikkes åpnes lenken i en nettleser
                            TW.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Intent browserIntent = null;
                                    try {
                                        browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(JSONSvar.getString("urlTilSoknadOmBarnehageplass")));
                                        kontekst.startActivity(browserIntent);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            });

                            //Henter epostadresse og legger den til i textview
                            final String epostadresse = JSONSvar.getJSONObject("kontaktinformasjon").getString("epost");
                            TW = (TextView) TWListe.get(i++);
                            TW.setText(TW.getText() + " " + epostadresse);

                            //setter en lytter, og åpner mail program hvis teksten klikkes
                            TW.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendMail(epostadresse, kontekst);


                                }
                            });

                            //Henter telefunnummeret og legger den til i textview
                            final String telefon = JSONSvar.getJSONObject("kontaktinformasjon").getString("telefon");
                            TW = (TextView) TWListe.get(i++);
                            TW.setText(TW.getText() + " " + telefon);

                            //Setter en lytter, og ringer nummeret hvis teksten klikkes
                            TW.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ring(telefon, kontekst); //Ring lar brukeren velge om de vil ringe med en gang eller ikke.

                                }
                            });




                        epostBilde.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                sendMail(epostadresse, kontekst);


                            }
                        });

                            ringBilde.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    ring(telefon, kontekst); //Ring lar brukeren velge om de vil ringe med en gang eller ikke.

                                }
                            });
                        }
                        catch (JSONException e) {
                            Toast.makeText(kontekst, e.toString() + " JSONException", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(kontekst, error.toString(), Toast.LENGTH_LONG).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    /**
     * Starter opp en mail activity hvor den prøver sende en mail til epostadressen gitt
     * Kode hentet fra https://www.javatpoint.com/how-to-send-email-in-android-using-intent
     * @param adresse   adressen eposten skal sendes til
     * @param kontekst   kontekst til aktiviteten som startet metoden
     */
    public void sendMail(String adresse, Context kontekst){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ adresse});

//need this to prompts email client only
        email.setType("message/rfc822");

        kontekst.startActivity(Intent.createChooser(email, "Send mail med:"));
    }

    /**
     * Ring åpener ringe sjermen med nummeret taste og lar brukeren velge om de vil ringe med en gang eller ikke.
     * Kode hentet fra https://stackoverflow.com/questions/4275678/how-to-make-a-phone-call-using-intent-in-android
     * @param telefon telefonnummeret
     * @param kontekst konteksten til aktiviteten som kaller ring
     */
    public void ring(String telefon, Context kontekst){
        if(telefon.equals("")){
            Toast.makeText(kontekst, "Barnehagen har ikke listet et nummer", Toast.LENGTH_LONG).show();
        }
        else {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+47" + telefon.replaceAll(" ", "")));
            kontekst.startActivity(intent);
        }
    }


    /**
     * Tar inn kontekst fra GPSListe aktiviteten, et ListView som skal vise barenhagene i nærheten med data hentet fra en database med hjelp av Volley
     * 3 stringer som holder lengde-, breddegrad og radius, og en knapp for å vise barnehagene i et kart.
     * Noe kode hentet fra https://developer.android.com/training/volley/simple
     * @param kontekst  kontekst fra GPSListe
     * @param lw    Et list view som tar inn navnne på barnehagene i nærheten
     * @param lat   Lengdegraden brukeren er på
     * @param lng   Breddegraden brukeren er på
     * @param radius    Radiusen for søkeområdet
     * @param visIKart  Knapp som tar deg til BarnehageKart og viser barnehagene i nærheten gitt satt radius
     */

    public void volleyGpsListe(final Context kontekst, final ListView lw, final String lat, final String lng, final String radius, final Button visIKart){
        final ArrayList<SorteringsObjekt> sortObjListe = new ArrayList<>();  //Lagrer barnehagens navn så de kan vises i ListView
        final ArrayList<String> BHNavn = new ArrayList<>();  //Lagrer barnehagens navn så de kan vises i ListView
        final ArrayList<String> nsrId = new ArrayList<>(); //lagrer nsrID aå de kan brukes når brukeren sendes videre til BarnehageInfo
        final ArrayList<Barnehage> barnehager = new ArrayList<>(); //lagrer Barnehage element av alle barnehagene så de kan vises i BarnehageKart

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(kontekst);




// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MainActivity.locationRadius + lat + "/" + lng + "/" + radius,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray JSONArraySvar = new JSONArray(response); //Omgjør responsen til et JSONArray

                            //Går igjennom JSONArraySvar og putter informasjonen i barnehager, BHNavn og nsrId.
                            for (int i = 0; i < JSONArraySvar.length(); i++) {


                                JSONObject BHObjekt = JSONArraySvar.getJSONObject(i);  //Holder dataene for en enkelt barnehage
                                String navn = BHObjekt.getString("navn");
                                LatLng latLng = new LatLng(BHObjekt.getJSONArray("koordinatLatLng").getDouble(0),BHObjekt.getJSONArray("koordinatLatLng").getDouble(1));

                                barnehager.add(new Barnehage(latLng, navn ));
                                sortObjListe.add(new SorteringsObjekt(navn, BHObjekt.getString("nsrId")));

                            }

                            for(SorteringsObjekt sortObj: sortObjListe){
                                BHNavn.add(sortObj.getNokkel());
                                nsrId.add(sortObj.getVerdi());
                            }

                            //Ved klikk åpnes BarnehageKart og viser alle barnehagene i barnehager
                            visIKart.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    visIKart(kontekst, barnehager);


                                }
                            });


                            //Henter informasjon fra BHNavn og putter den i lw
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(kontekst,android.R.layout.simple_list_item_1,BHNavn);
                            lw.setAdapter(arrayAdapter);

                            //Hvis klikket åpner den BarnehageInfo for barnehagenavnet klikket ved hjelp av nsrId
                            lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Intent startIntent = new Intent(kontekst, BarnehageInfo.class);
                                    startIntent.putExtra("nsrId", nsrId.get( position));
                                    kontekst.startActivity(startIntent);

                                }
                            });
                        } catch (JSONException e) {
                            Toast.makeText(kontekst, e.toString() + " JSONException", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(kontekst, error.toString(), Toast.LENGTH_LONG).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    /**
     * Tar inn kontekst fra aktiviteten som kjører den og en ArrayList med Barnehage objekter og sender ArrayListen til BarnehageKart
     * @param kontekst
     * @param barnehager
     */

    public void visIKart(Context kontekst, ArrayList<Barnehage> barnehager){
        Intent startIntent = new Intent(kontekst, BarnehageKart.class);
        startIntent.putParcelableArrayListExtra("barnehager",  barnehager);
        kontekst.startActivity(startIntent);
    }

    /**
     * Tar inn kontekst fra Instillinger og en ListPreferance som skal holde kommunenavnene brukeren kan velge imellom. Sorterer på kommunenavn før de legges inn i ListPreferance
     * Noe kode hentet fra https://developer.android.com/training/volley/simple
     * @param kontekst kontekst fra Instillinger
     * @param listPreference skal holde og vise frem kommunene når en bruker skal velge favorit kommune.
     */
    public void volleyInstillingerKommune(final Context kontekst, final ListPreference listPreference){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(kontekst);



// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MainActivity.legacyKommuner,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONArray JSONArraySvar = new JSONArray(response); //Omgjør responsen fra databasen om til en JSONArray
                            ArrayList<SorteringsObjekt> sortObjListe = new ArrayList<>();  //Liste med SorteringsObjekt for å holde dem før de sorteres
                            CharSequence[] kommunesnavn = new CharSequence[JSONArraySvar.length()]; //Skal holde kommunenavnene som settes som Entry
                            CharSequence[] kommuneNr = new CharSequence[JSONArraySvar.length()]; // skal holde kommunenummerene som skal setter som EntryValues


                            //går igjennom JSONArraySvar og legger informasjon om hver kommune i sortObjListe
                            for (int i = 0; i < JSONArraySvar.length(); i++) {

                                JSONObject kommuneObjekt = JSONArraySvar.getJSONObject(i);
                                sortObjListe.add(new SorteringsObjekt(kommuneObjekt.getString("Navn"), kommuneObjekt.getString("Kommunenr")));

                            }

                            Collections.sort(sortObjListe);  //sorterer listen på kommunenavn

                            //putter de sorterte verdiene i CharSequance lister så de kan setter som Entries og EntryValues
                            for (int i = 0; i < sortObjListe.size(); i++) {


                                kommunesnavn[i] = sortObjListe.get(i).nokkel;
                                kommuneNr[i] = sortObjListe.get(i).verdi;
                            }

                            listPreference.setEntries(kommunesnavn);
                            listPreference.setEntryValues(kommuneNr);
                        } catch (JSONException e) {
                            Toast.makeText(kontekst, e.toString() + " JSONException", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(kontekst, error.toString(), Toast.LENGTH_LONG).show();

            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }


    /**
     * Tar in en string og sjekker om den sier null
     * @param tekst
     * @return returnerer true hvis tekst sier null
     */
    private boolean sjekkNull(String tekst){
        return tekst.equals("null");
    }

}
