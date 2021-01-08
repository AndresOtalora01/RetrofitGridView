package com.example.retrofitgridview.ui.book;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.retrofitgridview.R;

public class ImageFragment extends Fragment {
    private String imageUrl;
    private int totalPages;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.page_fragment, container, false);
        ImageView image = (ImageView) v.findViewById(R.id.ivCover);
        TextView textView = (TextView) v.findViewById(R.id.tvPageNumber);
        Glide.with(getActivity()).load(imageUrl).into(image);
        textView.setText(totalPages+" páginas");
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageUrl = getArguments().getString("url");
        totalPages = getArguments().getInt("pages");
    }

    public static ImageFragment newInstance(String text, int totalPages) {

        ImageFragment imageFragment = new ImageFragment();
        Bundle b = new Bundle();
        b.putString("url", text);
        b.putInt("pages", totalPages);

        imageFragment.setArguments(b);

        return imageFragment;
    }
}
