package com.example.retrofitgridview.ui.main;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.retrofitgridview.R;
import com.example.retrofitgridview.ui.book.MainListFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends BaseActivity implements FilterDialog.FilterDialogListener {


    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private SearchView searchView;
    private String searchQuery;
    private String fromYear;
    private String toYear;
    private String language;
    private Boolean copyright;
    private FilterDialog filterDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setNavigationDrawer();
        filterDialog = new FilterDialog(this);
        getSavedFilters();
        Log.d("filtrosOnCreate", fromYear + " " + toYear + " " + copyright+ " "+ language);
        selectedItem(R.id.nav_all_books);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem filterItem = menu.findItem(R.id.actionFilter);
        MenuItem searchItem = menu.findItem(R.id.actionSearch);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setQueryHint(getResources().getString(R.string.search));

        filterItem.setOnMenuItemClickListener(item -> {
            openDialog();
            return false;
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                MainListFragment mainListFragment = MainListFragment.newInstance(searchQuery, fromYear, toYear, copyright, language);
                setFragment(mainListFragment);
                searchView.clearFocus();
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
            MainListFragment mainListFragment;
            switch (id) {

                case R.id.nav_all_books:
                    mainListFragment = MainListFragment.newInstance();
                    searchView.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_downloaded_books:
                    mainListFragment = MainListFragment.newInstance(true);
                    searchView.setVisibility(View.GONE);
                    break;
                default:
                    return true;
            }
            setFragment(mainListFragment);
            drawerLayout.closeDrawers();
            return true;
        });
    }

    public void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.contentFragment, fragment, "fragment")
                .commit();
    }


    public void selectedItem(int id) {
        MainListFragment mainListFragment;
        if (id == R.id.nav_all_books) {
            mainListFragment = MainListFragment.newInstance();
        } else {
            mainListFragment = new MainListFragment();
        }
        setFragment(mainListFragment);
    }

    @Override
    public void onBackPressed() {
        if (searchView.getVisibility() == View.GONE) {
            searchView.setVisibility(View.VISIBLE);
        }
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void openDialog() {
        filterDialog.show(getSupportFragmentManager(), "filter dialog");
    }

    @Override
    public void getFilters(Boolean copyright, String fromYear, String toYear, String language) {
        this.copyright = copyright;
        this.fromYear = fromYear;
        this.toYear = toYear;
        this.language = language;
        Log.d("testFiltrosGetFILTER", fromYear + " " + toYear + " " + copyright +" " + language);
    }

    public void updateTextSearchView(String searchQuery) {
        if (searchView == null) {
            return;
        }
        if (searchQuery != null)
            searchView.setQuery(searchQuery, false);
        else {
            searchView.setQuery("", false);
            searchView.setIconified(true);
        }
    }

    public void getSavedFilters() {
        fromYear = filterDialog.getSavedFromYear();
        toYear = filterDialog.getSavedToYear();
        copyright = filterDialog.getSavedSwitch();
    }
}