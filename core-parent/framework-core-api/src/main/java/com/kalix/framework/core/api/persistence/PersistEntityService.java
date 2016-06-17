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


/**
 * Service to persist entities and collections of entities.
 *
 * @author Denis Skarbichev
 */
public interface PersistEntityService {
    /**
     * Persists any entity of type PersistentEntity.
     *
     * @param entity - entity to persist
     * @param <T>    - entity type
     * @return saved PersistentEntity object.
     */
//    public <T extends PersistentEntity> T save(T entity);

    /**
     * Persists collection of PersistentEntity entities.
     *
     * @param entities - entities to persist
     * @param <T>      - entity type
     * @return saved collection of PersistentEntity objects.
     */
//    public <T extends PersistentEntity> Collection<T> save(Collection<T> entities);

    /**
     * Finds PlatformEntity objects by unique key identifier  and entity type class.
     *
     * @param aClass - type class of entity.
     * @param key    - unique key identifier of the entity
     * @param <T>    - entity type
     * @return found PlatformEntity object
     */
//    public <T extends PlatformEntity> T findPlatformEntityByKey(Class<T> aClass, String key);
}
