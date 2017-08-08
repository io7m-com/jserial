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
 * An implementation of 24-bit serial number arithmetic.
 */

public final class SerialNumber24 implements SerialNumberIntType
{
  private static final SerialNumber24 INSTANCE;

  static {
    INSTANCE = new SerialNumber24();
  }

  private SerialNumber24()
  {

  }

  /**
   * @return A serial number calculator
   */

  public static SerialNumberIntType get()
  {
    return SerialNumber24.INSTANCE;
  }

  @Override
  public int add(
    final int s0,
    final int s1)
  {
    final int d = s0 + s1;

    /*
     * Pretend that d is an unsigned 24 bit value by masking off the high 8
     * bits.
     */

    return d & 0x00ffffff;
  }

  @Override
  public int bits()
  {
    return 24;
  }

  @Override
  public int distance(
    final int s0,
    final int s1)
  {
    final int s0_w = s0 & 0xff_ffff;
    final int s1_w = s1 & 0xff_ffff;
    final int d = (s1_w - s0_w);
    final int r;
    if (Math.abs(d) >= 0x7f_ffff) {
      r = -d % 0x7f_ffff;
    } else {
      r = d;
    }
    return r;
  }

  @Override
  public int compare(
    final int s0,
    final int s1)
  {
    return -this.distance(s0, s1);
  }

  @Override
  public boolean inRange(
    final int s0)
  {
    return (s0 >= 0) && (s0 <= 0xffffff);
  }
}
