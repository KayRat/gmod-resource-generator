package io.github.bananalord.gmodresourcegenerator;

import org.apache.commons.cli.*;

import java.io.File;

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
                OptionBuilder.withArgName("filename:sv_resources.lua")
                        .hasArg()
                        .withDescription("the output filename for the resources file")
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
        final File objInputDir = new File(strInputDirName);

        ResourceFileBuilder objBuilder = new ResourceFileBuilder(objInputDir, strOutputFilename) {
            @Override
            public void onFinished() {
                System.out.println("All done!");
            }
        };
        objBuilder.start();
    }

    public static void error(String strError) {
        System.err.println("[error] " + strError);
    }

    public static void error(String strError, Object... args) {
        Driver.error(String.format(strError, args));
    }
}
