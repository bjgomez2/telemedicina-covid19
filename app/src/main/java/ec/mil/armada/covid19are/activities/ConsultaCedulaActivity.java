package ec.mil.armada.covid19are.activities;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ec.mil.armada.covid19are.R;
import ec.mil.armada.covid19are.model.PacienteConsultaModel;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ConsultaCedulaActivity extends AppCompatActivity {

    //location
    protected LocationManager mLocationManager;
    //componentes
    private TextInputEditText txtCedulaConsulta;
    private CheckBox cbxAutorizacion;
    private ProgressBar pgbProcesoConsulta;
    private Button btnConsultarDatos;
    //firebase
    private FirebaseDatabase dbFirebase;
    private DatabaseReference dbPacientesRef;
    private boolean statusOfGPS;

    //model
    private PacienteConsultaModel pacienteConsulta;

    public static void hideKeyboard(AppCompatActivity activity) {
        View view = activity.findViewById(android.R.id.content);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_cedula);

        txtCedulaConsulta = findViewById(R.id.txtCedulaConsulta);
        cbxAutorizacion = findViewById(R.id.cbxAutorizacion);
        pgbProcesoConsulta = findViewById(R.id.pgbProcesoConsulta);
        btnConsultarDatos = findViewById(R.id.btnConsultarDatos);

        dbFirebase = FirebaseDatabase.getInstance();
        dbPacientesRef = dbFirebase.getReference("Datos").child("Pacientes");

        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        assert mLocationManager != null;
        statusOfGPS = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        checkingStatusGPS();

        cbxAutorizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxAutorizacion.getError() != null) {
                    cbxAutorizacion.setError(null);
                }

                if (!cbxAutorizacion.isChecked()) {
                    cbxAutorizacion.setError("Para continuar debe autorizar el uso de información");
                    cbxAutorizacion.requestFocus();
                }


            }
        });

        btnConsultarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(txtCedulaConsulta.getText().toString())) {
                    txtCedulaConsulta.setError("Campo requerido");
                    txtCedulaConsulta.requestFocus();
                    return;
                }

                if (!isValidCedula(txtCedulaConsulta.getText().toString())) {
                    txtCedulaConsulta.setError("Cédula no válida");
                    txtCedulaConsulta.requestFocus();
                    return;
                }

                if (!cbxAutorizacion.isChecked()) {
                    cbxAutorizacion.setError("Para continuar debe autorizar el uso de información");
                    cbxAutorizacion.requestFocus();
                    return;
                }

                hideKeyboard(ConsultaCedulaActivity.this);

                ConsultaCedulaActivityPermissionsDispatcher.openPermisosWithPermissionCheck(ConsultaCedulaActivity.this);

            }
        });
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void openPermisos() {
        System.out.println("Obteniendo permisos");

        pgbProcesoConsulta.setVisibility(View.VISIBLE);
        btnConsultarDatos.setVisibility(View.GONE);

        dbPacientesRef.orderByKey().equalTo(txtCedulaConsulta.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        pacienteConsulta = snapshot.getValue(PacienteConsultaModel.class);
                    }

                    Intent intent = new Intent(ConsultaCedulaActivity.this, VerificacionDatosActivity.class);
                    intent.putExtra("paciente", pacienteConsulta);
                    intent.putExtra("cedula", txtCedulaConsulta.getText().toString());
                    intent.putExtra("resultadoPaciente", "pacienteNuevo");
                    startActivity(intent);

                } else {
                    pgbProcesoConsulta.setVisibility(View.GONE);
                    btnConsultarDatos.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder = new AlertDialog.Builder(ConsultaCedulaActivity.this);
                    builder.setTitle("Información");
                    builder.setMessage("No existe información con la cédula ingresada.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pgbProcesoConsulta.setVisibility(View.GONE);
                btnConsultarDatos.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ConsultaCedulaActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle("Perminos requeridos")
                .setMessage("Si desea continuar con el proceso acepte los permisos requeridos")
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

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedForLocation() {
        Toast.makeText(ConsultaCedulaActivity.this, "Permiso denegado", Toast.LENGTH_SHORT);
    }

    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForLocation() {
        displayNeverAskAgainDialog();
    }

    private void displayNeverAskAgainDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Necesitamos permisos para que la aplicación funcione de manera correcta. Por favor configure manualmente los permisos en la pantalla de configuración."
                + "\n\nSeleccione el permiso -> Permitir");
        builder.setCancelable(false);
        builder.setPositiveButton("Permitir Manualmente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent();
                intent.setAction(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Cancelar", null);
        builder.show();
    }

    private boolean isValidCedula(CharSequence value) {
        boolean cedulaCorrecta = false;

        int total = 0;
        int tamanoLongitudCedula = 10;
        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int numeroProvincias = 24;
        int tercerDigito = 6;

        if (value.toString().matches("[0-9]*") && value.toString().length() == tamanoLongitudCedula) {

            int provincia = Integer.parseInt(value.toString().charAt(0) + "" + value.toString().charAt(1));
            int digitoTres = Integer.parseInt(value.toString().charAt(2) + "");
            if ((provincia > 0 && provincia <= numeroProvincias) && digitoTres < tercerDigito) {
                int digitoVerificadorRecibido = Integer.parseInt(value.toString().charAt(9) + "");
                for (int i = 0; i < coeficientes.length; i++) {
                    int valor = Integer.parseInt(coeficientes[i] + "") * Integer.parseInt(value.toString().charAt(i) + "");
                    total = valor >= 10 ? total + (valor - 9) : total + valor;
                }
                int digitoVerificadorObtenido = total >= 10 ? (total % 10) != 0 ? 10 - (total % 10) : (total % 10) : total;
                if (digitoVerificadorObtenido == digitoVerificadorRecibido) {
                    cedulaCorrecta = true;
                } else {
                    cedulaCorrecta = false;
                }
            } else {
                cedulaCorrecta = false;
            }

        } else {
            cedulaCorrecta = false;
        }

        return cedulaCorrecta;
    }

    private void checkingStatusGPS() {
        if (statusOfGPS) {
        } else {
            new AlertDialog.Builder(ConsultaCedulaActivity.this)
                    .setMessage("Por favor para continuar es necesario que active el GPS.")
                    .setCancelable(false)
                    .setPositiveButton("Activar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    }).setNegativeButton("Cancelar", null)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        pgbProcesoConsulta.setVisibility(View.GONE);
        btnConsultarDatos.setVisibility(View.VISIBLE);
    }


}