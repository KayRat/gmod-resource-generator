package me.ratti.gmodresourcegenerator;

import me.ratti.gmodresourcegenerator.content.CustomFile;
import me.ratti.gmodresourcegenerator.content.CustomScript;
import me.ratti.gmodresourcegenerator.content.CustomContent;
import me.ratti.gmodresourcegenerator.parser.AddonContentParser;
import me.ratti.gmodresourcegenerator.parser.ContentParser;
import me.ratti.gmodresourcegenerator.parser.GamemodeContentParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

public class ResourceFileBuilder extends Thread {
    private final File root;
    private final String outputFilename;

    public ResourceFileBuilder(File objRoot, String strOutputFilename) {
        super("Resource File Builder");

        this.root = objRoot;
        this.outputFilename = strOutputFilename;
    }

    public File getRoot() {
        return this.root;
    }

    public String getOutputFilename() {
        return this.outputFilename;
    }

    private void writeHeader(PrintWriter objOutput) {
        objOutput.println("--");
        objOutput.println("-- " + getOutputFilename());
        objOutput.println("--");
        objOutput.println("-- Created by GMod Resource Generator");
        objOutput.println("-- https://bananalord.github.io/gmod-resource-generator/");
        objOutput.println("--");
        objOutput.println();
        objOutput.println();
    }

    private void writeFooter(PrintWriter objOutput) {
        objOutput.println();
        objOutput.println("-- Thanks for shopping with us!");
    }

    private void writeAddons(final PrintWriter objOutput) {
        objOutput.println("do -- garrysmod/addons");
        try {
            File objAddonsRoot = new File(getRoot(), "addons");

            if(!objAddonsRoot.exists() || !objAddonsRoot.isDirectory()) return;

            AddonContentParser objParser = new AddonContentParser(objAddonsRoot) {
                @Override
                public void onAddonEntered(CustomScript objAddon) {
                    if(getNumExplored() > 0)
                        objOutput.println();

                    objOutput.println("\t-- /" + objAddon.getName());
                }

                @Override
                public void onFileFound(CustomFile objFile, CustomScript objScript) {
                    System.out.println("[addon - " + objScript.getName() + "] Adding: " + objFile.getPath());
                    objOutput.println(String.format("\t\tresource.AddFile(\"%s\")", objFile.getPath()));
                }

                @Override
                public void onNewContentType(CustomContent.TYPE enType, CustomScript objAddon) {
                    if(objAddon.getNumContentExplored() > 0)
                        objOutput.println();

                    objOutput.println("\t\t-- /" + CustomContent.getNameOfType(enType));
                }
            };
            objParser.run();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        objOutput.println("end");
    }

    private void writeGamemodes(final PrintWriter objOutput) {
        objOutput.println();
        objOutput.println("do -- garrysmod/gamemodes");
        try {
            File objGamemodesRoot = new File(getRoot(), "gamemodes");

            if(!objGamemodesRoot.exists() || !objGamemodesRoot.isDirectory()) return;

            GamemodeContentParser objParser = new GamemodeContentParser(objGamemodesRoot) {
                @Override
                public void onAddonEntered(CustomScript objAddon) {
                    if(getNumExplored() > 0)
                        objOutput.println();

                    objOutput.println("\t-- /" + objAddon.getName() + "/content");
                }

                @Override
                public void onFileFound(CustomFile objFile, CustomScript objScript) {
                    System.out.println("[gamemode - " + objScript.getName() + "] Adding: " + objFile.getPath());
                    objOutput.println(String.format("\t\tresource.AddFile(\"%s\")", objFile.getPath()));
                }

                @Override
                public void onNewContentType(CustomContent.TYPE enType, CustomScript objAddon) {
                    if(objAddon.getNumContentExplored() > 0)
                        objOutput.println();

                    objOutput.println("\t\t-- /" + CustomContent.getNameOfType(enType));
                }
            };
            objParser.run();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(IOException e) {
            e.printStackTrace();
        }
        objOutput.println("end");
    }

    private void writeBase(final PrintWriter objOutput) {
        objOutput.println();
        objOutput.println("do -- garrysmod");

        try {
            ContentParser objParser = new ContentParser(getRoot()) {
                @Override
                public void onFileFound(CustomFile objFile) {
                    System.out.println("Adding custom file " + objFile.getPath());
                    objOutput.println(String.format("\tresource.AddFile(\"%s\")", objFile.getPath()));
                }

                @Override
                public void onNewContentType(CustomContent.TYPE enType) {
                    if(getNumContentTypes() > 0)
                        objOutput.println();

                    objOutput.println("\t-- /" + CustomContent.getNameOfType(enType));
                }
            };
            objParser.run();
        } catch(FileNotFoundException e) {
            Driver.error(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }

        objOutput.println("end");
    }

    @Override
    public void run() {
        try {
            // TODO: Use the output file specified by the command line
            PrintWriter objOutput = new PrintWriter(getOutputFilename(), "UTF-8");
            writeHeader(objOutput);

            writeAddons(objOutput); // TODO
            writeGamemodes(objOutput); // TODO
            writeBase(objOutput);

            writeFooter(objOutput);

            objOutput.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

        onFinished();
    }

    public void onFinished() {}
}
