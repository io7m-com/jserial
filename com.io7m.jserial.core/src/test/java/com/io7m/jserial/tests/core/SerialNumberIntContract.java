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

  protected abstract int getIntegerBits();

  protected abstract int getNearUpper();

  private int getMask()
  {
    return (1 << this.getIntegerBits()) - 1;
  }

  private int getLargestDistance()
  {
    return (1 << this.getIntegerBits() - 1) - 1;
  }

  @Test
  public final void testDistances0_Pow2()
  {
    final SerialNumberIntType s = this.get();

    for (int exp = 0; exp < this.getIntegerBits(); ++exp) {
      final int x = (int) Math.pow(2.0, (double) exp) & this.getMask();
      final int d0 = s.distance(0, x);
      final int d1 = s.distance(0, x + 1);

      this.showDistance(0, x, d0);
      this.showDistance(0, x + 1, d1);

      if (exp < this.getIntegerBits() - 1) {
        Assert.assertEquals((long) x, (long) d0);
        Assert.assertEquals((long) (x + 1), (long) d1);
      } else {
        Assert.assertEquals(-1L, (long) d0);
        Assert.assertEquals(-2L, (long) d1);
      }
    }
  }

  @Test
  public final void testCompareOrder()
  {
    final SerialNumberIntType s = this.get();

    for (int exp = 0; exp < this.getIntegerBits(); ++exp) {
      final int x = (int) Math.pow(2.0, (double) exp) & this.getMask();
      final int d0 = s.distance(0, x);
      final int d1 = s.distance(0, x + 1);

      this.showDistance(0, x, d0);
      this.showDistance(0, x + 1, d1);

      Assert.assertEquals(0L, (long) s.compare(x, x));

      if (exp < this.getIntegerBits() - 1) {
        Assert.assertEquals((long) x, (long) d0);
        Assert.assertEquals((long) (x + 1), (long) d1);
      } else {
        Assert.assertEquals(-1L, (long) d0);
        Assert.assertEquals(-2L, (long) d1);
      }
    }
  }

  @Test
  public final void testAddOrder()
  {
    final SerialNumberIntType s = this.get();

    for (int exp = 0; exp < this.getIntegerBits(); ++exp) {
      final int x = (int) Math.pow(2.0, (double) exp) & this.getMask();
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
    Assert.assertEquals((long) this.getIntegerBits(), (long) s.bits());
  }

  @Test
  public final void testInRange()
  {
    final SerialNumberIntType s = this.get();

    for (int exp = 1; exp < this.getIntegerBits(); ++exp) {
      final int x = (int) Math.pow(2.0, (double) exp) & this.getMask();
      Assert.assertTrue(s.inRange(x));
      Assert.assertFalse(s.inRange(-x));
    }

    final int x = (int) Math.pow(2.0, (double) s.bits());
    Assert.assertFalse(s.inRange(x));
  }

  @Test
  public final void testDistancesPow2()
  {
    final SerialNumberIntType s = this.get();

    for (int exp = 1; exp < this.getIntegerBits(); ++exp) {
      this.log().debug("exp: {}", Integer.valueOf(exp));

      final int x = (int) Math.pow(2.0, (double) exp) & this.getMask();
      final int y = (int) Math.pow(2.0, (double) (exp - 1)) & this.getMask();
      final int d0 = s.distance(x, y);
      final int d1 = s.distance(y, x);

      this.log().debug(
        "distance: {} {} -> {}",
        Integer.valueOf(x),
        Integer.valueOf(y),
        Integer.valueOf(d0));
      this.log().debug(
        "distance: {} {} -> {}",
        Integer.valueOf(y),
        Integer.valueOf(x),
        Integer.valueOf(d1));

      Assert.assertTrue(Math.abs(d0) < this.getLargestDistance());
      Assert.assertTrue(Math.abs(d1) < this.getLargestDistance());
      Assert.assertEquals((long) d0, (long) -y);
      Assert.assertEquals((long) d1, (long) y);

      this.showDistance(x, y, d0);
      this.showDistance(y, x, d1);
    }
  }

  @Test
  public final void testWrap()
  {
    final SerialNumberIntType s = this.get();

    int curr = this.getNearUpper();
    for (int index = 0; index < 6; ++index) {
      final int next = s.add(curr, 1);
      final int distance_curr_next = s.distance(curr, next);
      final int distance_next_curr = s.distance(next, curr);

      this.log().debug(
        "distance: curr {} next {} -> {}",
        Integer.valueOf(curr),
        Integer.valueOf(next),
        Integer.valueOf(distance_curr_next));

      this.log().debug(
        "distance: next {} curr {} -> {}",
        Integer.valueOf(next),
        Integer.valueOf(curr),
        Integer.valueOf(distance_next_curr));

      Assert.assertEquals(1, distance_curr_next);
      Assert.assertEquals(-1, distance_next_curr);
      curr = next;

      this.log().debug("--");
    }
  }
}
