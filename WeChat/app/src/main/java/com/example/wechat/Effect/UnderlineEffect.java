package com.example.wechat.Effect;

import com.example.wechat.span.Span;
import com.example.wechat.span.UnderlineSpan;

public class UnderlineEffect extends Effect<Boolean> {
    @Override
    protected Class<? extends Span> getSpanClazz() {
        return UnderlineSpan.class;
    }

    @Override
    protected Span<Boolean> newSpan(Boolean value) {
        return value ? new UnderlineSpan() : null;
    }
}
