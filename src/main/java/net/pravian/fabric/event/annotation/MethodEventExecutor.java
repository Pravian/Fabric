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

import net.pravian.fabric.event.AbstractEventExecutor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.pravian.fabric.event.Event;
import net.pravian.fabric.event.EventException;
import net.pravian.fabric.event.cancellable.CancellableEvent;

public class MethodEventExecutor extends AbstractEventExecutor {

    private final Listener handler;
    private final Method method;
    private final Modify modify;
    private final Class<? extends Event> listenClass;

    public MethodEventExecutor(Listener handler, Method method) {
        this.handler = handler;
        this.method = method;
        this.modify = method.getAnnotation(Modify.class);
        this.listenClass = (Class<? extends Event>) method.getParameterTypes()[0];
    }

    @Override
    public Class<? extends Event> getEventClass() {
        return listenClass;
    }

    @Override
    public int getPriority() {
        if (modify != null) {
            return modify.priority().getPriority();
        }

        return EventPriority.NORMAL.getPriority();
    }

    @Override
    public void execute(Event event) throws EventException {
        if (modify != null && modify.ignoreCancelled()) {
            if (event instanceof CancellableEvent
                    && ((CancellableEvent) event).isCancelled()) {
                return;
            }
        }

        try {
            method.invoke(handler, event);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new EventException("Could not invoke event method: " + handler.getClass() + "#" + method.getName(), ex);
        } catch (Exception ex) {
            throw new EventException("Uncaught exception whilst executing event", ex);
        }
    }

    public static List<MethodEventExecutor> createExecutors(Listener listener) throws IllegalArgumentException {
        List<MethodEventExecutor> executors = new ArrayList<>();

        Class<?> current = listener.getClass();

        do {
            for (Method method : current.getMethods()) {
                if (method.getAnnotation(Register.class) == null) {
                    continue;
                }

                final Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1
                        || !Event.class.isAssignableFrom(parameterTypes[0])
                        || !method.getReturnType().equals(Void.TYPE)) {
                    throw new IllegalArgumentException("Could not register handlers for class: " + listener.getClass().getName()
                            + ". Handler " + method.getName() + " isn't defined properly.");
                }

                executors.add(new MethodEventExecutor(listener, method));
            }
        } while ((current = current.getSuperclass()) != null);

        return executors;
    }

}
