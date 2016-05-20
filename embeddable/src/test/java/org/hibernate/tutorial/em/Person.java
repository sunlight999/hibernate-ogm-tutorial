/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.tutorial.em;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;


@Entity
@Table(name = "PERSONS")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorValue("P")
@DiscriminatorColumn(name="P_TYPE", discriminatorType=DiscriminatorType.STRING)
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "person")
   @TableGenerator(
      name = "person",
      table = "sequences",
      pkColumnName = "key",
      pkColumnValue = "person",
      valueColumnName = "seed"
   )
    @Column(name = "PERSON_ID")
    protected Long       id;

    @Column(name = "age")
    protected int        age;

    @Column(name = "first_name")
    protected String     firstname;

    @Column(name = "last_name")
    protected String     lastname;

    public Person() {
    }

    public Person(int age, String firstname, String lastname) {
        this.age = age;
        this.firstname = firstname;
        this.lastname = lastname;
    }

    // Accessor methods for all properties, private setter for 'id'
    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public int getAge() {
        return age;
    }

}
