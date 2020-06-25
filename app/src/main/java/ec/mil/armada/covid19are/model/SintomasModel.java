package ec.mil.armada.covid19are.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

public class SintomasModel implements Parcelable {

    public static final Creator<SintomasModel> CREATOR = new Creator<SintomasModel>() {
        @Override
        public SintomasModel createFromParcel(Parcel in) {
            return new SintomasModel(in);
        }

        @Override
        public SintomasModel[] newArray(int size) {
            return new SintomasModel[size];
        }
    };
    private Boolean fiebre, tosSeca, dolorGarganta, dolorMuscular, dificultadRespirar, malestarGeneral, perdidaOlfato, diarrea, dolorCabeza, flema;
    private String tiempo;
    private Timestamp fecha;

    public SintomasModel() {
    }


    public SintomasModel(Boolean fiebre, Boolean tosSeca, Boolean dolorGarganta, Boolean dolorMuscular, Boolean dificultadRespirar, Boolean malestarGeneral, Boolean perdidaOlfato, Boolean diarrea, Boolean dolorCabeza, Boolean flema, String tiempo, Timestamp fecha) {
        this.fiebre = fiebre;
        this.tosSeca = tosSeca;
        this.dolorGarganta = dolorGarganta;
        this.dolorMuscular = dolorMuscular;
        this.dificultadRespirar = dificultadRespirar;
        this.malestarGeneral = malestarGeneral;
        this.perdidaOlfato = perdidaOlfato;
        this.diarrea = diarrea;
        this.dolorCabeza = dolorCabeza;
        this.flema = flema;
        this.tiempo = tiempo;
        this.fecha = fecha;
    }

    protected SintomasModel(Parcel in) {
        byte tmpFiebre = in.readByte();
        fiebre = tmpFiebre == 0 ? null : tmpFiebre == 1;
        byte tmpTosSeca = in.readByte();
        tosSeca = tmpTosSeca == 0 ? null : tmpTosSeca == 1;
        byte tmpDolorGarganta = in.readByte();
        dolorGarganta = tmpDolorGarganta == 0 ? null : tmpDolorGarganta == 1;
        byte tmpDolorMuscular = in.readByte();
        dolorMuscular = tmpDolorMuscular == 0 ? null : tmpDolorMuscular == 1;
        byte tmpDificultadRespirar = in.readByte();
        dificultadRespirar = tmpDificultadRespirar == 0 ? null : tmpDificultadRespirar == 1;
        byte tmpMalestarGeneral = in.readByte();
        malestarGeneral = tmpMalestarGeneral == 0 ? null : tmpMalestarGeneral == 1;
        byte tmpPerdidaOlfato = in.readByte();
        perdidaOlfato = tmpPerdidaOlfato == 0 ? null : tmpPerdidaOlfato == 1;
        byte tmpDiarrea = in.readByte();
        diarrea = tmpDiarrea == 0 ? null : tmpDiarrea == 1;
        byte tmpDolorCabeza = in.readByte();
        dolorCabeza = tmpDolorCabeza == 0 ? null : tmpDolorCabeza == 1;
        byte tmpFlema = in.readByte();
        flema = tmpFlema == 0 ? null : tmpFlema == 1;
        tiempo = in.readString();
        fecha = in.readParcelable(Timestamp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (fiebre == null ? 0 : fiebre ? 1 : 2));
        dest.writeByte((byte) (tosSeca == null ? 0 : tosSeca ? 1 : 2));
        dest.writeByte((byte) (dolorGarganta == null ? 0 : dolorGarganta ? 1 : 2));
        dest.writeByte((byte) (dolorMuscular == null ? 0 : dolorMuscular ? 1 : 2));
        dest.writeByte((byte) (dificultadRespirar == null ? 0 : dificultadRespirar ? 1 : 2));
        dest.writeByte((byte) (malestarGeneral == null ? 0 : malestarGeneral ? 1 : 2));
        dest.writeByte((byte) (perdidaOlfato == null ? 0 : perdidaOlfato ? 1 : 2));
        dest.writeByte((byte) (diarrea == null ? 0 : diarrea ? 1 : 2));
        dest.writeByte((byte) (dolorCabeza == null ? 0 : dolorCabeza ? 1 : 2));
        dest.writeByte((byte) (flema == null ? 0 : flema ? 1 : 2));
        dest.writeString(tiempo);
        dest.writeParcelable(fecha, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Boolean getFiebre() {
        return fiebre;
    }

    public void setFiebre(Boolean fiebre) {
        this.fiebre = fiebre;
    }

    public Boolean getTosSeca() {
        return tosSeca;
    }

    public void setTosSeca(Boolean tosSeca) {
        this.tosSeca = tosSeca;
    }

    public Boolean getDolorGarganta() {
        return dolorGarganta;
    }

    public void setDolorGarganta(Boolean dolorGarganta) {
        this.dolorGarganta = dolorGarganta;
    }

    public Boolean getDolorMuscular() {
        return dolorMuscular;
    }

    public void setDolorMuscular(Boolean dolorMuscular) {
        this.dolorMuscular = dolorMuscular;
    }

    public Boolean getDificultadRespirar() {
        return dificultadRespirar;
    }

    public void setDificultadRespirar(Boolean dificultadRespirar) {
        this.dificultadRespirar = dificultadRespirar;
    }

    public Boolean getMalestarGeneral() {
        return malestarGeneral;
    }

    public void setMalestarGeneral(Boolean malestarGeneral) {
        this.malestarGeneral = malestarGeneral;
    }

    public Boolean getPerdidaOlfato() {
        return perdidaOlfato;
    }

    public void setPerdidaOlfato(Boolean perdidaOlfato) {
        this.perdidaOlfato = perdidaOlfato;
    }

    public Boolean getDiarrea() {
        return diarrea;
    }

    public void setDiarrea(Boolean diarrea) {
        this.diarrea = diarrea;
    }

    public Boolean getDolorCabeza() {
        return dolorCabeza;
    }

    public void setDolorCabeza(Boolean dolorCabeza) {
        this.dolorCabeza = dolorCabeza;
    }

    public Boolean getFlema() {
        return flema;
    }

    public void setFlema(Boolean flema) {
        this.flema = flema;
    }

    public String getTiempo() {
        return tiempo;
    }

    public void setTiempo(String tiempo) {
        this.tiempo = tiempo;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
