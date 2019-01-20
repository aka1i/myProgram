package com.example.wechat.Utils;

import android.widget.EditText;

import java.io.Serializable;

public class Selection implements Serializable {
    private int mStart;
    private int mEnd;

    public Selection(int mStart, int mEnd) {
        this.mStart = mStart;
        this.mEnd = mEnd;

        if (mStart > mEnd) {
            int temp = mEnd;
            mEnd = mStart;
            mStart = temp;
        }
    }

    public Selection(EditText editText){
        this(editText.getSelectionStart(),editText.getSelectionEnd());
    }

    public int start() {
        return mStart;
    }

    public int end() {
        return mEnd;
    }

    public boolean isEmpty() {
        return mStart == mEnd;
    }

    public void offset(int offsetLeft, int offsetRight) {
        mStart = Math.max(0, mStart - offsetLeft);
        mEnd = mEnd + offsetRight;
    }

    public Selection expand(int offsetLeft, int offsetRight) {
        int newStart = Math.max(0, mStart - offsetLeft);
        int newEnd = mEnd + offsetRight;
        return new Selection(newStart, newEnd);
    }

    public void union(Selection other) {
        mStart = Math.min(mStart, other.mStart);
        mEnd = Math.max(mEnd, other.mEnd);
    }
}
