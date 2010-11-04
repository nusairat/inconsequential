/* Copyright (C) 2010 SpringSource
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
package org.springframework.datastore.mapping.keyvalue.engine;

import org.springframework.dao.CannotAcquireLockException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.datastore.mapping.collection.PersistentList;
import org.springframework.datastore.mapping.collection.PersistentSet;
import org.springframework.datastore.mapping.core.Session;
import org.springframework.datastore.mapping.core.SessionImplementor;
import org.springframework.datastore.mapping.engine.*;
import org.springframework.datastore.mapping.keyvalue.mapping.Family;
import org.springframework.datastore.mapping.keyvalue.mapping.KeyValue;
import org.springframework.datastore.mapping.model.*;
import org.springframework.datastore.mapping.model.types.*;
import org.springframework.datastore.mapping.proxy.ProxyFactory;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import java.io.Serializable;
import java.util.*;

/**
 * Abstract implementation of the EntityPersister abstract class
 * for key/value style stores
 * 
 * @author Graeme Rocher
 * @since 1.0
 */
public abstract class AbstractKeyValueEntityPesister<T,K> extends NativeEntryEntityPersister<T,K> {
    protected String entityFamily;

    public AbstractKeyValueEntityPesister(MappingContext context, PersistentEntity entity, Session session) {
        super(context, entity, session);
        entityFamily = getFamily(entity, classMapping);
    }


    public String getEntityFamily() {
        return entityFamily;
    }

    @Override
    public ClassMapping getClassMapping() {
        return classMapping;
    }

    @Override
    protected String getNativePropertyKey(PersistentProperty prop) {
        PropertyMapping<KeyValue> pm = prop.getMapping();
        String propKey = null;
        if(pm.getMappedForm()!=null) {
            propKey = pm.getMappedForm().getKey();
        }
        if(propKey == null) {
            propKey = prop.getName();
        }
        return propKey;
    }

    protected String getFamily(PersistentEntity persistentEntity, ClassMapping<Family> cm) {
        String table = null;
        if(cm.getMappedForm() != null) {
            table = cm.getMappedForm().getFamily();
        }
        if(table == null) table = persistentEntity.getJavaClass().getName();
        return table;
    }

    protected String getKeyspace(ClassMapping<Family> cm, String defaultValue) {
        String keyspace = null;
        if(cm.getMappedForm() != null) {
            keyspace = cm.getMappedForm().getKeyspace();
        }
        if(keyspace == null) keyspace = defaultValue;
        return keyspace;
    }


}
