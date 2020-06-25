package ec.mil.armada.covid19are.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.GeoPoint;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import ec.mil.armada.covid19are.R;
import ec.mil.armada.covid19are.model.ComplementariasModel;
import ec.mil.armada.covid19are.model.EnfermedadesCatastroficasModel;
import ec.mil.armada.covid19are.model.PacienteConsultaModel;
import ec.mil.armada.covid19are.model.PacienteModel;
import ec.mil.armada.covid19are.model.SintomasModel;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class FormularioActivity extends AppCompatActivity {

    private static final String fotoMasculino = "https://firebasestorage.googleapis.com/v0/b/covid19-are.appspot.com/o/FotosPacientes%2Fpaciente_m_sin_foto.png?alt=media&token=7fb3f229-2bd5-496f-b6e8-0c6e3d2f7790";
    private static final String fotoFemenino = "https://firebasestorage.googleapis.com/v0/b/covid19-are.appspot.com/o/FotosPacientes%2Fpaciente_f_sin_foto.png?alt=media&token=b09e9401-d133-4ae8-9ce2-dc5dc0747b3d";
    FusedLocationProviderClient mFusedLocationClient;
    TextInputEditText txtCiudad, txtDireccion, txtCelular, txtCorreo;
    private ProgressBar pgbProcesoFormulario;
    private Button btnProcesoFormulario;

    //location
    private Double latitud, longitud;
    private String ciudad;

    //model
    private PacienteModel paciente;
    private SintomasModel sintomas;
    private EnfermedadesCatastroficasModel enfermedadesCatastroficas;
    private ComplementariasModel complementarias;

    //foto
    private CircleImageView foto;
    private Uri fotoURI;

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitud = mLastLocation.getLatitude();
            longitud = mLastLocation.getLongitude();
            getCiudad();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        //DATOS PERSONALES
        foto = findViewById(R.id.foto);
        CircleImageView cambiarImagen = findViewById(R.id.cambiarImagen);
        TextInputEditText txtCedula = findViewById(R.id.txtCedula);
        TextInputEditText txtTipoPaciente = findViewById(R.id.txtTipoPaciente);
        TextInputEditText txtGrado = findViewById(R.id.txtGrado);
        TextInputEditText txtNombres = findViewById(R.id.txtNombres);
        TextInputEditText txtSexo = findViewById(R.id.txtSexo);
        TextInputEditText txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);
        TextInputEditText txtReparto = findViewById(R.id.txtReparto);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtDireccion = findViewById(R.id.txtDireccionDomiciliaria);
        txtCelular = findViewById(R.id.txtCelular);
        txtCorreo = findViewById(R.id.txtEmail);
        TextInputLayout txtInputGrado = findViewById(R.id.txtInputGrado);
        TextInputLayout txtInputReparto = findViewById(R.id.txtInputReparto);

        pgbProcesoFormulario = findViewById(R.id.pgbProcesoFormulario);
        btnProcesoFormulario = findViewById(R.id.btnProcesoFormulario);

        Intent intent = getIntent();
        String resultadoPaciente = intent.getExtras().getString("resultadoPaciente");

        if (resultadoPaciente.equalsIgnoreCase("pacienteNuevo")) {
            PacienteConsultaModel pacienteConsulta = (PacienteConsultaModel) intent.getParcelableExtra("paciente");
            String cedula = intent.getExtras().getString("cedula");
            paciente = new PacienteModel();
            paciente.setCedula(cedula);
            paciente.setTipoPaciente(pacienteConsulta.getTipoPaciente());
            paciente.setNombres(pacienteConsulta.getNombres());
            Timestamp fechaNacimiento = stringToTimestamp(pacienteConsulta.getFechaNacimiento());
            paciente.setFechaNacimiento(fechaNacimiento);
            paciente.setGrado(pacienteConsulta.getGrado());
            paciente.setSexo(pacienteConsulta.getSexo());
            paciente.setReparto(pacienteConsulta.getReparto());
            paciente.setEstado("pendiente");
            paciente.setPonderacion(0);

            if (paciente.getSexo().equalsIgnoreCase("MASCULINO")) {
                foto.setImageResource(R.drawable.paciente_m_sin_foto);
            } else {
                foto.setImageResource(R.drawable.paciente_f_sin_foto);
            }

            if (paciente.getSexo().equalsIgnoreCase("MASCULINO")) {
                paciente.setFoto(fotoMasculino);
            } else {
                paciente.setFoto(fotoFemenino);
            }


        } else {
            paciente = (PacienteModel) intent.getParcelableExtra("paciente");
            sintomas = (SintomasModel) intent.getParcelableExtra("sintomas");
            enfermedadesCatastroficas = (EnfermedadesCatastroficasModel) intent.getParcelableExtra("enfermedadesCatastroficas");
            complementarias = (ComplementariasModel) intent.getParcelableExtra("complementarias");
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        if (paciente.getGrado() != null && !paciente.getGrado().isEmpty()) {
            txtInputGrado.setVisibility(View.VISIBLE);
            txtGrado.setText(paciente.getGrado());
        }

        if (paciente.getReparto() != null && !paciente.getReparto().isEmpty()) {
            txtInputReparto.setVisibility(View.VISIBLE);
            txtReparto.setText(paciente.getReparto());
        }

        if (paciente.getFoto() != null && !paciente.getFoto().isEmpty()) {
            Picasso.get().load(paciente.getFoto())
                    .placeholder(R.drawable.paciente_m_sin_foto).error(R.drawable.paciente_m_sin_foto)
                    .into(foto);
        }

        txtCedula.setText(paciente.getCedula());
        txtTipoPaciente.setText(paciente.getTipoPaciente());
        txtNombres.setText(paciente.getNombres());
        txtSexo.setText(paciente.getSexo());

        String fechaNacimiento = timestampToString(paciente.getFechaNacimiento());
        txtFechaNacimiento.setText(fechaNacimiento);

        Integer edad = getEdad(fechaNacimiento);
        paciente.setEdad(edad);

        if (paciente.getDireccion() != null && !paciente.getDireccion().isEmpty()) {
            txtDireccion.setText(paciente.getDireccion());
        }

        if (paciente.getCelular() != null && !paciente.getCelular().isEmpty()) {
            txtCelular.setText(paciente.getCelular());
        }

        if (paciente.getCorreo() != null && !paciente.getCorreo().isEmpty()) {
            txtCorreo.setText(paciente.getCorreo());
        }

        cambiarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormularioActivityPermissionsDispatcher.openPermisosWithPermissionCheck(FormularioActivity.this);
            }
        });

        btnProcesoFormulario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pgbProcesoFormulario.setVisibility(View.VISIBLE);
                btnProcesoFormulario.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(txtCiudad.getText().toString())) {
                    paciente.setCiudad(txtCiudad.getText().toString());
                }

                if (TextUtils.isEmpty(txtDireccion.getText().toString())) {
                    txtDireccion.setError("Campo requerido");
                    pgbProcesoFormulario.setVisibility(View.GONE);
                    btnProcesoFormulario.setVisibility(View.VISIBLE);
                    return;
                } else {
                    paciente.setDireccion(txtDireccion.getText().toString());
                }

                if (TextUtils.isEmpty(txtCelular.getText().toString())) {
                    txtCelular.setError("Campo requerido");
                    pgbProcesoFormulario.setVisibility(View.GONE);
                    btnProcesoFormulario.setVisibility(View.VISIBLE);
                    return;
                } else {
                    paciente.setCelular(txtCelular.toString().toString());
                }

                if (TextUtils.isEmpty(txtCorreo.getText().toString())) {
                    txtCorreo.setError("Campo requerido");
                    pgbProcesoFormulario.setVisibility(View.GONE);
                    btnProcesoFormulario.setVisibility(View.VISIBLE);
                    return;
                }
                if (!isValidPhoneNumber(txtCelular.getText().toString())) {
                    txtCelular.setError(" Celular inválido");
                    pgbProcesoFormulario.setVisibility(View.GONE);
                    btnProcesoFormulario.setVisibility(View.VISIBLE);
                    return;
                } else {
                    paciente.setCelular(txtCelular.getText().toString());
                }

                if (!isValidEmail(txtCorreo.getText().toString())) {
                    txtCorreo.setError(" Email inválido");
                    pgbProcesoFormulario.setVisibility(View.GONE);
                    btnProcesoFormulario.setVisibility(View.VISIBLE);
                    return;
                } else {
                    paciente.setCorreo(txtCorreo.getText().toString());
                }

                GeoPoint geoPoint = new GeoPoint(latitud, longitud);
                paciente.setGeoPoint(geoPoint);

                pgbProcesoFormulario.setVisibility(View.GONE);
                btnProcesoFormulario.setVisibility(View.VISIBLE);

                Intent intent = new Intent(FormularioActivity.this, SintomasActivity.class);

                if (fotoURI != null) {
                    intent.putExtra("isFotoURI", "YES");
                    paciente.setFoto(fotoURI.toString());
                } else {
                    intent.putExtra("isFotoURI", "NOT");
                }

                intent.putExtra("paciente", paciente);
                intent.putExtra("sintomas", sintomas);
                intent.putExtra("enfermedadesCatastroficas", enfermedadesCatastroficas);
                intent.putExtra("complementarias", complementarias);

                startActivity(intent);
            }
        });
    }

    private Timestamp stringToTimestamp(String fechaConvertir) {

        DateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date date = null;

        try {
            date = format.parse(fechaConvertir);
        } catch (ParseException e) {
            System.out.println("Error al convertir la fecha");
        }

        date.setHours(12);

        Timestamp timestamp = new Timestamp(date);

        return timestamp;
    }

    private String timestampToString(Timestamp fechaConvertir) {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = fechaConvertir.toDate();
        return formatter.format(date);
    }

    private void getLastLocation() {
        mFusedLocationClient.getLastLocation().addOnCompleteListener(
                new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            latitud = location.getLatitude();
                            longitud = location.getLongitude();
                        }
                    }
                }
        );
    }

    private void getCiudad() {
        Geocoder gcd = new Geocoder(getBaseContext(),
                Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = gcd.getFromLocation(latitud, longitud, 5);

            if (addresses != null && addresses.size() > 0) {
                for (Address adr : addresses) {
                    if (adr.getLocality() != null && adr.getLocality().length() > 0) {
                        ciudad = adr.getLocality().toUpperCase();
                        Log.i("Ciudades", ciudad);
                        txtCiudad.setText(ciudad);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (phoneNumber.length() != 10) {
            return false;
        } else {
            return android.util.Patterns.PHONE.matcher(phoneNumber).matches();
        }
    }

    private int getEdad(String dobString) {
        Date date = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            date = sdf.parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (date == null) return 0;

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.setTime(date);

        int year = dob.get(Calendar.YEAR);
        int month = dob.get(Calendar.MONTH);
        int day = dob.get(Calendar.DAY_OF_MONTH);

        dob.set(year, month + 1, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void openPermisos() {
        System.out.println("Obteniendo permisos");

        ImagePicker.Companion.with(FormularioActivity.this)
                .cropSquare()
                .compress(512)
                .maxResultSize(200, 200)
                .start(101);

    }

    @SuppressLint("NeedOnRequestPermissionsResult")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FormularioActivityPermissionsDispatcher.onRequestPermissionsResult(FormularioActivity.this, requestCode, grantResults);
    }

    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(FormularioActivity.this)
                .setTitle("Perminos requeridos")
                .setMessage("Si desea añadir una foto de perfil acepte los permisos requeridos")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .show();
    }

    @SuppressLint("ShowToast")
    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForCamera() {
        Toast.makeText(FormularioActivity.this, "Permiso denegado", Toast.LENGTH_SHORT);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForCamera() {
        displayNeverAskAgainDialog();
    }

    private void displayNeverAskAgainDialog() {
        new AlertDialog.Builder(FormularioActivity.this)
                .setMessage("Por favor configure manualmente los permisos en la pantalla de configuración."
                        + "\n\nSeleccione el permiso -> Permitir")
                .setCancelable(false)
                .setPositiveButton("Permitir Manualmente", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data.getData() != null) {
            fotoURI = data.getData();
            foto.setImageURI(fotoURI);
            System.out.println("Imagen seleccionada ------->>>>>" + fotoURI.toString());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestNewLocationData();
        Log.i("Location ", "Iniciando pedido location");
    }
}
