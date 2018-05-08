package xyz.qn0x.copypasta.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import xyz.qn0x.copypasta.R;

public class NewSnippetActivity extends AppCompatActivity {

    public static final String NAME = "NAME";
    public static final String TAGS = "TAGS";
    public static final String TEXT = "TEXT";

    private EditText snippetNameView;
    private EditText snippetTextView;
    private EditText snippetTagsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_snippet);
        Toolbar toolbar = findViewById(R.id.new_snippet_toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        snippetNameView = findViewById(R.id.snippetName);
        snippetTagsView = findViewById(R.id.tags);
        snippetTextView = findViewById(R.id.snippetText);

        FloatingActionButton fab = findViewById(R.id.saveButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndFinish();
            }
        });
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
            case R.id.app_bar_save:
                saveAndFinish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

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

            setResult(RESULT_OK, replyIntent);
        }

        finish();
    }
}
