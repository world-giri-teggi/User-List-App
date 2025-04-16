package com.userlistapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<User> userList = new ArrayList<>();
    private UserAdapter adapter;
    private ListView userListView;
    private EditText searchEditText;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchEditText = findViewById(R.id.searchEditText);
        userListView = findViewById(R.id.userListView);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);

        adapter = new UserAdapter(this, userList);
        userListView.setAdapter(adapter);

        userListView.setOnItemClickListener((parent, view, position, id) -> {
            User clickedUser = (User) adapter.getItem(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("name", clickedUser.getFirstName() + " " + clickedUser.getLastName());
            intent.putExtra("email", clickedUser.getEmail());
            intent.putExtra("avatar", clickedUser.getAvatar());
            startActivity(intent);
        });

        swipeRefreshLayout.setOnRefreshListener(this::fetchUsers);

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        fetchUsers();
    }

    private void fetchUsers() {
        swipeRefreshLayout.setRefreshing(true);
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://reqres.in/api/users?page=2")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> swipeRefreshLayout.setRefreshing(false));
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        JSONObject obj = new JSONObject(jsonData);
                        JSONArray data = obj.getJSONArray("data");
                        Gson gson = new Gson();

                        List<User> parsedList = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject userObj = data.getJSONObject(i);
                            User u = gson.fromJson(userObj.toString(), User.class);
                            parsedList.add(u);
                        }

                        runOnUiThread(() -> {
                            userList.clear();
                            userList.addAll(parsedList);
                            adapter.updateData(parsedList);
                            swipeRefreshLayout.setRefreshing(false);
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
