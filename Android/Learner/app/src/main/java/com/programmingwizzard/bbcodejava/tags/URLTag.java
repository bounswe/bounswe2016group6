package com.programmingwizzard.bbcodejava.tags;

import com.programmingwizzard.bbcodejava.Tag;

public class URLTag implements Tag
{
    private final String url;
    private final String text;

    public URLTag(String url, String text)
    {
        this.url = url;
        this.text = text;
    }

    @Override
    public String getStart()
    {
        return "[url=\"" + this.url + "\"]";
    }

    @Override
    public String getText()
    {
        return this.text;
    }

    @Override
    public String getEnd()
    {
        return "[/url]";
    }
}
