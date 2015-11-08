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
package net.pravian.fabric.event.annotation;

import java.util.Date;
import net.pravian.fabric.event.Events.DummyEvent;
import net.pravian.fabric.event.Events.OtherDummyEvent;

public class Listeners {

    public static class DummyListener implements Listener {

        private boolean processed = false;
        private DummyEvent event;

        @Register
        public void onEvent(DummyEvent event) {
            this.processed = true;
            this.event = event;
        }

        public boolean isProcessed() {
            return processed;
        }

        public DummyEvent getEvent() {
            return event;
        }
    }

    public static class DummySubListener extends DummyListener {

        private boolean subProcessed = false;
        private OtherDummyEvent subEvent;

        @Register
        public void onSubEvent(OtherDummyEvent event) {
            this.subProcessed = true;
            this.subEvent = event;
        }

        public boolean isSubProcessed() {
            return subProcessed;
        }

        public OtherDummyEvent getSubEvent() {
            return subEvent;
        }
    }

    public static class LowPriorityListener implements Listener {

        @Register
        @Modify(priority = EventPriority.LOW)
        public void onEvent(DummyEvent event) {
        }

    }

    public static class ExceptionDummyListener implements Listener {

        @Register
        public void onEvent(DummyEvent event) {
            throw new RuntimeException("42");
        }

    }

    public static class InvalidDummyListener1 implements Listener {

        @Register
        public void onEvent(String noevent) {
        }
    }

    public static class InvalidDummyListener2 implements Listener {

        @Register
        public void onEvent(DummyEvent event, Date noevent) {
        }
    }

    public static class InvalidDummyListener3 implements Listener {

        @Register
        public Date onEvent(DummyEvent event) {
            return null;
        }
    }

}
