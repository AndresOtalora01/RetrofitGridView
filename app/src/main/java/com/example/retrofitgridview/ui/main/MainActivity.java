package com.example.retrofitgridview.ui.main;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.retrofitgridview.network.ApiClient;
import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.models.BooksResponse;
import com.example.retrofitgridview.R;
import com.example.retrofitgridview.ui.book.DownloadedBooksFragment;
import com.example.retrofitgridview.ui.book.MainListFragment;
import com.example.retrofitgridview.ui.book.SearchBooksFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private MainListFragment mainListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationDrawer();
        selectedItem(R.id.nav_all_books);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint(getResources().getString(R.string.search));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchBooksFragment downloadedBooksFragment = new SearchBooksFragment(query);
                setFragment(downloadedBooksFragment);
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
                mainListFragment = new MainListFragment();
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