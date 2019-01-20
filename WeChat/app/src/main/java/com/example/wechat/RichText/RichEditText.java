package com.example.wechat.RichText;

import android.content.Context;
import android.text.Spanned;
import android.util.AttributeSet;
import android.widget.EditText;

import com.example.wechat.Effect.Effects;
import com.example.wechat.Utils.RichTextUtils;

import org.ccil.cowan.tagsoup.Parser;

public class RichEditText extends android.support.v7.widget.AppCompatEditText {
    private Context mContext;
    private RichEditTextListener mListener;
    private Parser mParser;
    public RichEditText(Context context) {
        super(context);
        mContext = context;
    }
    public RichEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public RichEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }
//    public void setRichEditTextListener(RichEditTextListener listener) {
//        mListener = listener;
//    }

//    @Override
//    protected void onSelectionChanged(int selStart, int selEnd) {
//        super.onSelectionChanged(selStart, selEnd);
//        if (mListener != null) {
//            mListener.onSelectionChanged(selStart, selEnd);
//        }
//    }

//    public void setRichText(String richText) {
//        setText(RichTextUtils.convertRichTextToSpanned(richText, mIEmojiFactory));
//    }

//    public String getRichText() {
//        return RichTextUtils.convertSpannedToRichText(getText());
 //   }

    public void applyBoldEffect(int start, int end, boolean value) {
        Effects.BOLD.apply(this, start, end, value);
    }

    public void applyBoldEffect(boolean value) {
        Effects.BOLD.applyToSelection(this, value);
    }

//    public boolean getBoldEffectValue() {
//        return Effects.BOLD.existsInSelection(this, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//    }
    public void applyStrikethroughEffect(int start, int end, boolean value) {
        Effects.STRIKETHROUGH.apply(this, start, end, value);
    }

    public void applyStrikethroughEffect(boolean value) {
        Effects.STRIKETHROUGH.applyToSelection(this, value);
    }
    public void applyItalicEffect(int start, int end, boolean value) {
        Effects.ITALIC.apply(this, start, end, value);
    }

    public void applyItalicEffect(boolean value) {
        Effects.ITALIC.applyToSelection(this, value);
    }
    public void applyUnderlineEffect(int start, int end, boolean value) {
        Effects.UNDERLINE.apply(this, start, end, value);
    }

    public void applyUnderlineEffect(boolean value) {
        Effects.UNDERLINE.applyToSelection(this, value);
    }
}
