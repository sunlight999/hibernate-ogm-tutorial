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
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


import junit.framework.TestCase;


import com.mongodb.DB;
import com.mongodb.MongoClient;


import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.DatabaseRetrievalMethod;
import org.hibernate.search.query.ObjectLookupMethod;
import org.hibernate.search.query.dsl.QueryBuilder;


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
       MongoClient mongoClient = new MongoClient();
        DB db = mongoClient.getDB("ogm-jpa-tutorial");
        db.dropDatabase();
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
        
         ActivPerson person = new ActivPerson(30, "jean-michel", "dupont");
               Event event1=new Event("Our very first event!", new Date(),person);
         Event event2=new Event("A follow up event", new Date(),person);
         
         person.getEvents().add(event1);
         person.getEvents().add(event2);
         
         entityManager.persist((Person)person);
         entityManager.getTransaction().commit();
         entityManager.close();

         // now lets pull persons from the database and list them
         entityManager = entityManagerFactory.createEntityManager();
         List<ActivPerson> result = entityManager.createQuery("from ActivPerson", ActivPerson.class).getResultList();

         for (ActivPerson activPerson : result) {
             System.out.println("Person (" + activPerson.getId() + ") first name: " + activPerson.getFirstname() + ", age : "
                               + activPerson.getAge());
                                // now lets pull events from the database and list them
         Set<Event> resultEvent=activPerson.getEvents();
                               for (Event event : resultEvent) {
             System.out.println("Event (" + event.getDate() + ") : " + event.getTitle());
         }
                               
         }
      
      
        //Add full-text superpowers to any EntityManager:
        FullTextEntityManager ftem = Search.getFullTextEntityManager(entityManager);

        //Optionally use the QueryBuilder to simplify Query definition:
        QueryBuilder b = ftem.getSearchFactory()
        .buildQueryBuilder()
        .forEntity(ActivPerson.class)
        .get();

        //Create a Lucene Query:
        Query lq = b.keyword().onField("events.title").matching("first").createQuery();

        //Transform the Lucene Query in a JPA Query:
        FullTextQuery ftQuery = ftem.createFullTextQuery(lq, ActivPerson.class);

        ftQuery.initializeObjectsWith( ObjectLookupMethod.SKIP, DatabaseRetrievalMethod.FIND_BY_ID );

        //List all matching Hypothesis:
        List<ActivPerson> resultList = ftQuery.getResultList();
         
        for (ActivPerson activPerson : resultList) {
             System.out.println("Matching Person with event title containing first word: (" + activPerson.getId() + ") first name: " + activPerson.getFirstname() + ", age : "
                               + activPerson.getAge());
        }
        entityManager.close();


    }

}
