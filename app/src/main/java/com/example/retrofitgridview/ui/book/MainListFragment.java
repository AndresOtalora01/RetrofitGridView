package com.example.retrofitgridview.ui.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitgridview.R;
import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.models.BooksResponse;
import com.example.retrofitgridview.network.ApiClient;
import com.example.retrofitgridview.ui.main.BaseActivity;
import com.example.retrofitgridview.ui.main.BooksListAdapter;
import com.example.retrofitgridview.ui.main.MainActivity;
import com.fasterxml.jackson.databind.ser.Serializers;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainListFragment extends Fragment implements BooksListAdapter.OnBookListener {

    private ArrayList<Book> booksList;
    private ImageView backArrow;
    private ImageView nextArrow;
    private TextView tvBooksPage;
    private int actualPage = 1;
    private RecyclerView recyclerView;
    private BooksListAdapter booksListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public MainListFragment() {
        booksList = new ArrayList<>();
    }


    public static DownloadedBooksFragment newInstance(String param1, String param2) {
        DownloadedBooksFragment fragment = new DownloadedBooksFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllBooks();
        backArrow.setOnClickListener(v -> {
            actualPage--;
            getAllBooks();
        });
        nextArrow.setOnClickListener(v -> {
            actualPage++;
            getAllBooks();
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list, container, false);

        backArrow = view.findViewById(R.id.ivPrevious);
        nextArrow = view.findViewById(R.id.ivNext);
        tvBooksPage = view.findViewById(R.id.tvBooksPage);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        BooksListAdapter booksListAdapter = new BooksListAdapter(getContext(), this::onBookClick);

        recyclerView.setAdapter(booksListAdapter);
        booksListAdapter.setItems(booksList);
        layoutManager.scrollToPosition(0);
        return view;
    }

    @Override
    public void onBookClick(int position) {
        Book book = (Book) booksListAdapter.getItem(position);
        startActivity(new Intent(getActivity(), BookActivity.class).putExtra("data", book));
    }


    public void getAllBooks() {
        if (actualPage == 1) {
            backArrow.setVisibility(View.GONE);
        } else {
            backArrow.setVisibility(View.VISIBLE);
        }

        ((BaseActivity) getActivity()).showProgressDialog();
        Call<BooksResponse> booksResponse = ApiClient.getInterface().getAllBooks(actualPage);
        booksResponse.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                tvBooksPage.setText(actualPage + "");
                if (response.code() == 200) {
                    Log.d("Buscaminas", response.body().toString());
                    booksList = response.body().getResults();
                    adapterManagement(response.body());
                } else {
                    String message = "ERROR. Try again later";
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    Log.d("Buscaminas", "no funciona");
                }
            }

            @Override
            public void onFailure(Call<BooksResponse> call, Throwable t) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                String message = t.getLocalizedMessage();
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (booksListAdapter != null)
            booksListAdapter.notifyDataSetChanged();
    }

    public void adapterManagement(BooksResponse booksResponse) {
        booksResponse = ((MainActivity) getActivity()).deleteZipFiles(booksResponse);
        if (booksListAdapter == null) {
            booksListAdapter = new BooksListAdapter(getContext(), this::onBookClick);
            recyclerView.setAdapter(booksListAdapter);
        }
        booksListAdapter.setItems(booksResponse.getResults());
        layoutManager.scrollToPosition(0);
    }


}