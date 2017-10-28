package com.example.android.personasmaterialdiplomado;

import android.content.Intent;
import android.content.res.Resources;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class Modificar extends AppCompatActivity {
    private EditText txtCedulam, txtNombrem, txtApellidom;
    private TextInputLayout cajaCedulam, cajaNombrem, cajaApellidom;
    private ArrayList<Integer> fotos;
    private Resources res;
    private Spinner sexom;
    private int foto, sexo;
    private ArrayAdapter<String> adapter;
    private String[] opcm;
    private Bundle bundle, b3;
    private Intent i;
    private String cedula, nombre, apellido,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        txtCedulam = (EditText) findViewById(R.id.txtCedulam);
        txtNombrem = (EditText) findViewById(R.id.txtNombrem);
        txtApellidom = (EditText) findViewById(R.id.txtApellidom);

        res = this.getResources();
        cajaCedulam = (TextInputLayout) findViewById(R.id.cajaCedulam);
        cajaNombrem = (TextInputLayout) findViewById(R.id.cajaNombrem);
        cajaApellidom = (TextInputLayout) findViewById(R.id.cajaApellidom);
        sexom = (Spinner) findViewById(R.id.cmbSexom);
        opcm = res.getStringArray(R.array.sexo);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,opcm);
        sexom.setAdapter(adapter);

        i = getIntent();
        bundle = i.getBundleExtra("datos");
        id= bundle.getString("id");
        foto = bundle.getInt("foto");
        cedula = bundle.getString("cedula");
        nombre = bundle.getString("nombre");
        apellido = bundle.getString("apellido");
        sexo = bundle.getInt("sexo");

        txtCedulam.setText(cedula);
        txtNombrem.setText(nombre);
        txtApellidom.setText(apellido);
        sexom.setSelection(sexo);

    }

    public void editar(View v){
        if (validarm()){
            Persona p = new Persona(id,foto, txtCedulam.getText().toString(), txtNombrem.getText().toString(), txtApellidom.getText().toString(),sexom.getSelectedItemPosition());
            p.editar();
            Snackbar.make(v, res.getString(R.string.mensaje_guardado), Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            onBackPressedE();
        }
    }

    public boolean validarm(){
        if (validar_auxE(txtCedulam, cajaCedulam)) return false;
        else if (validar_auxE(txtNombrem, cajaNombrem)) return false;
        else if (validar_auxE(txtApellidom, cajaApellidom)) return false;
        else if (Metodos.persona_editar(Datos.obtenerPersonas(), txtCedulam.getText().toString(),cedula)) {
            txtCedulam.setError(res.getString(R.string.persona_existente_error));
            txtCedulam.requestFocus();
            return false;
        }
        return true;
    }

    public boolean validar_auxE(TextView t, TextInputLayout ct){
        if (t.getText().toString().isEmpty()){
            t.requestFocus();
            t.setError("No puede ser vacio");
            return true;
        }
        return false;
    }

    public void onBackPressedE(){
        Intent i = new Intent(this, DetallePersona.class);
        Bundle b3 = new Bundle();
        b3.putString("id",id);
        b3.putInt("foto",foto);
        b3.putString("cedula", txtCedulam.getText().toString());
        b3.putString("nombre", txtNombrem.getText().toString());
        b3.putString("apellido", txtApellidom.getText().toString());
        b3.putInt("sexo",sexom.getSelectedItemPosition());
        i.putExtra("datos",b3);
        startActivity(i);
    }

    }

