package com.example.wechat.span;

import android.graphics.Typeface;
import android.text.style.StyleSpan;

public class BoldSpan extends StyleSpan implements Span {
    public BoldSpan() {
        super(Typeface.BOLD);
    }

    @Override
    public Object getValue() {
        return Boolean.TRUE;
    }
}
