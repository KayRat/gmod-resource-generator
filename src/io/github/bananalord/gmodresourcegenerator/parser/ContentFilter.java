package io.github.bananalord.gmodresourcegenerator.parser;

import io.github.bananalord.gmodresourcegenerator.content.CustomContent;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class ContentFilter {
    public static final WildcardFileFilter MAPS = new WildcardFileFilter(new String[]{
            "*.bsp",
            "*.nav",
            "*.ain",
    });
    private static final WildcardFileFilter MATERIALS = new WildcardFileFilter(new String[]{
            //"*.vmt",
            "*.vtf",
            "*.png",
            "*.jpg",
            "*.jpeg",
    });
    private static final WildcardFileFilter MODELS = new WildcardFileFilter(new String[]{
            "*.mdl",
            //"*.vtx",
            //"*.phy",
            //"*.ani",
            //"*.vvd",
    });
    private static final WildcardFileFilter RESOURCE = new WildcardFileFilter(new String[]{
            "*.ttf"
    });
    private static final WildcardFileFilter SOUND = new WildcardFileFilter(new String[]{
            "*.wav",
            "*.mp3",
            "*.ogg",
    });
    public static final String[] DIRECTORIES = new String[] {
            "maps",
            "materials",
            "models",
            "resource",
            "sound",
    };

    public static WildcardFileFilter get(CustomContent.TYPE enType) {
        switch (enType) {
            case MATERIALS:
                return MATERIALS;
            case MODELS:
                return MODELS;
            case RESOURCE:
                return RESOURCE;
            case SOUND:
                return SOUND;
        }

        return null;
    }
}
