package xyz.qn0x.copypasta.keyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import java.util.List;

import xyz.qn0x.copypasta.R;
import xyz.qn0x.copypasta.persistence.SnippetDatabase;
import xyz.qn0x.copypasta.persistence.dao.SnippetDao;
import xyz.qn0x.copypasta.persistence.entities.Snippet;


public class SnippetInputService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private final static String TAG = "SnippetInputService";

    SnippetDatabase db;
    SnippetDao snippetDao;
    private List<Snippet> favorites;


    @Override
    public View onCreateInputView() {

        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.xml.keyboard);
        keyboardView.setPreviewEnabled(false);


        // pull favorites from the db
        db = SnippetDatabase.getDatabase(getApplicationContext());
        snippetDao = db.snippetDao();
        favorites = snippetDao.getAllFavorites();
        Log.d(TAG, "loaded " + favorites.size() + " favorites from the database");

        List<Keyboard.Key> keys = keyboard.getKeys();

        // initialize keyboard
        for (int i = 0; i < favorites.size(); i++) {
            keys.get(i).text = favorites.get(i).getText();
            keys.get(i).label = favorites.get(i).getName();
        }

        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);

        return keyboardView;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {


        InputConnection inputConnection = getCurrentInputConnection();

        if (inputConnection != null) {
            switch (primaryCode) {
                case Keyboard.KEYCODE_DELETE:
                    CharSequence selectedText = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }

                    break;
                default:
                    char code = (char) primaryCode;
                    inputConnection.commitText(String.valueOf(code), 1);
            }

        }
    }

    @Override
    public void onText(CharSequence charSequence) {
        Log.d(TAG, "KeyOutputText: " + charSequence.toString());
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            inputConnection.commitText(charSequence.toString(), 1);
        }
    }

    @Override
    public void swipeLeft() {
        //TODO evtl Delete auf den eingefügten Text machen
    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }
}


