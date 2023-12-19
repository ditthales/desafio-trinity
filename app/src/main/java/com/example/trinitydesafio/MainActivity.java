package com.example.trinitydesafio;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private EditText cepEditText;
    private CardView cardView;
    private TextView cepTextView, logradouroTextView, bairroTextView, localidadeTextView, ufTextView;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cepEditText = findViewById(R.id.cepEditText);
        cardView = findViewById(R.id.cardView);
        cepTextView = findViewById(R.id.cepTextView);
        logradouroTextView = findViewById(R.id.logradouroTextView);
        bairroTextView = findViewById(R.id.bairroTextView);
        localidadeTextView = findViewById(R.id.localidadeTextView);
        ufTextView = findViewById(R.id.ufTextView);
        searchButton = findViewById(R.id.search_button);

        cardView.setVisibility(View.INVISIBLE);

        cepEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                searchButton.setEnabled(editable.length() == 8);
            }
        });
    }

    public void consultarCep(View view) {
        cardView.setVisibility(View.INVISIBLE);

        hideKeyboard(view);

        String cep = cepEditText.getText().toString();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://viacep.com.br/ws/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        CepAPI cepApi = retrofit.create(CepAPI.class);
        Call<CepResponse> call = cepApi.getCep(cep);

        call.enqueue(new Callback<CepResponse>() {
            @Override
            public void onResponse(@NonNull Call<CepResponse> call, @NonNull Response<CepResponse> response) {
                if (response.isSuccessful()) {
                    CepResponse cepResponse = response.body();
                    if (cepResponse != null) {
                        updateCard(cepResponse);
                        cardView.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CepResponse> call, @NonNull Throwable t) {
                // Tratar falha na requisição
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void updateCard(CepResponse cepResponse) {
        Resources resources = getResources();

        cepTextView.setText(resources.getString(R.string.cep_label, cepResponse.getCep()));
        logradouroTextView.setText(resources.getString(R.string.logradouro_label, cepResponse.getLogradouro()));
        bairroTextView.setText(resources.getString(R.string.bairro_label, cepResponse.getBairro()));
        localidadeTextView.setText(resources.getString(R.string.localidade_label, cepResponse.getLocalidade()));
        ufTextView.setText(resources.getString(R.string.uf_label, cepResponse.getUf()));
    }



}