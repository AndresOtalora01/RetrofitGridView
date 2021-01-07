package com.example.retrofitgridview.network;

public interface LoaderListener {
    void onLoaded(String result);
    void onFailure (String error);
}
