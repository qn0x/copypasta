package xyz.qn0x.copypasta;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

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
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        snippetNameView = findViewById(R.id.snippetName);
        snippetTagsView = findViewById(R.id.tags);
        snippetTextView = findViewById(R.id.snippetText);

        FloatingActionButton fab = findViewById(R.id.saveButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(snippetNameView.getText()) ||
                        TextUtils.isEmpty(snippetTagsView.getText()) ||
                        TextUtils.isEmpty(snippetTextView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                } else {
                    String name = snippetNameView.getText().toString();
                    String text = snippetTextView.getText().toString();
                    String tags = snippetTagsView.getText().toString();
                    replyIntent.putExtra(NAME, name);
                    replyIntent.putExtra(TEXT, text);
                    replyIntent.putExtra(TAGS, tags);

                    setResult(RESULT_OK, replyIntent);
                }

                finish();
            }
        });
    }

}
