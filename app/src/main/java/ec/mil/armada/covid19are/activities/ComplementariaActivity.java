package ec.mil.armada.covid19are.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import ec.mil.armada.covid19are.R;
import ec.mil.armada.covid19are.model.ComplementariasModel;
import ec.mil.armada.covid19are.model.EnfermedadesCatastroficasModel;
import ec.mil.armada.covid19are.model.PacienteModel;
import ec.mil.armada.covid19are.model.SintomasModel;

public class ComplementariaActivity extends AppCompatActivity {

    private PacienteModel paciente;
    private SintomasModel sintomas;
    private EnfermedadesCatastroficasModel enfermedadesCatastroficas;
    private ComplementariasModel complementarias;
    private String isFotoURI;
    private Uri fotoURI;

    private Chip chipCancer, chipInsuficienciaRenalCronica, chipInmunoDeprimidos, chipDiabetico, chipHipertenso, chipEnfermedadPulmonarCronica, chipEnfermedadCardiovascular, chipEnfermedadCerebrovascular;

    private CheckBox cbxEnfermedadesCatastroficas;
    private CheckBox cbxViajoAlExterior, cbxRecibioFamiliar, cbxHospedoPersona, cbxCuidoEnfermo, cbxTuvoContactoSintomas, cbxTomoMedicamento, cbxVacunadoInfluenza, cbxVacunadoNeumonia, cbxAtencionHospitalaria;

    private LinearLayout lyEnfermedadesCatastroficas;

    private ProgressBar pgbProcesoGuardar;
    private Button btnProcesoGuardar;

    private FirebaseUser userAnonymous;

    //firestore
    private FirebaseFirestore dbFirestore;
    private DocumentReference pacienteRef;
    private DocumentReference enfermedadesRef;
    private DocumentReference complementariasRef;
    private CollectionReference sintomasRef;

