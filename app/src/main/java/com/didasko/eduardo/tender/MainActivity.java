package com.didasko.eduardo.tender;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.didasko.eduardo.tender.tindercard.FlingCardListener;
import com.didasko.eduardo.tender.tindercard.SwipeFlingAdapterView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, FlingCardListener.ActionDownInterface{
    public static MyAppAdapter myAppAdapter;
    public static ViewHolder viewHolder;
    private ArrayList<Recipe> al;
    private SwipeFlingAdapterView flingContainer;
    private static final int VER = 0;

    ImageView user_picture;
    NavigationView navigation_view;

    JSONObject response, profile_pic_data, profile_pic_url;




    public static void removeBackground() {


        viewHolder.background.setVisibility(View.GONE);
        myAppAdapter.notifyDataSetChanged();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tender Alpha");

        Intent intent = getIntent();
        String jsondata = intent.getStringExtra("jsondata");

        setNavigationHeader();    // call setNavigationHeader Method.
        setUserProfile(jsondata);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        new EndpointsAsyncTask().execute(new Pair<Context, String>(this, "Manfred"));


        Button botaoVer = (Button) findViewById(R.id.botaoVer);
        Button botaoProx = (Button) findViewById(R.id.botaoProx);

        flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);

        al = new ArrayList<>();
        al.add(new Recipe("http://www.mulher.com.br/sites/itodas.com.br/files/styles/big-featured/public/field/image/bolo-chocolate-brigadeiro-granulado-belga-receita.jpg?itok=h24g74Sz", "Bolo de Chocolate", Type.DOCE));
        al.add(new Recipe("http://www.menuvegano.com.br/download/plugin_ckeditor_upload.upload.971c5aa1466079eb.68616d627572677565722e6a7067.jpg", "Hamburguer Vegano", Type.VEGAN));
        al.add(new Recipe("http://www.vamosbaterpapo.com.br/wp-content/uploads/2015/11/ceasar-salad.jpg", "Salada Caesar", Type.SALADA));
        al.add(new Recipe("http://imagem.band.com.br/05/f_227505.jpg", "Espaguete a Carbonara", Type.MASSA));
        al.add(new Recipe("http://liquor.s3.amazonaws.com/wp-content/uploads/2012/01/classic-bloody-mary.jpg", "Maria Sangrenta", Type.DRINK));


        myAppAdapter = new MyAppAdapter(al, MainActivity.this);
        flingContainer.setAdapter(myAppAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {

            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();
                //Do something on the left!
                //You also have access to the original object.
                //If you want to use it just cast it (String) dataObject
                if (al.size() == 0){
                    al.add(new Recipe("http://www.mulher.com.br/sites/itodas.com.br/files/styles/big-featured/public/field/image/bolo-chocolate-brigadeiro-granulado-belga-receita.jpg?itok=h24g74Sz", "Bolo de Chocolate", Type.DOCE));
                    al.add(new Recipe("http://www.menuvegano.com.br/download/plugin_ckeditor_upload.upload.971c5aa1466079eb.68616d627572677565722e6a7067.jpg", "Hamburguer Vegano", Type.VEGAN));
                    al.add(new Recipe("http://www.vamosbaterpapo.com.br/wp-content/uploads/2015/11/ceasar-salad.jpg", "Salada Caesar", Type.SALADA));
                    al.add(new Recipe("http://imagem.band.com.br/05/f_227505.jpg", "Espaguete a Carbonara", Type.MASSA));
                    al.add(new Recipe("http://liquor.s3.amazonaws.com/wp-content/uploads/2012/01/classic-bloody-mary.jpg", "Maria Sangrenta", Type.DRINK));
                }
            }



            @Override
            public void onRightCardExit(Object dataObject) {
                al.remove(0);
                myAppAdapter.notifyDataSetChanged();

                if (al.size() == 0){
                    al.add(new Recipe("http://www.mulher.com.br/sites/itodas.com.br/files/styles/big-featured/public/field/image/bolo-chocolate-brigadeiro-granulado-belga-receita.jpg?itok=h24g74Sz", "Bolo de Chocolate", Type.DOCE));
                    al.add(new Recipe("http://www.menuvegano.com.br/download/plugin_ckeditor_upload.upload.971c5aa1466079eb.68616d627572677565722e6a7067.jpg", "Hamburguer Vegano", Type.VEGAN));
                    al.add(new Recipe("http://www.vamosbaterpapo.com.br/wp-content/uploads/2015/11/ceasar-salad.jpg", "Salada Caesar", Type.SALADA));
                    al.add(new Recipe("http://imagem.band.com.br/05/f_227505.jpg", "Espaguete a Carbonara", Type.MASSA));
                    al.add(new Recipe("http://liquor.s3.amazonaws.com/wp-content/uploads/2012/01/classic-bloody-mary.jpg", "Maria Sangrenta", Type.DRINK));
                    myAppAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        botaoProx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (al.size() > 0) {
                    flingContainer.getNextRecipe();
                    al.remove(0);
                    myAppAdapter.notifyDataSetChanged();
                } else {
                    al.add(new Recipe("http://www.mulher.com.br/sites/itodas.com.br/files/styles/big-featured/public/field/image/bolo-chocolate-brigadeiro-granulado-belga-receita.jpg?itok=h24g74Sz", "Bolo de Chocolate", Type.DOCE));
                    al.add(new Recipe("http://www.menuvegano.com.br/download/plugin_ckeditor_upload.upload.971c5aa1466079eb.68616d627572677565722e6a7067.jpg", "Hamburguer Vegano", Type.VEGAN));
                    al.add(new Recipe("http://www.vamosbaterpapo.com.br/wp-content/uploads/2015/11/ceasar-salad.jpg", "Salada Caesar", Type.SALADA));
                    al.add(new Recipe("http://imagem.band.com.br/05/f_227505.jpg", "Espaguete a Carbonara", Type.MASSA));
                    al.add(new Recipe("http://liquor.s3.amazonaws.com/wp-content/uploads/2012/01/classic-bloody-mary.jpg", "Maria Sangrenta", Type.DRINK));
                    myAppAdapter.notifyDataSetChanged();
                }
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {

                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.background).setAlpha(0);

                Recipe receita = al.get(itemPosition);
                Log.v("RECEITA", receita.toString());

                Intent intent = new Intent(view.getContext(), VerReceita.class);
                intent.putExtra("receita", (Serializable) receita);
                startActivityForResult(intent, VER);

                myAppAdapter.notifyDataSetChanged();
            }
        });





    }

    public  void  setUserProfile(String jsondata){

        try {
            response = new JSONObject(jsondata);
            profile_pic_data = new JSONObject(response.get("picture").toString());
            profile_pic_url = new JSONObject(profile_pic_data.getString("data"));

            Picasso.with(this).load(profile_pic_url.getString("url"))
                    .into(user_picture);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setNavigationHeader(){

        Profile profile = Profile.getCurrentProfile();


//        ImageView img_user = (ImageView)hView.findViewById(R.id.ivUser);



//        ProfilePictureView ppv = (ProfilePictureView) hView.findViewById(R.id.ivUser);
//        ppv.setProfileId(profile.getId());

        navigation_view = (NavigationView) findViewById(R.id.nav_view);

        View header = LayoutInflater.from(this).inflate(R.layout.nav_header_main, null);
        navigation_view.addHeaderView(header);

        TextView user_name = (TextView)header.findViewById(R.id.nome);
        user_name.setText(Prefs.getNome(this));
        user_picture = (ImageView) header.findViewById(R.id.profile_pic);
    }


    public void novaReceita(View v){
        Intent intent = new Intent(this, NovaReceita.class);
        startActivity(intent);
    }

    @Override
    public void onActionDownPerform() {
        Log.e("action", "bingo");
    }

    public static class ViewHolder {
        public static FrameLayout background;
        public TextView DataText;
        public ImageView cardImage;
        public TextView tipo;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.myRecipes) {
            Intent intent = new Intent(this, NovaReceita.class);
            startActivity(intent);
        } else if (id == R.id.likedRecipes) {
            Intent intent = new Intent(this, NovaReceita.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class MyAppAdapter extends BaseAdapter {


        public List<Recipe> parkingList;
        public Context context;

        private MyAppAdapter(List<Recipe> apps, Context context) {
            this.parkingList = apps;
            this.context = context;
        }

        @Override
        public int getCount() {
            return parkingList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View rowView = convertView;


            if (rowView == null) {

                LayoutInflater inflater = getLayoutInflater();
                rowView = inflater.inflate(R.layout.item, parent, false);
                // configure view holder
                viewHolder = new ViewHolder();
                viewHolder.DataText = (TextView) rowView.findViewById(R.id.bookText);
                viewHolder.background = (FrameLayout) rowView.findViewById(R.id.background);
                viewHolder.tipo = (TextView) rowView.findViewById(R.id.tvType);
                viewHolder.cardImage = (ImageView) rowView.findViewById(R.id.cardImage);
                rowView.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.DataText.setText(parkingList.get(position).getDescription() + "");
            viewHolder.tipo.setText(parkingList.get(position).getTipo().toString()+"");

            Glide.with(MainActivity.this).load(parkingList.get(position).getImagePath()).into(viewHolder.cardImage);

            return rowView;
        }
    }
}
