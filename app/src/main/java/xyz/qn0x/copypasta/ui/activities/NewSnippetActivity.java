package xyz.qn0x.copypasta.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.SnippetViewModel;
import xyz.qn0x.copypasta.persistence.entities.Snippet;
import xyz.qn0x.copypasta.persistence.entities.Tag;


/**
 * Handles activity: create a new snippet
 *
 * @author Janine Kostka
 */
public class NewSnippetActivity extends AppCompatActivity {

    private static final String TAG = "NewSnippetActivity";

    private boolean favorite = false;

    private EditText snippetNameView;
    private EditText snippetTextView;
    private EditText snippetTagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_snippet);

        // add app bar
        Toolbar toolbar = findViewById(R.id.new_snippet_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        snippetNameView = findViewById(R.id.snippetName);
        snippetTagsView = findViewById(R.id.tags);
        snippetTextView = findViewById(R.id.snippetText);

        FloatingActionButton fab = findViewById(R.id.saveButton);
        fab.setOnClickListener(view -> saveAndFinish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_snippet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.app_bar_favorite:
                if (!favorite) {
                    item.setIcon(R.drawable.ic_favorite_black_24dp);
                    favorite = true;
                } else {
                    item.setIcon(R.drawable.ic_favorite_border_black_24dp);
                    favorite = false;
                }
                return true;
            case R.id.app_bar_save:
                saveAndFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Gathers the user-entered data and closes the activity
     */
    private void saveAndFinish() {
        Intent replyIntent = new Intent();
        if (TextUtils.isEmpty(snippetNameView.getText()) ||
                TextUtils.isEmpty(snippetTagsView.getText()) ||
                TextUtils.isEmpty(snippetTextView.getText())) {
            setResult(RESULT_CANCELED, replyIntent);
        } else {
            StringBuilder log = new StringBuilder();

            // make new snippet
            String name = snippetNameView.getText().toString().trim();
            String text = snippetTextView.getText().toString().trim();
            Snippet snippet = new Snippet(name, text);
            log.append("NAME: ").append(name).append("\n");
            log.append("TEXT: ").append(text).append("\n");

            // parse tags
            String tags = snippetTagsView.getText().toString().trim();
            ArrayList<String> tagsArray = new ArrayList<>();
            String[] list = tags.split(",");
            Arrays.stream(list).forEach(s -> {
                tagsArray.add(s.trim().toLowerCase());
                log.append(s);
                log.append(" + ");
            });
            if (tagsArray.size() >= 1) {
                List<Tag> tagsList = new ArrayList<>();
                tagsArray.forEach(t -> tagsList.add(new Tag(t)));
                snippet.setTags(tagsList);
                log.append(tags).append("\n");
            } else {
                Log.d(TAG, "no tags to save");
            }

            // set favorite
            snippet.setFavorite(favorite);
            log.append(Boolean.toString(favorite));


            // persist to db
            SnippetViewModel snippetViewModel = ViewModelProviders.of(this).get(SnippetViewModel.class);
            snippetViewModel.insert(snippet);
            Log.d(TAG, log.toString());


            setResult(RESULT_OK, replyIntent);
        }


        // wait for db operation to finish

        finish();
    }
}
