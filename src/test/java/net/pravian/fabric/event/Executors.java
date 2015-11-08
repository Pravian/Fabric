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

import net.pravian.fabric.event.Events.DummyEvent;
import net.pravian.fabric.event.Events.PriorityDummyEvent;

public class Executors {

    public static class DummyEventExecutor extends AbstractEventExecutor {

        private final int priority;
        private boolean processed = false;
        private Event event;

        public DummyEventExecutor(int priority) {
            this.priority = priority;
        }

        @Override
        public Class<?> getEventClass() {
            return DummyEvent.class;
        }

        @Override
        public int getPriority() {
            return priority;
        }

        @Override
        public void execute(Event event) throws EventException {
            this.processed = true;
            this.event = event;
        }

        public boolean isProcessed() {
            return processed;
        }

        public Event getEvent() {
            return event;
        }
    }

    public static class PriorityDummyEventExecutor extends AbstractEventExecutor {

        private final int priority;

        public PriorityDummyEventExecutor(int priority) {
            this.priority = priority;
        }

        @Override
        public Class<?> getEventClass() {
            return PriorityDummyEvent.class;
        }

        @Override
        public int getPriority() {
            return priority;
        }

        @Override
        public void execute(Event event) throws EventException {
            ((PriorityDummyEvent) event).check(priority);
        }

    }

    public static class ExceptionDummyEventExecutor extends AbstractEventExecutor {

        private final int priority;

        public ExceptionDummyEventExecutor(int priority) {
            this.priority = priority;
        }

        @Override
        public Class<?> getEventClass() {
            return DummyEvent.class;
        }

        @Override
        public int getPriority() {
            return priority;
        }

        @Override
        public void execute(Event event) throws EventException {
            throw new RuntimeException("42");
        }
    }

}
