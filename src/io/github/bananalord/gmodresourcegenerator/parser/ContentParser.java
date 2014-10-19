package io.github.bananalord.gmodresourcegenerator.parser;

import io.github.bananalord.gmodresourcegenerator.content.CustomContent.TYPE;
import io.github.bananalord.gmodresourcegenerator.content.CustomContent;
import io.github.bananalord.gmodresourcegenerator.content.CustomFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public abstract class ContentParser {
    private final File root;
    private final String rootPath;
    private int numFilesFound;
    private int numContentTypes;

    public ContentParser(File objRoot) throws IOException {
        this.root = objRoot;
        this.rootPath = this.root.getCanonicalPath();
        this.numFilesFound = 0;
        this.numContentTypes = 0;
    }

    public abstract void onFileFound(CustomFile objFile);

    public File getRoot() {
        return this.root;
    }

    public String getRootPath() {
        return this.rootPath;
    }

    public int getNumFilesFound() {
        return this.numFilesFound;
    }

    // TODO: Better method name
    public int getNumContentTypes() {
        return this.numContentTypes;
    }

    public void walkDirectory(CustomContent.TYPE enType) throws IOException {
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

    // TODO: Come up with a better method name and maybe a better way to do this
    public String getRelativeFilenameAndPath(File objFile) throws IOException {
        return objFile.getCanonicalPath().substring(this.rootPath.length() + 1);
    }

    // TODO: actually check to see if there is content we can add so we avoid adding empty headings
    public boolean directoryHasContent(File objDir) {
        for(String strDir : ContentFilter.DIRECTORIES) {
            File objTarget = new File(objDir, strDir);
            System.out.println(objTarget);

            if(objTarget.exists() && objTarget.isDirectory()) return true;
        }

        return false;
    }
}
