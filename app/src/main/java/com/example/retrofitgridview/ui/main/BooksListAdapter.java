package com.example.retrofitgridview.ui.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.R;
import com.example.retrofitgridview.ui.book.BooksManagement;

import java.util.ArrayList;
import java.util.List;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.ViewHolder> implements Filterable {
    private OnBookListener mOnBookListener;
    private List<Book> books = new ArrayList<>();
    private Context context;
    private LayoutInflater mInflater;
    private List<Book> fullBooksList;

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
        fullBooksList = new ArrayList<>(books);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconImage;
        TextView title;
        TextView author;
        ImageView iconDownloaded;
        OnBookListener onBookListener;
        List<Integer> downloadedBooksList;
        ViewHolder(View itemView, OnBookListener onBookListener) {
            super(itemView);
            iconImage = (ImageView) itemView.findViewById(R.id.iconImageView);
            title = itemView.findViewById(R.id.tvTitle);
            author = itemView.findViewById(R.id.tvAuthor);
            iconDownloaded = itemView.findViewById(R.id.ivDownloaded);
            this.onBookListener = onBookListener;
            itemView.setOnClickListener(this);
        }

        void bindData(final Book item) {
            Glide.with(context).load(item.getFormats().getImage()).into(iconImage);
            title.setText(item.getTitle());
            if (item.getAuthors().size() > 0) {
                author.setText(item.getAuthors().get(0).getName());
            } else {
                author.setText("Unknown");
            }

        }

        @Override
        public void onClick(View v) {
            onBookListener.onBookClick(getAdapterPosition());
            Log.d("posicion", getAdapterPosition()+"");
        }
    }



    public interface OnBookListener {
        void onBookClick(int position);
    }


    @Override
    public Filter getFilter() {
        return bookFilter;
    }

    private Filter bookFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) { // this method will be automatically executed on the background thread.
            List<Book> filteredBooks = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredBooks.addAll(fullBooksList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Book book : fullBooksList) {
                    if (book.getTitle().toLowerCase().contains(filterPattern)) {
                        filteredBooks.add(book);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredBooks;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            books.clear();
            books.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


    public Book getItem(int position) {
        return books.get(position);
    }

}
