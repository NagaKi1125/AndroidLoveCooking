package com.sict.android.lovecooking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sict.android.lovecooking.Adapter.DishSearchAdapter;
import com.sict.android.lovecooking.Model.SearchFilter;
import com.sict.android.lovecooking.Remote.RetrofitClient;
import com.sict.android.lovecooking.Services.ApplicationActivityServices;
import com.sict.android.lovecooking.Services.ApplicationInfoServices;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    ApplicationInfoServices applicationInfoServices;
    ApplicationActivityServices applicationActivityServices;

    RecyclerView searchRecyclerView;
    DishSearchAdapter dishSearchAdapter;
    LinearLayout searchNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //find view id
        searchRecyclerView = (RecyclerView)findViewById(R.id.searchRecycler);
        searchNote = (LinearLayout)findViewById(R.id.search_note);

        dishSearchAdapter = new DishSearchAdapter();

        applicationActivityServices =
                RetrofitClient.getRetrofit().create(ApplicationActivityServices.class);

        searchRecyclerView.setVisibility(View.GONE);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSearchFilter("");
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu_item,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRecyclerView.setVisibility(View.VISIBLE);
                searchNote.setVisibility(View.GONE);
                dishSearchAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchRecyclerView.setVisibility(View.VISIBLE);
                searchNote.setVisibility(View.GONE);
                dishSearchAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return true;
    }

    public void getSearchFilter(String data){
        Call<List<SearchFilter>> searchFilterCall = applicationActivityServices.getSearchResult(data);
        searchFilterCall.enqueue(new Callback<List<SearchFilter>>() {
            @Override
            public void onResponse(Call<List<SearchFilter>> call, Response<List<SearchFilter>> response) {
                if(response.isSuccessful()){
                    dishSearchAdapter.setData(response.body(),SearchActivity.this);
                    searchRecyclerView.setAdapter(dishSearchAdapter);
                    dishSearchAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(SearchActivity.this,
                            "data not found",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchFilter>> call, Throwable t) {
                Toast.makeText(SearchActivity.this,
                        "Search: Serve could not response\n"+t.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SearchActivity.this,MainActivity.class);
        finish();startActivity(intent);
        super.onBackPressed();
    }
}