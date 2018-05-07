package xyz.qn0x.copypasta.ui.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.ui.utility.RecyclerTouchListener;
import xyz.qn0x.copypasta.ui.utility.SnippetAdapter;
import xyz.qn0x.copypasta.SnippetViewModel;
import xyz.qn0x.copypasta.persistence.entities.Snippet;


public class MainActivity extends AppCompatActivity {

    public static final int NEW_SNIPPET_ACTIVITY_REQUEST_CODE = 1;

    private SnippetViewModel snippetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        toolbar.getVisibility();

        // set up recycler view
        RecyclerView recyclerView = findViewById(R.id.snippetList);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final SnippetAdapter adapter = new SnippetAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set up floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewSnippetActivity.class);
                startActivityForResult(intent, NEW_SNIPPET_ACTIVITY_REQUEST_CODE);
            }
        });

        // instantiate ViewModel
        snippetViewModel = ViewModelProviders.of(this).get(SnippetViewModel.class);
        // ensure the recycler view stays updated with the current db state
        snippetViewModel.getAllSnippets().observe(this, new Observer<List<Snippet>>() {
            @Override
            public void onChanged(@Nullable List<Snippet> snippets) {
                adapter.setSnippets(snippets);
            }
        });

        // react to touches on the recycler view list
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (snippetViewModel.getAllSnippets().getValue() == null) {
                            Toast.makeText(getApplicationContext(), "position is null",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Snippet snippet = snippetViewModel.getAllSnippets().getValue().get(position);
                            Toast.makeText(getApplicationContext(), snippet.getName() + " is selected!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ViewSnippetActivity.class);
                            intent.putExtra("ID", snippet.getName());
                            //intent.putExtra("TAGS", snippet.getTags());
                            intent.putExtra("TEXT", snippet.getText());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Snackbar.make(view, "long click detected", Snackbar.LENGTH_LONG);
                    }
                }));

    }

    // draw app bar options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    // digest any activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // a new snippet was successfully inserted
        if (requestCode == NEW_SNIPPET_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewSnippetActivity.NAME);
            String text = data.getStringExtra(NewSnippetActivity.TEXT);
            String tags = data.getStringExtra(NewSnippetActivity.TAGS);
            Snippet snippet = new Snippet(name, text);
            snippetViewModel.insert(snippet);
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG)
                    .show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_search:
                Toast.makeText(getApplicationContext(), "search clicked", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "settings clicked", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
