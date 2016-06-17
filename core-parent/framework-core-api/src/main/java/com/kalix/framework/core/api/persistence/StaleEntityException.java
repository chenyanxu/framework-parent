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

package com.kalix.framework.core.api.persistence;


import com.kalix.framework.core.api.Translations;

/**
 * Stale entity exception occurs when entity database version is not consistent with entity version in memory
 *
 * @author Denis Skarbichev
 */
public class StaleEntityException extends InternalRuntimeException {
    public StaleEntityException(PersistentEntity entity) {
        super(new InternalError(Translations.STALE_OBJECT_STATE_EXCEPTION,
                entity.getClass(),
                entity.getVersion()));
    }
}
