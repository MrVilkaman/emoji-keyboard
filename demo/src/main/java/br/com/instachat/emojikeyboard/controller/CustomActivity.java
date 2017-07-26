package br.com.instachat.emojikeyboard.controller;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import br.com.instachat.emojikeyboard.R;
import br.com.instachat.emojilibrary.controller.CustomPanel;
import br.com.instachat.emojilibrary.model.layout.EmojiEditText;
import br.com.instachat.emojilibrary.model.layout.EmojiKeyboardLayout;

public class CustomActivity extends AppCompatActivity{


	private EmojiEditText editText;
	private EmojiKeyboardLayout keyboard;
	private CustomPanel customPanel;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_activity);

		editText = (EmojiEditText) findViewById(R.id.edit_text);
		keyboard = (EmojiKeyboardLayout) findViewById(R.id.emoji_keyboard_view);
		ImageView viewById = (ImageView) findViewById(R.id.emoji_icon_btn);
		customPanel = new CustomPanel(this, editText, keyboard, viewById);
	}

	@Override
	public void onBackPressed() {
		if (!customPanel.handleBackPressed()) {
			super.onBackPressed();
		}
	}

}
