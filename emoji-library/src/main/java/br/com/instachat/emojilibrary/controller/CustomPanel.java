package br.com.instachat.emojilibrary.controller;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.instachat.emojilibrary.R;
import br.com.instachat.emojilibrary.model.layout.EmojiEditText;
import br.com.instachat.emojilibrary.model.layout.EmojiKeyboardLayout;

/**
 * Created by edgar on 18/02/2016.
 */
public class CustomPanel {

	private static final String TAG = "TelegramPanel";
	private final EmojiKeyboardLayout keyboardLayout;
	private final ImageView imageView;
	private AppCompatActivity mActivity;
	private EmojiEditText mInput;
	private EmojiKeyboard mEmojiKeyboard;
	private LinearLayout mCurtain;

	private Boolean isEmojiKeyboardVisible = Boolean.FALSE;
	private EmojiEditText.OnSoftKeyboardListener mOnSoftKeyboardListener;

	// CONSTRUCTOR
	public CustomPanel(AppCompatActivity activity, EmojiEditText emojiEditText,
	                   EmojiKeyboardLayout keyboardLayout, ImageView imageView) {
		this.mActivity = activity;
		mInput = emojiEditText;
		this.keyboardLayout = keyboardLayout;
		this.imageView = imageView;
		this.initBottomPanel();
		this.setInputConfig();
		this.mEmojiKeyboard = new EmojiKeyboard(this.mActivity, this.mInput);
	}

	// INITIALIZATION
	private void initBottomPanel() {
		imageView.setImageResource(R.drawable.input_emoji);
		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (CustomPanel.this.isEmojiKeyboardVisible) {
					CustomPanel.this.closeCurtain();
					if (CustomPanel.this.mInput.isSoftKeyboardVisible()) {
						CustomPanel.this.imageView.setImageResource(R.drawable.ic_keyboard_grey600_24dp);
						CustomPanel.this.mInput.hideSoftKeyboard();
					} else {
						CustomPanel.this.imageView.setImageResource(R.drawable.input_emoji);
						CustomPanel.this.mInput.showSoftKeyboard();
					}
				} else {
					CustomPanel.this.closeCurtain();
					CustomPanel.this.showEmojiKeyboard(0);
				}
			}
		});

		this.mCurtain = (LinearLayout) keyboardLayout.findViewById(R.id.curtain);
	}

	private void setInputConfig() {
//		mInput.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
		this.mInput.addOnSoftKeyboardListener(new EmojiEditText.OnSoftKeyboardListener() {
			@Override
			public void onSoftKeyboardDisplay() {
				if (mOnSoftKeyboardListener != null) {
					mOnSoftKeyboardListener.onSoftKeyboardDisplay();
				}

				if (!CustomPanel.this.isEmojiKeyboardVisible) {
					final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
					scheduler.schedule(new Runnable() {
						@Override
						public void run() {
							Handler mainHandler = new Handler(CustomPanel.this.mActivity.getMainLooper());
							Runnable myRunnable = new Runnable() {
								@Override
								public void run() {
									CustomPanel.this.openCurtain();
									CustomPanel.this.showEmojiKeyboard(0);
								}
							};
							mainHandler.post(myRunnable);
						}
					}, 150, TimeUnit.MILLISECONDS);

				}
			}

			@Override
			public void onSoftKeyboardHidden() {
				if (mOnSoftKeyboardListener != null) {
					mOnSoftKeyboardListener.onSoftKeyboardHidden();
				}
				if (CustomPanel.this.isEmojiKeyboardVisible) {
					CustomPanel.this.closeCurtain();
					CustomPanel.this.hideEmojiKeyboard(200);
				}
			}
		});

	}

	public void addOnSoftKeyboardListener(EmojiEditText.OnSoftKeyboardListener mOnSoftKeyboardListener) {
		this.mOnSoftKeyboardListener = mOnSoftKeyboardListener;
	}

	public boolean handleBackPressed() {
		if (mOnSoftKeyboardListener != null) {
			mOnSoftKeyboardListener.onSoftKeyboardHidden();
		}
		if (CustomPanel.this.isEmojiKeyboardVisible) {
			CustomPanel.this.isEmojiKeyboardVisible = Boolean.FALSE;
			CustomPanel.this.hideEmojiKeyboard(0);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private void showEmojiKeyboard(int delay) {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		CustomPanel.this.imageView.setImageResource(R.drawable.ic_keyboard_grey600_24dp);
		CustomPanel.this.isEmojiKeyboardVisible = Boolean.TRUE;
		CustomPanel.this.mEmojiKeyboard.getEmojiKeyboardLayout()
				.setVisibility(LinearLayout.VISIBLE);
	}

	private void hideEmojiKeyboard(int delay) {
		if (delay > 0) {
			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		CustomPanel.this.imageView.setImageResource(R.drawable.input_emoji);
		CustomPanel.this.isEmojiKeyboardVisible = Boolean.FALSE;
		CustomPanel.this.mEmojiKeyboard.getEmojiKeyboardLayout()
				.setVisibility(LinearLayout.GONE);
	}

	private void openCurtain() {
		this.mCurtain.setVisibility(LinearLayout.VISIBLE);
	}

	private void closeCurtain() {
		this.mCurtain.setVisibility(LinearLayout.INVISIBLE);
	}

	//GETTER AND SETTERS

	public String getText() {
		return this.mInput.getText()
				.toString();
	}

	public void setText(String text) {
		this.mInput.setText(text);
	}

}
