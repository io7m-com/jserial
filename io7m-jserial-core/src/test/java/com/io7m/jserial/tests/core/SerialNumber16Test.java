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

import com.io7m.jserial.core.SerialNumber16;
import com.io7m.junreachable.UnreachableCodeException;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class SerialNumber16Test
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SerialNumber16Test.class);
  }

  @Rule public final ExpectedException expected = ExpectedException.none();

  private static void execNoArgPrivateConstructor(final String name)
    throws
    ClassNotFoundException,
    InstantiationException,
    IllegalAccessException
  {
    try {
      final Class<?> c = Class.forName(name);
      final Constructor<?>[] cons = c.getDeclaredConstructors();
      cons[0].setAccessible(true);
      cons[0].newInstance();
    } catch (final InvocationTargetException e) {
      throw (UnreachableCodeException) e.getCause();
    }
  }

  @Test public void testUnreachable() throws Exception
  {
    this.expected.expect(UnreachableCodeException.class);
    SerialNumber16Test.execNoArgPrivateConstructor(
      "com.io7m.jserial.core.SerialNumber16");
  }

  @Test
  public void testDistances()
  {
    final int s1 = 1;

    Assert.assertEquals(
      32767L, (long) SerialNumber16.distanceBetween(s1, 32768));
    Assert.assertEquals(
      32766L, (long) SerialNumber16.distanceBetween(s1, 32767));
    Assert.assertEquals(
      0L, (long) SerialNumber16.distanceBetween(s1, 1));
    Assert.assertEquals(
      1L, (long) SerialNumber16.distanceBetween(s1, 2));
    Assert.assertEquals(
      -1L, (long) SerialNumber16.distanceBetween(s1, 0));
    Assert.assertEquals(
      -2L, (long) SerialNumber16.distanceBetween(s1, 65535));
    Assert.assertEquals(
      -32767L, (long) SerialNumber16.distanceBetween(s1, 32770));
    Assert.assertEquals(
      -32768L, (long) SerialNumber16.distanceBetween(s1, 32769));
  }

  @Test public void testAddition()
  {
    final int i1 = 12345;
    final int i2 = 12346;
    final int i3 = 65535;
    final int i4 = 0;
    final int i5 = 1;

    Assert.assertEquals(
      (long) i2, (long) SerialNumber16.add(i1, i5));
    Assert.assertEquals(
      (long) i4, (long) SerialNumber16.add(i3, 1));
    Assert.assertEquals(
      (long) i4, (long) SerialNumber16.add(i3, i5));
    Assert.assertEquals(
      (long) i3, (long) SerialNumber16.add(i4, -1));
    Assert.assertEquals(
      (long) i5, (long) SerialNumber16.add(i3, SerialNumber16.add(i5, i5)));
    Assert.assertEquals(
      (long) i5, (long) SerialNumber16.add(i3, 2));
  }

  @Test public void testRange()
  {
    for (int index = 0; index <= 0xffff; ++index) {
      Assert.assertTrue(SerialNumber16.inRange(index));
    }
  }

  @Test public void testEquals()
  {
    for (int index = 0; index <= 0xffff; ++index) {
      Assert.assertTrue(SerialNumber16.equals(index, index));
      Assert.assertTrue(SerialNumber16.greaterThanOrEqual(index, index));
      Assert.assertTrue(SerialNumber16.lessThanOrEqual(index, index));
      Assert.assertFalse(SerialNumber16.equals(index, SerialNumber16.add(index, 1)));
    }
  }

  @Test public void testIncrement()
  {
    for (int index = 0; index <= 0xffff; ++index) {
      Assert.assertTrue(
        SerialNumber16.equals(
          SerialNumber16.add(index, 1),
          SerialNumber16.increment(index)));
    }
  }

  @Test public void testOrderingEdge()
  {
    LOG.debug("testOrderingEdge: {}",
              Integer.valueOf(SerialNumber16.distanceBetween(0xffff, 0)));

    Assert.assertTrue(SerialNumber16.lessThan(0xffff, 0));
  }

  @Test public void testOrderingShort()
  {
    for (int index = 0; index <= 0xffff; ++index) {
      final int next = SerialNumber16.add(index, 1);

      SerialNumber16Test.LOG.debug(
        "ordering: {}, {}",
        Integer.valueOf(index),
        Integer.valueOf(next));

      Assert.assertTrue(
        SerialNumber16.greaterThan(next, index));
      Assert.assertTrue(
        SerialNumber16.greaterThanOrEqual(next, index));

      Assert.assertTrue(
        SerialNumber16.lessThan(index, next));
      Assert.assertTrue(
        SerialNumber16.lessThanOrEqual(index, next));
    }
  }

  @Test public void testOrderingCases()
  {
    final int sFFFF = 0xFFFF;
    final int s0000 = 0;
    final int s0001 = 1;
    final int s7FFF = 0x7FFF;
    final int s8000 = 0x8000;
    final int s8001 = 0x8001;

    Assert.assertEquals(
      32767L, (long) SerialNumber16.distanceBetween(s0000, 0x7FFF));
    Assert.assertEquals(
      -32768L, (long) SerialNumber16.distanceBetween(s0000, 0x8000));
    Assert.assertEquals(
      -32767L, (long) SerialNumber16.distanceBetween(s0000, 0x8001));

    Assert.assertEquals(
      32767L, (long) SerialNumber16.distanceBetween(s8000, 0xFFFF));
    Assert.assertEquals(
      -32768L, (long) SerialNumber16.distanceBetween(s8000, 0));
    Assert.assertEquals(
      -32767L, (long) SerialNumber16.distanceBetween(s8000, 1));

    Assert.assertTrue(SerialNumber16.lessThan(s0000, s7FFF));
    Assert.assertFalse(SerialNumber16.greaterThan(s0000, s7FFF));
    Assert.assertFalse(SerialNumber16.lessThan(s0000, s8000));
    Assert.assertTrue(SerialNumber16.greaterThan(s0000, s8000));
    Assert.assertFalse(SerialNumber16.lessThan(s0000, s8001));
    Assert.assertTrue(SerialNumber16.greaterThan(s0000, s8001));

    Assert.assertTrue(SerialNumber16.lessThan(s8000, sFFFF));
    Assert.assertFalse(SerialNumber16.greaterThan(s8000, sFFFF));
    Assert.assertFalse(SerialNumber16.lessThan(s8000, s0000));
    Assert.assertTrue(SerialNumber16.greaterThan(s8000, s0000));
    Assert.assertFalse(SerialNumber16.lessThan(s8000, s0001));
    Assert.assertTrue(SerialNumber16.greaterThan(s8000, s0001));

    Assert.assertTrue(SerialNumber16.greaterThan(s0000, s8000));
    Assert.assertTrue(SerialNumber16.greaterThan(s8000, s0000));

    Assert.assertFalse(SerialNumber16.lessThan(s0000, s8000));
    Assert.assertFalse(SerialNumber16.lessThan(s8000, s0000));
  }
}
