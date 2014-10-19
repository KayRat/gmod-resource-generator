package io.github.bananalord.gmodresourcegenerator.content;

import io.github.bananalord.gmodresourcegenerator.parser.ContentFilter;
import io.github.bananalord.gmodresourcegenerator.parser.ContentParser;

import java.io.File;
import java.io.IOException;

// TODO: Come up with a better, more descriptive class name
public abstract class CustomScript {
    private final File file;
    private final File root;
    private final CustomContent.CATEGORY type;
    private final String name;
    private int numContentExplored;

    CustomScript(File objRoot, File objFile) {
        this.root = objRoot;
        this.file = objFile;

        if(getRoot().getName().equals("addons"))
            this.type = CustomContent.CATEGORY.ADDON;
        else if(objRoot.getName().equals("gamemodes"))
            this.type = CustomContent.CATEGORY.GAMEMODE;
        else
            this.type = CustomContent.CATEGORY.UNKNOWN;

        if(isAddon())
            this.name = getFile().getName();
        else
            this.name = getFile().getParentFile().getName();

        this.numContentExplored = 0;
    }

    public abstract void onFileFound(CustomFile objFile);
    public abstract void onNewContentType(CustomContent.TYPE enType);

    final File getRoot() {
        return this.root;
    }

    final File getFile() {
        return this.file;
    }

    final CustomContent.CATEGORY getCategory() {
        return this.type;
    }

    final boolean isAddon() {
        return getCategory() == CustomContent.CATEGORY.ADDON;
    }

    public final boolean isGamemode() {
        return getCategory() == CustomContent.CATEGORY.GAMEMODE;
    }

    public final String getName() {
        return this.name;
    }

    public final int getNumContentExplored() {
        return this.numContentExplored;
    }

    // TODO: actually check to see if there is content we can add so we avoid adding empty headings
    public final boolean hasContent() {
        for(String strDir : ContentFilter.DIRECTORIES) {
            File objTarget = new File(getFile(), strDir);

            if(objTarget.exists() && objTarget.isDirectory()) return true;
        }

        return false;
    }

    public void parse() {
        try {
            ContentParser objParser = new ContentParser(getFile()) {
                @Override
                public void onFileFound(CustomFile objFile) {
                    CustomScript.this.onFileFound(objFile);
                }

                @Override
                public void onNewContentType(CustomContent.TYPE enType) {
                    CustomScript.this.onNewContentType(enType);
                    CustomScript.this.numContentExplored++;
                }
            };
            objParser.run();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
