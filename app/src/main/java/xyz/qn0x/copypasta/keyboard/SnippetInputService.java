package xyz.qn0x.copypasta.keyboard;

import android.inputmethodservice.InputMethodService;
import android.view.KeyEvent;
import android.view.View;


public class SnippetInputService extends InputMethodService {
    @Override
    public View onCreateInputView() {
//        SnippetKeyboardView inputView = getLayoutInflater().inflate(R.layout.input, null);
//        inputView.setOnKeyboardActionListener(this);
//        inputView.setKeyboard();
//        return inputView;
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }
}
