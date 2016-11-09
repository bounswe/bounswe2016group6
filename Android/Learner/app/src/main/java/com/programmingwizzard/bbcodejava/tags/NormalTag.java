package com.programmingwizzard.bbcodejava.tags;

import com.programmingwizzard.bbcodejava.Tag;

public class NormalTag implements Tag
{
    private final String text;

    public NormalTag(String text)
    {
        this.text = text;
    }

    @Override
    public String getStart()
    {
        return "";
    }

    @Override
    public String getText()
    {
        return this.text;
    }

    @Override
    public String getEnd()
    {
        return "";
    }
}
