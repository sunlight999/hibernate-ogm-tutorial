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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import junit.framework.TestCase;






/**
 * Illustrates the use of Hibernate native APIs.  The code here is unchanged from the {@code basic} example, the
 * only difference being the use of annotations to supply the metadata instead of Hibernate mapping files.
 *
 * @author Steve Ebersole
 */
public class BidirectionalMappingTest extends TestCase {
    private EntityManagerFactory entityManagerFactory;




    @Override
    protected void setUp() throws Exception {
        // like discussed with regards to SessionFactory, an EntityManagerFactory is set up once for an application
        // 		IMPORTANT: notice how the name here matches the name we gave the persistence-unit in persistence.xml!
        entityManagerFactory = Persistence.createEntityManagerFactory("ogm-jpa-tutorial");
       

    }

    @Override
    protected void tearDown() throws Exception {
        entityManagerFactory.close();
        
    }

    @SuppressWarnings({"unchecked"})
    public void testSubclassBasicUsage() {
        // create a couple of persons...
         EntityManager entityManager = entityManagerFactory.createEntityManager();
         entityManager.getTransaction().begin();
         entityManager.persist(new Event("Our very first event!", new Date()));
         entityManager.persist(new Event("A follow up event", new Date()));

         ActivPerson person = new ActivPerson(30, "jean-michel", "dupont");
         entityManager.persist((Person)person);
         entityManager.getTransaction().commit();
         entityManager.close();

         // now lets pull persons from the database and list them
         entityManager = entityManagerFactory.createEntityManager();
         List<Person> result = entityManager.createQuery("from Person", Person.class).getResultList();

         for (Person activPerson : result) {
             System.out.println("Person (" + activPerson.getId() + ") first name: " + activPerson.getFirstname() + ", age : "
                               + activPerson.getAge());
         }
      
       // now lets pull events from the database and list them
         List<Event> resultEvent = entityManager.createQuery("from Event", Event.class).getResultList();
         for (Event event : resultEvent) {
             System.out.println("Event (" + event.getDate() + ") : " + event.getTitle());
         }

        entityManager.close();


    }

}
