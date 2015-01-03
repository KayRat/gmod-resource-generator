package me.ratti.gmodresourcegenerator.content;

import java.io.File;
import java.io.IOException;

public class CustomFile {
    private final File file;
    private final File root;
    private final String path;

    public CustomFile(File objFile, File objRoot) throws IOException {
        this.file = objFile;
        this.root = objRoot;

        this.path = getFile().getCanonicalPath().substring(getRoot().getCanonicalPath().length() + 1);
    }

    File getFile() {
        return this.file;
    }

    File getRoot() {
        return this.root;
    }

    public boolean isValid() {
        return getFile().exists() && getFile().isFile();
    }

    public String getPath() {
        return this.path;
    }
}
