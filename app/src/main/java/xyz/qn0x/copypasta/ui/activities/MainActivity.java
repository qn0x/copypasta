package xyz.qn0x.copypasta.ui.activities;

import android.app.SearchManager;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.Arrays;
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

    private SnippetViewModel snippetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        final SnippetAdapter adapter = new SnippetAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // set up floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewSnippetActivity.class);
            startActivityForResult(intent, NEW_SNIPPET_ACTIVITY_REQUEST_CODE);
        });

        // instantiate ViewModel
        snippetViewModel = ViewModelProviders.of(this).get(SnippetViewModel.class);
        // ensure the recycler view stays updated with the current db state
        List<Snippet> snippetList = new LinkedList<>();
        snippetViewModel.getAllSnippets().observe(this, snippets -> {
            snippetList.addAll(snippets);
            adapter.setSnippets(snippets);
        });
        snippetViewModel.getAllSnippetTags().observe(this, snippetTags -> {
            if (snippetTags != null) {
                snippetList.forEach(snippet -> snippetTags.forEach(snippetTag -> {
                    if (snippet.getId() == snippetTag.getSnippet_id()) {
                        snippet.getTags().add(snippetTag.getTag());
                    }
                }));
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
                            Toast.makeText(getApplicationContext(), snippet.getName() + " is selected! id: " + snippet.getId(),
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, ViewSnippetActivity.class);
                            intent.putExtra("ID", snippet.getName());

                            StringBuilder sb = new StringBuilder("");
                            if (snippet.getTags() != null || snippet.getTags().size() != 0) {
                                snippet.getTags().forEach(tag -> sb.append(tag.getTag()).append(","));
                                if (sb.length() > 0)
                                    sb.deleteCharAt(sb.length() - 1);
                            }
                            intent.putExtra("TAGS", sb.toString());

                            intent.putExtra("TEXT", snippet.getText());
                            intent.putExtra("FAV", snippet.isFavorite());
                            Log.d(TAG, "view snippet ID: " + snippet.getId() + " FAV: " + snippet.isFavorite());
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Snackbar.make(view, "long click detected", Snackbar.LENGTH_LONG);
                    }
                }));

        // search stuff
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d(TAG, "Search query: " + query);
            List<Snippet> results = doMySearch(query);
            adapter.setSnippets(results);
        }

    }

    private List<Snippet> doMySearch(String query) {
        // we recommend that you return search results to your searchable activity with an Adapter
        // TODO search stuff here
        LiveData<List<Snippet>> results = snippetViewModel.getSnippetsByName(query);
        if (results.getValue() != null) {
            return results.getValue();
        } else {
            return null;
        }
    }

    // draw app bar options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        // set up search
        // TODO what the fuck
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    // digest any activity results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // a new snippet was successfully inserted
        if (requestCode == NEW_SNIPPET_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            StringBuilder log = new StringBuilder();

            // make new snippet
            String name = data.getStringExtra(NewSnippetActivity.NAME);
            String text = data.getStringExtra(NewSnippetActivity.TEXT);
            Snippet snippet = new Snippet(name, text);
            log.append("NAME: ").append(name).append("\n");
            log.append("TEXT: ").append(text).append("\n");

            // parse tags
            String tags = data.getStringExtra(NewSnippetActivity.TAGS);
            String[] list = tags.split(",");
            ArrayList<Tag> tagsList = new ArrayList<>();
            Arrays.stream(list).forEach(s -> tagsList.add(new Tag(s.trim())));
            snippet.setTags(tagsList);
            log.append(tags).append("\n");

            // set favorite
            boolean favorite = data.getBooleanExtra(NewSnippetActivity.FAV, false);
            snippet.setFavorite(favorite);
            log.append(Boolean.toString(favorite));

            snippetViewModel.insert(snippet);
            Log.d(TAG, log.toString());

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
