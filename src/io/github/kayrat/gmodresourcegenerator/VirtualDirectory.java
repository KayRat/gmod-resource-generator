package io.github.kayrat.gmodresourcegenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

public class VirtualDirectory {
    private final DirectoryParser parser;
    private final Path root;
    private final HashMap<String, VirtualDirectory> directories;
    private final HashMap<String, VirtualFile> files;

    public VirtualDirectory(DirectoryParser objParser, Path objPath) {
        this.parser = objParser;
        this.root = objPath;
        this.directories = new HashMap<String, VirtualDirectory>();
        this.files = new HashMap<String, VirtualFile>();
    }

    public Path getRoot() {
        return this.root;
    }

    public ArrayList<VirtualFile> getFiles() {
        ArrayList<VirtualFile> objFiles = new ArrayList<VirtualFile>(this.files.size());

        for (VirtualFile objFile : this.files.values()) {
            objFiles.add(objFile);
        }

        return objFiles;
    }

    public ArrayList<VirtualDirectory> getDirectories() {
        ArrayList<VirtualDirectory> objDirectories = new ArrayList<VirtualDirectory>(this.directories.size());

        for (VirtualDirectory objDir : this.directories.values()) {
            objDirectories.add(objDir);
        }


        return objDirectories;
    }

    private String getRelativeFilePath(VirtualFile objFile) {
        String strOutput = null;

        try {
            strOutput = objFile.getCanonicalPath().substring(this.parser.getRootPathName().length() + 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return strOutput;
    }

    public void add(VirtualDirectory objRoot) throws FileAlreadyExistsException {
        for (VirtualFile objFile : objRoot.getFiles())
            this.add(objFile);

        for(VirtualDirectory objDir: objRoot.getDirectories())
            this.add(objDir);
    }

    public void add(VirtualFile objFile) throws FileAlreadyExistsException {
        String strFilename = objFile.getName();
        String strPathWithName = this.getRelativeFilePath(objFile) + "/" + strFilename;

        if(this.files.containsKey(strPathWithName))
            throw new FileAlreadyExistsException(strPathWithName);

        this.files.put(strPathWithName, objFile);
    }
}
