package ec.mil.armada.covid19are.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ComplementariasModel implements Parcelable {


    public static final Creator<ComplementariasModel> CREATOR = new Creator<ComplementariasModel>() {
        @Override
        public ComplementariasModel createFromParcel(Parcel in) {
            return new ComplementariasModel(in);
        }

        @Override
        public ComplementariasModel[] newArray(int size) {
            return new ComplementariasModel[size];
        }
    };
    private Boolean enfermedadesCatastroficas, viajoAlExterior, recibioFamiliar, hospedoPersona, cuidoEnfermo, tuvoContactoSintomas, tomoMedicamento, vacunadoInfluenza, vacunadoNeumonia, atencionHospitalaria;

    public ComplementariasModel() {
    }

    public ComplementariasModel(Boolean enfermedadesCatastroficas, Boolean viajoAlExterior, Boolean recibioFamiliar, Boolean hospedoPersona, Boolean cuidoEnfermo, Boolean tuvoContactoSintomas, Boolean tomoMedicamento, Boolean vacunadoInfluenza, Boolean vacunadoNeumonia, Boolean atencionHospitalaria) {
        this.enfermedadesCatastroficas = enfermedadesCatastroficas;
        this.viajoAlExterior = viajoAlExterior;
        this.recibioFamiliar = recibioFamiliar;
        this.hospedoPersona = hospedoPersona;
        this.cuidoEnfermo = cuidoEnfermo;
        this.tuvoContactoSintomas = tuvoContactoSintomas;
        this.tomoMedicamento = tomoMedicamento;
        this.vacunadoInfluenza = vacunadoInfluenza;
        this.vacunadoNeumonia = vacunadoNeumonia;
        this.atencionHospitalaria = atencionHospitalaria;
    }

    protected ComplementariasModel(Parcel in) {
        byte tmpEnfermedadesCatastroficas = in.readByte();
        enfermedadesCatastroficas = tmpEnfermedadesCatastroficas == 0 ? null : tmpEnfermedadesCatastroficas == 1;
        byte tmpViajoAlExterior = in.readByte();
        viajoAlExterior = tmpViajoAlExterior == 0 ? null : tmpViajoAlExterior == 1;
        byte tmpRecibioFamiliar = in.readByte();
        recibioFamiliar = tmpRecibioFamiliar == 0 ? null : tmpRecibioFamiliar == 1;
        byte tmpHospedoPersona = in.readByte();
        hospedoPersona = tmpHospedoPersona == 0 ? null : tmpHospedoPersona == 1;
        byte tmpCuidoEnfermo = in.readByte();
        cuidoEnfermo = tmpCuidoEnfermo == 0 ? null : tmpCuidoEnfermo == 1;
        byte tmpTuvoContactoSintomas = in.readByte();
        tuvoContactoSintomas = tmpTuvoContactoSintomas == 0 ? null : tmpTuvoContactoSintomas == 1;
        byte tmpTomoMedicamento = in.readByte();
        tomoMedicamento = tmpTomoMedicamento == 0 ? null : tmpTomoMedicamento == 1;
        byte tmpVacunadoInfluenza = in.readByte();
        vacunadoInfluenza = tmpVacunadoInfluenza == 0 ? null : tmpVacunadoInfluenza == 1;
        byte tmpVacunadoNeumonia = in.readByte();
        vacunadoNeumonia = tmpVacunadoNeumonia == 0 ? null : tmpVacunadoNeumonia == 1;
        byte tmpAtencionHospitalaria = in.readByte();
        atencionHospitalaria = tmpAtencionHospitalaria == 0 ? null : tmpAtencionHospitalaria == 1;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (enfermedadesCatastroficas == null ? 0 : enfermedadesCatastroficas ? 1 : 2));
        dest.writeByte((byte) (viajoAlExterior == null ? 0 : viajoAlExterior ? 1 : 2));
        dest.writeByte((byte) (recibioFamiliar == null ? 0 : recibioFamiliar ? 1 : 2));
        dest.writeByte((byte) (hospedoPersona == null ? 0 : hospedoPersona ? 1 : 2));
        dest.writeByte((byte) (cuidoEnfermo == null ? 0 : cuidoEnfermo ? 1 : 2));
        dest.writeByte((byte) (tuvoContactoSintomas == null ? 0 : tuvoContactoSintomas ? 1 : 2));
        dest.writeByte((byte) (tomoMedicamento == null ? 0 : tomoMedicamento ? 1 : 2));
        dest.writeByte((byte) (vacunadoInfluenza == null ? 0 : vacunadoInfluenza ? 1 : 2));
        dest.writeByte((byte) (vacunadoNeumonia == null ? 0 : vacunadoNeumonia ? 1 : 2));
        dest.writeByte((byte) (atencionHospitalaria == null ? 0 : atencionHospitalaria ? 1 : 2));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Boolean getEnfermedadesCatastroficas() {
        return enfermedadesCatastroficas;
    }

    public void setEnfermedadesCatastroficas(Boolean enfermedadesCatastroficas) {
        this.enfermedadesCatastroficas = enfermedadesCatastroficas;
    }

    public Boolean getViajoAlExterior() {
        return viajoAlExterior;
    }

    public void setViajoAlExterior(Boolean viajoAlExterior) {
        this.viajoAlExterior = viajoAlExterior;
    }

    public Boolean getRecibioFamiliar() {
        return recibioFamiliar;
    }

    public void setRecibioFamiliar(Boolean recibioFamiliar) {
        this.recibioFamiliar = recibioFamiliar;
    }

    public Boolean getHospedoPersona() {
        return hospedoPersona;
    }

    public void setHospedoPersona(Boolean hospedoPersona) {
        this.hospedoPersona = hospedoPersona;
    }

    public Boolean getCuidoEnfermo() {
        return cuidoEnfermo;
    }

    public void setCuidoEnfermo(Boolean cuidoEnfermo) {
        this.cuidoEnfermo = cuidoEnfermo;
    }

    public Boolean getTuvoContactoSintomas() {
        return tuvoContactoSintomas;
    }

    public void setTuvoContactoSintomas(Boolean tuvoContactoSintomas) {
        this.tuvoContactoSintomas = tuvoContactoSintomas;
    }

    public Boolean getTomoMedicamento() {
        return tomoMedicamento;
    }

    public void setTomoMedicamento(Boolean tomoMedicamento) {
        this.tomoMedicamento = tomoMedicamento;
    }

    public Boolean getVacunadoInfluenza() {
        return vacunadoInfluenza;
    }

    public void setVacunadoInfluenza(Boolean vacunadoInfluenza) {
        this.vacunadoInfluenza = vacunadoInfluenza;
    }

    public Boolean getVacunadoNeumonia() {
        return vacunadoNeumonia;
    }

    public void setVacunadoNeumonia(Boolean vacunadoNeumonia) {
        this.vacunadoNeumonia = vacunadoNeumonia;
    }

    public Boolean getAtencionHospitalaria() {
        return atencionHospitalaria;
    }

    public void setAtencionHospitalaria(Boolean atencionHospitalaria) {
        this.atencionHospitalaria = atencionHospitalaria;
    }
}
