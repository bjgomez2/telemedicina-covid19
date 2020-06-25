package ec.mil.armada.covid19are.activities;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import ec.mil.armada.covid19are.R;
import ec.mil.armada.covid19are.model.ComplementariasModel;
import ec.mil.armada.covid19are.model.EnfermedadesCatastroficasModel;
import ec.mil.armada.covid19are.model.PacienteModel;
import ec.mil.armada.covid19are.model.SintomasModel;

public class SintomasActivity extends AppCompatActivity {

    private PacienteModel paciente;
    private SintomasModel sintomas;
    private EnfermedadesCatastroficasModel enfermedadesCatastroficas;
    private ComplementariasModel complementarias;
    private String isFotoURI;

    private Chip chipFiebre, chipTosSeca, chipDolorGarganta, chipDolorMuscular, chipDificultadRespirar, chipMalestarGeneral, chipPerdidaOlfato, chipDiarrea, chipDolorCabeza, chipFlema;
    private TextInputEditText txtNumeroDias;

    private ProgressBar pgbProcesoSistomas;
    private Button btnProcesoSintomas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sintomas);

        Intent intent = getIntent();
        paciente = (PacienteModel) intent.getParcelableExtra("paciente");
        sintomas = (SintomasModel) intent.getParcelableExtra("sintomas");
        enfermedadesCatastroficas = (EnfermedadesCatastroficasModel) intent.getParcelableExtra("enfermedadesCatastroficas");
        complementarias = (ComplementariasModel) intent.getParcelableExtra("complementarias");
        isFotoURI = Objects.requireNonNull(intent.getExtras()).getString("isFotoURI");

        //CHIPS SINTOMAS
        chipFiebre = findViewById(R.id.chipFiebre);
        chipTosSeca = findViewById(R.id.chipTosSeca);
        chipDolorGarganta = findViewById(R.id.chipDolorGarganta);
        chipDolorMuscular = findViewById(R.id.chipDolorMuscular);
        chipDificultadRespirar = findViewById(R.id.chipDificultadRespiratoriaFatiga);
        chipMalestarGeneral = findViewById(R.id.chipMalestarGeneral);
        chipPerdidaOlfato = findViewById(R.id.chipPerdidaOlfato);
        chipDiarrea = findViewById(R.id.chipDiarrea);
        chipDolorCabeza = findViewById(R.id.chipDolorCabeza);
        chipFlema = findViewById(R.id.chipFlema);

        txtNumeroDias = findViewById(R.id.txtNumeroDias);

        pgbProcesoSistomas = findViewById(R.id.pgbProcesoSistomas);
        btnProcesoSintomas = findViewById(R.id.btnProcesoSintomas);

        if (sintomas != null) {
            if (sintomas.getFiebre()) {
                chipFiebre.setChecked(true);
            }
            if (sintomas.getTosSeca()) {
                chipTosSeca.setChecked(true);
            }
            if (sintomas.getDolorGarganta()) {
                chipDolorGarganta.setChecked(true);
            }
            if (sintomas.getDolorMuscular()) {
                chipDolorMuscular.setChecked(true);
            }
            if (sintomas.getDificultadRespirar()) {
                chipDificultadRespirar.setChecked(true);
            }
            if (sintomas.getMalestarGeneral()) {
                chipMalestarGeneral.setChecked(true);
            }
            if (sintomas.getPerdidaOlfato()) {
                chipPerdidaOlfato.setChecked(true);
            }
            if (sintomas.getDiarrea()) {
                chipDiarrea.setChecked(true);
            }
            if (sintomas.getDolorCabeza()) {
                chipDolorCabeza.setChecked(true);
            }
            if (sintomas.getFlema()) {
                chipFlema.setChecked(true);
            }
        } else {
            sintomas = new SintomasModel();
            sintomas.setFiebre(false);
            sintomas.setTosSeca(false);
            sintomas.setDolorGarganta(false);
            sintomas.setDolorMuscular(false);
            sintomas.setDificultadRespirar(false);
            sintomas.setMalestarGeneral(false);
            sintomas.setPerdidaOlfato(false);
            sintomas.setDiarrea(false);
            sintomas.setDolorCabeza(false);
            sintomas.setFlema(false);
        }

        //FIEBRE  ----> 1
        chipFiebre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipFiebre.isChecked()) {
                    sintomas.setFiebre(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 1);
                } else {
                    sintomas.setFiebre(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 1);
                }
            }
        });

        //TOS SECA ----> 2
        chipTosSeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipTosSeca.isChecked()) {
                    sintomas.setTosSeca(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 2);
                } else {
                    sintomas.setTosSeca(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 2);
                }
            }
        });

        //DOLOR DE GARGANTA ----> 3
        chipDolorGarganta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipDolorGarganta.isChecked()) {
                    sintomas.setDolorGarganta(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 3);
                } else {
                    sintomas.setDolorGarganta(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 3);
                }
            }
        });

        //DOLOR MUSCULAR ----> 4
        chipDolorMuscular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipDolorMuscular.isChecked()) {
                    sintomas.setDolorMuscular(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 4);
                } else {
                    sintomas.setDolorMuscular(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 4);
                }
            }
        });

        //DIFICULTAD RESPIRATORIA Y FATIGA ----> 5
        chipDificultadRespirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipDificultadRespirar.isChecked()) {
                    sintomas.setDificultadRespirar(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 5);
                } else {
                    sintomas.setDificultadRespirar(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 5);
                }
            }
        });

        //MALESTAR GENERAL ----> 6
        chipMalestarGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipMalestarGeneral.isChecked()) {
                    sintomas.setMalestarGeneral(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 6);
                } else {
                    sintomas.setMalestarGeneral(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 6);
                }
            }
        });

        //PERDIDA OLFATO ----> 7
        chipPerdidaOlfato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipPerdidaOlfato.isChecked()) {
                    sintomas.setPerdidaOlfato(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 7);
                } else {
                    sintomas.setPerdidaOlfato(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 7);
                }
            }
        });

        //DIARREA ----> 8
        chipDiarrea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipDiarrea.isChecked()) {
                    sintomas.setDiarrea(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 8);
                } else {
                    sintomas.setDiarrea(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 8);
                }
            }
        });

        //DOLOR CABEZA ----> 9
        chipDolorCabeza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipDolorCabeza.isChecked()) {
                    sintomas.setDolorCabeza(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 9);
                } else {
                    sintomas.setDolorCabeza(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 9);
                }
            }
        });

        //FLEMA ----> 10
        chipFlema.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipFlema.isChecked()) {
                    sintomas.setFlema(true);
                    paciente.setPonderacion(paciente.getPonderacion() + 10);
                } else {
                    sintomas.setFlema(false);
                    paciente.setPonderacion(paciente.getPonderacion() - 10);
                }
            }
        });

        txtNumeroDias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final android.app.AlertDialog.Builder d = new android.app.AlertDialog.Builder(SintomasActivity.this);
                LayoutInflater inflater = SintomasActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_number, null);
                d.setMessage("Seleccione el número de días");
                d.setView(dialogView);
                d.setCancelable(false);
                final NumberPicker numberPicker = (NumberPicker) dialogView.findViewById(R.id.dialog_number_picker);
                numberPicker.setMaxValue(15);
                numberPicker.setMinValue(0);
                numberPicker.setDisplayedValues( new String[] { "Hoy", "1 día", "2 días", "3 días", "4 días", "5 días", "6 días", "7 días", "8 días", "9 días", "10 días", "11 días", "12 días", "13 días", "14 días", "15 días" } );
                numberPicker.setWrapSelectorWheel(false);
                numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        Log.d("", "onValueChange: ");
                    }
                });
                d.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int number = numberPicker.getValue();
                        sintomas.setTiempo(String.valueOf(number));

                        if (number == 0){
                            txtNumeroDias.setText("Hoy");
                        }

                        if (number == 1){
                            txtNumeroDias.setText("Hace " +number + " día");
                        }

                        if (number > 1){
                            txtNumeroDias.setText("Hace " +number + " días");
                        }

                        if (txtNumeroDias.getError()!=null){
                            txtNumeroDias.setError(null);
                        }
                    }
                });
                android.app.AlertDialog alertDialog = d.create();
                alertDialog.show();
            }
        });



        btnProcesoSintomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pgbProcesoSistomas.setVisibility(View.VISIBLE);
                btnProcesoSintomas.setVisibility(View.GONE);

                if (!sintomas.getFiebre() && !sintomas.getTosSeca() && !sintomas.getDolorGarganta() && !sintomas.getDolorMuscular() &&
                        !sintomas.getDificultadRespirar() && !sintomas.getMalestarGeneral() && !sintomas.getPerdidaOlfato() &&
                        !sintomas.getDiarrea() && !sintomas.getDolorCabeza() && !sintomas.getFlema()) {

                    pgbProcesoSistomas.setVisibility(View.GONE);
                    btnProcesoSintomas.setVisibility(View.VISIBLE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(SintomasActivity.this);
                    builder.setTitle("Información");
                    builder.setMessage("Para continuar con el registro usted debe seleccionar al menos un síntoma.");
                    builder.setPositiveButton("OK", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }

                if (TextUtils.isEmpty(txtNumeroDias.getText().toString())){
                    pgbProcesoSistomas.setVisibility(View.GONE);
                    btnProcesoSintomas.setVisibility(View.VISIBLE);
                    txtNumeroDias.setError("Campo Requerido");
                    txtNumeroDias.setFocusable(true);
                    txtNumeroDias.setFocusableInTouchMode(true);
                    txtNumeroDias.requestFocus();
                    return;
                }

                pgbProcesoSistomas.setVisibility(View.GONE);
                btnProcesoSintomas.setVisibility(View.VISIBLE);
                Intent intent = new Intent(SintomasActivity.this, ComplementariaActivity.class);
                intent.putExtra("paciente", paciente);
                intent.putExtra("sintomas", sintomas);
                intent.putExtra("enfermedadesCatastroficas", enfermedadesCatastroficas);
                intent.putExtra("complementarias", complementarias);
                intent.putExtra("isFotoURI", isFotoURI);
                startActivity(intent);
            }
        });
    }


}
