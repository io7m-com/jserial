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

import com.io7m.junreachable.UnreachableCodeException;

/**
 * An implementation of 16-bit serial number arithmetic.
 */

public final class SerialNumber16
{
  private SerialNumber16()
  {
    throw new UnreachableCodeException();
  }

  /**
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return {@code s0 + s1}
   */

  public static int add(
    final int s0,
    final int s1)
  {
    final int d = s0 + s1;

    /**
     * Pretend that d is an unsigned 16 bit value by masking off the high 16
     * bits.
     */

    return d & 0xffff;
  }

  /**
   * Calculate the distance between {@code s0} and {@code s1}.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return The distance
   */

  public static int distanceBetween(
    final int s0,
    final int s1)
  {
    final char d = (char) (s1 - s0);

    /**
     * Extend d to a 32 bit signed value
     */

    return (d > 0x7FFF) ? d | 0xFFFF0000 : d;
  }

  /**
   * Compare serial numbers.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return {@code true} iff the distance between {@code s0} and {@code s1} is
   * {@code 0}
   */

  public static boolean equals(
    final int s0,
    final int s1)
  {
    return SerialNumber16.distanceBetween(s0, s1) == 0;
  }

  /**
   * Determine serial ordering.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return {@code true} iff the distance between {@code s0} and {@code s1} is
   * negative
   */

  public static boolean greaterThan(
    final int s0,
    final int s1)
  {
    return SerialNumber16.distanceBetween(s0, s1) < 0;
  }

  /**
   * Determine serial ordering.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return {@code true} iff the distance between {@code s0} and {@code s1} is
   * negative or {@code 0}
   */

  public static boolean greaterThanOrEqual(
    final int s0,
    final int s1)
  {
    return SerialNumber16.distanceBetween(s0, s1) <= 0;
  }

  /**
   * Increment serial numbers.
   *
   * @param s0 A serial number
   *
   * @return {@code s0 + 1}
   */

  public static int increment(
    final int s0)
  {
    return SerialNumber16.add(s0, 1);
  }

  /**
   * Determine serial number validity
   *
   * @param s0 A serial number
   *
   * @return {@code true} iff {@code s0} is in the range {@code [0, 0xffff]}
   */

  public static boolean inRange(
    final int s0)
  {
    return (s0 >= 0) && (s0 <= 0xffff);
  }

  /**
   * Determine serial ordering.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return {@code true} iff the distance between {@code s0} and {@code s1} is
   * positive
   */

  public static boolean lessThan(
    final int s0,
    final int s1)
  {
    return SerialNumber16.distanceBetween(s0, s1) > 0;
  }

  /**
   * Determine serial ordering.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return {@code true} iff the distance between {@code s0} and {@code s1} is
   * positive or {@code 0}
   */

  public static boolean lessThanOrEqual(
    final int s0,
    final int s1)
  {
    return SerialNumber16.distanceBetween(s0, s1) >= 0;
  }
}
