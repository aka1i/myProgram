package com.example.wechat.Utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.util.Log;

import com.example.wechat.span.BoldSpan;
import com.example.wechat.span.ItalicSpan;
import com.example.wechat.span.StrikethroughSpan;
import com.example.wechat.span.UnderlineSpan;

import java.util.Arrays;
import java.util.List;

public class RichTextUtils {
    public static Spanned convertRichTextToSpanned(String richText) {
        return RichTextConvertor.fromRichText(richText);
    }
    public static String convertSpannedToRichText(Spanned spanned) {
        List<CharacterStyle> spanList =
                Arrays.asList(spanned.getSpans(0, spanned.length(), CharacterStyle.class));
        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(spanned);
        for (CharacterStyle characterStyle : spanList) {
            int start = stringBuilder.getSpanStart(characterStyle);
            int end = stringBuilder.getSpanEnd(characterStyle);
            if (start >= 0) {
                String htmlStyle = handleCharacterStyle(characterStyle,
                        stringBuilder.subSequence(start, end).toString());
                if (htmlStyle != null) {
                    stringBuilder.replace(start, end, htmlStyle);
                }
            }
        }
        return stringBuilder.toString();
    }

    private static String handleCharacterStyle(CharacterStyle characterStyle, String text) {
        if (characterStyle instanceof BoldSpan) {
            return String.format("<b>%s</b>", text);
        } else if (characterStyle instanceof ItalicSpan)
            return String.format("<i>%s</i>", text);
        else if (characterStyle instanceof StrikethroughSpan)
            return String.format("<s>%s</s>", text);
        else if (characterStyle instanceof UnderlineSpan)
            return String.format("<u>%s</u>", text);
        return null;
    }

}
