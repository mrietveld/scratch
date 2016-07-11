/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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
package org.ocram.experiment;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

@Entity
@SequenceGenerator(name = "foreignKeyParentIdSeq", sequenceName = "FOREIGN_INSTANCE_INFO_ID_SEQ")
public class ForeignKeyParent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "foreignKeyParentIdSeq")
    @Column(name = "Id")
    public Long parentId;

    @Version
    @Column(name = "OPTLOCK")
    public int version;

    @ElementCollection
    @CollectionTable(name = "ChildTypes", joinColumns = @JoinColumn(name = "ParentId"))
    @Column(name = "element")
    public Set<String> childTypes = new HashSet<String>();

}
