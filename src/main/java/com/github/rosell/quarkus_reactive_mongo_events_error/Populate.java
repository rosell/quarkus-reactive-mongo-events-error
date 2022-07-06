/*
 * -----------------------------------------------------------------------------
 *
 * Copyright 2019 - 2022 UDT-IA, IIIA-CSIC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * -----------------------------------------------------------------------------
 */
package com.github.rosell.quarkus_reactive_mongo_events_error;

import io.quarkus.arc.Priority;
import io.quarkus.logging.Log;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.runtime.StartupEvent;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.bson.Document;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

/**
 * Populate the database with documents.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Priority(1)
@ApplicationScoped
public class Populate {

  /**
   * The number of people that has to be created.
   */
  @ConfigProperty(name = "numPeople", defaultValue = "10")
  int numPeople;

  /**
   * The number of people that has to be created.
   */
  @ConfigProperty(name = "quarkus.mongodb.database", defaultValue = "test")
  String dbName;

  /**
   * The channel to populate.
   */
  public static final String POPULATE_CHANNEL = "populate-documents";

  /**
   * The name of the collection to populate.
   */
  public static final String COLLECTION_NAME = "personDocs";

  /**
   * The component to manage steps.
   */
  @Inject
  @Channel(POPULATE_CHANNEL)
  Emitter<Integer> stepper;

  /**
   * The client to the database.
   */
  @Inject()
  ReactiveMongoClient client;

  /**
   * Called when the quarkus is started to start to fill in with documents.
   *
   * @param event with the start up status.
   */
  public void onStart(@Observes final StartupEvent event) {

    Log.info("Starting populating");
    this.stepper.send(0);
  }

  /**
   * Create a new person.
   *
   * @param count number of created people.
   */
  @Incoming(POPULATE_CHANNEL)
  public void createPerson(final Integer count) {

    Log.infov("Try to create person {0}", count);
    final var doc = new Document();
    doc.put("name", "person_doc_" + count);
    this.client.getDatabase(this.dbName).getCollection(COLLECTION_NAME).insertOne(doc).onFailure().invoke(error -> {

      Log.errorv(error, "Cannot insert {0}", doc);

    }).onItem().invoke(created -> {

      Log.infov("Created doc {0} as {1}", count, created);
      if (count < this.numPeople) {

        this.stepper.send(count + 1);

      }
    });

  }

}
