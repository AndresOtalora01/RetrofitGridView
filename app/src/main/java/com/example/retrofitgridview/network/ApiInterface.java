package com.example.retrofitgridview.network;

import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.models.BooksResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {
    @GET("books/")
    Call<BooksResponse> getAllBooks(@Query("page") int page);

    @GET
    Call<BooksResponse> getPreviousBooks(@Url String url);

    @GET
    Call<BooksResponse> getNextBooks(@Url String url);

    @GET("books/")
    Call<BooksResponse> getSpecificBooks(@Query("ids") String query);

    @GET("books/")
    Call<BooksResponse> getSpecificBook(@Query("search") String query);

    @GET("books/")
    Call<BooksResponse> getAllBooks(@Query("search") String query,
                                    @Query("copyright") Boolean copyright,
                                    @Query("author_year_start") String fromYear,
                                    @Query("author_year_end") String toYear,
                                    @Query("languages") String language);


}
