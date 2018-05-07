package xyz.qn0x.copypasta.ui.utility;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.persistence.entities.Snippet;

/**
 * Created by qn0x on 18/04/19.
 */

public class SnippetAdapter extends RecyclerView.Adapter<SnippetAdapter.SnippetViewHolder> {

    private final LayoutInflater mInflater;

    private List<Snippet> snippetList;

    public class SnippetViewHolder extends RecyclerView.ViewHolder {
        public TextView name, text, tags;

        SnippetViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            text = view.findViewById(R.id.text);
            tags = view.findViewById(R.id.tags);
        }
    }

    public SnippetAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public SnippetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.snippet_list_row, parent, false);
        return new SnippetViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SnippetViewHolder holder, int position) {
        Snippet snippet = snippetList.get(position);
        holder.name.setText(snippet.getName());
        holder.text.setText(snippet.getText());
        //holder.tags.setText(snippet.getTags());
    }

    public void setSnippets(List<Snippet> words){
        snippetList = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return (snippetList == null ? 0 : snippetList.size());
    }
}
