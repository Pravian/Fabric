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
package net.pravian.fabric.config.file.adapter.yaml;

import lombok.Getter;
import net.pravian.fabric.config.file.FileConfigOptions;
import org.yaml.snakeyaml.DumperOptions;

public class YamlConfigOptions extends FileConfigOptions {

    private final DumperOptions dumperOptions = new DumperOptions();

    public YamlConfigOptions() {
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setIndent(2);
        dumperOptions.setAllowUnicode(true);
        dumperOptions.setLineBreak(DumperOptions.LineBreak.UNIX);
        dumperOptions.setSplitLines(true);
        dumperOptions.setWidth(80);
    }

    public DumperOptions rawDumperOptions() {
        return dumperOptions;
    }

    public int indentation() {
        return dumperOptions.getIndent();
    }

    public YamlConfigOptions indentation(int indentation) {
        dumperOptions.getIndent();
        return this;
    }

    public FlowStyle flowStyle() {
        return FlowStyle.valueOf(dumperOptions.getDefaultFlowStyle().toString());
    }

    public YamlConfigOptions flowStyle(FlowStyle style) {
        dumperOptions.setDefaultFlowStyle(style.style);
        return this;
    }

    public ScalarStyle scalarStyle() {
        return ScalarStyle.valueOf(dumperOptions.getDefaultScalarStyle().toString());
    }

    public YamlConfigOptions scalarStyle(ScalarStyle style) {
        dumperOptions.setDefaultScalarStyle(style.style);
        return this;
    }

    public static enum FlowStyle {

        AUTO(DumperOptions.FlowStyle.AUTO),
        BLOCK(DumperOptions.FlowStyle.BLOCK),
        FLOW(DumperOptions.FlowStyle.FLOW);
        //
        @Getter
        private final DumperOptions.FlowStyle style;

        private FlowStyle(DumperOptions.FlowStyle style) {
            this.style = style;
        }
    }

    public static enum ScalarStyle {

        DOUBLE_QUOTED(DumperOptions.ScalarStyle.DOUBLE_QUOTED),
        FOLDED(DumperOptions.ScalarStyle.FOLDED),
        LITERAL(DumperOptions.ScalarStyle.LITERAL),
        PLAIN(DumperOptions.ScalarStyle.PLAIN),
        SINGLE_QUOTED(DumperOptions.ScalarStyle.SINGLE_QUOTED);
        //
        @Getter
        private final DumperOptions.ScalarStyle style;

        private ScalarStyle(DumperOptions.ScalarStyle style) {
            this.style = style;
        }

    }

}
