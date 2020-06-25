package ec.mil.armada.covid19are.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;

public class PacienteModel implements Parcelable {

    public static final Creator<PacienteModel> CREATOR = new Creator<PacienteModel>() {
        @Override
        public PacienteModel createFromParcel(Parcel in) {
            return new PacienteModel(in);
        }

        @Override
        public PacienteModel[] newArray(int size) {
            return new PacienteModel[size];
        }
    };
    //datosPersonales
    private String cedula, nombres, direccion, celular, correo, sexo, grado, reparto, tipoPaciente, foto, estado, ciudad;
    private Integer edad;
    private Timestamp fechaNacimiento, fechaRegistro;
    private Integer ponderacion;
    //ubicacion
    private GeoPoint geoPoint;

    public PacienteModel() {
    }

    public PacienteModel(String cedula, String nombres, String direccion, String celular, String correo, String sexo, String grado, String reparto, String tipoPaciente, String foto, String estado, String ciudad, Integer edad, Timestamp fechaNacimiento, Timestamp fechaRegistro, Integer ponderacion, GeoPoint geoPoint) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.direccion = direccion;
        this.celular = celular;
        this.correo = correo;
        this.sexo = sexo;
        this.grado = grado;
        this.reparto = reparto;
        this.tipoPaciente = tipoPaciente;
        this.foto = foto;
        this.estado = estado;
        this.ciudad = ciudad;
        this.edad = edad;
        this.fechaNacimiento = fechaNacimiento;
        this.fechaRegistro = fechaRegistro;
        this.ponderacion = ponderacion;
        this.geoPoint = geoPoint;
    }

    protected PacienteModel(Parcel in) {
        cedula = in.readString();
        nombres = in.readString();
        direccion = in.readString();
        celular = in.readString();
        correo = in.readString();
        sexo = in.readString();
        grado = in.readString();
        reparto = in.readString();
        tipoPaciente = in.readString();
        foto = in.readString();
        estado = in.readString();
        ciudad = in.readString();
        if (in.readByte() == 0) {
            edad = null;
        } else {
            edad = in.readInt();
        }
        fechaNacimiento = in.readParcelable(Timestamp.class.getClassLoader());
        fechaRegistro = in.readParcelable(Timestamp.class.getClassLoader());
        if (in.readByte() == 0) {
            ponderacion = null;
        } else {
            ponderacion = in.readInt();
        }
        double lat = in.readDouble();
        double lng = in.readDouble();
        geoPoint = new GeoPoint(lat, lng);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cedula);
        dest.writeString(nombres);
        dest.writeString(direccion);
        dest.writeString(celular);
        dest.writeString(correo);
        dest.writeString(sexo);
        dest.writeString(grado);
        dest.writeString(reparto);
        dest.writeString(tipoPaciente);
        dest.writeString(foto);
        dest.writeString(estado);
        dest.writeString(ciudad);
        if (edad == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(edad);
        }
        dest.writeParcelable(fechaNacimiento, flags);
        dest.writeParcelable(fechaRegistro, flags);
        if (ponderacion == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(ponderacion);
        }
        dest.writeDouble(geoPoint.getLatitude());
        dest.writeDouble(geoPoint.getLongitude());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public Timestamp getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Timestamp fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Integer getPonderacion() {
        return ponderacion;
    }

    public void setPonderacion(Integer ponderacion) {
        this.ponderacion = ponderacion;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
