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

import au.com.bytecode.opencsv.CSVReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Loads the datasource file.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class FileLoader {

    private final HashMap content;
    private final List<ReaderMapping> mapping;

    /**
     * Constructor.
     *
     * @param mapping The current mapping.
     */
    public FileLoader(List<ReaderMapping> mapping) {
        this.mapping = mapping;
        content = new HashMap();
    }

    /**
     * Loads all datasources.
     *
     * @throws DuckityException Exception is thrown if datasource is not found
     * or if extension is invalid.
     */
    public void load() throws DuckityException {
        List<String> identifiers = new ArrayList<String>();
        for (ReaderMapping rm : mapping) {
            File file = new File(rm.getFile());
            if (file.exists()) {

                if (!identifiers.contains(rm.getIdentifier())) {
                    identifiers.add(rm.getIdentifier());
                } else {
                    throw new DuckityException("There are duplicated "
                            + "identifiers [".concat(rm.
                                    getIdentifier()).concat("]."));
                }

                if (rm.isCSV()) {

                    try {
                        CSVReader reader;
                        if (rm.getSeparator() == null) {
                            reader = new CSVReader(new FileReader(file));
                        } else {
                            if (rm.getQuotechar() == null) {
                                reader = new CSVReader(new FileReader(file),
                                        rm.getSeparator().charAt(0));
                            } else {
                                if (rm.getEscape() == null) {

                                    reader = new CSVReader(new FileReader(file),
                                            rm.getSeparator().charAt(0),
                                            rm.getQuotechar().charAt(0));
                                } else {

                                    if (!rm.isFl()) {
                                        reader = new CSVReader(
                                                new FileReader(file),
                                                rm.getSeparator().charAt(0),
                                                rm.getQuotechar().charAt(0),
                                                rm.getEscape().charAt(0));
                                    } else {
                                        if (!rm.isFsq()) {
                                            reader = new CSVReader(
                                                    new FileReader(file),
                                                    rm.getSeparator().charAt(0),
                                                    rm.getQuotechar().charAt(0),
                                                    rm.getEscape().charAt(0),
                                                    rm.getLine());
                                        } else {
                                            if (!rm.isIlws()) {
                                                reader = new CSVReader(
                                                        new FileReader(file),
                                                        rm.getSeparator().charAt(0),
                                                        rm.getQuotechar().charAt(0),
                                                        rm.getEscape().charAt(0),
                                                        rm.getLine(),
                                                        rm.isStrictquote());
                                            } else {
                                                reader = new CSVReader(
                                                        new FileReader(file),
                                                        rm.getSeparator().charAt(0),
                                                        rm.getQuotechar().charAt(0),
                                                        rm.getEscape().charAt(0),
                                                        rm.getLine(),
                                                        rm.isStrictquote(),
                                                        rm.isIgnoreleadingwhitespace());
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        List<String[]> lines = reader.readAll();
                        reader.close();
                        content.put(rm.getIdentifier(), lines);
                    } catch (IOException nothandled) {
                        throw new DuckityException("An IO error occurred "
                                + "while trying to read from '".concat(
                                        file.getName()).concat("'."));
                    }
                } else {
                    JSONParser parser = new JSONParser();
                    ContainerFactory factory = new ContainerFactory() {
                        public List creatArrayContainer() {
                            return new LinkedList();
                        }

                        public Map createObjectContainer() {
                            return new LinkedHashMap();
                        }
                    };

                    try {
                        FileReader freader = new FileReader(rm.getFile());
                        Map jheader = (Map) parser.parse(freader, factory);
                        content.put(rm.getIdentifier(), jheader);
                        freader.close();
                    } catch (IOException nothandled) {
                        throw new DuckityException("An IO error occurred while "
                                + "trying to read from '".concat(
                                        file.getName()).concat("'."));
                    } catch (ParseException nothandled) {
                        throw new DuckityException("An error occurred while "
                                + "trying to parse '".concat(
                                        file.getName()).concat("'."));
                    }
                }
            } else {
                throw new DuckityException("File '".concat(file.getName()).
                        concat("' does not exist."));
            }
        }
    }

    /**
     * Get the content.
     *
     * @return The datasources content.
     */
    public HashMap getContent() {
        return content;
    }
}
