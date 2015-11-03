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

    public static boolean asBoolean(Object object) {
        if (object instanceof Boolean) {
            return ((Boolean) object);
        }

        if (object == null) {
            return false;
        }

        try {
            return Boolean.parseBoolean(asString(object));
        } catch (Exception ignored) {
            return false;
        }
    }

    public static byte asByte(Object object) {
        if (object instanceof Number) {
            return ((Number) object).byteValue();
        }

        try {
            return Byte.parseByte(object.toString());
        } catch (Exception ex) {
        }

        return 0;
    }

    public static char asChar(Object object) {
        if (object instanceof Character) {
            return (Character) object;
        }

        try {
            String s = asString(object);
            return s.length() == 1 ? s.charAt(0) : (char) 0;
        } catch (Exception ex) {
        }

        return 0;
    }

    public static short asShort(Object object) {
        if (object instanceof Number) {
            return ((Number) object).shortValue();
        }

        try {
            return Short.parseShort(object.toString());
        } catch (Exception ex) {
        }

        return 0;
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

    public static long asLong(Object object) {
        if (object instanceof Number) {
            return ((Number) object).longValue();
        }

        try {
            return Long.parseLong(object.toString());
        } catch (Exception ex) {
        }

        return 0;
    }

    public static float asFloat(Object object) {
        if (object instanceof Number) {
            return ((Number) object).floatValue();
        }

        try {
            return Float.parseFloat(object.toString());
        } catch (Exception ex) {
        }

        return 0;
    }

    public static double asDouble(Object object) {
        if (object instanceof Number) {
            return ((Number) object).doubleValue();
        }

        try {
            return Double.parseDouble(object.toString());
        } catch (Exception ex) {
        }

        return 0;
    }

    public static String asString(Object object) {
        return object == null ? null : object.toString();
    }

    public static List<Object> asList(Object object) {
        if (object == null) {
            return null;
        }

        final List<Object> list = new ArrayList<>();

        if (object instanceof Iterable<?>) { // Iterables
            for (Object it : (Iterable<?>) object) {
                list.add(it);
            }
        } else if (object instanceof Object[]) { // Object arrays
            list.addAll(Arrays.asList((Object[]) object));
        }

        return list;
    }

    public static List<String> asStringList(Object object) {
        if (object == null) {
            return null;
        }

        final List<Object> objList = asList(object);
        if (objList == null) {
            return null;
        }

        final List<String> strList = new ArrayList<>();
        for (Object it : objList) {
            final String s = asString(it);
            if (s != null) {
                strList.add(s);
            }
        }

        return strList;
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
