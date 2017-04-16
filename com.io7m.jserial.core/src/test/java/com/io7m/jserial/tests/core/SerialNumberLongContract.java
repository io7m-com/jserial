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
    this.getLog().debug(
      "distance({} [{}], {} [{}]) = {} [{}]",
      Long.valueOf(s0),
      Long.toUnsignedString(s0, 16),
      Long.valueOf(s1),
      Long.toUnsignedString(s1, 16),
      Long.valueOf(d),
      Long.toUnsignedString(d, 16));
  }

  protected abstract SerialNumberLongType get();

  protected abstract Logger getLog();

  protected abstract long getIntegerBits();

  private long getMask()
  {
    return (1L << this.getIntegerBits()) - 1L;
  }

  private long getLargestDistance()
  {
    return (1L << this.getIntegerBits() - 1L) - 1L;
  }

  @Test
  public final void testDistances0_Pow2()
  {
    final SerialNumberLongType s = this.get();

    for (long exp = 0L; exp < this.getIntegerBits(); ++exp) {
      final long raw0 = (long) Math.pow(2.0, (double) exp);
      final long x = raw0 & this.getMask();
      final long d0 = s.distance(0L, x);
      final long d1 = s.distance(0L, x + 1L);

      this.showDistance(0L, x, d0);
      this.showDistance(0L, x + 1L, d1);

      if (exp < this.getIntegerBits() - 1L) {
        Assert.assertEquals(x, d0);
        Assert.assertEquals(x + 1L, d1);
      } else {
        Assert.assertEquals(-1L, d0);
        Assert.assertEquals(-2L, d1);
      }
    }
  }

  @Test
  public final void testCompareOrder()
  {
    final SerialNumberLongType s = this.get();

    for (long exp = 0L; exp < this.getIntegerBits(); ++exp) {
      final long raw0 = (long) Math.pow(2.0, (double) exp);
      final long x = raw0 & this.getMask();
      final long d0 = s.distance(0L, x);
      final long d1 = s.distance(0L, x + 1L);

      this.showDistance(0L, x, d0);
      this.showDistance(0L, x + 1L, d1);

      Assert.assertEquals(0L, s.compare(x, x));

      if (exp < this.getIntegerBits() - 1L) {
        Assert.assertEquals(x, d0);
        Assert.assertEquals(x + 1L, d1);
      } else {
        Assert.assertEquals(-1L, d0);
        Assert.assertEquals(-2L, d1);
      }
    }
  }

  @Test
  public final void testAddOrder()
  {
    final SerialNumberLongType s = this.get();

    for (long exp = 0L; exp < this.getIntegerBits(); ++exp) {
      final long raw0 = (long) Math.pow(2.0, (double) exp);
      final long x = raw0 & this.getMask();
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
    Assert.assertEquals(this.getIntegerBits(), (long) s.bits());
  }

  @Test
  public final void testInRange()
  {
    final SerialNumberLongType s = this.get();

    for (long exp = 1L; exp < this.getIntegerBits(); ++exp) {
      final long raw = (long) Math.pow(2.0, (double) exp);
      final long x = raw & this.getMask();
      Assert.assertTrue(s.inRange(x));
      Assert.assertFalse(s.inRange(-x));
    }

    final long x = (long) Math.pow(2.0, (double) s.bits());
    Assert.assertFalse(s.inRange(x));
  }

  @Test
  public final void testDistancesPow2()
  {
    final SerialNumberLongType s = this.get();

    for (long exp = 1L; exp < this.getIntegerBits(); ++exp) {
      final long raw0 = (long) Math.pow(2.0, (double) exp);
      final long raw1 = (long) Math.pow(2.0, (double) (exp - 1L));
      final long x = raw0 & this.getMask();
      final long y = raw1 & this.getMask();
      final long d0 = s.distance(x, y);
      final long d1 = s.distance(y, x);

      Assert.assertTrue(Math.abs(d0) < this.getLargestDistance());
      Assert.assertTrue(Math.abs(d1) < this.getLargestDistance());
      Assert.assertEquals(d0, -y);
      Assert.assertEquals(d1, y);

      this.showDistance(x, y, d0);
      this.showDistance(y, x, d1);
    }
  }
}
