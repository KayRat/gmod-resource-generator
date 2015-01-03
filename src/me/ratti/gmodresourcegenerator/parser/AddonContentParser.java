package me.ratti.gmodresourcegenerator.parser;

import me.ratti.gmodresourcegenerator.content.Addon;
import me.ratti.gmodresourcegenerator.content.CustomFile;
import me.ratti.gmodresourcegenerator.content.CustomContent;
import me.ratti.gmodresourcegenerator.content.CustomScript;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class AddonContentParser {
    private final File root;
    private int numAddonsFound = 0;

    public AddonContentParser(File objRoot) {
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
            final CustomScript objAddon = new Addon(getRoot(), objPath.toFile()) {
                @Override
                public void onFileFound(CustomFile objFile) {
                    AddonContentParser.this.onFileFound(objFile, this);
                }

                @Override
                public void onNewContentType(CustomContent.TYPE enType) {
                    AddonContentParser.this.onNewContentType(enType, this);
                }
            };

            if(!objAddon.hasContent()) continue;

            onAddonEntered(objAddon);
            this.numAddonsFound++;

            objAddon.parse();
        }
    }
}
