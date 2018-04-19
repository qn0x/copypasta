package xyz.qn0x.copypasta;

import java.util.Arrays;
import java.util.LinkedList;


public class Snippet {
    private String name = "";
    private String text = "";
    private LinkedList<String> tags = new LinkedList<>();

    public Snippet(String name, String text) {
        this.name = name;
        this.text = text;
    }

    public Snippet(String name, String text, String... tags) {
        this.name = name;
        this.text = text;
        this.tags.addAll(Arrays.asList(tags));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LinkedList<String> getTags() {
        return tags;
    }

    public void setTags(LinkedList<String> tags) {
        this.tags = tags;
    }
}
