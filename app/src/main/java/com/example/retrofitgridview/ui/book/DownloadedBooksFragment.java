package com.example.retrofitgridview.ui.book;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.retrofitgridview.R;
import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.ui.main.BooksListAdapter;
import com.example.retrofitgridview.ui.main.MainActivity;

import java.util.ArrayList;


public class DownloadedBooksFragment extends Fragment implements BooksListAdapter.OnBookListener {

    private ArrayList<Book> downloadedBooks = new ArrayList<>();

    public DownloadedBooksFragment() {
        // Required empty public constructor
    }
public DownloadedBooksFragment (ArrayList<Book> downloadedBooks) {
        this.downloadedBooks = downloadedBooks;
}
    public static DownloadedBooksFragment newInstance(String param1, String param2) {
        DownloadedBooksFragment fragment = new DownloadedBooksFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_downloaded_books, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView recyclerView;
        recyclerView = view.findViewById(R.id.recyclerView2);
        recyclerView.setLayoutManager(layoutManager);
        BooksListAdapter booksListAdapter = new BooksListAdapter(getContext(),this::onBookClick);

        recyclerView.setAdapter(booksListAdapter);
        booksListAdapter.setItems(downloadedBooks);
        layoutManager.scrollToPosition(0);
        return view;
    }

    @Override
    public void onBookClick(int position) {
    }
}