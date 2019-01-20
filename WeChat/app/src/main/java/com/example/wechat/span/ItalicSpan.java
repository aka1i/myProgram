package com.example.wechat.span;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

public class ItalicSpan extends StyleSpan implements Span {
    public ItalicSpan() {
        super(Typeface.ITALIC);
    }

    @Override
    public Boolean getValue() {
        return Boolean.TRUE;
    }
}
