package io.github.kayrat.gmodresourcegenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public abstract class DirectoryParser extends Thread {
    private final File inputDir;
    private boolean collectingStats;
    private final String[] validBaseDirectories = new String[]{"materials", "models", "sound", "resource"};

    public DirectoryParser(File objInputDir) throws IOException {
        this.inputDir = objInputDir;
        this.collectingStats = false;

        if(this.inputDir.exists()) {
            if(!this.inputDir.isDirectory()) {
                throw new NotDirectoryException(this.inputDir.getCanonicalPath());
            }
        }
        else {
            throw new FileNotFoundException();
        }
    }

    public File getRoot() {
        return this.inputDir;
    }

    public String getRootPathName() {
        return this.getRoot().getPath();
    }

    public void setCollectingStats(boolean bShouldCollect) {
        this.collectingStats = bShouldCollect;
    }

    public boolean isCollectingStats() {
        return this.collectingStats;
    }

    public abstract void onFinished();

    /**
     * Called when the DirectoryParser encounters an error
     * @param e The IOException that caused the error
     * @return How the parser should proceed
     */
    public abstract FileVisitResult onError(IOException e);

    private VirtualDirectory walkDirectory(final Path objPath) throws IOException {
        final File objDir = objPath.toFile();
        final VirtualDirectory objVirtualDir = new VirtualDirectory(this, objPath);

        Files.walkFileTree(objPath, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path objDir, BasicFileAttributes objAttr) {
                if(!objDir.equals(objPath)) {
                    try {
                        DirectoryParser.this.walkDirectory(objDir);
                    }
                    catch(IOException e) {
                        e.printStackTrace();
                    }
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path objPath, BasicFileAttributes objAttr) {
                try {
                    VirtualFile objFile = new VirtualFile(objPath.toFile());
                    objVirtualDir.add(objFile);
                }
                catch(FileAlreadyExistsException e) {
                    e.printStackTrace();
                }

                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path objFile, IOException e) {
                e.printStackTrace();

                return FileVisitResult.CONTINUE;
            }
        });

        return objVirtualDir;
    }

    @Override
    public void run() {
        try {
            Path objPath = Paths.get(this.inputDir.getCanonicalPath());

            VirtualDirectory objFinal = this.walkDirectory(objPath);
            ArrayList<VirtualFile> objFinalFiles = objFinal.getFiles();

            StringBuilder objOutput = new StringBuilder();

            for (VirtualFile objFile : objFinalFiles) {
                objOutput.append("resource.AddFile(\"");
                objOutput.append(objFile.getFullRelativePath(this.inputDir));
                objOutput.append("\")");
                objOutput.append("\r\n");
            }


            System.out.println(String.format("if(not SERVER) then return end\r\n\r\n%s\r\n\r\nTest generation", objOutput));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
