package com.programmingwizzard.bbcodejava;

import com.programmingwizzard.bbcodejava.exceptions.BBCodeException;
import com.programmingwizzard.bbcodejava.tags.*;

import java.util.ArrayList;
import java.util.List;

public class BBCode
{
    private final List<Tag> tags;

    public BBCode()
    {
        this.tags = new ArrayList<>();
    }

    public BBCode addText(String text) throws BBCodeException
    {
        if (text == null)
        {
            throw new BBCodeException("text == NULL");
        }
        Tag tag = new NormalTag(text);
        this.addTag(tag);
        return this;
    }

    public BBCode addBold(String text) throws BBCodeException
    {
        if (text == null)
        {
            throw new BBCodeException("text == NULL");
        }
        Tag tag = new BoldTag(text);
        this.addTag(tag);
        return this;
    }

    public BBCode addItalic(String text) throws BBCodeException
    {
        if (text == null)
        {
            throw new BBCodeException("text == NULL");
        }
        Tag tag = new ItalicTag(text);
        this.addTag(tag);
        return this;
    }

    public BBCode addURL(String url, String text) throws BBCodeException
    {
        if (url == null || text == null)
        {
            throw new BBCodeException("url || text == NULL");
        }
        Tag tag = new URLTag(url, text);
        this.addTag(tag);
        return this;
    }

    public BBCode addUnderline(String text) throws BBCodeException
    {
        if (text == null)
        {
            throw new BBCodeException("text == NULL");
        }
        Tag tag = new UnderlineTag(text);
        this.addTag(tag);
        return this;
    }

    public BBCode addImage(String url) throws BBCodeException
    {
        if (url == null)
        {
            throw new BBCodeException("url == NULL");
        }
        Tag tag = new ImageTag(url);
        this.addTag(tag);
        return this;
    }

    public BBCode addColor(String hex, String text) throws BBCodeException
    {
        if (hex == null || text == null)
        {
            throw new BBCodeException("hex || text == NULL");
        }
        Tag tag = new ColorTag(hex, text);
        this.addTag(tag);
        return this;
    }

    public BBCode addColor(Color color, String text) throws BBCodeException
    {
        if (color == null || text == null)
        {
            throw new BBCodeException("color || text == NULL");
        }
        Tag tag = new ColorTag(color, text);
        this.addTag(tag);
        return this;
    }

    public BBCode addTag(Tag tag) throws BBCodeException
    {
        if (tag == null)
        {
            throw new BBCodeException("tag == NULL");
        }
        this.tags.add(tag);
        return this;
    }

    @Override
    public String toString()
    {
        String result = "";
        for (Tag tag : this.tags)
        {
            if (tag.getStart() == null || tag.getText() == null || tag.getEnd() == null)
            {
                continue;
            }
            result = result + tag.getStart() + tag.getText() + tag.getEnd();
        }
        return result;
    }
}
