package com.programmingwizzard.bbcodejava.tags;

import com.programmingwizzard.bbcodejava.Tag;

public class UnderlineTag implements Tag
{
    private final String text;

    public UnderlineTag(String text)
    {
        this.text = text;
    }

    @Override
    public String getStart()
    {
        return "[u]";
    }

    @Override
    public String getText()
    {
        return this.text;
    }

    @Override
    public String getEnd()
    {
        return "[/u]";
    }
}
