package com.example.retrofitgridview.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.R;

import java.util.ArrayList;
import java.util.List;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ViewHolder> {
    private OnBookListener mOnBookListener;
    private List<Book> books = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;

    public BooksListAdapter(Context context, OnBookListener mOnBookListener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mOnBookListener = mOnBookListener;
    }

    @Override
    public BooksListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.book_item, null);
        return new BooksListAdapter.ViewHolder(view, mOnBookListener);
    }

    @Override
    public void onBindViewHolder(BooksListAdapter.ViewHolder holder, int position) {
        holder.bindData(books.get(position));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public void setItems(List<Book> items) {
        books.clear();
        books.addAll(items);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconImage;
        TextView title;
        TextView author;
        OnBookListener onBookListener;

        ViewHolder(View itemView, OnBookListener onBookListener) {
            super(itemView);
            iconImage = (ImageView) itemView.findViewById(R.id.iconImageView);
            title = itemView.findViewById(R.id.tvTitle);
            author = itemView.findViewById(R.id.tvAuthor);
            this.onBookListener = onBookListener;
            itemView.setOnClickListener(this);
        }

        void bindData(final Book item) {
            Glide.with(context).load(item.getFormats().getImage()).into(iconImage);
            title.setText(item.getTitle());
            if(item.getAuthors().size() > 0) {
                author.setText(item.getAuthors().get(0).getName());
            } else {
                author.setText("Unknown");
            }

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
