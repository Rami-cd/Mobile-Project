package com.example.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login_button;
    CheckBox remember_me;
    SharedPreferences credentials_sp;

    String email_string, password_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void login(View view) {
        email = findViewById(R.id.email_ed);
        password = findViewById(R.id.password_ed);
        remember_me = findViewById(R.id.remember_me_checkbox);
        credentials_sp = getSharedPreferences("credentials", MODE_PRIVATE);
        email_string = email.getText().toString();
        password_string = password.getText().toString();

        Log.i("email", email_string);
        Log.i("password", password_string);

        // Check if fields are empty
        if (TextUtils.isEmpty(email_string)) {
            email.setError("Empty field");
            return;
        } else if (TextUtils.isEmpty(password_string)) {
            password.setError("Empty field");
            return;
        } else {
            try {
                // Set up request queue and URL
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                String URL = "http://host_ip/backend-module/src/login.php";

                // Create the request body (JSON)
                JSONObject jsonBody = new JSONObject();
                jsonBody.put("email", email_string);
                jsonBody.put("password", password_string);

                // Create the JsonObjectRequest to handle the JSON response
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Log the entire JSON response
                                Log.i("Volley Response", response.toString());

                                try {
                                    // Extract status and message from the JSON response
                                    String status = response.getString("status");
                                    String message = response.getString("message");
                                    String token = response.getString("token");

                                    // Log the extracted values
//                                    Log.i("Status", status);
//                                    Log.i("Message", message);

                                    if(status.equals("error")) {
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        if(remember_me.isChecked()) {
                                            SharedPreferences.Editor cred_sp_editor = credentials_sp.edit();
                                            cred_sp_editor.putString("email", email_string);
                                            cred_sp_editor.putString("password", password_string);
                                            cred_sp_editor.putString("token", token);
                                            cred_sp_editor.apply();
                                        }
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // Log any error that occurs during the request
                                Log.e("Volley Error", error.toString());
                            }
                        }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() {
                        try {
                            return jsonBody == null ? null : jsonBody.toString().getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody.toString(), "utf-8");
                            return null;
                        }
                    }
                };

                // Add the request to the request queue
                requestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void go_to_signUp_activity(View view) {
        Intent to_sign_up = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(to_sign_up);
        finish();
    }
}
