package com.kandidat24.eksamen;
import android.content.Context;
import android.content.Intent;

import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import android.widget.TextView;



import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * RecyclerView adapter for å vise informasjon om en kommune.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Integer> bildeListe;  //Liste over bildene til kategorien
    private ArrayList<String> overskiftListe; //Liste over overskriften til kategorien
    private ArrayList<String> tekstListe; //Liste over teksten til kategorien
    private String kommuneNummer; //kommunenummeret
    private Context kontekst;

    /**
     * Konstuktør metode for RecyclerViewAdapter
     * @param contekst konteksten til KommuneInfo
     * @param bildeListe Liste over bildene til kategorien
     * @param overskiftListe Liste over overskriften til kategorien
     * @param tekstListe Liste over teksten til kategorien
     * @param kommuneNummer kommunenummeret
     */
        public RecyclerViewAdapter(Context contekst, ArrayList<Integer> bildeListe, ArrayList<String> overskiftListe, ArrayList<String> tekstListe, String kommuneNummer) {
        this.bildeListe = bildeListe;
        this.overskiftListe = overskiftListe;
        this.tekstListe = tekstListe;
        this.kommuneNummer = kommuneNummer;

        this.kontekst = contekst;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_kommune_instans, viewGroup, false);
        ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    /**
     *
     * @param holder et holder element i listen
     * @param i tellevariabel
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        holder.kommuneInfoOverskrift.setText(overskiftListe.get(i));
        holder.kommuneInfoTekst.setText(tekstListe.get(i));
        holder.kommuneInfoBilde.setImageResource(bildeListe.get(i));

//Første elementet skal ikke ha padding, og tar det til BarnehagelisteActivity hvis teksten klikkes
    if(i == 0) {
        holder.kommuneInfoTekst.setPadding(0,0,0,0);
        holder.kommuneInfoOverskrift.setPadding(0,0,0,0);

        holder.kommuneInfoTekst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent startIntent = new Intent(kontekst, BarnehagelisteActivity.class);
                startIntent.putExtra("KOMMUNE_NUMMER", kommuneNummer);
                kontekst.startActivity(startIntent);


    }

    });
    }

//Hvis det er siste element så skal et klikk åpne nettleser med kommunens nettside.
        if(i == tekstListe.size()-1) {
            holder.kommuneInfoTekst.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tekstListe.get(i)));
                    kontekst.startActivity(browserIntent);

                }
            });
        }



    }

    /**
     *
     * @return størrelsen på tekstListe
     */
    @Override
    public int getItemCount() {
        return tekstListe.size();
    }

    /**
     * ViewHolder konstruktør
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView kommuneInfoOverskrift;
        TextView kommuneInfoTekst;
        ImageView kommuneInfoBilde;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            kommuneInfoOverskrift = itemView.findViewById(R.id.kommuneInfoOverskrift);
            kommuneInfoTekst = itemView.findViewById(R.id.kommuneInfoTekst);
            kommuneInfoBilde = itemView.findViewById(R.id.kommuneInfoBilde);
        }
    }
}

