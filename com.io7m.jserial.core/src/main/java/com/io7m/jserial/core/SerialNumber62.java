/*
 * Copyright © 2016 <code@io7m.com> http://io7m.com
 *
 * Permission to use, copy, modify, and/or distribute this software for any
 * purpose with or without fee is hereby granted, provided that the above
 * copyright notice and this permission notice appear in all copies.
 *
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 * WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY
 * SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 * ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF OR
 * IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 */

package com.io7m.jserial.core;

/**
 * An implementation of 56-bit serial number arithmetic.
 */

public final class SerialNumber62 implements SerialNumberLongType
{
  private static final SerialNumber62 INSTANCE;
  private static final long MAX_N = 0x3fffffff_ffffffffL;
  private static final long MAX_N_M1 = 0x1fffffff_ffffffffL;

  static {
    INSTANCE = new SerialNumber62();
  }

  private SerialNumber62()
  {

  }

  /**
   * @return A serial number calculator
   */

  public static SerialNumberLongType get()
  {
    return SerialNumber62.INSTANCE;
  }

  @Override
  public long add(
    final long s0,
    final long s1)
  {
    return (s0 + s1) & SerialNumber62.MAX_N;
  }

  @Override
  public int bits()
  {
    return 62;
  }

  @Override
  public long distance(
    final long s0,
    final long s1)
  {
    final long s0_m = s0 & SerialNumber62.MAX_N;
    final long s1_m = s1 & SerialNumber62.MAX_N;
    final long d = s1_m - s0_m;
    final long r;
    if (d > SerialNumber62.MAX_N_M1) {
      r = SerialNumber62.MAX_N_M1 - d;
    } else {
      r = d;
    }
    return r;
  }

  @Override
  public long compare(
    final long s0,
    final long s1)
  {
    return -this.distance(s0, s1);
  }

  @Override
  public boolean inRange(
    final long s0)
  {
    return (s0 >= 0L) && (Long.compareUnsigned(s0, SerialNumber62.MAX_N) <= 0);
  }
}
