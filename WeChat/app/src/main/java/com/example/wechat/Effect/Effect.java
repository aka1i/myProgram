package com.example.wechat.Effect;


import android.text.Spannable;
import android.text.Spanned;
import android.util.Log;
import android.widget.Toast;

import com.example.wechat.RichText.RichEditText;
import com.example.wechat.Utils.Selection;
import com.example.wechat.span.Span;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public abstract class Effect<V extends Object> {

    abstract protected Class<? extends Span> getSpanClazz();

    abstract protected Span<V> newSpan(V value);

    //返回全部span数组
    final public Span<V>[] getSpans(Spannable str, Selection selection) {
        Class<? extends Span> spanClazz = getSpanClazz();
        Span<V>[] result = str.getSpans(selection.start(), selection.end(), spanClazz);
        return result != null ? result : (Span<V>[]) Array.newInstance(spanClazz);
    }

//    //检查当前effect存在选择的文本
//    final public boolean existsInSelection(RichEditText editor, int spanType) {
//        Selection expandedSelection = getExpandedSelection(editor, spanType);
//        if (expandedSelection != null) {
//            Span<V>[] spans = getSpans(editor.getText(), expandedSelection);
//            return spans.length > 0;
//        }
//
//        return false;
//    }

    /**
     * Returns the value of this effect in the current selection.
     *
     * @return The returned list, must NEVER be null.getExpandedSelection
     */

//    //返回当前selection的effect的value数组
//    public List<V> valuesInSelection(RichEditText editor, int spanType) {
//        List<V> result = new ArrayList<V>();
//
//        Selection expandedSelection = getExpandedSelection(editor, spanType);
//        if (expandedSelection != null) {
//            for (Span<V> span : getSpans(editor.getText(), expandedSelection)) {
//                result.add(span.getValue());
//            }
//        }
//
//        return result;
//    }

    public void apply(RichEditText editor, int start, int end, V value) {
        Selection selection = new Selection(start, end);
        Spannable str = editor.getText();
        Span<V> spans[] = getSpans(str,selection);
        int len = spans.length;
        if (len == 0) {
            Span<V> newSpan = newSpan(value);
            if (newSpan != null) {
                int flags = selection.isEmpty() ? Spanned.SPAN_INCLUSIVE_INCLUSIVE : Spanned.SPAN_EXCLUSIVE_INCLUSIVE;
                str.setSpan(newSpan, selection.start(), selection.end(), flags);
            }
            return;
        }
        int left = str.getSpanStart(spans[0]);
        int right = str.getSpanEnd(spans[spans.length - 1]);
        for (int i = 0; i < spans.length; i++){
            str.removeSpan(spans[i]);
        }
        if(selection.start() > left && selection.end() >= right)
            str.setSpan(newSpan(value), left, selection.start(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        else if (selection.start() <= left && selection.end() < right)
            str.setSpan(newSpan(value), selection.end(), right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        else if(selection.start() > left && selection.end() < right){
            str.setSpan(newSpan(value), left, selection.start(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            str.setSpan(newSpan(value), selection.end(), right, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        else if (selection.start() == left &&  selection.end() == right && len > 1 && right - left > 2||
                selection.start() < left &&  selection.end() > right  || selection.start() <= left &&  selection.end() > right ||
                selection.start() < left &&  selection.end() >= right)
            str.setSpan(newSpan(value), selection.start(), selection.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        /**
         * 合并selection前后的span
         */
     //   Selection expandedSelection = selection.expand(1, 1);
//        for (Span<V> span : getSpans(str, selection)) {
//            Log.d("dasdsadsa","spanStart:" + str.getSpanStart(span) + "spanEnd:" + str.getSpanEnd(span) + "selectionStart:" + selection.start()
//            + "selectionEnd:" + selection.end());
//            if (selection.start() == str.getSpanStart(span) && selection.end() == str.getSpanEnd(span)){
//                str.removeSpan(span);
//                return;
//            }
//            boolean equalSpan = span.getValue() == value;
//            int spanStart = str.getSpanStart(span);
//            if (spanStart < selection.start()) {
//                if (equalSpan) {
//                    selection.offset(selection.start() - spanStart, 0);
//                }
//                else {
//                    str.setSpan(newSpan(span.getValue()), spanStart, selection.start(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
//            }
//            int spanEnd = str.getSpanEnd(span);
//            if (spanEnd > selection.end()) {
//                if (equalSpan) {
//                    selection.offset(0, spanEnd - selection.end());
//                }
//                else {
//                    str.setSpan(newSpan(span.getValue()), selection.end(), spanEnd, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//                }
//            }
            /**
             * 移除合并前的span
             */
//            str.removeSpan(span);
//        }

    }



    public void applyToSelection(RichEditText editor, V value) {
        apply(editor, editor.getSelectionStart(), editor.getSelectionEnd(), value);
    }

    /**
     * Remove all effects of this type from the current selection.
     */

    //移除该类型所有effect
//    final public void clearFormattingInSelection(RichEditText editor) {
//        Spannable text = editor.getText();
//        Selection selection = new Selection(editor);
//        if (selection.isEmpty()) {
//            selection = new Selection(0, text.length());
//        }
//        for (Object span : getSpans(text, selection)) {
//            editor.getText().removeSpan(span);
//        }
//    }

//    //获取扩大的selection
//    protected Selection getExpandedSelection(RichEditText editor, int spanType) {
//        Selection selection = new Selection(editor);
//        int offsetLeft = spanType == Spanned.SPAN_INCLUSIVE_EXCLUSIVE || spanType == Spanned.SPAN_INCLUSIVE_INCLUSIVE ? 1 : 0;
//        int offsetRight = spanType == Spanned.SPAN_EXCLUSIVE_INCLUSIVE || spanType == Spanned.SPAN_INCLUSIVE_INCLUSIVE ? 1 : 0;
//        return selection.expand(offsetLeft, offsetRight);
//    }

    /**
     * Spanned() unfortunately doesn't respects the mark/point flags (SPAN_EXCLUSIVE_EXCLUSIVE etc.).
     * If a selection starts at the end or ends at the start of a span it will be returned by getSpans()
     * regardless whether the span would really be applied to that selection if we were to enter text.
     * E.g. [abc] with SPAN_EXCLUSIVE_EXCLUSIVE would not expand to a character entered at the end: [abc]d,
     * nevertheless getSpans(3, 3, type) would still return the span.
     * <p>
     * This method returns just the spans that will affect the selection if text is entered.
     */
    //返回起作用的span
//    final protected Object[] getCleanSpans(Spannable str, Selection sel) {
//        List<Object> spans = new ArrayList<Object>();
//        if (spans != null) {
//            for (Object span : getSpans(str, sel)) {
//                if (isCleanSpan(str, sel, span)) {
//                    spans.add(span);
//                }
//            }
//        }
//        return spans.toArray();
//    }
//
//    private boolean isCleanSpan(Spannable str, Selection sel, Object span) {
//        int spanStart = str.getSpanStart(span);
//        int spanEnd = str.getSpanEnd(span);
//        int start = Math.max(spanStart, sel.start());
//        int end = Math.min(spanEnd, sel.end());
//
//        if (start < end) {
//            // 1) at least one character in common:
//            // span...
//            // [  [xx]    ]
//            //    selection
//            return true;
//        } else if (start > end) {
//            // 2) no character in common and not adjunctive
//            // [span]...[selection] or [selection]...[span]
//            return false;
//        } else {
//            // 3) adjunctive
//            int flags = str.getSpanFlags(span) & Spanned.SPAN_POINT_MARK_MASK;
//            if (spanEnd == sel.start()) {
//                // [span][selection] -> span must include at the end
//                return ((flags & Spanned.SPAN_EXCLUSIVE_INCLUSIVE) == Spanned.SPAN_EXCLUSIVE_INCLUSIVE) ||
//                        ((flags & Spanned.SPAN_INCLUSIVE_INCLUSIVE) == Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            } else {
//                // [selection][span] -> span must include at the start
//                return ((flags & Spanned.SPAN_INCLUSIVE_EXCLUSIVE) == Spanned.SPAN_INCLUSIVE_EXCLUSIVE) ||
//                        ((flags & Spanned.SPAN_INCLUSIVE_INCLUSIVE) == Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//            }
//
//        }
//
//    }
}
