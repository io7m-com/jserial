/*
 * Copyright © 2016 Mark Raynsford <code@io7m.com> https://www.io7m.com
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
 * An implementation of 32-bit serial number arithmetic.
 */

public final class SerialNumber32 implements SerialNumberLongType
{
  private static final SerialNumber32 INSTANCE;

  static {
    INSTANCE = new SerialNumber32();
  }

  private SerialNumber32()
  {

  }

  /**
   * @return A serial number calculator
   */

  public static SerialNumberLongType get()
  {
    return INSTANCE;
  }

  @Override
  public long add(
    final long s0,
    final long s1)
  {
    return (s0 + s1) % 4294967296L;
  }

  @Override
  public int bits()
  {
    return 32;
  }

  @Override
  public long distance(
    final long s0,
    final long s1)
  {
    return SerialDistance.distanceL(s0, s1, 4294967296L);
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
    return (s0 >= 0L) && (s0 < 4294967296L);
  }
}
