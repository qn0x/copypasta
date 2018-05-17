package xyz.qn0x.copypasta.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import xyz.qn0x.copypasta.R;


/**
 * Handles activity: create a new snippet
 *
 * @author Janine Kostka
 */
public class NewSnippetActivity extends AppCompatActivity {

    public static final String NAME = "NAME";
    public static final String TAGS = "TAGS";
    public static final String TEXT = "TEXT";
    public static final String FAV = "FAV";

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
            String name = snippetNameView.getText().toString();
            replyIntent.putExtra(NAME, name);

            String text = snippetTextView.getText().toString();
            replyIntent.putExtra(TEXT, text);

            String tags = snippetTagsView.getText().toString();
            replyIntent.putExtra(TAGS, tags);

            replyIntent.putExtra(FAV, favorite);

            setResult(RESULT_OK, replyIntent);
        }

        finish();
    }
}
