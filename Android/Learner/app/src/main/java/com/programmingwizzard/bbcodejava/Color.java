package com.programmingwizzard.bbcodejava;

public enum Color
{
    AQUA("00FFFF"),
    BLUE("0000FF"),
    BROWN("A52A2A"),
    GOLD("FFD700"),
    GRAY("808080"),
    GREEN("008000"),
    ORANGE("FFA500"),
    PINK("FFC0CB"),
    PURPLE("800080"),
    RED("FF0000"),
    WHITE("FFFFFF"),
    YELLOW("FFFF00");

    private final String hex;

    Color(String hex)
    {
        this.hex = hex;
    }

    public String getHex()
    {
        return hex;
    }
}
