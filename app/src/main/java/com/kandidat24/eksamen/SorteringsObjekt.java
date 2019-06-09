package com.kandidat24.eksamen;

public class SorteringsObjekt implements Comparable<SorteringsObjekt> {
    String nokkel;
    String verdi;

    @Override
    public int compareTo(SorteringsObjekt o) {

        return this.nokkel.compareTo(o.getNokkel());
    }

    @Override
    public boolean equals(Object objekt){
        if (this == objekt) return true;
        if (!(objekt instanceof SorteringsObjekt)) return false;

        SorteringsObjekt sort = (SorteringsObjekt)objekt;
        return this.nokkel.equals(sort.nokkel);
    }

    @Override
    public int hashCode(){
        return nokkel.hashCode();
    }

    public SorteringsObjekt(String nokkel, String verdi) {
        this.nokkel = nokkel;
        this.verdi = verdi;
    }

    public String getNokkel() {
        return nokkel;
    }

    public String getVerdi() {
        return verdi;
    }
}
