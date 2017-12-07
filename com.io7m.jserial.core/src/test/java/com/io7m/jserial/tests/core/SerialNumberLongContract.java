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

package com.io7m.jserial.tests.core;

import com.io7m.jserial.core.SerialNumberLongType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

public abstract class SerialNumberLongContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  private void showDistance(
    final long s0,
    final long s1,
    final long d)
  {
    this.log().debug(
      "distance({} [{}], {} [{}]) = {} [{}]",
      Long.valueOf(s0),
      Long.toUnsignedString(s0, 16),
      Long.valueOf(s1),
      Long.toUnsignedString(s1, 16),
      Long.valueOf(d),
      Long.toUnsignedString(d, 16));
  }

  protected abstract SerialNumberLongType get();

  protected abstract Logger log();

  protected abstract long integerBits();

  protected abstract long nearLargestValue();

  private long largestDistance()
  {
    return (1L << (this.integerBits() - 1L));
  }

  @Test
  public final void testDistances0_Pow2()
  {
    final SerialNumberLongType s = this.get();

    final long bits = this.integerBits();
    for (long exp = 0L; exp <= bits; ++exp) {
      final long x0 = (1L << exp) - 1L;
      final long x1 = x0 + 1L;
      final long d0 = s.distance(0L, x0);
      final long d1 = s.distance(0L, x1);
      final long d0u = s.distanceUnsigned(0L, x0);
      final long d1u = s.distanceUnsigned(0L, x1);

      this.log().debug("exp: {}", Long.valueOf(exp));
      this.showDistance(0L, x0, d0);
      this.showDistance(0L, x1, d1);

      if (exp != bits) {
        Assert.assertEquals(x0, d0);
        Assert.assertEquals(x1, d1);
        Assert.assertEquals(x0, d0u);
        Assert.assertEquals(x1, d1u);
      } else {
        Assert.assertEquals(-1L, d0);
        Assert.assertEquals(1L, d0u);
      }
    }
  }

  @Test
  public final void testDistancesPow2()
  {
    final SerialNumberLongType s = this.get();

    final long bits = this.integerBits();
    for (long exp = 1L; exp <= bits; ++exp) {
      final long x0 = (1L << (exp - 1L)) - 1L;
      final long x1 = (1L << exp) - 1L;
      final long d0 = s.distance(x0, x1);
      final long d1 = s.distance(x1, x0);

      this.log().debug("exp: {}", Long.valueOf(exp));
      this.showDistance(x0, x1, d0);
      this.showDistance(x1, x0, d1);

      final long ad0 = Math.abs(d0);
      final long ad1 = Math.abs(d1);
      Assert.assertTrue(ad0 <= this.largestDistance());
      Assert.assertTrue(ad1 <= this.largestDistance());
      Assert.assertEquals(1L << (exp - 1L), d0);
      Assert.assertEquals(-(1L << (exp - 1L)), d1);
    }
  }

  @Test
  public final void testCompareOrder0_Pow2()
  {
    final SerialNumberLongType s = this.get();

    final long bits = this.integerBits();
    for (long exp = 1L; exp <= bits; ++exp) {
      final long x0 = (1L << exp) - 1L;
      final long c0_x0 = s.compare(0L, x0);
      final long x0_c0 = s.compare(x0, 0L);

      this.log().debug(
        "exp: {}", Long.valueOf(exp));

      this.log().debug(
        "compare: {} {} -> {}",
        Long.valueOf(0),
        Long.valueOf(x0),
        Long.valueOf(c0_x0));

      this.log().debug(
        "compare: {} {} -> {}",
        Long.valueOf(x0),
        Long.valueOf(0),
        Long.valueOf(x0_c0));

      Assert.assertEquals(0L, (long) s.compare(x0, x0));

      if (exp != bits) {
        Assert.assertTrue(c0_x0 < 0);
        Assert.assertTrue(x0_c0 > 0);
      } else {
        Assert.assertEquals(1L, c0_x0);
        Assert.assertEquals(-1L, x0_c0);
      }
    }
  }

  @Test
  public final void testCompareOrderPow2_Pow2m1()
  {
    final SerialNumberLongType s = this.get();

    final long bits = this.integerBits();
    for (long exp = 1L; exp <= bits; ++exp) {
      final long x0 = (1L << (exp - 1L)) - 1L;
      final long x1 = (1L << exp) - 1L;

      final long c_x0_x1 = s.compare(x0, x1);
      final long c_x1_x0 = s.compare(x1, x0);

      this.log().debug(
        "exp: {}", Long.valueOf(exp));

      this.log().debug(
        "compare: {} {} -> {}",
        Long.valueOf(x0),
        Long.valueOf(x1),
        Long.valueOf(c_x0_x1));

      this.log().debug(
        "compare: {} {} -> {}",
        Long.valueOf(x1),
        Long.valueOf(x0),
        Long.valueOf(c_x1_x0));

      Assert.assertTrue(c_x0_x1 < 0);
      Assert.assertTrue(c_x1_x0 > 0);
    }
  }

  @Test
  public final void testAddOrder()
  {
    final SerialNumberLongType s = this.get();

    for (long exp = 0L; exp <= this.integerBits(); ++exp) {
      final long x = (1L << exp) - 1L;
      final long y = s.add(x, 1L);
      final long z = s.add(x, -1L);

      Assert.assertEquals(0L, s.compare(x, x));
      Assert.assertTrue(s.compare(x, y) < 0L);
      Assert.assertTrue(s.compare(x, z) > 0L);
    }
  }

  @Test
  public final void testBits()
  {
    final SerialNumberLongType s = this.get();
    Assert.assertTrue(s.bits() > 0);
    Assert.assertEquals(this.integerBits(), (long) s.bits());
  }

  @Test
  public final void testInRange()
  {
    final SerialNumberLongType s = this.get();

    for (long exp = 1L; exp < this.integerBits(); ++exp) {
      final long x = (1L << exp) - 1L;
      Assert.assertTrue(s.inRange(x));
      Assert.assertFalse(s.inRange(-x));
    }

    final long x = (long) Math.pow(2.0, (double) s.bits());
    Assert.assertFalse(s.inRange(x));
  }

  @Test
  public final void testWrap()
  {
    final SerialNumberLongType s = this.get();

    long curr = this.nearLargestValue();
    for (int index = 0; index < 6; ++index) {
      final long next = s.add(curr, 1L);
      final long distance_curr_next = s.distance(curr, next);
      final long distance_next_curr = s.distance(next, curr);

      this.log().debug(
        "distance: curr {} next {} -> {}",
        Long.valueOf(curr),
        Long.valueOf(next),
        Long.valueOf(distance_curr_next));

      this.log().debug(
        "distance: next {} curr {} -> {}",
        Long.valueOf(next),
        Long.valueOf(curr),
        Long.valueOf(distance_next_curr));

      Assert.assertEquals(1L, distance_curr_next);
      Assert.assertEquals(-1L, distance_next_curr);
      curr = next;

      this.log().debug("--");
    }
  }
}
