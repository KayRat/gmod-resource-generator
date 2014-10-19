package io.github.bananalord.gmodresourcegenerator.content;

public class CustomContent {
    public static enum CATEGORY {
        ADDON,
        GAMEMODE,
        UNKNOWN,
    }
    public static enum TYPE {
        //MAPS,
        MATERIALS,
        MODELS,
        RESOURCE,
        SOUND,
    }

    public static String getNameOfType(CustomContent.TYPE enType) {
        switch (enType) {
            case MATERIALS:
                return "materials";
            case MODELS:
                return "models";
            case RESOURCE:
                return "resource";
            case SOUND:
                return "sound";
        }

        return null;
    }

    public static String getNameOfCategory(CustomContent.CATEGORY enCategory) {
        switch (enCategory) {
            case ADDON:
                return "addon";
            case GAMEMODE:
                return "gamemode";
        }

        return "unknown";
    }
}
