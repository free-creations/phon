/*
 * Copyright 2013 Harald Postner <Harald at free-creations.de>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.free_creations.dbEntities;

import java.util.Objects;

/**
 * In javax.persistence two Entities reference the same record if the are of the
 * same class and have the same primary key.
 *
 * @author Harald Postner <Harald at free-creations.de>
 */
public class EntityIdentity {

  public final Class entityClass;
  public final Object primaryKey;

  EntityIdentity(Class entityClass, Object primaryKey) {
    this.entityClass = entityClass;
    this.primaryKey = primaryKey;
  }

  @Override
  public int hashCode() {
    int hash = Objects.hashCode(this.entityClass);
    hash = 64007 * hash + Objects.hashCode(this.primaryKey);
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final EntityIdentity other = (EntityIdentity) obj;
    if (!Objects.equals(this.entityClass, other.entityClass)) {
      return false;
    }
    if (!Objects.equals(this.primaryKey, other.primaryKey)) {
      return false;
    }
    return true;
  }
}
