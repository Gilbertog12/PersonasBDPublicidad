package com.example.android.personasmaterialdiplomado;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetallePersona extends AppCompatActivity {
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Persona p;
    private String cedula,nombre,apellido,id,fot;
    private int sexo;
    private Bundle bundle;
    private Intent i;
    private ImageView foto;
    private Resources res;
    private TextView ced,nomb,app,sex;
    private String [] opc;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_persona);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar2);
       setSupportActionBar(toolbar);
        storageReference = FirebaseStorage.getInstance().getReference();
        ced =(TextView)findViewById(R.id.lblCedulaD);
        nomb = (TextView)findViewById(R.id.lblNombreD);
        app = (TextView)findViewById(R.id.lblApellidoD);
        sex = (TextView)findViewById(R.id.lblSexoD);

        collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar);
        foto = (ImageView) findViewById(R.id.fotoPersona);
        res = this.getResources();
        i = getIntent();
        bundle = i.getBundleExtra("datos");
        id = bundle.getString("id");
        cedula = bundle.getString("cedula");
        nombre = bundle.getString("nombre");
        apellido = bundle.getString("apellido");
        fot = bundle.getString("foto");
        sexo = bundle.getInt("sexo");

        opc = res.getStringArray(R.array.sexo);


        collapsingToolbarLayout.setTitle(nombre+" "+apellido);
        //foto.setImageDrawable(ResourcesCompat.getDrawable(res,fot,null));
        storageReference.child(fot).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(DetallePersona.this).load(uri).into(foto);
            }
        });
        ced.setText(cedula);
        nomb.setText(nombre);
        app.setText(apellido);
        sex.setText(opc[sexo]);


    }
    public void editar(View v){
        Intent i = new Intent(DetallePersona.this, Modificar.class);
        Bundle b2 = new Bundle();
        b2.putString("id",id);
        b2.putString("foto",fot);
        b2.putString("cedula",cedula);
        b2.putString("nombre",nombre);
        b2.putString("apellido",apellido);
        b2.putInt("sexo",sexo);
        i.putExtra("datos",b2);
        startActivity(i);
    }

    public void eliminar(View v){
        String positivo,negativo;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(res.getString(R.string.titulo_eliminar_mensaje));
        builder.setMessage(res.getString(R.string.eliminar_mensaje));
        positivo = res.getString(R.string.si_eliminar_mensaje);
        negativo = res.getString(R.string.no_eliminar_mensaje);



        builder.setPositiveButton(positivo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Persona p = new Persona(id);
                Datos.eliminarPersona(p);
                onBackPressed();

            }
        });
        builder.setNegativeButton(negativo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();



    }
    public void onBackPressed(){
        finish();
        Intent i = new Intent(DetallePersona.this,Principal.class);
        startActivity(i);
    }
}
