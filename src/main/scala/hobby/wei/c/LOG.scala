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
import hobby.chenai.nakam.basis.TAG
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

  @Burden
  def S(s: String) = S.obtain(s)

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

    override def hashCode = if (s == null) "".hashCode else s.hashCode

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
  private def checkArgs(args: Any*): Unit = {
    if (args.nonEmpty) for (o <- args) {
      if (o.isInstanceOf[String]) throw new IllegalArgumentException("请不要使用String作为参数，"
        + "以防使用常量字符串，在数组里无法被混淆优化掉。常量请拼接到String类型的那个参数一起，"
        + "如果为变量，请使用L.s(s)方法包装。")
    }
  }

  @Burden
  def v(s: => String = null, e: Throwable = null)(args: Any*)(implicit tag: TAG.LogTag) = {
    checkArgs(args) // 放在外面接受检查，等release的时候会被直接删除。
    if (Log.isLoggable(tag.toString, Log.VERBOSE)) {
      val msg = if (args.isEmpty) String.valueOf(s) else String.format(s, args)
      if (e == null) Log.v(tag.toString, msg) else Log.v(tag.toString, msg, e)
    }
  }

  @Burden
  def d(s: => String = null, e: Throwable = null)(args: Any*)(implicit tag: TAG.LogTag) = {
    checkArgs(args)
    if (Log.isLoggable(tag.toString, Log.DEBUG)) {
      val msg = if (args.isEmpty) String.valueOf(s) else String.format(s, args)
      if (e == null) Log.d(tag.toString, msg) else Log.d(tag.toString, msg, e)
    }
  }

  @Burden
  def i(s: => String = null, e: Throwable = null)(args: Any*)(implicit tag: TAG.LogTag) = {
    checkArgs(args)
    if (Log.isLoggable(tag.toString, Log.INFO)) {
      val msg = if (args.isEmpty) String.valueOf(s) else String.format(s, args)
      if (e == null) Log.i(tag.toString, msg) else Log.i(tag.toString, msg, e)
    }
  }

  @Burden
  def w(s: => String, e: Throwable = null)(args: Any*)(implicit tag: TAG.LogTag) = {
    checkArgs(args)
    if (BuildConfig.DEBUG || Log.isLoggable(tag.toString, Log.WARN)) {
      val msg = if (args.isEmpty) String.valueOf(s) else String.format(s, args)
      if (e == null) Log.w(tag.toString, msg) else Log.w(tag.toString, msg, e)
    }
  }

  def e(s: => String, e: Throwable = null)(args: Any*)(implicit tag: TAG.LogTag) = {
    checkArgs(args)
    if (BuildConfig.DEBUG || Log.isLoggable(tag.toString, Log.ERROR)) {
      val msg = if (args.isEmpty) String.valueOf(s) else String.format(s, args)
      if (e == null) Log.e(tag.toString, msg) else Log.e(tag.toString, msg, e)
    }
    //发送错误统计数据
  }
}
