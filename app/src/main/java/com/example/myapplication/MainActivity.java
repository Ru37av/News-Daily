package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements categoryRVAdaptor.CategoryClickInterface{
//API_KEY = e5b97646a9274e7197caec20773f78f2

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<categoryRVmodel> categoryRVmodelArrayList;
    private categoryRVAdaptor categoryRVAdaptor;
    private newsRVAdaptor newsRVAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        newsRV = findViewById(R.id.idRVnews);
        categoryRV = findViewById(R.id.idRVcategories);
        loadingPB = findViewById(R.id.idPBloading);
        articlesArrayList = new ArrayList<>();
        categoryRVmodelArrayList = new ArrayList<>();
        newsRVAdaptor = new newsRVAdaptor(articlesArrayList,this);
        categoryRVAdaptor = new categoryRVAdaptor(categoryRVmodelArrayList,this, this);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdaptor);
        categoryRV.setAdapter(categoryRVAdaptor);
        getCategories();
        getNews("All");
        newsRVAdaptor.notifyDataSetChanged();



    }
    private void getCategories(){
        categoryRVmodelArrayList.add(new categoryRVmodel("All","https://images.unsplash.com/photo-1504711331083-9c895941bf81?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzZ8fG5ld3N8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVmodelArrayList.add(new categoryRVmodel("Technology","https://images.unsplash.com/photo-1542831371-29b0f74f9713?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTcwfHx0ZWNobm9sb2d5fGVufDB8fDB8fA%3D%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVmodelArrayList.add(new categoryRVmodel("Science","https://images.unsplash.com/flagged/photo-1584036561584-b03c19da874c?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Njl8fHNjaWVuY2V8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVmodelArrayList.add(new categoryRVmodel("Sports","https://images.unsplash.com/photo-1593265035899-fa2a74a21b53?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTEwfHxzcG9ydHN8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVmodelArrayList.add(new categoryRVmodel("General","https://images.unsplash.com/photo-1504465039710-0f49c0a47eb7?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8Nzl8fG5ld3MlMjByZXBvcnRlcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVmodelArrayList.add(new categoryRVmodel("Business","https://images.unsplash.com/photo-1532619187608-e5375cab36aa?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MjA1fHxidXNzaW5lc3N8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVmodelArrayList.add(new categoryRVmodel("Entertainment","https://images.unsplash.com/photo-1624364543955-9b3edc4aa0be?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzZ8fGVudGVydGFpbm1lbnR8ZW58MHx8MHx8&auto=format&fit=crop&w=600&q=60"));
        categoryRVmodelArrayList.add(new categoryRVmodel("Health","https://images.unsplash.com/photo-1624727828489-a1e03b79bba8?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxzZWFyY2h8MzB8fGRvY3RvcnxlbnwwfHwwfHw%3D&auto=format&fit=crop&w=600&q=60"));
        categoryRVAdaptor.notifyDataSetChanged();
    }
    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apikey=e5b97646a9274e7197caec20773f78f2";
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDmains=stackoverflow.com&sortBy=publishedAt&language=en&apikey=e5b97646a9274e7197caec20773f78f2";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;
        if (category.equals("All")){
            call = retrofitAPI.getAllNews(url);
        }
        else {
            call = retrofitAPI.getAllByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();
                for (int i=0; i<articles.size(); i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));

                }
                newsRVAdaptor.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this,"SORRY...Fail to get NEWS",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVmodelArrayList.get(position).getCategory();
        getNews(category);
    }
}