package com.programmingwizzard.bbcodejava.tags;

import com.programmingwizzard.bbcodejava.Tag;

public class ItalicTag implements Tag
{
    private final String text;

    public ItalicTag(String text)
    {
        this.text = text;
    }

    @Override
    public String getStart()
    {
        return "[i]";
    }

    @Override
    public String getText()
    {
        return this.text;
    }

    @Override
    public String getEnd()
    {
        return "[/i]";
    }
}
