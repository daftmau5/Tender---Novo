package com.didasko.eduardo.tender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.File;

/**
 * Created by Eduardo on 11/07/2016.
 */
public class VerReceita extends AppCompatActivity {
    EditText etNome;
    Spinner tipo;
    Recipe receita;
    ImageView ivFoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ver_receita);

        etNome = (EditText) findViewById(R.id.etNome);
        tipo = (Spinner) findViewById(R.id.sp_tipoV);
        ivFoto = (ImageView) findViewById(R.id.ivReceita);
        tipo.setEnabled(false);

        ArrayAdapter<Type> adapter1 = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_dropdown_item, Type.values());
        tipo.setAdapter(adapter1);

        if (getIntent().hasExtra("receita")) {
            receita = (Recipe) getIntent().getSerializableExtra("receita");
            etNome.setText(String.valueOf(receita.getDescription()));

            File imgFile = new  File(receita.getImagePath());
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivFoto.setImageBitmap(myBitmap);
            }

            int posTipo = adapter1.getPosition(receita.getTipo());
            tipo.setSelection(posTipo);
        }
    }
}
