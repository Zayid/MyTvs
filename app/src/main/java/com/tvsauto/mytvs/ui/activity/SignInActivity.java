package com.tvsauto.mytvs.ui.activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tvsauto.mytvs.R;
import com.tvsauto.mytvs.holder.ErrorResponse;
import com.tvsauto.mytvs.holder.SignInResponse;
import com.tvsauto.mytvs.network.ApiConfiguration;
import com.tvsauto.mytvs.network.ApiService;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout tilUsername, tilPassword;
    private TextInputEditText etUsername, etPassword;
    private CardView cardSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        tilUsername = findViewById(R.id.til_username);
        tilPassword = findViewById(R.id.til_password);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        cardSignIn = findViewById(R.id.card_sign_in);

        //signIn("test", "123456");

        cardSignIn.setOnClickListener(view -> validateInput());
    }

    @Override
    protected void onStart() {
        super.onStart();

        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    tilUsername.setError("");
                    tilUsername.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0) {
                    tilPassword.setError("");
                    tilPassword.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void validateInput() {
        if (etUsername.getText().toString().isEmpty()) {
            tilUsername.setErrorEnabled(true);
            tilUsername.setError("Username can't be empty");
        }

        if (etPassword.getText().toString().isEmpty()) {
            tilPassword.setErrorEnabled(true);
            tilPassword.setError("Password can't be empty");
        }

        if (!etUsername.getText().toString().isEmpty() && !etPassword.getText().toString().isEmpty()) {
            signIn(etUsername.getText().toString(), etPassword.getText().toString());
        }
    }

    private void signIn(String username, String password) {

        Map<String, Object> jsonParams = new ArrayMap<>();
        jsonParams.put("username", username);
        jsonParams.put("password", password);

        ApiService apiService = ApiConfiguration.getClient().create(ApiService.class);
        RequestBody requestBody = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                (new JSONObject(jsonParams)).toString());
        Call<SignInResponse> call = apiService.signIn(requestBody);
        call.enqueue(new Callback<SignInResponse>() {
            @Override
            public void onResponse(Call<SignInResponse> call, Response<SignInResponse> response) {
                if (response.isSuccessful()) {
                    if (!response.body().getTABLEDATA().isEmpty()) {
                        Intent intent = new Intent(SignInActivity.this, EmployeeListActivity.class);
                        intent.putExtra("empList", response.body().getTABLEDATA());
                        startActivity(intent);
                        finish();
                    }
                } else if (response.code() == 400) {
                    Gson gson = new GsonBuilder().create();
                    new ErrorResponse();
                    ErrorResponse mError;
                    try {
                        mError = gson.fromJson(response.errorBody().string(), ErrorResponse.class);
                        Toast.makeText(SignInActivity.this, mError.getErrorDescription(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<SignInResponse> call, Throwable t) {

            }
        });
    }
}
