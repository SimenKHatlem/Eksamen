package com.kandidat24.eksamen;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

/**
 * Barnehage klassen brukes til å lage Barnehageobjekter som kan sendes til Class.BarnehageKart. For å kunne sende de i en array list valgte jeg å gjøre de Parcelable.
 * Jeg hentet teknikken fra https://stackoverflow.com/questions/45446452/send-arraylist-by-intent, men mesteparten av koden her ble generert av Android Studio.
 * Jeg bruker kun konstruktør og getter metodene
 */

public class Barnehage implements Parcelable {

    LatLng latLng;  //lengdegrad og breddegrad til barnehagen
    String navn; //navnet på barnehagen


    /**
     * Konstruktør
     * @param in
     */
    protected Barnehage(Parcel in) {
        latLng = in.readParcelable(LatLng.class.getClassLoader());
        navn = in.readString();
    }

    public static final Creator<Barnehage> CREATOR = new Creator<Barnehage>() {
        @Override
        public Barnehage createFromParcel(Parcel in) {
            return new Barnehage(in);
        }

        @Override
        public Barnehage[] newArray(int size) {
            return new Barnehage[size];
        }
    };

    /**
     *
     * @return Returnerer et LatLng objekt med lengde- og breddegrad til barnehagen
     */
    public LatLng getLatLng() {
        return latLng;
    }

    /**
     *
     * @return Returnerer en String med navnet til barnehagen
     */
    public String getNavn() {
        return navn;
    }

    public Barnehage(LatLng latLng, String navn) {
        this.latLng = latLng;
        this.navn = navn;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(latLng, flags);
        dest.writeString(navn);
    }
}
