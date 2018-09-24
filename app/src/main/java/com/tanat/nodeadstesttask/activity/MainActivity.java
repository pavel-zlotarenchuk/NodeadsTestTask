package com.tanat.nodeadstesttask.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.tanat.nodeadstesttask.Constants;
import com.tanat.nodeadstesttask.R;
import com.tanat.nodeadstesttask.adapter.RecyclerViewAdapter;
import com.tanat.nodeadstesttask.api.ApiService;
import com.tanat.nodeadstesttask.api.RetroClient;
import com.tanat.nodeadstesttask.db.DataSource;
import com.tanat.nodeadstesttask.fragment.EditComment;
import com.tanat.nodeadstesttask.loader.FavoritesLoader;
import com.tanat.nodeadstesttask.model.Declaration;
import com.tanat.nodeadstesttask.model.Declarations;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.RecyclerViewAdapterListener,
        EditComment.CommentListener{
    private EditText searchEditText;
    private Button searchButton;
    private Button favoritesButton;
    private RecyclerView recyclerView;
    private ProgressDialog dialog;
    private LinearLayout emptyLinearLayout;

    private ApiService api;
    private RecyclerViewAdapter adapter;
    private DataSource dataSource;

    private List<Declaration> declarationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        api = RetroClient.getApiService();
        dataSource = DataSource.getInstance(MainActivity.this);
    }

    private void initView() {
        searchEditText = findViewById(R.id.searchEditText);
        searchButton = findViewById(R.id.searchButton);
        favoritesButton = findViewById(R.id.favoritesButton);
        recyclerView = findViewById(R.id.recyclerView);
        emptyLinearLayout = findViewById(R.id.emptyLinearLayout);

        searchButton.setOnClickListener(searchListener);
        favoritesButton.setOnClickListener(favoritesListener);

        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(llm);

        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Loading...");
    }

    private void loadLocalData() {
        getSupportLoaderManager().restartLoader(0, null, loaderCallbacks);
    }

    private void setAdapter(List<Declaration> listNetDeclaration, List<Declaration> listDBDeclaration) {
        adapter = new RecyclerViewAdapter(MainActivity.this, listNetDeclaration, listDBDeclaration);
        recyclerView.setAdapter(adapter);
    }

    View.OnClickListener searchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            dialog.show();
            String name = String.valueOf(searchEditText.getText());
            Call<Declarations> call = api.getDeclarations(name);
            call.enqueue(new Callback<Declarations>() {
                @Override
                public void onResponse(Call<Declarations> call, Response<Declarations> response) {
                    if (response.isSuccessful() && response.body().getMessage() == null) {
                        List<Declaration> declarations = response.body().getItems();
                        if (null != declarations || !declarations.isEmpty()) {
                            Log.d("test", String.valueOf(declarations.size()));
                            declarationList = declarations;
                        }
                    } else if (response.body().getMessage() != null) {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Server error", Toast.LENGTH_LONG).show();
                    }
                    loadLocalData();
                }

                @Override
                public void onFailure(Call<Declarations> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "No internet connection\n" +
                            "The will be loaded list of favorites", Toast.LENGTH_LONG).show();
                    loadLocalData();
                }
            });
        }
    };

    View.OnClickListener favoritesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            declarationList = null;
            loadLocalData();
        }
    };

    @Override
    public void openPdf(String link) {
        if (null != link) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra(Constants.LINK, link);
            startActivity(intent);
        } else {
            Toast.makeText(MainActivity.this, "PDF not found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setFavorites(Declaration declaration) {
        if (dataSource.itemIsExist(declaration.getId())) {
            dataSource.deleteItem(declaration.getId());
            declaration.setComment(null);
            loadLocalData();
        } else {
            editComment(declaration, EditComment.ADD);
        }
    }

    @Override
    public void editComment(Declaration declaration, int operationType){
        EditComment editComment = new EditComment();
        editComment.newInstance(declaration, operationType);
        editComment.show(getSupportFragmentManager(), "create_group");
    }

    private LoaderManager.LoaderCallbacks<List<Declaration>> loaderCallbacks = new LoaderManager
            .LoaderCallbacks<List<Declaration>>() {
        @NonNull
        @Override
        public Loader<List<Declaration>> onCreateLoader(int id, @Nullable Bundle args) {
            return new FavoritesLoader(MainActivity.this, null);
        }

        @Override
        public void onLoadFinished(@NonNull Loader<List<Declaration>> loader, List<Declaration> data) {
            dialog.dismiss();
            if ((declarationList == null || declarationList.isEmpty())
                    && (data == null || data.isEmpty())) {
                emptyLinearLayout.setVisibility(View.VISIBLE);
            } else {
                setAdapter(declarationList, data);
                emptyLinearLayout.setVisibility(View.GONE);
            }
        }

        @Override
        public void onLoaderReset(@NonNull Loader<List<Declaration>> loader) {

        }
    };

    @Override
    public void commentSave(Declaration declaration, int operationType) {
        if (operationType == EditComment.ADD) {
            dataSource.addItem(declaration);
        } else if (operationType == EditComment.EDIT){
            dataSource.updateItem(declaration);
        }
        loadLocalData();
    }
}
