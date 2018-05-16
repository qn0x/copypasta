package xyz.qn0x.copypasta.keyboard;

import android.view.View;
import android.view.inputmethod.BaseInputConnection;

public class SnippetInputConnection extends BaseInputConnection {
    public SnippetInputConnection(View targetView, boolean fullEditor) {
        super(targetView, fullEditor);
    }
}
