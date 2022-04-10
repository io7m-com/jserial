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
 * The type of serial number arithmetic functions that are implemented using
 * {@code 31} bits or fewer.
 */

public interface SerialNumberIntType
{
  /**
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return {@code s0 + s1}
   */

  int add(
    int s0,
    int s1);

  /**
   * @return The number of bits used
   */

  int bits();

  /**
   * Compare the serial numbers {@code s0} and {@code s1}.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return {@code 0} if the serial numbers are equal, a value less than {@code
   * 0} if {@code s0 < s1}, or a value greater than {@code 0} if {@code s0 >
   * s1}
   */

  int compare(
    int s0,
    int s1);

  /**
   * Calculate the signed distance between {@code s0} and {@code s1}.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return The distance
   */

  int distance(
    int s0,
    int s1);

  /**
   * Calculate the unsigned distance between {@code s0} and {@code s1}.
   *
   * @param s0 A serial number
   * @param s1 A serial number
   *
   * @return The distance
   */

  default int distanceUnsigned(
    final int s0,
    final int s1)
  {
    return Math.abs(this.distance(s0, s1));
  }

  /**
   * Determine serial number validity.
   *
   * @param s A serial number
   *
   * @return {@code true} iff {@code s0} is in the range {@code [0, (2 ^ bits())
   * - 1]}
   */

  boolean inRange(int s);
}

