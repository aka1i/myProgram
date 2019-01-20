package com.example.wechat.Effect;

import java.util.ArrayList;

public class Effects {
    // character effects
    public static final Effect BOLD = new BoldEffect();                           // boolean effect
    public static final Effect ITALIC = new ItalicEffect();
    public static final Effect STRIKETHROUGH = new StrikethroughEffect();
    public static final Effect UNDERLINE = new UnderlineEffect();
    /*
     * ALL_EFFECTS is a list of all defined effects, for simpler iteration over all effects.
     */
    public static final ArrayList<Effect> ALL_EFFECTS = new ArrayList<Effect>();

    static {
        ALL_EFFECTS.add(BOLD);
        ALL_EFFECTS.add(ITALIC);
        ALL_EFFECTS.add(STRIKETHROUGH);
        ALL_EFFECTS.add(UNDERLINE);
    }
}
