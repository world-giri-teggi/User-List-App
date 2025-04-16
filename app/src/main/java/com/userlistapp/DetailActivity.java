package com.userlistapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView nameTextView = findViewById(R.id.nameTextView);
        TextView emailTextView = findViewById(R.id.emailTextView);
        ImageView avatarImageView = findViewById(R.id.avatarImageView);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String avatar = getIntent().getStringExtra("avatar");

        nameTextView.setText(name);
        emailTextView.setText(email);

        Glide.with(this)
                .load(avatar)
                .into(avatarImageView);
    }
}
