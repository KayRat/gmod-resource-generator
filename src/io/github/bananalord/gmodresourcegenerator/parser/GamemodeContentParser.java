package io.github.bananalord.gmodresourcegenerator.parser;

import io.github.bananalord.gmodresourcegenerator.content.CustomContent;
import io.github.bananalord.gmodresourcegenerator.content.CustomFile;
import io.github.bananalord.gmodresourcegenerator.content.CustomScript;
import io.github.bananalord.gmodresourcegenerator.content.Gamemode;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class GamemodeContentParser {
    private final File root;
    private int numAddonsFound = 0;

    public GamemodeContentParser(File objRoot) {
        this.root = objRoot;
    }

    File getRoot() {
        return this.root;
    }

    public void onFileFound(CustomFile objFile, CustomScript objAddon) {}

    // TODO: This should do something internally
    public void onNewContentType(CustomContent.TYPE enType, CustomScript objAddon) {}
    public void onAddonEntered(CustomScript objAddon) {}

    public int getNumExplored() {
        return this.numAddonsFound;
    }

    public void run() throws IOException {
        DirectoryStream<Path> objDirStream = Files.newDirectoryStream(getRoot().toPath(), new DirectoriesOnlyFilter());

        for(Path objPath : objDirStream) {
            File objGamemodeRoot = objPath.toFile();
            File objGamemodeContentRoot = new File(objGamemodeRoot, "/content");

            final CustomScript objGamemode = new Gamemode(getRoot(), objGamemodeContentRoot) {
                @Override
                public void onFileFound(CustomFile objFile) {
                    GamemodeContentParser.this.onFileFound(objFile, this);
                }

                @Override
                public void onNewContentType(CustomContent.TYPE enType) {
                    GamemodeContentParser.this.onNewContentType(enType, this);
                }
            };

            if(!objGamemode.hasContent()) continue;

            onAddonEntered(objGamemode);
            this.numAddonsFound++;

            objGamemode.parse();
        }
    }
}
