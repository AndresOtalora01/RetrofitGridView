package com.example.retrofitgridview;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements CustomAdapter.OnBookListener {

    private int actualPage = 1;
    private RecyclerView recyclerView;
    private BooksResponse booksResponseList;
    private CustomAdapter customAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView backArrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backArrow = findViewById(R.id.ivPrevious);
        recyclerView = findViewById(R.id.recyclerView);
        getAllBooks();

    }

    public void getAllBooks() {
        if (actualPage == 1) {
            backArrow.setVisibility(View.GONE);
        } else {
            backArrow.setVisibility(View.VISIBLE);
        }
        showProgressDialog();
        Call<BooksResponse> booksResponse = ApiClient.getInterface().getAllBooks(actualPage);
        booksResponse.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                hideProgressDialog();
                if (response.code() == 200) {
                    Log.d("Buscaminas", response.body().toString());
                    booksResponseList = response.body();
                    booksResponseList = deleteZipFiles(booksResponseList);
                    if (customAdapter == null) {
                        customAdapter = new CustomAdapter(MainActivity.this, MainActivity.this::onBookClick);
                        layoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setAdapter(customAdapter);
                    }
                    customAdapter.setItems(booksResponseList.getResults());
                    layoutManager.scrollToPosition(0);
                } else {
                    String message = "ERROR. Try again later";
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    Log.d("Buscaminas", "no funciona");
                }

            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                hideProgressDialog();
                String message = t.getLocalizedMessage();
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onBookClick(int position) {
        startActivity(new Intent(this, BookActivity.class).putExtra("data", booksResponseList.getResults().get(position)));
    }


    public void onNextClick(View view) {
        actualPage++;
        getAllBooks();
    }

    public void onPreviousClick(View view) {
        if (actualPage > 0) {
            actualPage--;
            getAllBooks();
        }

    }

    private BooksResponse deleteZipFiles(BooksResponse booksResponse) {
        ArrayList<Book> books = booksResponse.getResults();
        for (Iterator<Book> iterator = books.iterator(); iterator.hasNext(); ) {
            Book book = iterator.next();
            if (book.getFormats().getTextPlain() != null && !book.getFormats().getTextPlain().endsWith(".txt")) {
                iterator.remove();
            }
        }
        booksResponse.setResults(books);
        return booksResponse;
    }
}