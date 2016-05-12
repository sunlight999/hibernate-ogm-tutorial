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

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.TableGenerator;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table( name = "EVENTS" )
public class Event {
	@Id
	 @GeneratedValue(strategy = GenerationType.TABLE, generator = "event")
   @TableGenerator(
      name = "event",
      table = "sequences",
      pkColumnName = "key",
      pkColumnValue = "event",
      valueColumnName = "seed"
   )
    private Long id;

    private String title;
    
  	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EVENT_DATE")
   private Date date;
  
  
   @ManyToOne
    private Person     person;
  

	public Event() {
		// this form used by Hibernate
	}

	public Event(String title, Date date) {
		// for application use, to create new events
		this.title = title;
		this.date = date;
	}

  public Event(String title, Date date,Person person) {
		// for application use, to create new events
		this.title = title;
		this.date = date;
      this.person=person;
	}

    public Long getId() {
		return id;
    }

    private void setId(Long id) {
		this.id = id;
    }

    public Date getDate() {
		return date;
    }

    public void setDate(Date date) {
		this.date = date;
    }

    public String getTitle() {
		return title;
    }

    public void setTitle(String title) {
		this.title = title;
    }
  
  public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}