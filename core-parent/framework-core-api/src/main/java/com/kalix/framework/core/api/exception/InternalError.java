/*
 * Copyright (C) 2012 "Lexaden.com"
 *     contact@lexaden.com [http://www.lexaden.com]
 *
 *     This file is part of Lexaden Administration.
 *
 *     Lexaden Administration is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.kalix.framework.core.api.exception;

import java.io.Serializable;

/**
 * Exception error description, contains information about translation message key and its parameters.
 *
 * @author Denis Skarbichev
 */
public class InternalError implements Serializable {
    private String code;
    private Object[] params;

    public InternalError(String code) {
        this.code = code;
    }

    public InternalError(String code, Object... params) {
        this(code);
        this.params = params;
    }

    public String getCode() {
        return code;
    }


    public Object[] getParams() {
        return params;
    }
}