package ec.mil.armada.covid19are.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ec.mil.armada.covid19are.R;

public class ExitoGuardarActivity extends AppCompatActivity {

    public TextView txtMensaje, txtMensajeSub;
    public Button btnSalir, btnMasInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exito_guardar);

        txtMensaje = findViewById(R.id.lblMensaje);
        txtMensajeSub = findViewById(R.id.lblMensajeSub);
        btnSalir = findViewById(R.id.btnSalir);
        btnMasInfo = findViewById(R.id.btnInformacion);

        Intent intent = getIntent();
        String mensaje = intent.getExtras().getString("mensaje");
        String mensajeSub = intent.getExtras().getString("mensajeSub");

        txtMensaje.setText(mensaje);
        txtMensajeSub.setText(mensajeSub);

        btnMasInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(ExitoGuardarActivity.this, MasInformacionActivitty.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                System.exit(0);

            }
        });

        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                System.exit(0);
            }
        });
    }
}
