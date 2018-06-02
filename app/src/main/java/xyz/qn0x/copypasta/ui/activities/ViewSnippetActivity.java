package xyz.qn0x.copypasta.ui.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.SnippetViewModel;
import xyz.qn0x.copypasta.persistence.entities.SnippetTags;
import xyz.qn0x.copypasta.persistence.entities.Tag;

public class ViewSnippetActivity extends AppCompatActivity {

    private static final String TAG = "ViewSnippetActivity";

    private boolean favorite = false;
    private long snippetId;
    private Menu appBarMenu;

    private String oldName;
    private String oldText;
    private String oldTags;

    EditText vName;
    EditText vTags;
    EditText vText;


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
        snippetId = intent.getLongExtra("ID", 1);

        vName = findViewById(R.id.view_name);
        vName.setEnabled(false);
        oldName = intent.getStringExtra("NAME");
        vName.setText(oldName);

        vTags = findViewById(R.id.view_tags);
        vTags.setEnabled(false);
        oldTags = intent.getStringExtra("TAGS");
        vTags.setText(oldTags);

        vText = findViewById(R.id.view_text);
        vText.setEnabled(false);
        oldText = intent.getStringExtra("TEXT");
        vText.setText(oldText);

        favorite = intent.getBooleanExtra("FAV", false);


        Log.d(TAG, "Selected Snippet. ID: " + intent.getLongExtra("ID", 1) + "  FAV: "
                + intent.getBooleanExtra("FAV", false));
    }

    // draw app bar options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_snippet, menu);
        updateActionBarFavorite();
        appBarMenu = menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SnippetViewModel snippetViewModel = ViewModelProviders.of(this).get(SnippetViewModel.class);
        switch (item.getItemId()) {
            case R.id.action_view_fav:
                snippetViewModel.updateFavoriteStatus(snippetId, !favorite);
                favorite = !favorite;
                updateActionBarFavorite();
                return true;

            case R.id.action_edit:
                Log.v(TAG, "edit clicked");

                vName.setEnabled(true);
                vTags.setEnabled(true);
                vText.setEnabled(true);

                MenuItem editButton = appBarMenu.findItem(R.id.action_edit);
                editButton.setVisible(false);

                MenuItem saveButton = appBarMenu.findItem(R.id.action_edit_save);
                saveButton.setVisible(true);

                return true;

            case R.id.action_delete:
                Toast.makeText(getApplicationContext(), "delete clicked", Toast.LENGTH_SHORT).show();

                snippetViewModel.deleteSnippet(snippetViewModel.getSnippetForId(snippetId));
                snippetViewModel.deleteStrayTags();
                MainActivity.instance.updateAdapter();
                finish();
                return true;

            case R.id.action_edit_save:
                Log.v(TAG, "save clicked");
                // save snippet and finish
                updateSnippet();

                MainActivity.instance.updateAdapter();
                finish();
                return true;
            default:
                Log.wtf(TAG, "you clicked a nonexistent option");
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateSnippet() {
        SnippetViewModel snippetViewModel = ViewModelProviders.of(this).get(SnippetViewModel.class);
        // look for changes and save them if present

        // save new name
        if (!vName.getText().toString().trim().equalsIgnoreCase(oldName.trim())) {
            Log.d(TAG, "name has changed");
            snippetViewModel.updateSnippetName(snippetId, vName.getText().toString().trim());
        }

        // save new text
        if (!vText.getText().toString().trim().equalsIgnoreCase(oldText.trim())) {
            Log.d(TAG, "text has changed");
            snippetViewModel.updateSnippetText(snippetId, vText.getText().toString().trim());
        }

        // save new tags
        if (!vTags.getText().toString().trim().equalsIgnoreCase(oldTags.trim())) {
            // parse tags from text
            Log.d(TAG, "tags have changed");
            List<String> tagsList = Arrays.asList(vTags.getText().toString().split(","));
            for (int i = 0; i < tagsList.size(); i++) {
                tagsList.set(i, tagsList.get(i).trim().toLowerCase());
            }
            List<String> filteredList = tagsList.stream().distinct().collect(Collectors.toList());

            List<Tag> tagsToSort = new LinkedList<>();
            filteredList.forEach(s -> tagsToSort.add(new Tag(s)));

            // delete all tag associations of this snippet
            snippetViewModel.deleteSnippetTagsForSnippetId(snippetId);


            // sort tags into 2 lists: newTags, inDb
            List<Tag> allTags = snippetViewModel.getAllTags();
            Log.v(TAG, "number of stored tags: " + allTags.size());
            Log.v(TAG, "snippet now has " + tagsToSort.size() + " tags");

            List<Tag> newTags = new LinkedList<>();
            List<Tag> existingTags = new LinkedList<>();

            for (int i = 0; i < tagsToSort.size(); i++) {
                Tag t = tagsToSort.get(i);
                if (!allTags.contains(t))
                    newTags.add(t);
                if (allTags.contains(t))
                    existingTags.add(t);
            }

            Log.v(TAG, "existing tags: ");
            if (existingTags.size() != 0) {
                for (Tag t : existingTags) {
                    Log.v(TAG, t.getTag());
                }
            }

            Log.v(TAG, "new tags: ");
            if (newTags.size() != 0) {
                for (Tag t : newTags) {
                    Log.v(TAG, t.getTag());
                }
            }

            // insert any new tags
            if (newTags.size() >= 0)
                newTags.forEach(snippetViewModel::insert);

            // insert all SnippetTags entries
            List<SnippetTags> snippetTags = new LinkedList<>();
            tagsToSort.forEach(tag -> snippetTags.add(new SnippetTags(snippetId, tag)));
            SnippetTags[] insert = new SnippetTags[snippetTags.size()];
            snippetViewModel.insert(snippetTags.toArray(insert));

            // delete any stray tags with no entry in SnippetTags
            snippetViewModel.deleteStrayTags();
        }
    }

    private void updateActionBarFavorite() {
        // set favorite state of the snippet
        Toolbar toolbar = findViewById(R.id.toolbar);
        MenuItem favoriteIcon = toolbar.getMenu().findItem(R.id.action_view_fav);
        if (favorite) {
            favoriteIcon.setIcon(R.drawable.ic_favorite_black_24dp);
        } else {
            favoriteIcon.setIcon(R.drawable.ic_favorite_border_black_24dp);
        }
    }

}
