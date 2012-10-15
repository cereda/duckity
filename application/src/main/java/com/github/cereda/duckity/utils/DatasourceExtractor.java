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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.json.simple.parser.ContainerFactory;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Maps the datasource into the model.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class DatasourceExtractor {

    private List<ReaderMapping> mapping;

    /**
     * Constructor.
     */
    public DatasourceExtractor() {
        mapping = new ArrayList<ReaderMapping>();
    }

    /**
     * Parses the datasources.
     * 
     * @param header The input header.
     * @throws DuckityException Exception is throw in case of bad arguments
     * ou invalid extensions.
     */
    public void parse(String header) throws DuckityException {
        JSONParser parser = new JSONParser();
        ContainerFactory containerFactory = new ContainerFactory() {
            public List creatArrayContainer() {
                return new LinkedList();
            }

            public Map createObjectContainer() {
                return new LinkedHashMap();
            }
        };

        Map jsonHeader = null;
        try {
            jsonHeader = (Map) parser.parse(header, containerFactory);
        } catch (ParseException pe) {
            throw new DuckityException("An error occurred while trying to parse the JSON header.");
        }

        ReaderMapping rm;

        if ((jsonHeader.size() == 1) && (jsonHeader.containsKey("datasources"))) {
            Object values = jsonHeader.get("datasources");
            if (values instanceof LinkedList) {
                for (Object value : (LinkedList) values) {
                    if (value instanceof Map) {
                        Map map = (Map) value;
                        Set keys = (Set) map.keySet();
                        Set validKeys = new HashSet();
                        validKeys.add("identifier");
                        validKeys.add("file");
                        validKeys.add("separator");
                        validKeys.add("quotechar");
                        validKeys.add("escape");
                        validKeys.add("line");
                        validKeys.add("strictquote");
                        validKeys.add("ignoreleadingwhitespace");
                        if (CollectionUtils.subtract(keys, validKeys).isEmpty()) {
                            if ((keys.contains("identifier") && (keys.contains("file")))) {
                                rm = new ReaderMapping();
                                rm.setCSV(true);
                                if (map.get("identifier") instanceof String) {
                                    rm.setIdentifier((String) map.get("identifier"));
                                    if (map.get("file") instanceof String) {
                                        rm.setFile((String) map.get("file"));

                                        if (((String) map.get("file")).toLowerCase().endsWith(".csv")) {
                                            rm.setCSV(true);

                                            if (map.get("separator") != null) {
                                                if (map.get("separator") instanceof String) {
                                                    rm.setSeparator((String) map.get("separator"));
                                                } else {
                                                    throw new DuckityException("The 'separator' argument has to be a string.");
                                                }
                                            }
                                            if (map.get("quotechar") != null) {
                                                if (map.get("quotechar") instanceof String) {
                                                    rm.setQuotechar((String) map.get("quotechar"));
                                                } else {
                                                    throw new DuckityException("The 'quotechar' argument has to be a string.");
                                                }
                                            }
                                            if (map.get("escape") != null) {
                                                if (map.get("escape") instanceof String) {
                                                    rm.setEscape((String) map.get("escape"));
                                                } else {
                                                    throw new DuckityException("The 'escape' argument has to be a string.");
                                                }
                                            }
                                            if (map.get("line") != null) {
                                                if (map.get("line") instanceof Integer) {
                                                    rm.setLine((Integer) map.get("line"));
                                                } else {
                                                    throw new DuckityException("The 'line' argument has to be a integer.");
                                                }
                                            }
                                            if (map.get("strictquote") != null) {
                                                if (map.get("strictquote") instanceof Boolean) {
                                                    rm.setStrictquote((Boolean) map.get("strictquote"));
                                                } else {
                                                    throw new DuckityException("The 'strictquote' argument has to be a boolean.");
                                                }
                                            }
                                            if (map.get("ignoreleadingwhitespace") != null) {
                                                if (map.get("ignoreleadingwhitespace") instanceof Boolean) {
                                                    rm.setIgnoreleadingwhitespace((Boolean) map.get("ignoreleadingwhitespace"));
                                                } else {
                                                    throw new DuckityException("The 'ignoreleadingwhitespace' argument has to be a boolean.");
                                                }
                                            }
                                        } else {
                                            if (((String) map.get("file")).toLowerCase().endsWith(".json")) {
                                                rm.setCSV(false);
                                            }
                                            else {
                                                throw new DuckityException("Only files with '.csv' or '.json' extensions are supported.");
                                            }
                                        }

                                        mapping.add(rm);
                                    } else {
                                        throw new DuckityException("The 'file' argument has to be a boolean.");
                                    }
                                } else {
                                    throw new DuckityException("The 'identifier' argument has to be a boolean.");
                                }
                            } else {
                                throw new DuckityException("Both 'file' and 'identifier' arguments are mandatory.");
                            }
                        } else {
                            throw new DuckityException("There are invalid arguments.");
                        }
                    } else {
                        throw new DuckityException("Every element from 'datasources' should be a map.");
                    }
                }
            } else {
                throw new DuckityException("The 'datasources' argument has to be a list.");
            }
        } else {
            throw new DuckityException("There are invalid arguments, expecting 'datasources'.");
        }
    }

    /**
     * Gets the mappings.
     * 
     * @return A list of mappings.
     */
    public List<ReaderMapping> getMapping() {
        return mapping;
    }
}
