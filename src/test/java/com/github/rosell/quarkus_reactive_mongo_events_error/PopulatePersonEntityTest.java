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

import io.quarkus.test.junit.QuarkusTest;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * Test the {@link PopulatePersonEntity}.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
public class PopulatePersonEntityTest {

  /**
   * The number of people that has to be created.
   */
  @ConfigProperty(name = "numPeople", defaultValue = "10")
  int numPeople;

  /**
   * Check that the people is filled in.
   *
   * @throws InterruptedException If not sleep.
   */
  @Test
  @Timeout(value = 1, unit = TimeUnit.MINUTES)
  public void shouldFillInPeople() throws InterruptedException {

    while (true) {

      final var count = PersonEntity.count().await().atMost(Duration.ofSeconds(10));

      if (count >= this.numPeople) {

        break;
      }
      Thread.sleep(Duration.ofSeconds(10).toMillis());

    }

  }

}
