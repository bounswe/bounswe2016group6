package com.programmingwizzard.bbcodejava;

import java.util.ArrayList;
import java.util.List;

public class BBCodeParser
{
    private final List<Tag> tags;

    public BBCodeParser()
    {
        this.tags = new ArrayList<>();
    }

    public List<Tag> parse(String content)
    {
        // TODO
        return this.tags;
    }
}
