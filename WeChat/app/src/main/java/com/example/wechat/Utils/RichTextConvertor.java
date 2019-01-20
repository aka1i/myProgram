package com.example.wechat.Utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;


import com.example.wechat.span.BoldSpan;
import com.example.wechat.span.ItalicSpan;
import com.example.wechat.span.StrikethroughSpan;
import com.example.wechat.span.UnderlineSpan;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;

import java.io.IOException;
import java.io.StringReader;

public class RichTextConvertor implements ContentHandler {
    private SpannableStringBuilder mResult;
    private String mSource;
    private Parser mParser;
    private static class TemporarySpan {
        Object mSpan;

        TemporarySpan(Object span) {
            mSpan = span;
        }

        void swapIn(SpannableStringBuilder builder) {
            int start = builder.getSpanStart(this);
            int end = builder.getSpanEnd(this);
            builder.removeSpan(this);
            if (start >= 0 && end > start && end <= builder.length()) {
                builder.setSpan(mSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }
    private static class HtmlParser {
        private static final HTMLSchema SCHEMA = new HTMLSchema();
    }


    public static Spanned fromRichText(String richText) {
        return new RichTextConvertor().convert(richText);
    }

    public Spanned convert(String richText) {
        mSource = richText;
        mParser = new Parser();
        try {
            mParser.setProperty(Parser.schemaProperty, HtmlParser.SCHEMA);
        } catch (SAXNotRecognizedException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        } catch (SAXNotSupportedException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }

        mResult = new SpannableStringBuilder();

        mParser.setContentHandler(this);
        try {
            mParser.parse(new InputSource(new StringReader(mSource)));
        } catch (IOException e) {
            // We are reading from a string. There should not be IO problems.
            throw new RuntimeException(e);
        } catch (SAXException e) {
            // TagSoup doesn't throw parse exceptions.
            throw new RuntimeException(e);
        }
        // replace all TemporarySpans by the "real" spans
        for (TemporarySpan span : mResult.getSpans(0, mResult.length(), TemporarySpan.class)) {

            span.swapIn(mResult);
        }

        return mResult;
    }


    @Override
    public void setDocumentLocator(Locator locator) {

    }

    @Override
    public void startDocument() throws SAXException {

    }

    @Override
    public void endDocument() throws SAXException {

    }

    @Override
    public void startPrefixMapping(String prefix, String uri) throws SAXException {

    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        handleStartTag(localName,atts);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        handleEndTag(localName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c = ch[i + start];
            sb.append(c);
        }

        mResult.append(sb);
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

    }

    @Override
    public void processingInstruction(String target, String data) throws SAXException {

    }

    @Override
    public void skippedEntity(String name) throws SAXException {

    }

    // ****************************************** Handle Tags *******************************************

    private void handleStartTag(String tag, Attributes attributes) {
        if (tag.equalsIgnoreCase("b") || tag.equalsIgnoreCase("strong")) {
            start(new Bold());
        }else if(tag.equalsIgnoreCase("i"))
            start(new Italic());
        else if (tag.equalsIgnoreCase("u"))
            start(new Underline());
        else if (tag.equalsIgnoreCase("s"))
            start(new Strikethough());
    }

    private void handleEndTag(String tag) {
        if (tag.equalsIgnoreCase("b")|| tag.equalsIgnoreCase("strong")) {
            end(Bold.class, new BoldSpan());
        }else if(tag.equalsIgnoreCase("i"))
            end(Italic.class,new ItalicSpan());
        else if (tag.equalsIgnoreCase("u"))
            end(Underline.class,new UnderlineSpan());
        else if (tag.equalsIgnoreCase("s"))
            end(Strikethough.class,new StrikethroughSpan());
    }

    private void start(Object mark) {
        int len = mResult.length();
        mResult.setSpan(mark, len, len, Spanned.SPAN_MARK_MARK);
    }



    private void end(Class<? extends Object> kind, Object repl) {
        int len = mResult.length();
        Object obj = getLast(kind);
        int where = mResult.getSpanStart(obj);
        mResult.removeSpan(obj);

        if (where != len) {
            // Note: use SPAN_EXCLUSIVE_EXCLUSIVE, the TemporarySpan will be replaced by a SPAN_EXCLUSIVE_INCLUSIVE span
            mResult.setSpan(new TemporarySpan(repl), where, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private Object getLast(Class<? extends Object> kind) {
        Object[] objs = mResult.getSpans(0, mResult.length(), kind);
        return objs.length == 0 ? null : objs[objs.length - 1];
    }


    private static class Bold {

    }
    private static class Italic{

    }
    private static class Strikethough{

    }
    private static class Underline{

    }
}
