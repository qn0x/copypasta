package xyz.qn0x.copypasta.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import xyz.qn0x.copypasta.R;

public class ViewSnippetActivity extends AppCompatActivity {

    private static final String TAG = "ViewSnippetActivity";

    private boolean favorite = false;

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

        favorite = intent.getBooleanExtra("FAV", false);

        Log.d(TAG, "Selected Snippet. ID: " + intent.getStringExtra("ID") + "  FAV: "
                + intent.getBooleanExtra("FAV", false));
    }

    // draw app bar options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_snippet, menu);

        // set favorite state of the snippet
        Toolbar toolbar = findViewById(R.id.toolbar);
        MenuItem favoriteIcon = toolbar.getMenu().findItem(R.id.action_view_fav);
        if (favorite) {
            favoriteIcon.setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            favoriteIcon.setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_view_fav:
                // TODO update favorite status in db here
                return true;
            case R.id.action_edit:
                Toast.makeText(getApplicationContext(), "edit clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_delete:
                Toast.makeText(getApplicationContext(), "delete clicked", Toast.LENGTH_SHORT).show();

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
