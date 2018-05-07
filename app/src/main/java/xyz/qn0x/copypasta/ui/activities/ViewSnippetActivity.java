package xyz.qn0x.copypasta.ui.activities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.SnippetViewModel;
import xyz.qn0x.copypasta.persistence.entities.Snippet;

public class ViewSnippetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_snippet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null)
            ab.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        TextView vName = findViewById(R.id.content_name);
        vName.setText(intent.getStringExtra("ID"));

        TextView vTags = findViewById(R.id.content_tags);
        vTags.setText(intent.getStringExtra("TAGS"));

        TextView vText = findViewById(R.id.content_text);
        vText.setText(intent.getStringExtra("TEXT"));
    }

}
