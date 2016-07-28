package com.didasko.eduardo.tender;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


/**
 * Created by Eduardo on 13/07/2016.
 */
public class Login extends AppCompatActivity {
    CallbackManager callbackManager;
    TextView tv5;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        tv5 = (TextView) findViewById(R.id.tv5);
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("user_friends", "public_profile"));
        loginButton.setReadPermissions("user_friends", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                tv5.setText("Carregando...");
                loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {

                            @Override
                            public void onCompleted(

                                    JSONObject object,
                                    GraphResponse response) {

                                Profile profile = Profile.getCurrentProfile();
                                Prefs.gravarNome(Login.this, profile.getFirstName()+" "+profile.getLastName());

                                Intent intent = new Intent(Login.this,MainActivity.class);
                                intent.putExtra("jsondata",object.toString());
                                startActivity(intent);

                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,picture.width(120).height(120)");
                request.setParameters(parameters);
                request.executeAsync();
//                Intent intent = new Intent(Login.this, MainActivity.class);
//                startActivity(intent);

            }

            @Override
            public void onCancel() {
                Log.v("FACE", "CALNCEL!!!!!!!!!!!!!!!!");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.v("FACE", "ERROU!!!!!!!!!!!!!!!!");
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        tv5.setText("Tender");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
