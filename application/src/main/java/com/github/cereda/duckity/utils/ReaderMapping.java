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

/**
 * Mapping model for datasources.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class ReaderMapping {

    private String identifier;
    private String file;
    private String separator;
    private String quotechar;
    private String escape;
    private int line;
    private boolean strictquote;
    private boolean ignoreleadingwhitespace;
    private boolean fl = false;
    private boolean fsq = false;
    private boolean ilws = false;
    private boolean CSV;

    public boolean isCSV() {
        return CSV;
    }

    public void setCSV(boolean CSV) {
        this.CSV = CSV;
    }

    public boolean isFl() {
        return fl;
    }

    public boolean isFsq() {
        return fsq;
    }

    public boolean isIlws() {
        return ilws;
    }

    public ReaderMapping() {
        identifier = null;
        file = null;
        separator = null;
        quotechar = null;
        escape = null;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void setQuotechar(String quotechar) {
        this.quotechar = quotechar;
    }

    public void setEscape(String escape) {
        this.escape = escape;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setStrictquote(boolean strictquote) {
        fsq = true;
        this.strictquote = strictquote;
    }

    public void setIgnoreleadingwhitespace(boolean ignoreleadingwhitespace) {
        ilws = true;
        this.ignoreleadingwhitespace = ignoreleadingwhitespace;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getFile() {
        return file;
    }

    public String getSeparator() {
        return separator;
    }

    public String getQuotechar() {
        return quotechar;
    }

    public String getEscape() {
        return escape;
    }

    public int getLine() {
        fl = true;
        return line;
    }

    public boolean isStrictquote() {
        return strictquote;
    }

    public boolean isIgnoreleadingwhitespace() {
        return ignoreleadingwhitespace;
    }
    
}
