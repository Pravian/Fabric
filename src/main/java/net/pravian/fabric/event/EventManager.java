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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.pravian.fabric.event.annotation.Listener;
import net.pravian.fabric.event.annotation.MethodEventExecutor;
import net.pravian.fabric.event.cancellable.CancellableEvent;
import net.pravian.fabric.exception.ExceptionHandler;
import net.pravian.fabric.exception.WrappingExceptionHandler;

public class EventManager {

    private final Map<Class<?>, List<EventExecutor>> exes = new HashMap<>();
    private final Map<Class<?>, EventExecutor[]> bakedExecutors = new LinkedHashMap<>();
    //
    private ExceptionHandler<EventException> exceptionHandler = new WrappingExceptionHandler<>();

    public void register(Listener listener) throws IllegalArgumentException {
        register(listener, true);
    }

    public void register(Listener listener, boolean rebake) throws IllegalArgumentException {
        final List<MethodEventExecutor> listenerExes = MethodEventExecutor.createExecutors(listener);

        for (EventExecutor listenerExe : listenerExes) {
            register(listenerExe, rebake);
        }
    }

    public void register(EventExecutor executor) {
        register(executor, true);
    }

    public void register(EventExecutor executor, boolean rebake) {
        final List<EventExecutor> exe = getOrCreate(executor.getEventClass());
        exe.add(executor);

        if (rebake) {
            bakeExecutors(executor.getEventClass());
        }
    }

    public void bakeExecutors() {
        bakedExecutors.clear();
        for (Class<?> listen : exes.keySet()) {
            bakeExecutors(listen);
        }
    }

    private void bakeExecutors(Class<?> listenClass) throws IllegalArgumentException {

        final List<EventExecutor> exesList = exes.get(listenClass);
        if (exesList == null) {
            throw new IllegalArgumentException("No executors found for: " + listenClass.getName());
        }

        Collections.sort(exesList);

        final EventExecutor[] exeArray = exes.get(listenClass).toArray(new EventExecutor[0]);
        bakedExecutors.put(listenClass, exeArray);
    }

    public void call(Event event) {
        final CancellableEvent cEvent = event instanceof CancellableEvent ? (CancellableEvent) event : null;

        // Add the executor to all the 
        Class<?> current = event.getClass();
        while (current != null) {
            final EventExecutor[] executors = bakedExecutors.get(current);

            if (executors != null) {
                for (EventExecutor exe : executors) {
                    if (cEvent != null && cEvent.isCancelled()) {
                        continue;
                    }

                    try {
                        exe.execute(event);
                    } catch (Exception ex) {
                        if (exceptionHandler != null) {
                            EventException eex = (EventException) (ex instanceof EventException ? ex : new EventException("Uncaught exception while handling event", ex));
                            exceptionHandler.handle(eex);
                        }
                    }
                }
            }

            // Find parent
            Class<?> superClass = current.getSuperclass();
            if (superClass == null) {
                // Find superinterface
                Class<?>[] inter = current.getInterfaces();
                if (inter.length == 1) {
                    current = inter[0];
                } else {
                    break; // No more parent interfaces
                }
            } else {
                current = superClass;
            }
        }
    }

    public ExceptionHandler<EventException> getExceptionHandler() {
        return exceptionHandler;
    }

    public void setExceptionHandler(ExceptionHandler<EventException> exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public Map<Class<?>, EventExecutor[]> getBakedExecutors() {
        return Collections.unmodifiableMap(bakedExecutors);
    }

    private List<EventExecutor> getOrCreate(Class<?> clazz) {
        List<EventExecutor> exe = exes.get(clazz);

        if (exe == null) {
            exe = new ArrayList<>();
            exes.put(clazz, exe);
        }

        return exe;
    }

}
