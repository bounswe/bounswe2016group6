package com.programmingwizzard.bbcodejava.tags;

import com.programmingwizzard.bbcodejava.Color;
import com.programmingwizzard.bbcodejava.Tag;

public class ColorTag implements Tag
{
    private final String hex;
    private final String text;

    public ColorTag(String hex, String text)
    {
        this.hex = hex;
        this.text = text;
    }

    public ColorTag(Color color, String text)
    {
        this.hex = color.getHex();
        this.text = text;
    }

    @Override
    public String getStart()
    {
        return "[color=\"" + this.hex + "\"]";
    }

    @Override
    public String getText()
    {
        return this.text;
    }

    @Override
    public String getEnd()
    {
        return "[/color]";
    }
}
