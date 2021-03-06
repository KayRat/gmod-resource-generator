package me.ratti.gmodresourcegenerator.parser;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

class DirectoriesOnlyFilter implements DirectoryStream.Filter<Path> {
    @Override
    public boolean accept(Path objEntry) throws IOException {
        return Files.isDirectory(objEntry);
    }
}
