/*
 * Copyright 2017 Google Inc.
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
 * limitations under the License
 */

package com.example.akka;

import akka.actor.ActorSystem;
import akka.actor.Address;
import akka.cluster.Cluster;
import akka.stream.ActorMaterializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleClusterMain {
  private static final String CLUSTER_NAME = "ClusterSystem";

  public static void main(String[] args) throws IOException {
    ActorSystem actorSystem = ActorSystem.create(CLUSTER_NAME);
    actorSystem.actorOf(SimpleClusterListener.props());
    final ActorMaterializer materializer = ActorMaterializer.create(actorSystem);

    Cluster cluster = Cluster.get(actorSystem);
    List<Address> addresses = Arrays.asList(System.getenv().get("SEED_NODES").split(","))
        .stream()
        .map(ip -> new Address("akka.tcp", CLUSTER_NAME, ip, 2551))
        .collect(Collectors.toList());
    cluster.joinSeedNodes(addresses);
  }
}
