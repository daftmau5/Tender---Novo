package com.didasko.eduardo.tender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
public class NovaReceita extends AppCompatActivity implements DialogInterface.OnClickListener {

    // constantes para o startactivityforresult
    private final int CAMERA = 1;
    private final int GALERIA = 2;
    // variavel para armazernar a imagem na memoria
    private Bitmap bmpImagem;
    // avriavel para armazenar o arquivo no armazenamento do celular
    private File fileImagem;

    private AlertDialog dialogOpcoes;

    ImageView imagem;
    EditText nome;
    Spinner spTipo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nova_receita);

        nome = (EditText) findViewById(R.id.etNome);
        imagem = (ImageView) findViewById(R.id.imageViewReceita);
        spTipo = (Spinner) findViewById(R.id.sp_tipo);

        // verifica se existe o bundle e se tem um file nele
        if (savedInstanceState != null && savedInstanceState.getSerializable("file") != null) {
            // guarda na variavel fileImagem o file salvo no bundle
            fileImagem = (File) savedInstanceState.getSerializable("file");
            // exibe a imagem no Imageview
            exibirImagem(Uri.fromFile(fileImagem));
        }

        //dialog das opcoes
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] opcoes = new String[]{getString(R.string.camera), getString(R.string.galeria), getString(R.string.visualizar)};
        builder.setItems(opcoes, this);
        builder.setTitle(R.string.slcOpcao);
        dialogOpcoes = builder.create();

        ArrayAdapter<Type> adapter1 = new ArrayAdapter<Type>(this, android.R.layout.simple_spinner_dropdown_item, Type.values());
        spTipo.setAdapter(adapter1);

    }

    public void btImg(View v) {
        dialogOpcoes.show();
    }

    private void exibirImagem(Uri uriImagem) {
        try {
            // instanciar a bmpImagem atravez da Uri
            bmpImagem = MediaStore.Images.Media.getBitmap(getContentResolver(), uriImagem);
            bmpImagem = Bitmap.createScaledBitmap(bmpImagem, 1024, 1024, false);
            // exibir o bmpImagem na imageView
            imagem.setImageBitmap(bmpImagem);
        } catch (Exception e) {
            Log.v("DEURUIM", "DEU RUIM NA FOTO 2");
        }
    }

    @Override
    // metodo chamado para salvar o "estado" ao rotacionar a tela
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // se houver uma imagem
        if (fileImagem != null) {
            outState.putSerializable("file", fileImagem);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Intent intent;
        switch (which) {
            // camera
            case 0:
                // referencia para o diretorio de imagens
                File diretorio = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                // criar o nome completo da imagem que será salva
                String nomeImagem = diretorio.getPath() + "/" + System.currentTimeMillis() + ".jpg";
                // cria o arquivo no celular atravez da variavel fileImagem
                fileImagem = new File(nomeImagem);
                // criar a intent da camera
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // passar para a intent o file onde será graavda a imagem
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(fileImagem));
                // abre a activity no modo result
                startActivityForResult(intent, CAMERA);


                break;
            // galeria
            case 1:
                // cria a intent para galeria
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // inicia a activity no modo result
                startActivityForResult(intent, GALERIA);
                break;
            // visualizar
            case 2:
                // se existir uma imagem
                if (fileImagem != null) {
                    // cria a intent para a visualizacao
                    intent = new Intent(Intent.ACTION_VIEW);
                    // cria uma Uri com fileImagem
                    Uri uri = Uri.fromFile(fileImagem);
                    // passar para a intent a uri, especificando o tipo
                    intent.setDataAndType(uri, "image/*");
                    // inicia a activity
                    startActivity(intent);
                } else {
                    // exibe msg se não existir imagem
                    Log.v("DEURUIM", "DEU RUIM NA FOTO");
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // se foi tudo ok na execução da activity
        if (resultCode == RESULT_OK) {
            // verfica de onde sta vindo
            switch (requestCode) {
                // verifica se esta vindo galeria
                case GALERIA:
                    // recuperar a informacao da imagem selecionada pela intent passada
                    Uri uriImagem = data.getData();
                    // vetor de string com o nome da coluna Data
                    String[] colunaData = {MediaStore.Images.Media.DATA};
                    // cria o cursor para buscar a localização da imagem
                    Cursor cursor = getContentResolver().query(uriImagem, colunaData, null, null, null);

                    // se existir resultado
                    if (cursor.moveToNext()) {
                        // recupera o indice da coluna pelo nome dela
                        int indice = cursor.getColumnIndex(colunaData[0]);
                        // recupera a localizacao do arquivo
                        String caminhoArquivo = cursor.getString(indice);
                        // instanciar o fileImagem com o caminho do arquivo
                        fileImagem = new File(caminhoArquivo);
                    }
                    break;
            }
            //exibe a imagem no ImageView


            exibirImagem(Uri.fromFile(fileImagem));
        }
    }
}
