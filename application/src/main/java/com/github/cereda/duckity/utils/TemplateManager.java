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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/**
 * Holds the template manager.
 * 
 * @author Paulo Roberto Massa Cereda
 * @version 1.0
 * @since 1.0
 */
public class TemplateManager {

    private String input;
    private String output;
    private HashMap content;
    private String template;

    /**
     * Constructor.
     * 
     * @param input The input file name.
     * @param content The mappings.
     * @param template The template.
     */
    public TemplateManager(String input, HashMap content, String template) {
        this.input = input;
        this.content = content;
        this.template = template;
        this.output = getBasename(input).concat(".new").concat(getFiletype(input).isEmpty() ? "" : ".".concat(getFiletype(input)));
    }

    /**
     * Sets the output file name.
     * 
     * @param output The output file name.
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * Generates the template.
     * 
     * @throws DuckityException Exception is thrown in case of an error.
     */
    public void generate() throws DuckityException {
        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        StringReader reader = new StringReader(template);
        SimpleNode node = null;
        try {
            node = runtimeServices.parse(reader, "Duckity");
        } catch (ParseException pe) {
            throw new DuckityException("An error occurred while trying to parse the template: " + pe.getMessage());
        }
        Template velocityTemplate = new Template();
        velocityTemplate.setRuntimeServices(runtimeServices);
        velocityTemplate.setData(node);
        velocityTemplate.initDocument();
        VelocityContext context = new VelocityContext();
        for (Object key : content.keySet()) {
            context.put((String) key, content.get(key));
        }
        context.put("math", new MyMathTool());

        try {
            FileWriter fw = new FileWriter(output);
            velocityTemplate.merge(context, fw);
            fw.close();
        } catch (IOException e) {
            throw new DuckityException("An IO error occurred while trying to read from '".concat(output).concat("'."));
        }
    }

    /**
     * Gets the file base name.
     * 
     * @param f The file name.
     * @return The file base name.
     */
    private String getBasename(String f) {
        try {
            f = (new File(f)).getName();
            int i = f.lastIndexOf(".") != -1 ? f.lastIndexOf(".") : f.length();
            return f.substring(0, i);
        } catch (Exception exception) {
            return "";
        }
    }

    /**
     * Gets the file type.
     * 
     * @param f The file name.
     * @return The file type.
     */
    private String getFiletype(String f) {
        try {
            f = (new File(f)).getName();
            if (f.lastIndexOf(".") != -1) {
                return f.substring(f.lastIndexOf(".") + 1, f.length());
            }
            return "";
        } catch (Exception exception) {
            return "";
        }
    }
}
