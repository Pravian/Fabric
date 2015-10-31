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
package net.pravian.fabric.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Conversions {

    private Conversions() {
        throw new AssertionError();
    }

    public static String asString(Object object) {
        return object == null ? null : object.toString();
    }

    public static int asInt(Object object) {
        if (object instanceof Number) {
            return ((Number) object).intValue();
        }

        try {
            return Integer.parseInt(object.toString());
        } catch (Exception ex) {
        }

        return 0;
    }

    public static List<String> asStringList(Object object) {
        final List<String> list = new ArrayList<>();

        if (object == null) {
            return list;
        }

        if (object instanceof Iterable<?>) { // Iterables
            for (Object it : (Iterable<?>) object) {
                final String value = asString(it);
                if (value != null) {
                    list.add(value);
                }
            }
        } else if (object instanceof String[]) { // String arrays
            list.addAll(Arrays.asList((String[]) object));
        } else if (object instanceof Object[]) { // Object arrays
            for (Object it : (Object[]) object) {
                final String value = asString(it);
                if (value != null) {
                    list.add(value);
                }
            }
        }

        return list;
    }

    public static Object objectify(Object object) {
        Object newObject = object;
        if (object instanceof String[]
                || object instanceof Object[]
                || object instanceof Iterable<?>) {
            newObject = asStringList(object);
        }

        return newObject;
    }

}
