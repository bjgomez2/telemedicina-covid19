package ec.mil.armada.covid19are.activities;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import ec.mil.armada.covid19are.R;
import ec.mil.armada.covid19are.commons.DatePickerFragment;
import ec.mil.armada.covid19are.model.ComplementariasModel;
import ec.mil.armada.covid19are.model.EnfermedadesCatastroficasModel;
import ec.mil.armada.covid19are.model.PacienteConsultaModel;
import ec.mil.armada.covid19are.model.PacienteModel;
import ec.mil.armada.covid19are.model.SintomasModel;

public class VerificacionDatosActivity extends AppCompatActivity {

    //componentes
    private TextInputEditText txtVerificarFechaNacimiento;
    private TextView txtVerificacionNombres, lblSaludo;
    private ProgressBar pgbProcesoVerificacion;
    private Button btnProcesoVerificacion;

    //model
    private PacienteModel paciente;
    private SintomasModel sintomas;
    private EnfermedadesCatastroficasModel enfermedadesCatastroficas;
    private ComplementariasModel complementarias;
    private PacienteConsultaModel pacienteConsulta;

    //firestore
    private FirebaseFirestore dbFirestore;

    //bandera
    private String cedula;
    private Integer intento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verificacion_datos);

        lblSaludo = findViewById(R.id.lblSaludo);
        txtVerificacionNombres = findViewById(R.id.txtVerificacionNombres);
        txtVerificarFechaNacimiento = findViewById(R.id.txtVerificarFechaNacimiento);
        pgbProcesoVerificacion = findViewById(R.id.pgbProcesoVerificacion);
        btnProcesoVerificacion = findViewById(R.id.btnProcesoVerificacion);

        Intent intent = getIntent();
        pacienteConsulta = (PacienteConsultaModel) intent.getParcelableExtra("paciente");
        cedula = intent.getExtras().getString("cedula");

        dbFirestore = FirebaseFirestore.getInstance();

        txtVerificacionNombres.setText(pacienteConsulta.getNombres());

        if (pacienteConsulta.getSexo().equalsIgnoreCase("MASCULINO")) {
            lblSaludo.setText("Bienvenido,");
        } else {
            lblSaludo.setText("Bienvenida,");
        }

        txtVerificarFechaNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        btnProcesoVerificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pgbProcesoVerificacion.setVisibility(View.VISIBLE);
                btnProcesoVerificacion.setVisibility(View.GONE);

                if (TextUtils.isEmpty(txtVerificarFechaNacimiento.getText().toString())){
                    pgbProcesoVerificacion.setVisibility(View.GONE);
                    btnProcesoVerificacion.setVisibility(View.VISIBLE);
                    txtVerificarFechaNacimiento.setError("Campo Requerido");
                    txtVerificarFechaNacimiento.setFocusable(true);
                    txtVerificarFechaNacimiento.setFocusableInTouchMode(true);
                    txtVerificarFechaNacimiento.requestFocus();
                    return;
                }

                if (pacienteConsulta.getFechaNacimiento().equalsIgnoreCase(txtVerificarFechaNacimiento.getText().toString())) {

                    Task tskPaciente = dbFirestore.collection("Pacientes").document(cedula).get();
                    Task tskSintomas = dbFirestore.collection("Pacientes").document(cedula).collection("sintomas").get();
                    Task tskEnfermedades = dbFirestore.collection("Pacientes").document(cedula).collection("enfermedades_catastroficas").document(cedula).get();
                    Task tskComplementarias = dbFirestore.collection("Pacientes").document(cedula).collection("complementarias").document(cedula).get();

                    Task tskCombinadas = Tasks.whenAllSuccess(tskPaciente, tskSintomas, tskEnfermedades, tskComplementarias).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                        @Override
                        public void onSuccess(List<Object> list) {
                            DocumentSnapshot pac = (DocumentSnapshot) list.get(0);
                            paciente = pac.toObject(PacienteModel.class);

                            if (paciente != null) {

                                if (paciente.getEstado().equalsIgnoreCase("pendiente")) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(VerificacionDatosActivity.this);
                                    builder.setTitle("Información");
                                    builder.setMessage("Usted tiene una solicitud de atención por telemedicina pendiente.");
                                    builder.setPositiveButton("OK", null);
                                    AlertDialog dialog = builder.create();
                                    dialog.show();

                                    pgbProcesoVerificacion.setVisibility(View.GONE);
                                    btnProcesoVerificacion.setVisibility(View.VISIBLE);

                                    return;

                                }

                                QuerySnapshot qs = (QuerySnapshot) list.get(1);
                                for (DocumentSnapshot document : qs.getDocuments()) {
                                    sintomas = document.toObject(SintomasModel.class);
                                }

                                DocumentSnapshot ec = (DocumentSnapshot) list.get(2);
                                enfermedadesCatastroficas = ec.toObject(EnfermedadesCatastroficasModel.class);

                                DocumentSnapshot co = (DocumentSnapshot) list.get(3);
                                complementarias = co.toObject(ComplementariasModel.class);

                                Intent intent = new Intent(VerificacionDatosActivity.this, FormularioActivity.class);
                                intent.putExtra("paciente", paciente);
                                intent.putExtra("sintomas", sintomas);
                                intent.putExtra("enfermedadesCatastroficas", enfermedadesCatastroficas);
                                intent.putExtra("complementarias", complementarias);
                                intent.putExtra("resultadoPaciente", "pacienteRegistrado");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            } else {

                                Intent intent = new Intent(VerificacionDatosActivity.this, FormularioActivity.class);
                                intent.putExtra("paciente", pacienteConsulta);
                                intent.putExtra("cedula", cedula);
                                intent.putExtra("resultadoPaciente", "pacienteNuevo");
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                } else {

                    pgbProcesoVerificacion.setVisibility(View.GONE);
                    btnProcesoVerificacion.setVisibility(View.VISIBLE);

                    if (intento < 2) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(VerificacionDatosActivity.this);
                        builder.setTitle("Información");
                        builder.setMessage("La fecha de nacimiento seleccionada no coincide con nuestros registros, por favor intente nuevamente.");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        intento++;
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(VerificacionDatosActivity.this);
                        builder.setTitle("Error");
                        builder.setMessage("Usted a execido la cantidad de intentos permitidos, por favor verifique sus datos e inicie el proceso nuevamente.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(VerificacionDatosActivity.this, ConsultaCedulaActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                }

            }
        });


    }

    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int dayEdit = day;
                String realDay = "";
                if (dayEdit > 0 && dayEdit < 10) {
                    realDay = "0" + dayEdit;
                } else {
                    realDay = String.valueOf(dayEdit);
                }

                int monthEdit = month + 1;
                String realMonth = "";
                if (monthEdit > 0 && monthEdit < 10) {
                    realMonth = "0" + monthEdit;
                } else {
                    realMonth = String.valueOf(monthEdit);
                }
                final String selectedDate = realDay + "/" + realMonth + "/" + year;
                txtVerificarFechaNacimiento.setText(selectedDate);

                if (txtVerificarFechaNacimiento.getError()!=null){
                    txtVerificarFechaNacimiento.setError(null);
                }

            }
        });
        newFragment.show(this.getSupportFragmentManager(), "datePicker");
    }

}
