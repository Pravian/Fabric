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

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import java.util.Map;
import net.pravian.fabric.event.EventException;
import net.pravian.fabric.event.EventExecutor;
import net.pravian.fabric.event.EventManager;
import net.pravian.fabric.event.Events;
import net.pravian.fabric.event.Events.DummyEvent;
import net.pravian.fabric.event.annotation.Listeners.DummyListener;
import net.pravian.fabric.event.annotation.Listeners.ExceptionDummyListener;
import org.junit.Test;

public class AnnotationEventTest {

    @Test
    public void annotationListener() {

        Listeners.DummyListener listener = new Listeners.DummyListener();

        EventManager manager = new EventManager();
        manager.register(listener);

        assertThat(listener.isProcessed()).isFalse();
        assertThat(listener.getEvent()).isNull();

        Events.DummyEvent event = new Events.DummyEvent();
        manager.call(event);

        assertWithMessage("Event processed").that(listener.isProcessed()).isTrue();
        assertWithMessage("Correct event").that(listener.getEvent()).isEqualTo(event);
    }

    @Test
    public void registration() {
        EventManager manager = new EventManager();

        DummyListener listener = new DummyListener();
        manager.register(listener);

        Map<Class<?>, EventExecutor[]> exes = manager.getBakedExecutors();
        assertThat(exes.size()).isEqualTo(1);
        EventExecutor[] eves = exes.get(DummyEvent.class);
        assertThat(eves).isNotNull();
        assertThat(eves.length).isEqualTo(1);
        assertThat(eves[0].getEventClass()).isEqualTo(DummyEvent.class);
    }

    @Test
    public void modify() {
        EventManager manager = new EventManager();

        Listeners.LowPriorityListener listener = new Listeners.LowPriorityListener();
        manager.register(listener);

        EventExecutor[] eves = manager.getBakedExecutors().get(DummyEvent.class);
        assertThat(eves[0].getPriority()).isEqualTo(EventPriority.LOW.getPriority());
    }

    @Test
    public void annotationSubListener() {
        Listeners.DummySubListener listener = new Listeners.DummySubListener();

        EventManager manager = new EventManager();
        manager.register(listener);

        assertThat(listener.isProcessed()).isFalse();
        assertThat(listener.getEvent()).isNull();
        assertThat(listener.isSubProcessed()).isFalse();
        assertThat(listener.getSubEvent()).isNull();

        Events.DummyEvent event = new Events.DummyEvent();
        manager.call(event);
        Events.OtherDummyEvent otherEvent = new Events.OtherDummyEvent();
        manager.call(otherEvent);

        assertWithMessage("Event processed").that(listener.isProcessed()).isTrue();
        assertWithMessage("Correct event").that(listener.getEvent()).isEqualTo(event);
        assertWithMessage("Subevent processed").that(listener.isSubProcessed()).isTrue();
        assertWithMessage("Correct subevent").that(listener.getSubEvent()).isEqualTo(otherEvent);
    }

    @Test
    public void invalidAnnotationListener() {
        EventManager manager = new EventManager();

        try {
            manager.register(new Listeners.InvalidDummyListener1());
            assertWithMessage("Registering method with non-Event parameter").fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            manager.register(new Listeners.InvalidDummyListener2());
            assertWithMessage("Registering method with multiple parameters").fail();
        } catch (IllegalArgumentException ex) {
        }

        try {
            manager.register(new Listeners.InvalidDummyListener3());
            assertWithMessage("Registering method with non-void signature").fail();
        } catch (IllegalArgumentException ex) {
        }

    }

    @Test
    public void exceptionHandling() {

        EventManager manager = new EventManager();

        ExceptionDummyListener listener = new ExceptionDummyListener();
        manager.register(listener);

        DummyEvent event = new Events.DummyEvent();

        try {
            manager.call(event);
            assertWithMessage("No exception thrown").fail();
        } catch (Exception ex) {
            assertWithMessage("Exception not wrapped").that(ex.getCause()).isInstanceOf(EventException.class);
        }
    }
}
