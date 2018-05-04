package xyz.qn0x.copypasta.ui;

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
import android.view.View;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import xyz.qn0x.copypasta.NewSnippetActivity;
import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.RecyclerTouchListener;
import xyz.qn0x.copypasta.SnippetAdapter;
import xyz.qn0x.copypasta.SnippetViewModel;
import xyz.qn0x.copypasta.persistence.entities.Snippet;


public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;

    private SnippetViewModel snippetViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.snippetList);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final SnippetAdapter adapter = new SnippetAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewSnippetActivity.class);
                startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

        snippetViewModel = ViewModelProviders.of(this).get(SnippetViewModel.class);
        snippetViewModel.getAllSnippets().observe(this, new Observer<List<Snippet>>() {
            @Override
            public void onChanged(@Nullable List<Snippet> snippets) {
                adapter.setSnippets(snippets);
            }
        });

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
                        }
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        Snackbar.make(view, "long click detected", Snackbar.LENGTH_LONG);
                    }
                }));

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            String name = data.getStringExtra(NewSnippetActivity.NAME);
            String text = data.getStringExtra(NewSnippetActivity.TEXT);
            String tags = data.getStringExtra(NewSnippetActivity.TAGS);
            Snippet snippet = new Snippet(name, text, tags);
            snippetViewModel.insert(snippet);
        } else {
            Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG)
                    .show();
        }
    }

    // temporary data generation
    private List<Snippet> addData() {
        List<Snippet> list = new LinkedList<>();
        for (int i = 0; i < 3; i++) {
            String name = UUID.randomUUID().toString();
            String text = UUID.randomUUID().toString();

            list.add(new Snippet(name, text, "Tag"));
        }
        return list;
    }
}
