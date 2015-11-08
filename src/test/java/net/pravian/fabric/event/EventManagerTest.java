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

import net.pravian.fabric.event.annotation.Listeners;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import net.pravian.fabric.event.Events.DummyEvent;
import net.pravian.fabric.event.Events.DummySubEvent;
import net.pravian.fabric.event.Events.OtherDummyEvent;
import net.pravian.fabric.event.Events.PriorityDummyEvent;
import net.pravian.fabric.event.Executors.DummyEventExecutor;
import net.pravian.fabric.event.Executors.ExceptionDummyEventExecutor;
import net.pravian.fabric.event.Executors.PriorityDummyEventExecutor;
import net.pravian.fabric.event.annotation.Listeners.DummyListener;
import net.pravian.fabric.exception.ExceptionHandler;
import org.junit.Test;

public class EventManagerTest {

    @Test
    public void simple() {
        DummyEventExecutor executor = new DummyEventExecutor(3);

        EventManager manager = new EventManager();
        manager.register(executor);

        assertThat(executor.isProcessed()).isFalse();
        assertThat(executor.getEvent()).isNull();

        DummyEvent event = new DummyEvent();
        manager.call(event);

        assertWithMessage("Event processed").that(executor.isProcessed()).isTrue();
        assertWithMessage("Correct event").that(executor.getEvent()).isEqualTo(event);
    }

    @Test
    public void subEvent() {
        DummyEventExecutor executor = new DummyEventExecutor(3);

        EventManager manager = new EventManager();
        manager.register(executor);

        assertThat(executor.isProcessed()).isFalse();
        assertThat(executor.getEvent()).isNull();

        DummySubEvent event = new DummySubEvent();
        manager.call(event);

        assertWithMessage("Event processed").that(executor.isProcessed()).isTrue();
        assertWithMessage("Correct event").that(executor.getEvent()).isEqualTo(event);
    }

    @Test
    public void exceptionHandling() {
        EventExecutor executor = new ExceptionDummyEventExecutor(3);

        EventManager manager = new EventManager();
        manager.register(executor);

        DummyEvent event = new DummyEvent();

        try {
            manager.call(event);
            assertWithMessage("No exception thrown").fail();
        } catch (Exception ex) {
            assertWithMessage("Exception not wrapped").that(ex.getCause()).isInstanceOf(EventException.class);
        }

        manager.setExceptionHandler(new ExceptionHandler<EventException>() {
            @Override
            public void handle(EventException throwable) {
                throw new AssertionError("Success", throwable);
            }
        });

        try {
            manager.call(event);
        } catch (Exception ex) {
            assertWithMessage("Exception thrown").fail();
        } catch (AssertionError err) {
            assertWithMessage("AssertionError not properly wrapped").that(err.getCause()).isInstanceOf(EventException.class);
        }
    }

    @Test
    public void nameEquality() {
        assertWithMessage("Event name equality").that(new DummyEvent().getName()).isEqualTo(new DummyEvent().getName());
        assertWithMessage("Event name inequality").that(new DummyEvent().getName()).isNotEqualTo(new OtherDummyEvent().getName());
    }

    @Test
    public void priority() {
        final PriorityDummyEventExecutor exe1 = new PriorityDummyEventExecutor(1);
        final PriorityDummyEventExecutor exe2 = new PriorityDummyEventExecutor(2);
        final PriorityDummyEventExecutor exe3 = new PriorityDummyEventExecutor(3);

        EventManager manager = new EventManager();
        manager.register(exe3);
        manager.register(exe2);
        manager.register(exe1);

        PriorityDummyEvent event = new PriorityDummyEvent();
        manager.call(event);

        assertWithMessage("Processed all events").that(event.getCurrent()).isEqualTo(4);

        // Reverse registration order
        manager = new EventManager();
        manager.register(exe1);
        manager.register(exe2);
        manager.register(exe3);

        event = new PriorityDummyEvent();
        manager.call(event);

        assertWithMessage("Processed all events").that(event.getCurrent()).isEqualTo(4);
    }

}
