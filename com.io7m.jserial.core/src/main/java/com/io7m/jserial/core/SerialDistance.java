/*
 * Copyright © 2017 Mark Raynsford <code@io7m.com> https://www.io7m.com
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

final class SerialDistance
{
  private SerialDistance()
  {
    throw new UnreachableCodeException();
  }

  private static int sign(
    final int x)
  {
    return x > 0 ? 1 : -1;
  }

  static int distance(
    final int s0,
    final int s1,
    final int max)
  {
    final int lower = Math.min(s0, s1);
    final int higher = Math.max(s0, s1);

    // Non-serial distance from higher number to 256.
    final int to_end = max - higher;

    // Non-serial distance going directly between inputs.
    final int inner = higher - lower;

    // Non-serial distance the way that wraps around on the number line.
    final int outer = lower + to_end;

    // Attempt to find the shortest distance.
    final int direction;
    if (Math.abs(inner) <= Math.abs(outer)) {
      // The inner route; go right on the number line if s1 > s0.
      direction = sign(s1 - s0);
      return inner * direction;
    }

    // The outer route that wraps around; go left on the number line if s0 < s1.
    direction = sign(s0 - s1);
    return outer * direction;
  }

  private static long signL(
    final long x)
  {
    return x > 0L ? 1L : -1L;
  }

  static long distanceL(
    final long s0,
    final long s1,
    final long max)
  {
    final long lower = Math.min(s0, s1);
    final long higher = Math.max(s0, s1);

    // Non-serial distance from higher number to 256.
    final long to_end = max - higher;

    // Non-serial distance going directly between inputs.
    final long inner = higher - lower;

    // Non-serial distance the way that wraps around on the number line.
    final long outer = lower + to_end;

    // Attempt to find the shortest distance.
    final long direction;
    if (Math.abs(inner) <= Math.abs(outer)) {
      // The inner route; go right on the number line if s1 > s0.
      direction = signL(s1 - s0);
      return inner * direction;
    }

    // The outer route that wraps around; go left on the number line if s0 < s1.
    direction = signL(s0 - s1);
    return outer * direction;
  }
}
