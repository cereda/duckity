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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parses the file into header and template.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class FileParser {

    private StringBuilder header;
    private StringBuilder template;
    private boolean rawTemplate;

    /**
     * Constructor.
     */
    public FileParser() {
        header = new StringBuilder();
        template = new StringBuilder();
        rawTemplate = true;
    }

    /**
     * Loads the input file.
     * 
     * @param filename The input file name.
     * @throws DuckityException Exception is thrown if something bad happened
     * while reading the file.
     */
    public void load(String filename) throws DuckityException {
        try {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String currentLine;
            boolean readingOnHeader = true;
            try {
                while ((currentLine = bufferedReader.readLine()) != null) {
                    if (currentLine.equalsIgnoreCase("[TEMPLATE]")) {
                        rawTemplate = false;
                        readingOnHeader = false;
                    } else {
                        if (readingOnHeader) {
                            header.append(currentLine).append("\n");
                        } else {
                            template.append(currentLine).append("\n");
                        }
                    }
                }
                bufferedReader.close();
                fileReader.close();
            } catch (IOException ioException) {
                throw new DuckityException("An IO error occurred while trying to read from '".concat(filename).concat("'."));
            }
        } catch (FileNotFoundException exception) {
            throw new DuckityException("File '".concat(filename).concat("' does not exist."));
        }
    }

    /**
     * Gets the header.
     * 
     * @return The header.
     */
    public String getHeader() {
        return header.toString();
    }

    /**
     * Gets the template.
     * 
     * @return The template.
     */
    public String getTemplate() {
        if ((!header.toString().isEmpty()) && (template.toString().isEmpty())) {
            return header.toString();
        } else {
            return template.toString();
        }
    }

    /**
     * Gets the flag indicating if there are no datasources.
     * 
     * @return The flag indicating if there are no datasources.
     */
    public boolean isRawTemplate() {
        return rawTemplate;
    }
}
