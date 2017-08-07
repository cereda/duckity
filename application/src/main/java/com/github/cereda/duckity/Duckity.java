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
package com.github.cereda.duckity;

import com.github.cereda.duckity.utils.CommandLineAnalyzer;
import com.github.cereda.duckity.utils.DatasourceExtractor;
import com.github.cereda.duckity.utils.DuckityException;
import com.github.cereda.duckity.utils.FileLoader;
import com.github.cereda.duckity.utils.FileParser;
import com.github.cereda.duckity.utils.ReaderMapping;
import com.github.cereda.duckity.utils.TemplateManager;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.WordUtils;

/**
 * Main class.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class Duckity {

    /**
     * Main method.
     *
     * @param args The command line arguments.
     */
    public static void main(String[] args) {

        int status = 0;

        try {
            drawLogo();
            CommandLineAnalyzer analyzer = new CommandLineAnalyzer(args);
            if (analyzer.parse()) {

                FileParser parser = new FileParser();
                parser.load(analyzer.getInput());

                List<ReaderMapping> mapping;

                if (!parser.isRaw()) {
                    DatasourceExtractor extractor = new DatasourceExtractor();
                    extractor.parse(parser.getHeader());
                    mapping = extractor.getMapping();
                } else {
                    mapping = new ArrayList<ReaderMapping>();
                }

                mapping.addAll(analyzer.getMappings());

                FileLoader loader = new FileLoader(mapping);
                loader.load();
                TemplateManager manager = new TemplateManager(
                        analyzer.getInput(),
                        loader.getContent(),
                        parser.getTemplate()
                );

                if (analyzer.getOutput() != null) {
                    manager.setOutput(analyzer.getOutput());
                }

                manager.generate();
                System.out.println("Done.");
            }
        } catch (DuckityException duckityException) {
            System.out.println(
                    WordUtils.wrap(
                            duckityException.getMessage(),
                            60,
                            "\n",
                            true
                    )
            );

            status = 1;
        }

        System.exit(status);

    }

    /**
     * Prints the application logo.
     */
    private static void drawLogo() {
        System.out.println("  _|      _ |  o _|_    ");
        System.out.println(" (_| |_| (_ |< |  |_ \\/ ");
        System.out.println("                     /  ");
    }

}
