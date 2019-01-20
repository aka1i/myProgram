package com.example.wechat.Effect;

import com.example.wechat.span.ItalicSpan;
import com.example.wechat.span.Span;

public class ItalicEffect extends Effect<Boolean>  {
    @Override
    protected Class<? extends Span> getSpanClazz() {
        return ItalicSpan.class;
    }

    @Override
    protected Span<Boolean> newSpan(Boolean value) {
        return value ? new ItalicSpan() : null;
    }
}
