package io.github.bananalord.gmodresourcegenerator.parser;

import io.github.bananalord.gmodresourcegenerator.content.CustomContent;
import io.github.bananalord.gmodresourcegenerator.content.CustomFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public abstract class ContentParser {
    private final File root;
    private int numFilesFound;
    private int numContentTypes;

    public ContentParser(File objRoot) throws IOException {
        this.root = objRoot;
        String rootPath = this.root.getCanonicalPath();
        this.numFilesFound = 0;
        this.numContentTypes = 0;
    }

    public abstract void onFileFound(CustomFile objFile);

    File getRoot() {
        return this.root;
    }

    // TODO: Better method name
    public int getNumContentTypes() {
        return this.numContentTypes;
    }

    void walkDirectory(CustomContent.TYPE enType) throws IOException {
        File objTargetDir = new File(getRoot(), CustomContent.getNameOfType(enType));

        if(!objTargetDir.exists() || !objTargetDir.isDirectory()) return;

        onNewContentType(enType);
        this.numContentTypes++;

        Collection<File> objFiles = FileUtils.listFiles(
                objTargetDir,
                ContentFilter.get(enType),
                DirectoryFileFilter.INSTANCE
        );

        for(File objFile : objFiles) {
            CustomFile objCustomFile = new CustomFile(objFile, getRoot());

            if(objCustomFile.isValid()) {
                onFileFound(objCustomFile);
            }

            /*if(objFile.isFile()) {
                CustomContent objCustomFile = new CustomContent(enType, objFile, getRoot());
                onFileFound(objCustomFile);

                this.numFilesFound++;
            }*/
        }
    }

    public void run() throws IOException {
        for(CustomContent.TYPE enType : CustomContent.TYPE.values()) {
            walkDirectory(enType);
        }
    }

    public void onNewContentType(CustomContent.TYPE enType) {}

}
