package com.example.retrofitgridview.network;

import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.models.BooksResponse;

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
    Call<BooksResponse> getSpecificBook (@Query(value = "search") String query );



}