    //firebase storage and StorageReference
    private FirebaseStorage dbStorage;
    private StorageReference storageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complementaria);

        //CHIPS ENFERMEDADES CATASTROFICAS
        chipCancer = findViewById(R.id.chipCancer);
        chipInsuficienciaRenalCronica = findViewById(R.id.chipInsuficienciaRenalCronica);
        chipInmunoDeprimidos = findViewById(R.id.chipInmunoDeprimidos);
        chipDiabetico = findViewById(R.id.chipDiabetico);
        chipHipertenso = findViewById(R.id.chipHipertenso);
        chipEnfermedadPulmonarCronica = findViewById(R.id.chipEnfermedadPulmonarCronica);
        chipEnfermedadCardiovascular = findViewById(R.id.chipEnfermedadCardiovascular);
        chipEnfermedadCerebrovascular = findViewById(R.id.chipEnfermedadCerebrovascular);
        cbxViajoAlExterior = findViewById(R.id.cbxViajoAlExterior);
        cbxRecibioFamiliar = findViewById(R.id.cbxRecibioFamiliar);
        cbxHospedoPersona = findViewById(R.id.cbxHospedoPersona);
        cbxCuidoEnfermo = findViewById(R.id.cbxCuidoEnfermo);
        cbxTuvoContactoSintomas = findViewById(R.id.cbxTuvoContactoSintomas);
        cbxTomoMedicamento = findViewById(R.id.cbxTomoMedicamento);
        cbxVacunadoInfluenza = findViewById(R.id.cbxVacunadoInfluenza);
        cbxVacunadoNeumonia = findViewById(R.id.cbxVacunadoNeumonia);
        cbxAtencionHospitalaria = findViewById(R.id.cbxAtencionHospitalaria);

        btnProcesoGuardar = findViewById(R.id.btnProcesoGuardar);
        pgbProcesoGuardar = findViewById(R.id.pgbProcesoGuardar);

        lyEnfermedadesCatastroficas = findViewById(R.id.lyEnfermedadesCatastroficas);
        cbxEnfermedadesCatastroficas = findViewById(R.id.cbxEnfermedadesCatastroficas);

        dbFirestore = FirebaseFirestore.getInstance();
        dbStorage = FirebaseStorage.getInstance();

        Intent intent = getIntent();
        paciente = intent.getParcelableExtra("paciente");
        sintomas = intent.getParcelableExtra("sintomas");
        enfermedadesCatastroficas = intent.getParcelableExtra("enfermedadesCatastroficas");
        complementarias = intent.getParcelableExtra("complementarias");

        isFotoURI = Objects.requireNonNull(intent.getExtras()).getString("isFotoURI");

        assert isFotoURI != null;
        if (isFotoURI.equalsIgnoreCase("YES")) {
            fotoURI = Uri.parse(paciente.getFoto());
        }

        userAnonymous = FirebaseAuth.getInstance().getCurrentUser();

        storageRef = dbStorage.getReference("FotosPacientes/");

        pacienteRef = dbFirestore.collection("Pacientes").document(paciente.getCedula());
        sintomasRef = dbFirestore.collection("Pacientes").document(paciente.getCedula()).collection("sintomas");
        enfermedadesRef = dbFirestore.collection("Pacientes").document(paciente.getCedula()).collection("enfermedades_catastroficas").document(paciente.getCedula());
        complementariasRef = dbFirestore.collection("Pacientes").document(paciente.getCedula()).collection("complementarias").document(paciente.getCedula());

        if (complementarias != null && complementarias.getEnfermedadesCatastroficas()) {
            cbxEnfermedadesCatastroficas.setChecked(true);
            lyEnfermedadesCatastroficas.setVisibility(View.VISIBLE);

            if (enfermedadesCatastroficas.getCancer()) {
                chipCancer.setChecked(true);
            }

            if (enfermedadesCatastroficas.getInsuficienciaRenalCronica()) {
                chipInsuficienciaRenalCronica.setChecked(true);
            }

            if (enfermedadesCatastroficas.getInmunoDeprimidos()) {
                chipInmunoDeprimidos.setChecked(true);
            }

            if (enfermedadesCatastroficas.getDiabetico()) {
                chipDiabetico.setChecked(true);
            }

            if (enfermedadesCatastroficas.getHipertenso()) {
                chipHipertenso.setChecked(true);
            }

            if (enfermedadesCatastroficas.getEnfermedadPulmonarCronica()) {
                chipEnfermedadPulmonarCronica.setChecked(true);
            }

            if (enfermedadesCatastroficas.getEnfermedadCardiovascular()) {
                chipEnfermedadCardiovascular.setChecked(true);
            }

            if (enfermedadesCatastroficas.getEnfermedadCerebrovascular()) {
                chipEnfermedadCerebrovascular.setChecked(true);
            }
        }

        if (complementarias != null) {

            if (complementarias.getCuidoEnfermo()) {
                cbxCuidoEnfermo.setChecked(true);
            }
            if (complementarias.getAtencionHospitalaria()) {
                cbxAtencionHospitalaria.setChecked(true);
            }
            if (complementarias.getHospedoPersona()) {
                cbxHospedoPersona.setChecked(true);
            }
            if (complementarias.getTomoMedicamento()) {
                cbxTomoMedicamento.setChecked(true);
            }
            if (complementarias.getVacunadoInfluenza()) {
                cbxVacunadoInfluenza.setChecked(true);
            }
            if (complementarias.getVacunadoNeumonia()) {
                cbxVacunadoNeumonia.setChecked(true);
            }
            if (complementarias.getViajoAlExterior()) {
                cbxViajoAlExterior.setChecked(true);
            }
            if (complementarias.getTuvoContactoSintomas()) {
                cbxTuvoContactoSintomas.setChecked(true);
            }
            if (complementarias.getRecibioFamiliar()) {
                cbxRecibioFamiliar.setChecked(true);
            }

        } else {
            complementarias = new ComplementariasModel();
            complementarias.setCuidoEnfermo(false);
            complementarias.setAtencionHospitalaria(false);
            complementarias.setHospedoPersona(false);
            complementarias.setTomoMedicamento(false);
            complementarias.setVacunadoInfluenza(false);
            complementarias.setVacunadoNeumonia(false);
            complementarias.setViajoAlExterior(false);
            complementarias.setTuvoContactoSintomas(false);
            complementarias.setRecibioFamiliar(false);
            complementarias.setEnfermedadesCatastroficas(false);
        }

        cbxEnfermedadesCatastroficas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxEnfermedadesCatastroficas.isChecked()) {
                    paciente.setPonderacion(paciente.getPonderacion() + 7);
                    lyEnfermedadesCatastroficas.setVisibility(View.VISIBLE);
                    complementarias.setEnfermedadesCatastroficas(true);

                    enfermedadesCatastroficas = new EnfermedadesCatastroficasModel();
                    enfermedadesCatastroficas.setCancer(false);
                    enfermedadesCatastroficas.setInsuficienciaRenalCronica(false);
                    enfermedadesCatastroficas.setInmunoDeprimidos(false);
                    enfermedadesCatastroficas.setDiabetico(false);
                    enfermedadesCatastroficas.setHipertenso(false);
                    enfermedadesCatastroficas.setEnfermedadPulmonarCronica(false);
                    enfermedadesCatastroficas.setEnfermedadCardiovascular(false);
                    enfermedadesCatastroficas.setEnfermedadCerebrovascular(false);

                } else {
                    paciente.setPonderacion(paciente.getPonderacion() - 7);
                    lyEnfermedadesCatastroficas.setVisibility(View.GONE);
                    complementarias.setEnfermedadesCatastroficas(false);
                    enfermedadesCatastroficas = null;

                    chipCancer.setChecked(false);
                    chipInsuficienciaRenalCronica.setChecked(false);
                    chipDiabetico.setChecked(false);
                    chipHipertenso.setChecked(false);
                    chipInmunoDeprimidos.setChecked(false);
                    chipEnfermedadPulmonarCronica.setChecked(false);
                    chipEnfermedadCardiovascular.setChecked(false);
                    chipEnfermedadCerebrovascular.setChecked(false);

                }
            }
        });

        //CLICK EVENTS ENFERMEDADES CATASTROFICAS
        chipCancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipCancer.isChecked()) {
                    enfermedadesCatastroficas.setCancer(true);
                } else {
                    enfermedadesCatastroficas.setCancer(false);
                }
            }
        });

        chipInsuficienciaRenalCronica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipInsuficienciaRenalCronica.isChecked()) {
                    enfermedadesCatastroficas.setInsuficienciaRenalCronica(true);
                } else {
                    enfermedadesCatastroficas.setInsuficienciaRenalCronica(false);
                }
            }
        });

        chipInmunoDeprimidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipInmunoDeprimidos.isChecked()) {
                    enfermedadesCatastroficas.setInmunoDeprimidos(true);
                } else {
                    enfermedadesCatastroficas.setInmunoDeprimidos(false);
                }
            }
        });

        chipDiabetico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipDiabetico.isChecked()) {
                    enfermedadesCatastroficas.setDiabetico(true);
                } else {
                    enfermedadesCatastroficas.setDiabetico(false);
                }
            }
        });

        chipHipertenso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipHipertenso.isChecked()) {
                    enfermedadesCatastroficas.setHipertenso(true);
                } else {
                    enfermedadesCatastroficas.setHipertenso(false);
                }
            }
        });

        chipEnfermedadPulmonarCronica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipEnfermedadPulmonarCronica.isChecked()) {
                    enfermedadesCatastroficas.setEnfermedadPulmonarCronica(true);
                } else {
                    enfermedadesCatastroficas.setEnfermedadPulmonarCronica(false);
                }
            }
        });

        chipEnfermedadCardiovascular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipEnfermedadCardiovascular.isChecked()) {
                    enfermedadesCatastroficas.setEnfermedadCardiovascular(true);
                } else {
                    enfermedadesCatastroficas.setEnfermedadCardiovascular(false);
                }
            }
        });

        chipEnfermedadCerebrovascular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chipEnfermedadCerebrovascular.isChecked()) {
                    enfermedadesCatastroficas.setEnfermedadCerebrovascular(true);
                } else {
                    enfermedadesCatastroficas.setEnfermedadCerebrovascular(false);
                }
            }
        });

        //PREGUNTAS COMPLEMENTARIAS
        cbxViajoAlExterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxViajoAlExterior.isChecked()) {
                    complementarias.setViajoAlExterior(true);
                } else {
                    complementarias.setViajoAlExterior(false);
                }
            }
        });
        cbxRecibioFamiliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxRecibioFamiliar.isChecked()) {
                    complementarias.setRecibioFamiliar(true);
                } else {
                    complementarias.setRecibioFamiliar(false);
                }
            }
        });
        cbxHospedoPersona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxHospedoPersona.isChecked()) {
                    complementarias.setHospedoPersona(true);
                } else {
                    complementarias.setHospedoPersona(false);
                }
            }
        });
        cbxCuidoEnfermo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxCuidoEnfermo.isChecked()) {
                    complementarias.setCuidoEnfermo(true);
                } else {
                    complementarias.setCuidoEnfermo(false);
                }
            }
        });
        cbxTuvoContactoSintomas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxTuvoContactoSintomas.isChecked()) {
                    complementarias.setTuvoContactoSintomas(true);
                } else {
                    complementarias.setTuvoContactoSintomas(false);
                }
            }
        });
        cbxTomoMedicamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxTomoMedicamento.isChecked()) {
                    complementarias.setTomoMedicamento(true);
                } else {
                    complementarias.setTomoMedicamento(false);
                }
            }
        });
        cbxVacunadoInfluenza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxVacunadoInfluenza.isChecked()) {
                    complementarias.setVacunadoInfluenza(true);
                } else {
                    complementarias.setVacunadoInfluenza(false);
                }
            }
        });
        cbxVacunadoNeumonia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxVacunadoNeumonia.isChecked()) {
                    complementarias.setVacunadoNeumonia(true);
                } else {
                    complementarias.setVacunadoNeumonia(false);
                }
            }
        });
        cbxAtencionHospitalaria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxAtencionHospitalaria.isChecked()) {
                    complementarias.setAtencionHospitalaria(true);
                } else {
                    complementarias.setAtencionHospitalaria(false);
                }
            }
        });


        btnProcesoGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnProcesoGuardar.setVisibility(View.GONE);
                pgbProcesoGuardar.setVisibility(View.VISIBLE);


                if (complementarias.getEnfermedadesCatastroficas()) {
                    if (!enfermedadesCatastroficas.getCancer() && !enfermedadesCatastroficas.getEnfermedadCerebrovascular() && !enfermedadesCatastroficas.getEnfermedadCardiovascular() && !enfermedadesCatastroficas.getHipertenso() && !enfermedadesCatastroficas.getInsuficienciaRenalCronica() && !enfermedadesCatastroficas.getInmunoDeprimidos() && !enfermedadesCatastroficas.getEnfermedadPulmonarCronica() && !enfermedadesCatastroficas.getDiabetico()) {
                        btnProcesoGuardar.setVisibility(View.VISIBLE);
                        pgbProcesoGuardar.setVisibility(View.GONE);

                        AlertDialog.Builder builder = new AlertDialog.Builder(ComplementariaActivity.this);
                        builder.setTitle("Información");
                        builder.setMessage("Para continuar con el registro usted debe seleccionar al menos una enfermedad catastrófica.");
                        builder.setPositiveButton("OK", null);
                        AlertDialog dialog = builder.create();
                        dialog.show();

                        return;
                    }
                }

                subirDatosPaciente();

                if (enfermedadesCatastroficas != null) {
                    enfermedadesRef.set(enfermedadesCatastroficas).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i("Exito", "Se guardo las enfermedades");
                        }
                    });
                } else {
                    enfermedadesRef.delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Exito", "Enfermedades catastroficas eliminadas");
                                }
                            });
                }


                complementariasRef.set(complementarias).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Exito", "Se guardo las complementarias");
                    }
                });

                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyyhhmmssMs");
                String claveTiempo = formatter.format(date);

                sintomas.setFecha(new Timestamp(new Date()));

                sintomasRef.document(claveTiempo).set(sintomas).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("Exito", "Se guardo los sintomas");
                    }
                });

                String mensaje = "Estimado(a) " + paciente.getNombres() + ", los datos fueron almacenados con éxito";

                String mensajeSub = "Pronto nos contactaremos vía email a la dirección de correo electrónico que proporcionaste, donde indicaremos la fecha y hora en que será atendido por telemedicina.";

                Intent intent = new Intent(ComplementariaActivity.this, ExitoGuardarActivity.class);
                intent.putExtra("mensaje", mensaje);
                intent.putExtra("mensajeSub", mensajeSub);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();

            }
        });

    }

    public void subirDatosPaciente() {

        paciente.setFechaRegistro(new Timestamp(new Date()));
        paciente.setEstado("pendiente");

        if (fotoURI != null) {
            String extension = fotoURI.toString().substring(fotoURI.toString().lastIndexOf(".") + 1);
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/" + extension)
                    .build();

            final StorageReference ref = storageRef.child(paciente.getCedula() + "." + extension);
            UploadTask uploadTask = ref.putFile(fotoURI, metadata);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return ref.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        paciente.setFoto(downloadUri.toString());
                        pacienteRef.set(paciente).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                assert userAnonymous != null;
                                userAnonymous.delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("TAG", "User annonymous account deleted.");
                                                }
                                            }
                                        });

                                Log.i("Exito", "Se guardo el paciente con foto");
                            }
                        });
                        Log.i("Exito", "Nueva uri seteada --->>>> " + downloadUri.toString());
                    }
                }
            });

        } else {
            pacienteRef.set(paciente).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    assert userAnonymous != null;
                    userAnonymous.delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User annonymous account deleted.");
                                    }
                                }
                            });
                    Log.i("Exito", "Se guardo el paciente sin foto");
                }
            });
        }

    }


}
