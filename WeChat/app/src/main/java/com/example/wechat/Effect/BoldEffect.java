package com.example.wechat.Effect;

import com.example.wechat.Utils.RichTextUtils;
import com.example.wechat.span.BoldSpan;
import com.example.wechat.span.Span;

public class BoldEffect extends Effect<Boolean> {
    @Override
    protected Class<? extends Span> getSpanClazz() {
        return BoldSpan.class;
    }

    @Override
    protected Span newSpan(Boolean value) {
        return value ? new BoldSpan() : null;
    }

//    public String getRichText() {
//        return RichTextUtils.convertSpannedToRichText(getText());
//    }
}
