package com.example.mobileproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import java.nio.charset.StandardCharsets;

public class RegisterActivity extends AppCompatActivity {

    TextView email, password, username;
    Button register_button;
    CheckBox remember_me;
    SharedPreferences credentials_sp;
    String email_string, password_string, username_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

    }

    public void signUp(View view) {
        email = findViewById(R.id.email_ed);
        password = findViewById(R.id.password_ed);
        username = findViewById(R.id.username_ed);
        remember_me = findViewById(R.id.remember_me_checkbox);
        credentials_sp = getSharedPreferences("credentials", MODE_PRIVATE);
        email_string = email.getText().toString();
        password_string = password.getText().toString();
        username_string = username.getText().toString();

        if (TextUtils.isEmpty(email_string)) {
            email.setError("Empty field");
            return;
        } else if (TextUtils.isEmpty(password_string)) {
            password.setError("Empty field");
            return;
        } else if (TextUtils.isEmpty(username_string)) {
            username.setError("Empty field");
        } else {
            try {
                RequestQueue requestQueue = Volley.newRequestQueue(this);
                String URL = "http://host_ip/backend-module/src/register.php";

                JSONObject jsonBody = new JSONObject();
                jsonBody.put("username", username_string);
                jsonBody.put("email", email_string);
                jsonBody.put("password", password_string);

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

                                    if(status.equals("error")) {
                                        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                    } else {
                                        if(remember_me.isChecked()) {
                                            SharedPreferences.Editor cred_sp_editor = credentials_sp.edit();
                                            cred_sp_editor.putString("email", email_string);
                                            cred_sp_editor.putString("password", password_string);
                                            cred_sp_editor.putString("token", token);
                                            cred_sp_editor.apply();
                                        }
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
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
                        return jsonBody.toString().getBytes(StandardCharsets.UTF_8);
                    }
                };

                // Add the request to the request queue
                requestQueue.add(jsonObjectRequest);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}