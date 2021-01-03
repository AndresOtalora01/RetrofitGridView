package com.example.retrofitgridview;

public interface LoaderListener {
    void onLoaded(String result);
    void onFailure (String error);
}
