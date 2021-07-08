package com.example.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class    BookAdapter extends ArrayAdapter {

    public BookAdapter(Context context, ArrayList<Book> books) {
        super(context, 0,books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null)
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.book_inf, parent, false);

        Book currentBook = (Book) getItem(position);

//        ImageView image = listItemView.findViewById(R.id.image);
//        Picasso.with(getContext()).load(currentBook.getImage()).into(image);
//        image.setImageBitmap(currentBook.getImage());

        TextView tittle = listItemView.findViewById(R.id.tittle);
        tittle.setText(currentBook.getTittle());

        TextView author = listItemView.findViewById(R.id.author);
        author.setText(currentBook.getAuthor());

        TextView des = listItemView.findViewById(R.id.des);
        des.setText(currentBook.getDescription());

        return listItemView;
    }
}
