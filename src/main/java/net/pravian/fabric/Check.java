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
package net.pravian.fabric;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Check {

    private Check() {
        throw new AssertionError();
    }

    // isNull
    public static void isNull(Object o) {
        isNull(o, "Object must be null");
    }

    public static void isNull(Object o, String msg) {
        if (o != null) {
            ex(msg);
        }
    }

    // notNull
    public static <T> T notNull(T o) {
        return notNull(o, "Object may not be null");
    }

    public static <T> T notNull(T o, Object msg) {
        if (o == null) {
            ex(msg);
        }
        return o;
    }

    // notEmpty
    public static String[] noneEmpty(String... s) {
        for (String s1 : s) {
            notEmpty(s1);
        }
        return s;
    }

    public static String notEmpty(String s) {
        return notEmpty(s, "String may not be empty");
    }

    public static CharSequence notEmpty(CharSequence s) {
        return notEmpty(s, "CharSequence may not be empty");
    }

    public static String notEmpty(String s, Object msg) {
        if (s == null || s.isEmpty()) {
            ex(msg);
        }
        return s;
    }

    public static <T> T notEmpty(T o) {
        return notEmpty(o, "Object may not be empty");
    }

    public static <T> T notEmpty(T o, Object msg) {
        if (o instanceof String) {
            throw new AssertionError("notEmpty() for String should be triggered");
        } else if (o instanceof Object[]) {
            not(((Object[]) o).length == 0, "Array may not be empty!");
        } else if (o instanceof Collection<?>) {
            not(((Collection<?>) o).isEmpty(), "List may not be empty");
        } else if (o instanceof Iterable<?>) {
            is(((Iterable<?>) o).iterator().hasNext(), "Iterable may not be empty");
        } else if (o instanceof Map<?, ?>) {
            not(((Map<?, ?>) o).isEmpty(), "May may not be empty");
        }

        return o;
    }

    // not
    public static boolean not(boolean b) {
        return not(b, "Expression may not be true");
    }

    public static boolean not(boolean b, Object msg) {
        if (b) {
            ex(msg);
        }

        return b;
    }

    // is
    public static boolean is(boolean b) {
        return is(b, "Expression may not be false");
    }

    public static boolean is(boolean b, Object msg) {
        if (!b) {
            ex(msg);
        }

        return b;
    }

    // index
    public static <T> int index(int index, T[] array) {
        return index(index, array.length);
    }

    public static <T> int index(int index, T[] array, Object msg) {
        return index(index, array.length, msg);
    }

    public static int index(int index, List<?> array) {
        return index(index, notNull(array).size());
    }

    public static int index(int index, List<?> array, Object msg) {
        return index(index, notNull(array).size(), msg);
    }

    public static <T> int index(int index, int size) {
        return index(index, size, "Index out of bounds");
    }

    public static <T> int index(int index, int size, Object msg) {
        if (index < 0 || index >= size) {
            ex(msg);
        }
        return index;
    }

    // util
    private static void ex(Object msg) {
        throw new IllegalArgumentException(String.valueOf(msg));
    }

}
