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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.Optional;
import java.util.concurrent.CompletionException;

import org.junit.Test;

import io.consensussyntra.Ordered;
import io.consensussyntra.QueryPolicy;
import io.consensussyntra.RaftNode;
import io.consensussyntra.exception.LaggingCommitIndexException;
import io.consensussyntra.statemachine.StateMachine;
import io.consensussyntra.tutorial.atomicregister.OperableAtomicRegister;

/*

   TO RUN THIS TEST ON YOUR MACHINE:

   $ gh repo clone ConsensusSyntra/ConsensusSyntra
   $ cd ConsensusSyntra && ./mvnw clean test -Dtest=io.consensussyntra.tutorial.MonotonicLocalQueryTest -DfailIfNoTests=false -Ptutorial

   YOU CAN SEE THIS CLASS AT:

   https://github.com/ConsensusSyntra/ConsensusSyntra/blob/master/consensussyntra-tutorial/src/test/java/io/consensussyntra/tutorial/MonotonicLocalQueryTest.java

 */
public class MonotonicLocalQueryTest extends BaseLocalTest {

    @Override
    protected StateMachine createStateMachine() {
        return new OperableAtomicRegister();
    }

    @Test
    public void testMonotonicLocalQuery() {
        RaftNode leader = waitUntilLeaderElected();
        RaftNode follower = getAnyNodeExcept(leader.getLocalEndpoint());

        leader.replicate(OperableAtomicRegister.newSetOperation("value1")).join();

        disconnect(leader.getLocalEndpoint(), follower.getLocalEndpoint());

        leader.replicate(OperableAtomicRegister.newSetOperation("value2")).join();

        Ordered<String> queryResult = leader.<String>query(OperableAtomicRegister.newGetOperation(),
                QueryPolicy.LINEARIZABLE, Optional.empty(), Optional.empty()).join();

        try {
            follower.query(OperableAtomicRegister.newGetOperation(), QueryPolicy.EVENTUAL_CONSISTENCY,
                    Optional.of(queryResult.getCommitIndex()), Optional.empty()).join();
            fail("non-monotonic query cannot succeed.");
        } catch (CompletionException e) {
            assertThat(e).hasCauseInstanceOf(LaggingCommitIndexException.class);
        }
    }

}
