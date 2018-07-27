/*
 * Copyright (C) 2014-present, Wei Chou(weichou2010@gmail.com)
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

package hobby.wei.c

import android.util.Log
import hobby.chenai.nakam.basis.TAG._
import hobby.chenai.nakam.lang.J2S._
import hobby.chenai.nakam.tool.pool.S
import hobby.chenai.nakam.tool.pool.S.obtain
import hobby.wei.c.anno.annoid._
import hobby.wei.c.log.Logger

/**
  * 日志包装工具，可用使用如下命令控制日志的输出：
  * {{{
  *   adb shell setprop "log.tag.{YourTag}" "{LEVEL}"
  * }}}
  * 注意：由于本日志组件会用到特殊的`TAG`标签，导致上述命令触发`invalid character`异常，因此
  * 应对具体的`TAG`字符串内容进行如下替换：`@|` -> `TA`, `~` -> `G`; 并将`@a32`这部分字符舍弃。
  *
  * 其中 LEVEL 的优先级顺序（从低到高）分别为：
  * {{{
  *   VERBOSE, DEBUG, INFO, WARN, ERROR, ASSERT, SUPPRESS（关闭日志输出）
  * }}}
  * 注意这里包含一个潜在规则：若某低优先级的日志可以输出，则比其更高优先级的日志总是能够输出。
  *
  * @author Wei Chou(weichou2010@gmail.com)
  * @version 2.0 upgrade to Scala version.
  */
object LOG extends Logger {
  lazy val DEBUG: Boolean = BuildConfig.DEBUG

  implicit class _2S(_s: String) {
    @inline def s: S = obtain(_s)
  }

  override protected def logv(tag: LogTag, e: Throwable, s: => String, args: Any*): Unit = {
    if (Log.isLoggable(tag.shell, Log.VERBOSE)) {
      val msg = if (args.isEmpty) String.valueOf(s) else s.format(args: _*)
      if (e.isNull) Log.v(tag.toString, msg) else Log.v(tag.toString, msg, e)
    }
  }

  override protected def logd(tag: LogTag, e: Throwable, s: => String, args: Any*): Unit = {
    if (Log.isLoggable(tag.shell, Log.DEBUG)) {
      val msg = if (args.isEmpty) String.valueOf(s) else s.format(args: _*)
      if (e.isNull) Log.d(tag.toString, msg) else Log.d(tag.toString, msg, e)
    }
  }

  override protected def logi(tag: LogTag, e: Throwable, s: => String, args: Any*): Unit = {
    if (Log.isLoggable(tag.shell, Log.INFO)) {
      val msg = if (args.isEmpty) String.valueOf(s) else s.format(args: _*)
      if (e.isNull) Log.i(tag.toString, msg) else Log.i(tag.toString, msg, e)
    }
  }

  override protected def logw(tag: LogTag, e: Throwable, s: => String, args: Any*): Unit = {
    if (DEBUG || Log.isLoggable(tag.toString, Log.WARN)) {
      val msg = if (args.isEmpty) String.valueOf(s) else s.format(args: _*)
      if (e.isNull) Log.w(tag.toString, msg) else Log.w(tag.toString, msg, e)
    }
  }

  override protected def loge(tag: LogTag, e: Throwable, s: => String, args: Any*): Unit = {
    if (DEBUG || Log.isLoggable(tag.toString, Log.ERROR)) {
      val msg = if (args.isEmpty) String.valueOf(s) else s.format(args: _*)
      if (e.isNull) Log.e(tag.toString, msg) else Log.e(tag.toString, msg, e)
    }
    //发送错误统计数据
  }
}
