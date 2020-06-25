package ec.mil.armada.covid19are.model;

import android.os.Parcel;
import android.os.Parcelable;

public class EnfermedadesCatastroficasModel implements Parcelable {

    public static final Creator<EnfermedadesCatastroficasModel> CREATOR = new Creator<EnfermedadesCatastroficasModel>() {
        @Override
        public EnfermedadesCatastroficasModel createFromParcel(Parcel in) {
            return new EnfermedadesCatastroficasModel(in);
        }

        @Override
        public EnfermedadesCatastroficasModel[] newArray(int size) {
            return new EnfermedadesCatastroficasModel[size];
        }
    };
    private Boolean cancer, insuficienciaRenalCronica, inmunoDeprimidos, diabetico, hipertenso, enfermedadPulmonarCronica, enfermedadCardiovascular, enfermedadCerebrovascular;

    public EnfermedadesCatastroficasModel() {
    }


    public EnfermedadesCatastroficasModel(Boolean cancer, Boolean insuficienciaRenalCronica, Boolean inmunoDeprimidos, Boolean diabetico, Boolean hipertenso, Boolean enfermedadPulmonarCronica, Boolean enfermedadCardiovascular, Boolean enfermedadCerebrovascular) {
        this.cancer = cancer;
        this.insuficienciaRenalCronica = insuficienciaRenalCronica;
        this.inmunoDeprimidos = inmunoDeprimidos;
        this.diabetico = diabetico;
        this.hipertenso = hipertenso;
        this.enfermedadPulmonarCronica = enfermedadPulmonarCronica;
        this.enfermedadCardiovascular = enfermedadCardiovascular;
        this.enfermedadCerebrovascular = enfermedadCerebrovascular;
    }

    protected EnfermedadesCatastroficasModel(Parcel in) {
        byte tmpCancer = in.readByte();
        cancer = tmpCancer == 0 ? null : tmpCancer == 1;
        byte tmpInsuficienciaRenalCronica = in.readByte();
        insuficienciaRenalCronica = tmpInsuficienciaRenalCronica == 0 ? null : tmpInsuficienciaRenalCronica == 1;
        byte tmpInmunoDeprimidos = in.readByte();
        inmunoDeprimidos = tmpInmunoDeprimidos == 0 ? null : tmpInmunoDeprimidos == 1;
        byte tmpDiabetico = in.readByte();
        diabetico = tmpDiabetico == 0 ? null : tmpDiabetico == 1;
        byte tmpHipertenso = in.readByte();
        hipertenso = tmpHipertenso == 0 ? null : tmpHipertenso == 1;
        byte tmpEnfermedadPulmonarCronica = in.readByte();
        enfermedadPulmonarCronica = tmpEnfermedadPulmonarCronica == 0 ? null : tmpEnfermedadPulmonarCronica == 1;
        byte tmpEnfermedadCardiovascular = in.readByte();
        enfermedadCardiovascular = tmpEnfermedadCardiovascular == 0 ? null : tmpEnfermedadCardiovascular == 1;
        byte tmpEnfermedadCerebrovascular = in.readByte();
        enfermedadCerebrovascular = tmpEnfermedadCerebrovascular == 0 ? null : tmpEnfermedadCerebrovascular == 1;
    }

    public Boolean getCancer() {
        return cancer;
    }

    public void setCancer(Boolean cancer) {
        this.cancer = cancer;
    }

    public Boolean getInsuficienciaRenalCronica() {
        return insuficienciaRenalCronica;
    }

    public void setInsuficienciaRenalCronica(Boolean insuficienciaRenalCronica) {
        this.insuficienciaRenalCronica = insuficienciaRenalCronica;
    }

    public Boolean getInmunoDeprimidos() {
        return inmunoDeprimidos;
    }

    public void setInmunoDeprimidos(Boolean inmunoDeprimidos) {
        this.inmunoDeprimidos = inmunoDeprimidos;
    }

    public Boolean getDiabetico() {
        return diabetico;
    }

    public void setDiabetico(Boolean diabetico) {
        this.diabetico = diabetico;
    }

    public Boolean getHipertenso() {
        return hipertenso;
    }

    public void setHipertenso(Boolean hipertenso) {
        this.hipertenso = hipertenso;
    }

    public Boolean getEnfermedadPulmonarCronica() {
        return enfermedadPulmonarCronica;
    }

    public void setEnfermedadPulmonarCronica(Boolean enfermedadPulmonarCronica) {
        this.enfermedadPulmonarCronica = enfermedadPulmonarCronica;
    }

    public Boolean getEnfermedadCardiovascular() {
        return enfermedadCardiovascular;
    }

    public void setEnfermedadCardiovascular(Boolean enfermedadCardiovascular) {
        this.enfermedadCardiovascular = enfermedadCardiovascular;
    }

    public Boolean getEnfermedadCerebrovascular() {
        return enfermedadCerebrovascular;
    }

    public void setEnfermedadCerebrovascular(Boolean enfermedadCerebrovascular) {
        this.enfermedadCerebrovascular = enfermedadCerebrovascular;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (cancer == null ? 0 : cancer ? 1 : 2));
        dest.writeByte((byte) (insuficienciaRenalCronica == null ? 0 : insuficienciaRenalCronica ? 1 : 2));
        dest.writeByte((byte) (inmunoDeprimidos == null ? 0 : inmunoDeprimidos ? 1 : 2));
        dest.writeByte((byte) (diabetico == null ? 0 : diabetico ? 1 : 2));
        dest.writeByte((byte) (hipertenso == null ? 0 : hipertenso ? 1 : 2));
        dest.writeByte((byte) (enfermedadPulmonarCronica == null ? 0 : enfermedadPulmonarCronica ? 1 : 2));
        dest.writeByte((byte) (enfermedadCardiovascular == null ? 0 : enfermedadCardiovascular ? 1 : 2));
        dest.writeByte((byte) (enfermedadCerebrovascular == null ? 0 : enfermedadCerebrovascular ? 1 : 2));
    }
}
