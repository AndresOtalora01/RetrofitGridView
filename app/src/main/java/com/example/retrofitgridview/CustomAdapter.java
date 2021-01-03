package com.example.retrofitgridview;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    private OnBookListener mOnBookListener;
    private List<Book> books;
    private Context context;
    private LayoutInflater mInflater;

    public CustomAdapter(List<Book> books, Context context, OnBookListener mOnBookListener) {
        this.books = books;
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mOnBookListener = mOnBookListener;
    }

    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_item, null);
        return new CustomAdapter.ViewHolder(view, mOnBookListener);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.ViewHolder holder, int position) {
        holder.bindData(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setItems(List<Book> items) {
        books = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconImage;
        TextView title;
        OnBookListener onBookListener;

        ViewHolder(View itemView, OnBookListener onBookListener) {
            super(itemView);
            iconImage = (ImageView) itemView.findViewById(R.id.iconImageView);
            title = itemView.findViewById(R.id.tvTitle);
            this.onBookListener = onBookListener;
            itemView.setOnClickListener(this);
        }

        void bindData(final Book item) {
            Glide.with(context).load(item.getFormats().getImage()).into(iconImage);
            title.setText(item.getTitle());
        }

        @Override
        public void onClick(View v) {
            onBookListener.onBookClick(getAdapterPosition());
        }
    }

    public interface OnBookListener {
        void onBookClick(int position);
    }

}
