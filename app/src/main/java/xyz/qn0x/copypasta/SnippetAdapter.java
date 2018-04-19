package xyz.qn0x.copypasta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by qn0x on 18/04/19.
 */

public class SnippetAdapter extends RecyclerView.Adapter<SnippetAdapter.MyViewHolder> {
    private List<Snippet> snippetList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, text, tags;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            text = view.findViewById(R.id.text);
            tags = view.findViewById(R.id.tags);
        }
    }

    public SnippetAdapter(List<Snippet> snippetList) {
        this.snippetList = snippetList;
    }

    @Override
    public SnippetAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.snippet_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SnippetAdapter.MyViewHolder holder, int position) {
        Snippet snippet = snippetList.get(position);
        holder.name.setText(snippet.getName());
        holder.text.setText(snippet.getText());
        String tags = "";
        for (String s : snippet.getTags()) {
            tags += s;
        }
        holder.tags.setText(tags);
    }

    @Override
    public int getItemCount() {
        return snippetList.size();
    }
}
