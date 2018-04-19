package xyz.qn0x.copypasta;

import android.os.Bundle;
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

    }

    private List<Snippet> addData() {
        List<Snippet> list = new LinkedList<>();
        for (int i = 0; i < 100; i++) {
            String name = UUID.randomUUID().toString();
            String text = UUID.randomUUID().toString();

            list.add(new Snippet(name, text, "Tag"));
        }

        /*for (Snippet s : list) {
            String out = s.getName() + s.getText();
            for (String string : s.getTags()) {
                out += string;
            }
            System.out.println(out);
        }*/
        return list;
    }
}
