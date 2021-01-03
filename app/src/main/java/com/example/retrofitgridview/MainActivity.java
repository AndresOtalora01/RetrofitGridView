package com.example.retrofitgridview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements CustomAdapter.OnBookListener {


    private RecyclerView recyclerView;
    private BooksResponse booksResponseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        getAllBooks();
    }

    public void getAllBooks() {
        Call<BooksResponse> booksResponse = ApiClient.getInterface().getAllBooks();
        booksResponse.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {

                if (response.code() == 200) {

                    Log.d("Buscaminas", response.body().toString());

                    booksResponseList = response.body();
                    Log.d("Buscaminas", booksResponseList.getResults().get(2).getFormats().toString());
                    CustomAdapter customAdapter = new CustomAdapter(booksResponseList.getResults(), MainActivity.this, MainActivity.this::onBookClick);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerView.setAdapter(customAdapter);
                } else {
                    String message = "ERROR. Try again later";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    Log.d("Buscaminas", "no funciona");
                }

            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                String message = t.getLocalizedMessage();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    
    @Override
    public void onBookClick(int position) {
        startActivity(new Intent(this, BookActivity.class).putExtra("data", booksResponseList.getResults().get(position)));
    }
}