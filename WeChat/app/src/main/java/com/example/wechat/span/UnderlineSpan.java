package com.example.wechat.span;

import android.text.style.StyleSpan;

public class UnderlineSpan extends android.text.style.UnderlineSpan implements Span {
    @Override
    public Boolean getValue() {
        return Boolean.TRUE;
    }
}
