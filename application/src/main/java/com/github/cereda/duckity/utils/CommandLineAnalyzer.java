/**
 * Duckity -- The template helper
 * Copyright (c) 2012, Paulo Roberto Massa Cereda
 * All rights reserved.
 *
 * Redistribution and  use in source  and binary forms, with  or without
 * modification, are  permitted provided  that the  following conditions
 * are met:
 *
 * 1. Redistributions  of source  code must  retain the  above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form  must reproduce the above copyright
 * notice, this list  of conditions and the following  disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither  the name  of the  project's author nor  the names  of its
 * contributors may be used to  endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS  PROVIDED BY THE COPYRIGHT  HOLDERS AND CONTRIBUTORS
 * "AS IS"  AND ANY  EXPRESS OR IMPLIED  WARRANTIES, INCLUDING,  BUT NOT
 * LIMITED  TO, THE  IMPLIED WARRANTIES  OF MERCHANTABILITY  AND FITNESS
 * FOR  A PARTICULAR  PURPOSE  ARE  DISCLAIMED. IN  NO  EVENT SHALL  THE
 * COPYRIGHT HOLDER OR CONTRIBUTORS BE  LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY,  OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT  NOT LIMITED  TO, PROCUREMENT  OF SUBSTITUTE  GOODS OR  SERVICES;
 * LOSS  OF USE,  DATA, OR  PROFITS; OR  BUSINESS INTERRUPTION)  HOWEVER
 * CAUSED AND  ON ANY THEORY  OF LIABILITY, WHETHER IN  CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
 * WAY  OUT  OF  THE USE  OF  THIS  SOFTWARE,  EVEN  IF ADVISED  OF  THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.github.cereda.duckity.utils;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * Analyzes the command line arguments.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class CommandLineAnalyzer {

    private final String[] arguments;
    private final Options options;
    private final List<ReaderMapping> mappings;
    private String output;
    private String input;

    /**
     * Constructor.
     *
     * @param arguments The command line arguments.
     */
    public CommandLineAnalyzer(String[] arguments) {
        this.arguments = arguments;
        mappings = new ArrayList<ReaderMapping>();
        output = null;
        options = new Options();
    }

    public boolean parse() throws DuckityException {
        options.addOption(new Option("v", "version",
                false, "print the application version"));
        options.addOption(new Option("h", "help",
                false, "print the help message"));
        options.addOption(new Option("d", "datasource",
                true, "set the datasource"));
        options.addOption(new Option("i", "identifier",
                true, "set the identifier"));
        options.addOption(new Option("o", "output",
                true, "set the output file"));

        CommandLineParser parser = new BasicParser();

        try {
            CommandLine line = parser.parse(options, arguments);

            if (line.hasOption("help")) {

                printVersion();
                printUsage();
                return false;

            } else {

                if (line.hasOption("version")) {

                    printVersion();
                    return false;

                } else {

                    String[] files = line.getArgs();

                    if (files.length != 1) {

                        printVersion();
                        printUsage();
                        return false;

                    } else {

                        if (line.hasOption("datasource")) {

                            if (line.hasOption("identifier")) {
                                setMapping(line.getOptionValues("datasource"),
                                        line.getOptionValues("identifier"));
                            } else {
                                throw new DuckityException("the '--datasource' "
                                        + "option only works with the "
                                        + "'--identifier' option. Please set "
                                        + "both.");
                            }
                        } else {
                            if (line.hasOption("identifier")) {
                                throw new DuckityException("the "
                                        + "'--identifier' option only works "
                                        + "with the '--datasource' option. "
                                        + "Please set both.");
                            }
                        }

                        if (line.hasOption("output")) {
                            output = line.getOptionValue("output");
                        }

                        input = files[0];
                        return true;
                    }

                }

            }
        } catch (ParseException nothandled) {
            printVersion();
            printUsage();
            return false;
        }
    }

    /**
     * Prints the application version.
     */
    private void printVersion() {
        System.out.println("duckity ".concat(DuckityConstants.VERSION).
                concat(" - ").concat("The template helper"));
        System.out.println("Copyright (c) ".
                concat(DuckityConstants.COPYRIGHTYEAR).
                concat(", Paulo Roberto Massa Cereda"));
        System.out.println(("All rights reserved.").concat("\n"));
    }

    /**
     * Prints the usage.
     */
    private void printUsage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("duckity [file [--datasource D --identifier I]* "
                + "--output file | --help | --version]", options);
    }

    /**
     * Sets the mapping.
     *
     * @param datasources An array of datasources.
     * @param identifiers An array of identifiers.
     * @throws DuckityException Exception is throw in case of wrong number of
     * arguments or invalid extensions.
     */
    private void setMapping(String[] datasources,
            String[] identifiers) throws DuckityException {

        if (datasources.length != identifiers.length) {
            throw new DuckityException("Both '--datasource' and '--identifier' "
                    + "options work in pairs. You need to declare the same "
                    + "number of values.");
        }

        for (int i = 0; i < datasources.length; i++) {
            ReaderMapping reader = new ReaderMapping();
            reader.setFile(datasources[i]);
            reader.setIdentifier(identifiers[i]);
            if ((datasources[i]).toLowerCase().endsWith(".json")) {
                reader.setCSV(false);
            } else {
                if ((datasources[i]).toLowerCase().endsWith(".csv")) {
                    reader.setCSV(true);
                } else {
                    throw new DuckityException("Only files with '.csv' or "
                            + "'.json' extensions are supported.");
                }
            }
            mappings.add(reader);
        }
    }

    /**
     * Gets the mappings.
     *
     * @return A list of mappings.
     */
    public List<ReaderMapping> getMappings() {
        return mappings;
    }

    /**
     * Gets the output file name, if any.
     *
     * @return The output file name.
     */
    public String getOutput() {
        return output;
    }

    /**
     * Gets the input file name.
     *
     * @return The input file name.
     */
    public String getInput() {
        return input;
    }

}
