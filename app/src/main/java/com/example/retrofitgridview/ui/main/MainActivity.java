package com.example.retrofitgridview.ui.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.retrofitgridview.ui.book.DownloadedBooksFragment;
import com.example.retrofitgridview.ui.book.MainListFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationDrawer();
        selectedItem(R.id.nav_all_books);

    }

    public BooksResponse deleteZipFiles(BooksResponse booksResponse) {
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.search_menu, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.actionSearch);
//        SearchView searchView = (SearchView) searchItem.getActionView();
//        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
//        searchView.setQueryHint(getResources().getString(R.string.search));
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                showProgressDialog();
//                Call<BooksResponse> booksResponse = ApiClient.getInterface().getSpecificBook(query);
//                booksResponse.enqueue(new Callback<BooksResponse>() {
//                    @Override
//                    public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
//                        hideProgressDialog();
//                        if (response.code() == 200) {
//                            Log.d("Buscaminas", response.body().toString());
//                           // adapterManagement(response.body());
//                        } else {
//                            String message = "ERROR. Try again later";
//                            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
//                            Log.d("Buscaminas", "no funciona");
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<BooksResponse> call, Throwable t) {
//                        hideProgressDialog();
//                        String message = t.getLocalizedMessage();
//                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
//                    }
//                });
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                // booksListAdapter.getFilter().filter(newText);
//                return false;
//            }
//        });
//        return true;
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }


    public void setNavigationDrawer() {
        drawerLayout = findViewById(R.id.activity_main);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView = findViewById(R.id.navigationView);


        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            switch (id) {

                case R.id.nav_all_books:
                    MainListFragment mainListFragment = new MainListFragment();
                    setFragment(mainListFragment);
                    drawerLayout.closeDrawers();
                    break;

                case R.id.nav_downloaded_books:
                    DownloadedBooksFragment downloadedBooksFragment = new DownloadedBooksFragment();
                    setFragment(downloadedBooksFragment);
                    drawerLayout.closeDrawers();
                    break;
                default:
                    return true;
            }
            return true;
        });
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contentFragment, fragment, "fragment")
                .addToBackStack(null)
                .commit();
    }


    public void selectedItem(int id) {
        switch (id) {
            case R.id.nav_all_books:
                MainListFragment mainListFragment = new MainListFragment();
                setFragment(mainListFragment);
                break;
            case R.id.nav_downloaded_books:
                DownloadedBooksFragment downloadedBooksFragment = new DownloadedBooksFragment();
                setFragment(downloadedBooksFragment);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}