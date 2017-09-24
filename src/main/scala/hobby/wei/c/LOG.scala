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

package hobby.wei.c

import android.util.Log
import hobby.chenai.nakam.basis.TAG._
import hobby.chenai.nakam.lang.J2S._
import hobby.wei.c.anno.annoid.BuildConfig
import hobby.wei.c.anno.proguard.Burden
import java.util.concurrent.locks.ReentrantLock

/**
  * 日志包装工具，可用使用如下命令控制日志的输出：
  * {{{
  *   adb shell setprop log.tag.{YourTag} {LEVEL}
  * }}}
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
object LOG {
  implicit class _2S(_s: String) {
    def s: S = LOG.S.obtain(_s)
  }

  object S {
    private val sPoolSync = new ReentrantLock
    private val MAX_POOL_SIZE = 50

    private var sPool: S = _
    private var sPoolSize = 0

    def obtain(s: String): S = {
      val so = obtain()
      so.s = s
      so
    }

    private def obtain(): S = {
      sPoolSync.lock()
      try
          if (sPool != null) {
            val sp = sPool
            sPool = sp.next
            sp.next = null
            sPoolSize -= 1
            return sp
          }
      finally sPoolSync.unlock()
      new S
    }
  }

  class S extends Equals {
    private var s: String = _
    private var next: S = _

    override def equals(o: Any) = o match {
      case that: S => that.canEqual(this) && that.s == this.s
      case _ => false
    }

    override def canEqual(that: Any) = that.isInstanceOf[S]

    override def hashCode = if (s.isNull) "".hashCode else s.hashCode

    override def toString = s

    @throws[InterruptedException]
    private def recycle(): Unit = {
      s = null
      S.sPoolSync.lockInterruptibly()
      try
          if (S.sPoolSize < S.MAX_POOL_SIZE) {
            next = S.sPool
            S.sPool = this
            S.sPoolSize += 1
          }
      finally S.sPoolSync.unlock()
    }

    @throws[Throwable]
    override protected def finalize(): Unit = {
      recycle()
      super.finalize()
    }
  }

  @Burden
  @throws[IllegalArgumentException]
  private def checkArgs(args: Any*): Unit = {
    for (o <- args) {
      if (o.isInstanceOf[String]) throw new IllegalArgumentException("\"" + o
        + "\"\n请不要使用 String 作为参数，"
        + "以防使用常量字符串，在数组里无法被混淆优化掉。常量请拼接到 String 类型的那个参数一起，"
        + "如果为变量，请使用 `str.s/L.s(str)` 方法包装。")
    }
  }

  @Burden
  def v(s: => String, args: Any*)(implicit tag: LogTag): Unit = v(null.asInstanceOf[Throwable], s, args)

  @Burden
  def v(e: Throwable, s: => String, args: Any*)(implicit tag: LogTag) = {
    val flats = args.flatten$
    checkArgs(flats: _*) // 放在外面接受检查，等release的时候会被直接删除。
    if (Log.isLoggable(tag.toString, Log.VERBOSE)) {
      val msg = if (flats.isEmpty) String.valueOf(s) else s.format(flats: _*)
      if (e.isNull) Log.v(tag.toString, msg) else Log.v(tag.toString, msg, e)
    }
  }

  @Burden
  def v(e: Throwable)(implicit tag: LogTag): Unit = v(e, null)

  @Burden
  def d(s: => String, args: Any*)(implicit tag: LogTag): Unit = d(null.asInstanceOf[Throwable], s, args)

  @Burden
  def d(e: Throwable, s: => String, args: Any*)(implicit tag: LogTag) = {
    val flats = args.flatten$
    checkArgs(flats: _*)
    if (Log.isLoggable(tag.toString, Log.DEBUG)) {
      val msg = if (flats.isEmpty) String.valueOf(s) else s.format(flats: _*)
      if (e.isNull) Log.d(tag.toString, msg) else Log.d(tag.toString, msg, e)
    }
  }

  @Burden
  def d(e: Throwable)(implicit tag: LogTag): Unit = d(e, null)

  @Burden
  def i(s: => String, args: Any*)(implicit tag: LogTag): Unit = i(null.asInstanceOf[Throwable], s, args)

  @Burden
  def i(e: Throwable, s: => String, args: Any*)(implicit tag: LogTag) = {
    val flats = args.flatten$
    checkArgs(flats: _*)
    if (Log.isLoggable(tag.toString, Log.INFO)) {
      val msg = if (flats.isEmpty) String.valueOf(s) else s.format(flats: _*)
      if (e.isNull) Log.i(tag.toString, msg) else Log.i(tag.toString, msg, e)
    }
  }

  @Burden
  def i(e: Throwable)(implicit tag: LogTag): Unit = i(e, null)

  @Burden
  def w(s: => String, args: Any*)(implicit tag: LogTag): Unit = w(null.asInstanceOf[Throwable], s, args)

  @Burden
  def w(e: Throwable, s: => String, args: Any*)(implicit tag: LogTag) = {
    val flats = args.flatten$
    checkArgs(flats: _*)
    if (BuildConfig.DEBUG || Log.isLoggable(tag.toString, Log.WARN)) {
      val msg = if (flats.isEmpty) String.valueOf(s) else s.format(flats: _*)
      if (e.isNull) Log.w(tag.toString, msg) else Log.w(tag.toString, msg, e)
    }
  }

  @Burden
  def w(e: Throwable)(implicit tag: LogTag): Unit = w(e, null)

  def e(s: => String, args: Any*)(implicit tag: LogTag): Unit = e(null.asInstanceOf[Throwable], s, args)

  def e(e: Throwable, s: => String, args: Any*)(implicit tag: LogTag) = {
    val flats = args.flatten$
    checkArgs(flats: _*)
    if (BuildConfig.DEBUG || Log.isLoggable(tag.toString, Log.ERROR)) {
      val msg = if (flats.isEmpty) String.valueOf(s) else s.format(flats: _*)
      if (e.isNull) Log.e(tag.toString, msg) else Log.e(tag.toString, msg, e)
    }
    //发送错误统计数据
  }

  def e(t: Throwable)(implicit tag: LogTag): Unit = e(t, null)
}
