package xyz.qn0x.copypasta.keyboard;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import xyz.qn0x.copypasta.R;


public class SnippetInputService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private final static String TAG = "SnippetInputService";

    @Override
    public View onCreateInputView() {

        KeyboardView keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        Keyboard keyboard = new Keyboard(this, R.xml.snippet_pad);



        // TODO hier Snippets aus der Datenbank ziehen und bei Key als android:keyOutputText setzen
        // TODO den Namen als keyLabel nehmen

        // Dummie data
        String snippetArray[] = {"Snippet 1", "Irgendetwas anderes", "Ja geil, es geht", "Oder auch nicht. Hm."};
        String nameArray[] = {"Name 1", "ID 2", "Short 3", "Huch_04"};


        int i = 0;
        for (String name : nameArray) {

            Keyboard.Row row = new Keyboard.Row(keyboard);
            Keyboard.Key key = new Keyboard.Key(row);


            key.text = snippetArray[i];
            key.label = name;
            i++;
        }


        // Schaltet die Key Preview aus
        keyboardView.setPreviewEnabled(false);

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


