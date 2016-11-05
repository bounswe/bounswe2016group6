package com.programmingwizzard.bbcodejava.tags;

import com.programmingwizzard.bbcodejava.Tag;

public class ImageTag implements Tag
{
    private final String url;

    public ImageTag(String url)
    {
        this.url = url;
    }

    @Override
    public String getStart()
    {
        return "[img]";
    }

    @Override
    public String getText()
    {
        return this.url;
    }

    @Override
    public String getEnd()
    {
        return "[/img]";
    }
}
