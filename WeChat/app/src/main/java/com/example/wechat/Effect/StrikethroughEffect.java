package com.example.wechat.Effect;


import com.example.wechat.span.Span;
import com.example.wechat.span.StrikethroughSpan;

public class StrikethroughEffect extends Effect<Boolean>  {
    @Override
    protected Class<? extends Span> getSpanClazz() {
        return StrikethroughSpan.class;
    }

    @Override
    protected Span<Boolean> newSpan(Boolean value) {
        return value ? new StrikethroughSpan(): null;
    }
}
