package com.programmingwizzard.bbcodejava.tags;

import com.programmingwizzard.bbcodejava.Tag;

public class BoldTag implements Tag
{
    private final String text;

    public BoldTag(String text)
    {
        this.text = text;
    }

    @Override
    public String getStart()
    {
        return "[b]";
    }

    @Override
    public String getText()
    {
        return this.text;
    }

    @Override
    public String getEnd()
    {
        return "[/b]";
    }
}
