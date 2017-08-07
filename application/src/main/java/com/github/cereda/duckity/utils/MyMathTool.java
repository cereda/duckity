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

import org.apache.velocity.tools.generic.MathTool;

/**
 * Extends a few methods from MathTool.
 *
 * @author Paulo Roberto Massa Cereda
 * @version 1.1
 * @since 1.0
 */
public class MyMathTool extends MathTool {

    /**
     * Converts the object to float.
     *
     * @param number The object.
     * @return The float value.
     */
    public Float toFloat(Object number) {
        if (number == null) {
            return null;
        }
        return (Float.parseFloat(number.toString()));
    }

    /**
     * Converts the object to double.
     *
     * @param number The object.
     * @return The double value.
     */
    @Override
    public Double toDouble(Object number) {
        if (number == null) {
            return null;
        }
        return (new Float(number.toString()).doubleValue());
    }

    /**
     * Converts the object to integer.
     *
     * @param number The object.
     * @return The integer value.
     */
    @Override
    public Integer toInteger(Object number) {
        if (number == null) {
            return null;
        }
        return (Integer.parseInt(number.toString()));
    }

}
