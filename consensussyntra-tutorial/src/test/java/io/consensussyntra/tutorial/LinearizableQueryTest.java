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

import java.util.Optional;

import org.junit.Test;

import io.consensussyntra.Ordered;
import io.consensussyntra.QueryPolicy;
import io.consensussyntra.RaftNode;
import io.consensussyntra.statemachine.StateMachine;
import io.consensussyntra.tutorial.atomicregister.OperableAtomicRegister;
import io.consensussyntra.tutorial.atomicregister.OperableAtomicRegister.AtomicRegisterOperation;

/*

   TO RUN THIS TEST ON YOUR MACHINE:

   $ gh repo clone ConsensusSyntra/ConsensusSyntra
   $ cd ConsensusSyntra && ./mvnw clean test -Dtest=io.consensussyntra.tutorial.LinearizableQueryTest -DfailIfNoTests=false -Ptutorial

   YOU CAN SEE THIS CLASS AT:

   https://github.com/ConsensusSyntra/ConsensusSyntra/blob/master/consensussyntra-tutorial/src/test/java/io/consensussyntra/tutorial/LinearizableQueryTest.java

 */
public class LinearizableQueryTest extends BaseLocalTest {

    @Override
    protected StateMachine createStateMachine() {
        return new OperableAtomicRegister();
    }

    @Test
    public void testLinearizableQuery() {
        RaftNode leader = waitUntilLeaderElected();

        String value = "value";
        AtomicRegisterOperation operation1 = OperableAtomicRegister.newSetOperation(value);
        Ordered<String> result1 = leader.<String>replicate(operation1).join();

        System.out.println("set operation commit index: " + result1.getCommitIndex());

        AtomicRegisterOperation operation2 = OperableAtomicRegister.newGetOperation();
        Ordered<String> queryResult = leader
                .<String>query(operation2, QueryPolicy.LINEARIZABLE, Optional.empty(), Optional.empty()).join();

        System.out.println(
                "get operation result: " + queryResult.getResult() + ", commit index: " + queryResult.getCommitIndex());

        assertThat(queryResult.getResult()).isEqualTo(value);
        assertThat(queryResult.getCommitIndex()).isEqualTo(result1.getCommitIndex());
    }

}
