package com.example.android.personasmaterialdiplomado;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Modificar extends AppCompatActivity {
    private EditText txtCedulam, txtNombrem, txtApellidom;
    private TextInputLayout cajaCedulam, cajaNombrem, cajaApellidom;
    private ArrayList<Integer> fotos;
    private Resources res;
    private Spinner sexom;
    private int sexo;
    private ArrayAdapter<String> adapter;
    private String[] opcm;
    private Bundle bundle, b3;
    private Intent i;
    private String cedula, nombre, apellido,id ,foto;
    private ImageView FotoC;
    private Uri filepath;
    private  StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        storageReference = FirebaseStorage.getInstance().getReference();
        txtCedulam = (EditText) findViewById(R.id.txtCedulam);
        txtNombrem = (EditText) findViewById(R.id.txtNombrem);
        txtApellidom = (EditText) findViewById(R.id.txtApellidom);
        FotoC = (ImageView)findViewById(R.id.FotoActualizar);

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
        foto = bundle.getString("foto");
        cedula = bundle.getString("cedula");
        nombre = bundle.getString("nombre");
        apellido = bundle.getString("apellido");
        sexo = bundle.getInt("sexo");

        txtCedulam.setText(cedula);
        txtNombrem.setText(nombre);
        txtApellidom.setText(apellido);
        sexom.setSelection(sexo);
        storageReference.child(foto).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(Modificar.this).load(uri).into(FotoC);
            }
        });

    }

    public void editar(View v){
        String nom = txtNombrem.getText().toString();
        String ape = txtApellidom.getText().toString();
        String ced = txtCedulam.getText().toString();
        Persona p = new Persona(id,foto,ced,nom,ape,sexom.getSelectedItemPosition());

        if(cedula.equals(ced)){

            p.editar();
            if (filepath !=null)SubirFoto(foto);
            Snackbar.make(v, res.getString(R.string.mensaje_exito_modificar), Snackbar.LENGTH_LONG).setAction("action", null).show();
            // Cancelar();
        }else{
            if(Metodos.existencia_persona(Datos.obtenerPersonas(),ced)){
                txtCedulam.setError(res.getString(R.string.persona_existente_error));
                txtCedulam.requestFocus();
            }else{
                p.editar();
                if (filepath !=null)SubirFoto(foto);
                Snackbar.make(v, res.getString(R.string.mensaje_exito_modificar), Snackbar.LENGTH_LONG).setAction("action", null).show();
                // Cancelar();
            }
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

    public void seleccionar_foto(View V){
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,res.getString(R.string.mensaje_seleccion)),1);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==1){
            filepath =data.getData();
            if (filepath !=null){
                FotoC.setImageURI(filepath);
            }
        }
    }


    public  void  SubirFoto(String foto){
        StorageReference childRef = storageReference.child(foto);
        UploadTask uploadTask = childRef.putFile(filepath);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Cancelar();
            }
        });
    }
    public void Cancelar(View v){
        Cancelar();
    }
    public void Cancelar(){
       /* String nom = txtnombre.getText().toString();
        String ape = txtapellido.getText().toString();
        String ced = txtcedula.getText().toString();
        Intent i = new Intent(Modificar.this,DetallePersona.class);
        Bundle b = new Bundle();
        b.putString("cedula",ced);
        b.putString("nombre",nom);
        b.putString("apellido",ape);
        b.putInt("sexo",genero_spiner.getSelectedItemPosition());
        b.putInt("foto",fot);
        i.putExtra("datos",b);
        startActivity(i);*/
        finish();
        Intent i = new Intent(Modificar.this,Principal.class);
        startActivity(i);

    }

    public void onBackPressedE(){
       Cancelar();
    }

    }

