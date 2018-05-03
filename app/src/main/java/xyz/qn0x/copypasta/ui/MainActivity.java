package xyz.qn0x.copypasta.ui;

import android.os.Bundle;
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

import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.RecyclerTouchListener;
import xyz.qn0x.copypasta.SnippetAdapter;
import xyz.qn0x.copypasta.persistence.entities.Snippet;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.snippetList);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // temporary
        final List<Snippet> foo = addData();

        SnippetAdapter adapter = new SnippetAdapter(foo);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView,
                new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Snippet snippet = foo.get(position);
                Toast.makeText(getApplicationContext(), snippet.getName() + " is selected!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here is a snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    // temporary data generation
    private List<Snippet> addData() {
        List<Snippet> list = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            String name = UUID.randomUUID().toString();
            String text = UUID.randomUUID().toString();

            list.add(new Snippet(name, text, "Tag"));
        }
        return list;
    }
}
