package com.example.retrofitgridview.ui.book;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitgridview.R;
import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.models.BooksResponse;
import com.example.retrofitgridview.network.ApiClient;
import com.example.retrofitgridview.ui.main.BaseActivity;
import com.example.retrofitgridview.ui.main.BooksListAdapter;
import com.example.retrofitgridview.ui.main.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainListFragment extends Fragment implements BooksListAdapter.OnBookListener {
    private static final String SAVED_BOOKS_ARG = "argSavedBooks";
    private static final String QUERY_ARG = "argQuery";
    private static final String FROM_YEAR_ARG = "argFromYear";
    private static final String TO_YEAR_ARG = "argToYear";
    private static final String COPYRIGHT_ARG = "argCopyright";
    private static final String LANGUAGE_ARG = "argLanguage";
    private ArrayList<Book> booksList;
    private ImageView backArrow;
    private ImageView nextArrow;
    private TextView tvBooksPage;
    private int actualPage = 1;
    private RecyclerView recyclerView;
    private LinearLayout notFoundContainer;
    private BooksListAdapter booksListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private boolean isSavedBooks;
    private String bookQuery;
    private View bottomContainer;
    private String fromYear;
    private String toYear;
    private Boolean copyright;
    private String language;

    public MainListFragment() {
        booksList = new ArrayList<>();
    }

    public static MainListFragment newInstance() {
        return new MainListFragment();
    }

    public static MainListFragment newInstance(String query, String fromYear, String toYear, Boolean copyright, String language) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        args.putString(QUERY_ARG, query);
        args.putString(FROM_YEAR_ARG, fromYear);
        args.putString(TO_YEAR_ARG, toYear);
        args.putBoolean(COPYRIGHT_ARG, copyright);
        args.putString(LANGUAGE_ARG, language);
        fragment.setArguments(args);
        return fragment;
    }

    public static MainListFragment newInstance(Boolean isSavedBooks) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        args.putBoolean(SAVED_BOOKS_ARG, isSavedBooks);
        fragment.setArguments(args);

        return fragment;
    }

    public static MainListFragment newInstance(String fromYear, String toYear, Boolean copyright, String language) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
        args.putString(FROM_YEAR_ARG, fromYear);
        args.putString(TO_YEAR_ARG, toYear);
        args.putBoolean(COPYRIGHT_ARG, copyright);
        args.putString(LANGUAGE_ARG, language);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            if (arguments.containsKey(QUERY_ARG)) {
                bookQuery = arguments.getString(QUERY_ARG);
                fromYear = arguments.getString(FROM_YEAR_ARG);
                toYear = arguments.getString(TO_YEAR_ARG);
                copyright = arguments.getBoolean(COPYRIGHT_ARG);
                language = arguments.getString(LANGUAGE_ARG);
            } else if (arguments.containsKey(SAVED_BOOKS_ARG)) {
                isSavedBooks = arguments.getBoolean(SAVED_BOOKS_ARG);
            } else {
                fromYear = arguments.getString(FROM_YEAR_ARG);
                toYear = arguments.getString(TO_YEAR_ARG);
                copyright = arguments.getBoolean(COPYRIGHT_ARG);
                language = arguments.getString(LANGUAGE_ARG);
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getBooks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list, container, false);
        notFoundContainer = view.findViewById(R.id.notFoundContainer);
        bottomContainer = view.findViewById(R.id.bottomContainer);
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
        Book book = booksListAdapter.getItem(position);
        startActivity(new Intent(getActivity(), BookActivity.class).putExtra("data", book));
    }

    public void getBooks() {
        if (bookQuery == null && isSavedBooks) {
            bottomContainer.setVisibility(View.GONE);
            getSavedBooks();
        } else if (bookQuery != null) {
            getSearchBooks();
        } else if (copyright != null || fromYear != null || toYear != null) {
            bottomContainer.setVisibility(View.GONE);
            getFilteredBooks();
        } else {
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

    public void getFilteredBooks() {
        if (actualPage == 1) {
            backArrow.setVisibility(View.GONE);
        } else {
            backArrow.setVisibility(View.VISIBLE);
        }
        ((BaseActivity) getActivity()).showProgressDialog();
        Call<BooksResponse> booksResponse = ApiClient.getInterface().getAllBooks(bookQuery, copyright, fromYear, toYear, language);
        booksResponse.enqueue(new Callback<BooksResponse>() {
            @Override
            public void onResponse(Call<BooksResponse> call, Response<BooksResponse> response) {
                ((BaseActivity) getActivity()).hideProgressDialog();
                if (response.code() == 200) {
                    Log.d("Buscaminas", response.body().toString());
                    if (response.body().getResults().isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        notFoundContainer.setVisibility(View.VISIBLE);
                        bottomContainer.setVisibility(View.GONE);
                    } else {
                        booksList = response.body().getResults();
                        adapterManagement(response.body());
                    }
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

    public void getSavedBooks() {
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

    public void getSearchBooks() {
        if (actualPage == 1) {
            backArrow.setVisibility(View.GONE);
        } else {
            backArrow.setVisibility(View.VISIBLE);
        }
        ((BaseActivity) getActivity()).showProgressDialog();
        Call<BooksResponse> booksResponse = ApiClient.getInterface().getSpecificBook(bookQuery);
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


//    @Override
//    public void onResume() {
//        super.onResume();
//        if (booksListAdapter != null)
//            booksListAdapter.notifyDataSetChanged();
//    }
//

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("bookSearch", "onViewStateRestored " + bookQuery);
        ((MainActivity) getActivity()).updateTextSearchView(bookQuery);
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