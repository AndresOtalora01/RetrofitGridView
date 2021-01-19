package com.example.retrofitgridview.ui.book;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofitgridview.R;
import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.models.BooksResponse;
import com.example.retrofitgridview.network.ApiClient;
import com.example.retrofitgridview.ui.main.BaseActivity;
import com.example.retrofitgridview.ui.main.BooksListAdapter;
import com.example.retrofitgridview.ui.main.MainActivity;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DownloadedBooksFragment extends Fragment implements BooksListAdapter.OnBookListener {

    private ArrayList<Book> booksList;
    private ImageView backArrow;
    private ImageView nextArrow;
    private TextView tvBooksPage;
    private int actualPage = 1;
    private RecyclerView recyclerView;
    private BooksListAdapter booksListAdapter;
    private RecyclerView.LayoutManager layoutManager;

    public DownloadedBooksFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloaded_books, container, false);

        backArrow = view.findViewById(R.id.ivPrevious);
        nextArrow = view.findViewById(R.id.ivNext);
        tvBooksPage = view.findViewById(R.id.tvBooksPage);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(layoutManager);
        booksListAdapter = new BooksListAdapter(getContext(), this::onBookClick);

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
        boolean nextPage = true;
        if (actualPage == 1) {
            backArrow.setVisibility(View.GONE);
        } else {
            backArrow.setVisibility(View.VISIBLE);
        }
        ((BaseActivity) getActivity()).showProgressDialog();
        String[] savedBooks = BooksManagement.getBooksManagement().getSavedBooks();
        StringBuilder queryValues = new StringBuilder();
        for (int i = 0; i < savedBooks.length; i++) {
            if (i != savedBooks.length - 1)
                queryValues = queryValues.append(savedBooks[i] + ",");
            else queryValues.append(savedBooks[i]);
        }
        Call<BooksResponse> booksResponse = ApiClient.getInterface().getSpecificBooks(queryValues.toString());
        booksResponse.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                tvBooksPage.setText(actualPage + "");
                if (response.code() == 200) {
                    ((BaseActivity) getActivity()).hideProgressDialog();
                    Log.d("Buscaminas", response.body().toString());
                    booksList = response.body().getResults();
                    adapterManagement(response.body());
                    actualPage++;
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

    public void adapterManagement(BooksResponse booksResponse) {
        booksResponse = deleteZipFiles(booksResponse);
        if (booksListAdapter == null) {
            booksListAdapter = new BooksListAdapter(getContext(), this::onBookClick);
            recyclerView.setAdapter(booksListAdapter);
        }
        booksListAdapter.setItems(booksResponse.getResults());
        layoutManager.scrollToPosition(0);
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
}