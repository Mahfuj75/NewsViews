package com.mahfuj.newsviews;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.gson.Gson;
import com.mahfuj.newsviews.JsonToJava.Article;
import com.mahfuj.newsviews.JsonToJava.JsonToJava;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GoogleSignInAccount account;
    JsonToJava jsonToJava;
    SwipeRefreshLayout swipeContainer;
    String url = "https://newsapi.org/v2/everything?q=";
    String quarry = "https://newsapi.org/v2/top-headlines?country=us&apiKey=fe9050a9325149b2adf22684fa8425b7";
    String extra = "&sortBy=recent";
    String key ="&apiKey=fe9050a9325149b2adf22684fa8425b7";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        account = GoogleSignIn.getLastSignedInAccount(this);
        swipeContainer =  findViewById(R.id.swipeContainer);
        handleIntent(getIntent());



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView textViewPersonName = navigationView.getHeaderView(0).findViewById(R.id.textViewPersonName);
        CircleImageView imageView = navigationView.getHeaderView(0).findViewById(R.id.imageView);
        TextView textViewEmail = navigationView.getHeaderView(0).findViewById(R.id.textViewEmail);


        textViewPersonName.setText(account.getDisplayName());
        Picasso.with(this).load(account.getPhotoUrl()).into(imageView);
        textViewEmail.setText(account.getEmail());

        navigationView.setNavigationItemSelectedListener(this);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override

            public void onRefresh() {

                JsonTask jsonTask = new JsonTask();
                jsonTask.execute(quarry);

            }

        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        JsonTask jsonTask = new JsonTask();
        jsonTask.execute(quarry);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        assert searchManager != null;
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_bbc:
                // Handle the camera action
                break;
            case R.id.nav_cnbc:

                break;
            case R.id.nav_engadget:

                break;
            case R.id.nav_espn:

                break;
            case R.id.nav_share:

                break;
            case R.id.nav_send:

                break;
        }

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            JsonTask jsonTask = new JsonTask();
            this.quarry = url+query+extra+key;
            jsonTask.execute(this.quarry);

        }
    }

    ///
    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);

        }


        protected String doInBackground(String... params) {


            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream stream = connection.getInputStream();

                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                jsonToJava = new Gson().fromJson(response.toString(), JsonToJava.class);
                return null;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();

                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(jsonToJava!=null)
            {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                RecyclerView recyclerView = findViewById(R.id.recycler_view);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(getApplicationContext(), (ArrayList<Article>) jsonToJava.getArticles(),lp);
                recyclerView.setAdapter(adapter);
                swipeContainer.setRefreshing(false);

            }
            else {
                swipeContainer.setRefreshing(false);
            }
        }
    }


    ///

}
