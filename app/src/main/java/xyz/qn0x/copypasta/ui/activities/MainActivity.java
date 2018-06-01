package xyz.qn0x.copypasta.ui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.LinkedList;
import java.util.List;

import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.SnippetViewModel;
import xyz.qn0x.copypasta.persistence.entities.Snippet;
import xyz.qn0x.copypasta.persistence.entities.Tag;
import xyz.qn0x.copypasta.ui.utility.RecyclerTouchListener;
import xyz.qn0x.copypasta.ui.utility.SnippetAdapter;


/**
 * Handles activity: main activity
 *
 * @author Janine Kostka
 */

// TODO Bug: Tags werden erst nach 1x auswählen + zurück eines Snippets angezeigt.
public class MainActivity extends AppCompatActivity {

    public static final int NEW_SNIPPET_ACTIVITY_REQUEST_CODE = 1;

    private static final String TAG = "MainActivity";

    public static MainActivity instance;

    private SnippetViewModel snippetViewModel;
    public SnippetAdapter adapter;

    LiveData<List<Snippet>> liveSnippets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        instance = this;

        // database reader for debugging purposes
        Stetho.initializeWithDefaults(this);

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
        adapter = new SnippetAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set up floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewSnippetActivity.class);
            startActivityForResult(intent, NEW_SNIPPET_ACTIVITY_REQUEST_CODE);
        });

        updateAdapter();

        recyclerView.setAdapter(adapter);


        //initPopData().forEach(snippetViewModel::insert);

        // react to touches on the recycler view list
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        if (snippetViewModel.getAllSnippets().getValue() == null) {
                            Toast.makeText(getApplicationContext(), "position is null",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Snippet snippet = adapter.getSnippetList().get(position);
                            Toast.makeText(getApplicationContext(), snippet.getName() + " is selected! id: " + snippet.getId(),
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ViewSnippetActivity.class);
                            intent.putExtra("ID", snippet.getId());
                            intent.putExtra("NAME", snippet.getName());

                            StringBuilder sb = new StringBuilder("");
                            if (snippet.getTags() != null || snippet.getTags().size() != 0) {
                                snippet.getTags().forEach(tag -> sb.append(tag.getTag()).append(","));
                                if (sb.length() > 0)
                                    sb.deleteCharAt(sb.length() - 1);
                            }
                            intent.putExtra("TAGS", sb.toString());

                            intent.putExtra("TEXT", snippet.getText());
                            intent.putExtra("FAV", snippet.isFavorite());
                            Log.d(TAG, "view snippet ID: " + snippet.getId() + " FAV: " + snippet.isFavorite() + "Tags: " + sb.toString());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Snackbar.make(view, "long click detected", Snackbar.LENGTH_LONG);
                    }
                }));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void updateAdapter() {
        Log.d(TAG, "updating adapter");
        // instantiate ViewModel
        snippetViewModel = ViewModelProviders.of(this).get(SnippetViewModel.class);
        // ensure the recycler view stays updated with the current db state
        liveSnippets = snippetViewModel.getAllSnippets();
        snippetViewModel.getAllSnippets().observeForever(snippets -> {
            for (Snippet snippet : snippets) {
                List<Tag> tags = snippetViewModel.getTagsForSnippetId(snippet.getId());

                StringBuilder sb = new StringBuilder();
                tags.forEach(tag -> sb.append(tag.getTag()).append(" | "));
                Log.d(TAG, snippet.getName() + " has the following tags: " + sb.toString());

                snippet.setTags(tags);
            }


            adapter.setSnippets(snippets);
        });
    }

    private List<Snippet> doSearch(String query) {
        if (query.equalsIgnoreCase("")) {
            Log.d(TAG, "no search query, returning all snippets");
            return snippetViewModel.getAllSnippets().getValue();
        }

        List<Long> result = new LinkedList<>();
        if (query.startsWith("#") && query.length() > 1) {
            Log.d(TAG, "searching for a tag");
            result = snippetViewModel.getSnippetsByTag(query.substring(1, query.length()));
        } else if (query.startsWith("@") && query.length() > 1) {
            Log.d(TAG, "searching for a name");
            result = snippetViewModel.getSnippetsByName(query.substring(1, query.length()));
        } else {
            Log.d(TAG, "searching the full text");
            result = snippetViewModel.getSnippetsForText(query);
        }

        Log.d(TAG, "result size " + result.size());

        List<Snippet> newList = new LinkedList<>();
        for (long id : result) {
            Snippet snippet = snippetViewModel.getSnippetForId(id);
            snippetViewModel.getTagsForSnippetId(id).forEach(snippet.getTags()::add);
            newList.add(snippet);
        }

        return newList;
    }

    // draw app bar options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // set up search
        // TODO suche nach text und suche nach tags
        final SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "search query: " + query);
                List<Snippet> results = doSearch(query);
                adapter.setSnippets(results);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "search query: " + newText);
                List<Snippet> results = doSearch(newText);
                adapter.setSnippets(results);
                return false;
            }
        });

        // show all snippets after closing the search
        searchView.setOnCloseListener(() -> {
            Log.d(TAG, "closing search");
            adapter.setSnippets(snippetViewModel.getAllSnippets().getValue());
            return false;
        });

        return true;
    }

    // digest any activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // a new snippet was successfully inserted
        if (requestCode == NEW_SNIPPET_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            updateAdapter();

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
                Log.wtf(TAG, "Options menu recorded a nonexistent action");
                return super.onOptionsItemSelected(item);
        }
    }

}
