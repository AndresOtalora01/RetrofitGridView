package com.example.retrofitgridview.ui.main;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofitgridview.models.Format;
import com.example.retrofitgridview.network.ApiClient;
import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.models.BooksResponse;
import com.example.retrofitgridview.R;
import com.example.retrofitgridview.ui.book.BookActivity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements BooksListAdapter.OnBookListener {

    private int actualPage = 1;
    private RecyclerView recyclerView;
    private BooksListAdapter booksListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ImageView backArrow;
    private TextView tvBooksPage;
//    private static LruCache<Integer, String> mMemoryCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        backArrow = findViewById(R.id.ivPrevious);
        recyclerView = findViewById(R.id.recyclerView);
        tvBooksPage = findViewById(R.id.tvBooksPage);
        Log.d("ruta", Environment.getExternalStorageDirectory().toString());
        getAllBooks();

//        final int maxMemorySize = (int) Runtime.getRuntime().maxMemory() / 1024;
//        final int cacheSize = maxMemorySize / 10;
//        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        int availMemInBytes = am.getMemoryClass() * 1024 * 1024;
//        mMemoryCache = new LruCache<Integer, String>(availMemInBytes / 8) {
//
//            @Override
//            protected int sizeOf(Integer key, String value) {
//                byte[] size;
//                size = value.getBytes();
//                return size.length / 8;
//            }
//        };

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
                tvBooksPage.setText(actualPage + "");
                if (response.code() == 200) {
                    Log.d("Buscaminas", response.body().toString());
                    adapterManagement(response.body());
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

    public void adapterManagement(BooksResponse booksResponse) {
        booksResponse = deleteZipFiles(booksResponse);
        if (booksListAdapter == null) {
            booksListAdapter = new BooksListAdapter(MainActivity.this, MainActivity.this::onBookClick);
            layoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(booksListAdapter);
        }
        booksListAdapter.setItems(booksResponse.getResults());
        layoutManager.scrollToPosition(0);
    }

    @Override
    public void onBookClick(int position) {
        Book book = (Book) booksListAdapter.getItem(position);
        startActivity(new Intent(this, BookActivity.class).putExtra("data", book));
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
            if ((book.getFormats().getTextPlain() != null && !book.getFormats().getTextPlain().endsWith(".txt"))
                    || (book.getFormats().getTextPlainIso() != null && !book.getFormats().getTextPlainIso().endsWith(".txt"))
                    && (book.getFormats().getTextPlainAscii() != null && !book.getFormats().getTextPlainAscii().endsWith(".txt"))) {
                iterator.remove();
            }
        }
        booksResponse.setResults(books);
        return booksResponse;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.book_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                showProgressDialog();
                Call<BooksResponse> booksResponse = ApiClient.getInterface().getSpecificBook(query);
                booksResponse.enqueue(new Callback<BooksResponse>() {
                    @Override
                    public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                        hideProgressDialog();
                        if (response.code() == 200) {
                            Log.d("Buscaminas", response.body().toString());
                            adapterManagement(response.body());
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
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // booksListAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

//    public static String getContentFromMemoryCache(Integer key) {
//        return mMemoryCache.get(key);
//    }
//
//    public static void setContentToMemoryCache(Integer key, String content) {
//        if(getContentFromMemoryCache(key) == null) {
//            mMemoryCache.put(key,content);
//        }
//    }

    @Override
    public void onResume() {
        super.onResume();
        //update whatever your list
        if (booksListAdapter != null)
            booksListAdapter.notifyDataSetChanged();
    }

}