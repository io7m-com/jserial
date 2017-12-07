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

import com.io7m.jserial.core.SerialNumberIntType;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class SerialNumberIntContract
{
  @Rule public final ExpectedException expected = ExpectedException.none();

  private void showDistance(
    final int s0,
    final int s1,
    final int d)
  {
    this.log().debug(
      "distance({} [{}], {} [{}]) = {} [{}]",
      Integer.valueOf(s0),
      Integer.toUnsignedString(s0, 16),
      Integer.valueOf(s1),
      Integer.toUnsignedString(s1, 16),
      Integer.valueOf(d),
      Integer.toUnsignedString(d, 16));
  }

  protected abstract SerialNumberIntType get();

  protected abstract Logger log();

  protected abstract int integerBits();

  protected abstract int nearLargestValue();

  private int largestDistance()
  {
    return (1 << (this.integerBits() - 1));
  }

  @Test
  public final void testDistances0_Pow2()
  {
    final SerialNumberIntType s = this.get();

    final int bits = this.integerBits();
    for (int exp = 0; exp <= bits; ++exp) {
      final int x0 = (1 << exp) - 1;
      final int x1 = x0 + 1;
      final int d0 = s.distance(0, x0);
      final int d1 = s.distance(0, x1);
      final int d0u = s.distanceUnsigned(0, x0);
      final int d1u = s.distanceUnsigned(0, x1);

      this.log().debug("exp: {}", Integer.valueOf(exp));
      this.showDistance(0, x0, d0);
      this.showDistance(0, x1, d1);

      if (exp != bits) {
        Assert.assertEquals((long) x0, (long) d0);
        Assert.assertEquals((long) x1, (long) d1);
        Assert.assertEquals((long) x0, (long) d0u);
        Assert.assertEquals((long) x1, (long) d1u);
      } else {
        Assert.assertEquals(-1L, (long) d0);
        Assert.assertEquals(1L, (long) d0u);
      }
    }
  }

  @Test
  public final void testDistancesPow2()
  {
    final SerialNumberIntType s = this.get();

    final int bits = this.integerBits();
    for (int exp = 1; exp <= bits; ++exp) {
      final int x0 = (1 << (exp - 1)) - 1;
      final int x1 = (1 << exp) - 1;
      final int d0 = s.distance(x0, x1);
      final int d1 = s.distance(x1, x0);

      this.log().debug("exp: {}", Integer.valueOf(exp));
      this.showDistance(x0, x1, d0);
      this.showDistance(x1, x0, d1);

      final int ad0 = Math.abs(d0);
      final int ad1 = Math.abs(d1);
      Assert.assertTrue(ad0 <= this.largestDistance());
      Assert.assertTrue(ad1 <= this.largestDistance());
      Assert.assertEquals((long) (1 << (exp - 1)), (long) d0);
      Assert.assertEquals((long) -(1 << (exp - 1)), (long) d1);
    }
  }

  @Test
  public final void testCompareOrder0_Pow2()
  {
    final SerialNumberIntType s = this.get();

    final int bits = this.integerBits();
    for (int exp = 1; exp <= bits; ++exp) {
      final int x0 = (1 << exp) - 1;
      final int c0_x0 = s.compare(0, x0);
      final int x0_c0 = s.compare(x0, 0);

      this.log().debug(
        "exp: {}", Integer.valueOf(exp));

      this.log().debug(
        "compare: {} {} -> {}",
        Integer.valueOf(0),
        Integer.valueOf(x0),
        Integer.valueOf(c0_x0));

      this.log().debug(
        "compare: {} {} -> {}",
        Integer.valueOf(x0),
        Integer.valueOf(0),
        Integer.valueOf(x0_c0));

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
    final SerialNumberIntType s = this.get();

    final int bits = this.integerBits();
    for (int exp = 1; exp <= bits; ++exp) {
      final int x0 = (1 << (exp - 1)) - 1;
      final int x1 = (1 << exp) - 1;

      final int c_x0_x1 = s.compare(x0, x1);
      final int c_x1_x0 = s.compare(x1, x0);

      this.log().debug(
        "exp: {}", Integer.valueOf(exp));

      this.log().debug(
        "compare: {} {} -> {}",
        Integer.valueOf(x0),
        Integer.valueOf(x1),
        Integer.valueOf(c_x0_x1));

      this.log().debug(
        "compare: {} {} -> {}",
        Integer.valueOf(x1),
        Integer.valueOf(x0),
        Integer.valueOf(c_x1_x0));

      Assert.assertTrue(c_x0_x1 < 0);
      Assert.assertTrue(c_x1_x0 > 0);
    }
  }

  @Test
  public final void testAddOrder()
  {
    final SerialNumberIntType s = this.get();

    for (int exp = 0; exp <= this.integerBits(); ++exp) {
      final int x = (1 << exp) - 1;
      final int y = s.add(x, 1);
      final int z = s.add(x, -1);

      Assert.assertEquals(0L, (long) s.compare(x, x));
      Assert.assertTrue(s.compare(x, y) < 0);
      Assert.assertTrue(s.compare(x, z) > 0);
    }
  }

  @Test
  public final void testBits()
  {
    final SerialNumberIntType s = this.get();
    Assert.assertTrue(s.bits() > 0);
    Assert.assertEquals((long) this.integerBits(), (long) s.bits());
  }

  @Test
  public final void testInRange()
  {
    final SerialNumberIntType s = this.get();

    for (int exp = 1; exp < this.integerBits(); ++exp) {
      final int x = (1 << exp) - 1;
      Assert.assertTrue(s.inRange(x));
      Assert.assertFalse(s.inRange(-x));
    }

    final int x = (int) Math.pow(2.0, (double) s.bits());
    Assert.assertFalse(s.inRange(x));
  }

  @Test
  public final void testWrap()
  {
    final SerialNumberIntType s = this.get();

    int current = this.nearLargestValue();
    for (int index = 0; index < 6; ++index) {
      final int next = s.add(current, 1);
      final int distance_curr_next = s.distance(current, next);
      final int distance_next_curr = s.distance(next, current);
      final int compare_curr_next = s.compare(current, next);
      final int compare_next_curr = s.compare(next, current);

      this.log().debug(
        "distance: curr {} next {} -> {}",
        Integer.valueOf(current),
        Integer.valueOf(next),
        Integer.valueOf(distance_curr_next));

      this.log().debug(
        "distance: next {} curr {} -> {}",
        Integer.valueOf(next),
        Integer.valueOf(current),
        Integer.valueOf(distance_next_curr));

      this.log().debug(
        "compare: curr {} next {} -> {}",
        Integer.valueOf(current),
        Integer.valueOf(next),
        Integer.valueOf(compare_curr_next));

      this.log().debug(
        "compare: next {} curr {} -> {}",
        Integer.valueOf(next),
        Integer.valueOf(current),
        Integer.valueOf(compare_next_curr));

      Assert.assertEquals(1, distance_curr_next);
      Assert.assertEquals(-1, distance_next_curr);
      Assert.assertTrue(compare_curr_next < 0);
      Assert.assertTrue(compare_next_curr > 0);
      current = next;

      this.log().debug("--");
    }
  }
}
