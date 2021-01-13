package com.example.retrofitgridview.ui.book;

import android.content.Context;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;

import com.example.retrofitgridview.models.Book;
import com.example.retrofitgridview.ui.main.BaseActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class BooksManagement {
    private Context context;
    private List<Integer> booksIds = new ArrayList<>();
    private static BooksManagement booksManagement;


    public static BooksManagement init(Context context) {
        if (booksManagement == null) {
            booksManagement = new BooksManagement();
            booksManagement.context = context;
            booksManagement.booksIds = new ArrayList<>();
        }
        return booksManagement;
    }

    public void saveBookToMemory(String fileName, String content) {
        File directory = context.getFilesDir();
        File file = new File(directory, "books");
        String path = context.getFilesDir() + "/books/";
        if (!file.exists()) {
            file.mkdirs();
        }
        try {
            File gpxfile = new File(file, fileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String loadBookFromMemory(String file) {
        StringBuilder sb = new StringBuilder();
        String path = context.getFilesDir() + "/books/";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + file));
            String content;
            while ((content = br.readLine()) != null) {
                sb.append(content).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }



    public List<Integer> getSavedBooks() {
        String path = context.getFilesDir() + "/books/";
        String id;
        File bookPath = new File(path);
        String [] names = bookPath.list();
        for (String f : names) {
            if(f.contains(".txt")) {
                id = f.substring(0, f.lastIndexOf('.'));
                booksIds.add(Integer.parseInt(id));
            }
        }
        return booksIds;
    }
    public static BooksManagement getBooksManagement() {
        if(booksManagement == null) {
            booksManagement = new BooksManagement();
        }
        return booksManagement;
    }
}
