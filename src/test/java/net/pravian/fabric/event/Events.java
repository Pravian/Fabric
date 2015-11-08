/*
 * Copyright 2015 Jerom van der Sar.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.pravian.fabric.event;

import static com.google.common.truth.Truth.*;

public class Events {

    public static class DummyEvent extends AbstractEvent {
    }

    public static class DummySubEvent extends DummyEvent {
    }

    public static class OtherDummyEvent extends AbstractEvent {
    }

    public static class PriorityDummyEvent extends AbstractEvent {

        private int current = 1;

        public void check(int newCurrent) {
            assertWithMessage("Currently waiting for " + this.current + " but handling " + newCurrent).that(current).isEqualTo(newCurrent);
            current++;
        }

        public int getCurrent() {
            return current;
        }
    }
}
