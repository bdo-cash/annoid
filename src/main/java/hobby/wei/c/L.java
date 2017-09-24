/*
 * Copyright (C) 2014-present, Wei Chou (weichou2010@gmail.com)
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

package hobby.wei.c;

import hobby.chenai.nakam.basis.TAG.LogTag;
import hobby.chenai.nakam.lang.J2S;
import hobby.wei.c.anno.proguard.Burden;
import scala.runtime.AbstractFunction0;

/**
 * @author Wei Chou(weichou2010@gmail.com)
 * @version 2.0 upgrade to Scala version.
 */
public class L {
    @Burden
    public static LOG.S s(String s) {
        return LOG.s(s);
    }

    @Burden
    public static void v(LogTag tag, String s, Object... args) {
        v(tag, null, s, args);
    }

    @Burden
    public static void v(LogTag tag, Throwable e, String s, Object... args) {
        LOG.v(new AbstractFunction0<String>() {
            @Override
            public String apply() {
                return s;
            }
        }, e, J2S.array(args), tag);
    }

    @Burden
    public static void v(LogTag tag, Throwable e) {
        v(tag, e, null);
    }

    @Burden
    public static void d(LogTag tag, String s, Object... args) {
        d(tag, null, s, args);
    }

    @Burden
    public static void d(LogTag tag, Throwable e, String s, Object... args) {
        LOG.d(new AbstractFunction0<String>() {
            @Override
            public String apply() {
                return s;
            }
        }, e, J2S.array(args), tag);
    }

    @Burden
    public static void d(LogTag tag, Throwable e) {
        d(tag, e, null);
    }

    @Burden
    public static void i(LogTag tag, String s, Object... args) {
        i(tag, null, s, args);
    }

    @Burden
    public static void i(LogTag tag, Throwable e, String s, Object... args) {
        LOG.i(new AbstractFunction0<String>() {
            @Override
            public String apply() {
                return s;
            }
        }, e, J2S.array(args), tag);
    }

    @Burden
    public static void i(LogTag tag, Throwable e) {
        i(tag, e, null);
    }

    @Burden
    public static void w(LogTag tag, String s, Object... args) {
        w(tag, null, s, args);
    }

    @Burden
    public static void w(LogTag tag, Throwable e, String s, Object... args) {
        LOG.w(new AbstractFunction0<String>() {
            @Override
            public String apply() {
                return s;
            }
        }, e, J2S.array(args), tag);
    }

    @Burden
    public static void w(LogTag tag, Throwable e) {
        w(tag, e, null);
    }

    public static void e(LogTag tag, String s, Object... args) {
        e(tag, null, s, args);
    }

    public static void e(LogTag tag, Throwable e, String s, Object... args) {
        LOG.e(new AbstractFunction0<String>() {
            @Override
            public String apply() {
                return s;
            }
        }, e, J2S.array(args), tag);
    }

    public static void e(LogTag tag, Throwable e) {
        e(tag, e, null);
    }
}
