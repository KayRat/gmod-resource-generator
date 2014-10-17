package io.github.kayrat.gmodresourcegenerator;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.NotDirectoryException;

public final class Driver {
    public static void main(final String[] args) {
        Options objOptions = new Options();
        objOptions.addOption(
                OptionBuilder.withArgName("directory")
                        .hasArg()
                        .isRequired()
                        .withDescription("the input directory to start looking for files in")
                        .create("input")
        );
        objOptions.addOption(
                OptionBuilder.withArgName("filename")
                        .hasArg()
                        .withDescription("the output filename for the resources file" +
                        "default: sv_resources.lua")
                        .create("output")
        );
        objOptions.addOption("stats", false, "show statistics about the generated resources");
        objOptions.addOption("help", false, "show this help");

        CommandLineParser objParser = new GnuParser();

        try {
            CommandLine objLine = objParser.parse(objOptions, args);

            Driver.parse(objLine);
        }
        catch(ParseException e) {
            HelpFormatter objHelp = new HelpFormatter();
            objHelp.printHelp("gmodresourcegen", objOptions);
            Driver.error("Parsing failed: %s", e.getMessage());
        }
    }

    private static void parse(CommandLine objLine) {
        final String strInputDirName = objLine.getOptionValue("input");
        final String strOutputFilename = objLine.hasOption("output") ? objLine.getOptionValue("output") : "sv_resources.lua";
        final boolean bStats = objLine.hasOption("stats");
        final File objInputDir = new File(strInputDirName);

        try {
            final DirectoryParser objParser = new DirectoryParser(objInputDir) {
                /*public void onFinished(DirectoryStats objStats) {
                    System.out.println("Directory parser finished!");
                }*/

                public void onFinished() {}

                public FileVisitResult onError(IOException e) {
                    return FileVisitResult.CONTINUE;
                }
            };
            objParser.setCollectingStats(bStats);
            objParser.start();
        }
        catch(FileNotFoundException e) {
            Driver.error("Specified input directory not found: %s", objInputDir.getPath());
        }
        catch(NotDirectoryException e) {
            Driver.error("Specified input directory is not a directory: %s", objInputDir.getPath());
        }
        catch(IOException e) {
            Driver.error("Unable to create DirectoryParser: %s", e.getMessage());
        }
    }

    private static void error(String strError) {
        System.err.println("[error] " + strError);
    }

    private static void error(String strError, Object... args) {
        Driver.error(String.format(strError, args));
    }
}
