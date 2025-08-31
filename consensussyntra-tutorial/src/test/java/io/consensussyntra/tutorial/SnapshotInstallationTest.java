/*
 * Copyright (c) 2020, ConsensusSyntra.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.consensussyntra.tutorial;

import static io.consensussyntra.QueryPolicy.EVENTUAL_CONSISTENCY;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.Test;

import io.consensussyntra.Ordered;
import io.consensussyntra.RaftConfig;
import io.consensussyntra.RaftNode;
import io.consensussyntra.report.RaftLogStats;
import io.consensussyntra.statemachine.StateMachine;
import io.consensussyntra.tutorial.atomicregister.SnapshotableAtomicRegister;

/*

   TO RUN THIS TEST ON YOUR MACHINE:

   $ gh repo clone ConsensusSyntra/ConsensusSyntra
   $ cd ConsensusSyntra && ./mvnw clean test -Dtest=io.consensussyntra.tutorial.SnapshotInstallationTest -DfailIfNoTests=false -Ptutorial

   YOU CAN SEE THIS CLASS AT:

   https://github.com/ConsensusSyntra/ConsensusSyntra/blob/master/consensussyntra-tutorial/src/test/java/io/consensussyntra/tutorial/SnapshotInstallationTest.java

 */
public class SnapshotInstallationTest extends BaseLocalTest {

    private static final int COMMIT_COUNT_TO_TAKE_SNAPSHOT = 100;

    @Override
    protected RaftConfig getConfig() {
        return RaftConfig.newBuilder().setCommitCountToTakeSnapshot(COMMIT_COUNT_TO_TAKE_SNAPSHOT).build();
    }

    @Override
    protected StateMachine createStateMachine() {
        return new SnapshotableAtomicRegister();
    }

    @Test
    public void testSnapshotInstallation() {
        RaftNode leader = waitUntilLeaderElected();
        RaftNode follower = getAnyNodeExcept(leader.getLocalEndpoint());

        disconnect(leader.getLocalEndpoint(), follower.getLocalEndpoint());

        for (int i = 0; i < COMMIT_COUNT_TO_TAKE_SNAPSHOT; i++) {
            leader.replicate(SnapshotableAtomicRegister.newSetOperation("value" + i)).join();
        }

        assertThat(getRaftLogStats(leader).getTakeSnapshotCount()).isEqualTo(1);

        connect(leader.getLocalEndpoint(), follower.getLocalEndpoint());

        eventually(() -> {
            RaftLogStats logStats = getRaftLogStats(follower);
            assertThat(logStats.getInstallSnapshotCount()).isEqualTo(1);
            assertThat(logStats.getCommitIndex()).isEqualTo(getRaftLogStats(leader).getCommitIndex());
        });

        eventually(() -> assertThat(getRaftLogStats(follower).getInstallSnapshotCount()).isEqualTo(1));

        Ordered<String> leaderQueryResult = leader.<String>query(SnapshotableAtomicRegister.newGetOperation(),
                EVENTUAL_CONSISTENCY, Optional.empty(), Optional.empty()).join();

        Ordered<String> followerQueryResult = follower.<String>query(SnapshotableAtomicRegister.newGetOperation(),
                EVENTUAL_CONSISTENCY, Optional.empty(), Optional.empty()).join();

        assertThat(followerQueryResult.getCommitIndex()).isEqualTo(leaderQueryResult.getCommitIndex());
        assertThat(followerQueryResult.getResult()).isEqualTo(leaderQueryResult.getResult());
    }

    private RaftLogStats getRaftLogStats(RaftNode leader) {
        return leader.getReport().join().getResult().getLog();
    }

}
