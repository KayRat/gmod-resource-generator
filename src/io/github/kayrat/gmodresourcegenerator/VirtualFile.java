package io.github.kayrat.gmodresourcegenerator;

import java.io.File;
import java.io.IOException;

public class VirtualFile {
    private final File file;

    public VirtualFile(File objFile) {
        this.file = objFile;
    }

    public File getFile() {
        return this.file;
    }

    public String getFullRelativePath(File objRelativeTo) {
        String strOutput = null;

        try {
            strOutput = this.file.getCanonicalPath().substring(objRelativeTo.getCanonicalPath().length() + 1);
        }
        catch(IOException e) {
            e.printStackTrace();
        }

        return strOutput;
    }

    public String getCanonicalPath() throws IOException {
        return this.file.getCanonicalPath();
    }

    public String getName() {
        return this.file.getName();
    }
}
