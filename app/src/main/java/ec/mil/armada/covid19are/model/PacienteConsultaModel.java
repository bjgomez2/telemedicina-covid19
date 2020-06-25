package ec.mil.armada.covid19are.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PacienteConsultaModel implements Parcelable {

    public static final Creator<PacienteConsultaModel> CREATOR = new Creator<PacienteConsultaModel>() {
        @Override
        public PacienteConsultaModel createFromParcel(Parcel in) {
            return new PacienteConsultaModel(in);
        }

        @Override
        public PacienteConsultaModel[] newArray(int size) {
            return new PacienteConsultaModel[size];
        }
    };
    //datosPersonales
    public String nombres, sexo, grado, reparto, tipoPaciente, fechaNacimiento;

    public PacienteConsultaModel() {
    }

    public PacienteConsultaModel(String nombres, String sexo, String grado, String reparto, String tipoPaciente, String fechaNacimiento) {
        this.nombres = nombres;
        this.sexo = sexo;
        this.grado = grado;
        this.reparto = reparto;
        this.tipoPaciente = tipoPaciente;
        this.fechaNacimiento = fechaNacimiento;
    }

    protected PacienteConsultaModel(Parcel in) {
        nombres = in.readString();
        sexo = in.readString();
        grado = in.readString();
        reparto = in.readString();
        tipoPaciente = in.readString();
        fechaNacimiento = in.readString();
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getGrado() {
        return grado;
    }

    public void setGrado(String grado) {
        this.grado = grado;
    }

    public String getReparto() {
        return reparto;
    }

    public void setReparto(String reparto) {
        this.reparto = reparto;
    }

    public String getTipoPaciente() {
        return tipoPaciente;
    }

    public void setTipoPaciente(String tipoPaciente) {
        this.tipoPaciente = tipoPaciente;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nombres);
        dest.writeString(sexo);
        dest.writeString(grado);
        dest.writeString(reparto);
        dest.writeString(tipoPaciente);
        dest.writeString(fechaNacimiento);
    }
}
