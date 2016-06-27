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


/**
 * Exception is for internal application exception handling.
 *
 * @author Denis Skarbichev
 */
public class InternalException extends Exception implements ExceptionAware {
    private InternalError error;

    public InternalException(String errorCode, Object... params) {
        error = new InternalError(errorCode, params);
    }

    public InternalException(InternalError error) {
        this.error = error;
    }

    @Override
    public InternalError getError() {
        return error;
    }
}