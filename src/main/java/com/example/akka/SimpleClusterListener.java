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

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.cluster.Cluster;
import akka.cluster.ClusterEvent;
import akka.cluster.ClusterEvent.MemberEvent;
import akka.cluster.ClusterEvent.MemberUp;
import akka.cluster.ClusterEvent.MemberRemoved;
import akka.cluster.ClusterEvent.UnreachableMember;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class SimpleClusterListener extends UntypedActor {
  LoggingAdapter log = Logging.getLogger(getContext().system(), this);
  Cluster cluster = Cluster.get(getContext().system());

  public static Props props() {
    return Props.create(SimpleClusterListener.class);
  }

  //subscribe to cluster changes
  @Override
  public void preStart() {
    //#subscribe
    cluster.subscribe(getSelf(), ClusterEvent.initialStateAsEvents(),
        MemberEvent.class, UnreachableMember.class);
    //#subscribe
  }

  //re-subscribe when restart
  @Override
  public void postStop() {
    cluster.unsubscribe(getSelf());
  }

  @Override
  public void onReceive(Object message) {
    if (message instanceof MemberUp) {
      MemberUp mUp = (MemberUp) message;
      log.info("Member is Up: {}", mUp.member());

    } else if (message instanceof UnreachableMember) {
      UnreachableMember mUnreachable = (UnreachableMember) message;
      log.info("Member detected as unreachable: {}", mUnreachable.member());

    } else if (message instanceof MemberRemoved) {
      MemberRemoved mRemoved = (MemberRemoved) message;
      log.info("Member is Removed: {}", mRemoved.member());

    } else if (message instanceof MemberEvent) {
      // ignore

    } else {
      unhandled(message);
    }
  }
}
