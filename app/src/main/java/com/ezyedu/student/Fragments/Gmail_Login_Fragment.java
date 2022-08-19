package com.ezyedu.student.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ezyedu.student.MainActivity;
import com.ezyedu.student.gmail_register;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ezyedu.student.R;
import com.ezyedu.student.model.Globals;
import com.ezyedu.student.model.ImageGlobals;

import org.json.JSONException;
import org.json.JSONObject;


public class Gmail_Login_Fragment extends Fragment {

    View view;
    //google
    GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    RequestQueue requestQueue;
    SharedPreferences sp;


    //retrive base url
    Globals sharedData = Globals.getInstance();
    String base_app_url;

    //get img global url
    ImageGlobals shareData1 = ImageGlobals.getInstance();
    String img_url_base;

    private static int RC_SIGN_IN = 100;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        sp = getContext().getSharedPreferences("Session_id", Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_gmail__login_, container, false);

        //get domain url
        base_app_url = sharedData.getValue();
        Log.i("domain_url",base_app_url);

        //get image loading url
        img_url_base = shareData1.getIValue();
        Log.i("img_url_global",img_url_base);


        requestQueue= Volley.newRequestQueue(getContext());

        signInButton  =view.findViewById(R.id.sign_in_button);

        //gmail login
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        // Set the dimensions of the sign-in button.

        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GooglesignIn();
            }
        });

        return view;

    }

    private void GooglesignIn()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task)
    {
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
            if (acct != null) {
                String personName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                String personEmail = acct.getEmail();
                String personId = acct.getId();
                Uri personPhoto = acct.getPhotoUrl();
                Log.i("E-Mail",personEmail);
                Log.i("personId",personId);
                Toast.makeText(getActivity(), "Sign-In Success", Toast.LENGTH_SHORT).show();
                if (personId != null)
                {
                   gmailLogin(personId,personEmail);
                    //logout...
                    mGoogleSignInClient.signOut();
                }


            }

        } catch (ApiException | JSONException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.d("Gmailmessage",e.toString());

        }
    }


    private void gmailLogin(String personId, String personEmail) throws JSONException {
        String url = base_app_url+"api/auth/login-social";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("provider","google");
        jsonObject.put("provider_id",personId);
        jsonObject.put("email",personEmail);
        jsonObject.put("role",3);
        Log.i("JSONBJ",jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response)
            {
                Log.i("ResponseFB",response.toString());

                try {
                    String session = response.getString("session");
                    Log.i("sessionLogged",session);

                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("session_val",session);
                    editor.commit();
                    if (!TextUtils.isEmpty(session))
                    {
                        Intent intent1 = new Intent(getContext(), MainActivity.class);
                        startActivity(intent1);
                    }
                    else
                    {
                        Intent intent1 = new Intent(getContext(), gmail_register.class);
                        intent1.putExtra("provider_id",personId);
                        intent1.putExtra("email",personEmail);
                        startActivity(intent1);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Log.i("error",error.toString());
                Toast.makeText(getContext(), "Please Register to Continue", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(getContext(), gmail_register.class);
                intent1.putExtra("provider_id",personId);
                intent1.putExtra("email",personEmail);
                startActivity(intent1);
            }
        });
        requestQueue.add(jsonObjectRequest);

    }




}