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

package com.io7m.jserial.tests.core;

import com.io7m.jserial.core.SerialNumber8;
import com.io7m.jserial.core.SerialNumberIntType;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SerialNumber8Test extends SerialNumberIntContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SerialNumber8Test.class);
  }

  @Override
  protected SerialNumberIntType get()
  {
    return SerialNumber8.get();
  }

  @Override
  protected Logger log()
  {
    return LOG;
  }

  @Override
  protected int integerBits()
  {
    return 8;
  }

  @Override
  protected int nearLargestValue()
  {
    return 255 - 3;
  }

  @Test
  public final void testWrapComplete()
  {
    final SerialNumberIntType s = this.get();

    int curr = 0;
    for (int index = 0; index < 0xff * 2; ++index) {
      final int next = s.add(curr, 1);
      final int distance_curr_next = s.distance(curr, next);
      final int distance_next_curr = s.distance(next, curr);

      this.log().debug(
        "distance: curr {} next {} -> {} (expecting 1)",
        Integer.valueOf(curr),
        Integer.valueOf(next),
        Integer.valueOf(distance_curr_next));

      this.log().debug(
        "distance: next {} curr {} -> {} (expecting -1)",
        Integer.valueOf(next),
        Integer.valueOf(curr),
        Integer.valueOf(distance_next_curr));

      Assert.assertEquals(1, distance_curr_next);
      Assert.assertEquals(-1, distance_next_curr);
      curr = next;

      this.log().debug("--");
    }
  }

  @Test
  public void testDifficultValues()
  {
    final SerialNumberIntType s = this.get();
    Assert.assertEquals(0, s.distance(0, 0));
    Assert.assertEquals(1, s.distance(0xff, 0));
    Assert.assertEquals(2, s.distance(0xfe, 0));
    Assert.assertEquals(3, s.distance(0xfd, 0));
    Assert.assertEquals(4, s.distance(0xfc, 0));
    Assert.assertEquals(5, s.distance(0xfb, 0));
  }

  @Test
  public void testZero255()
  {
    final SerialNumberIntType s = this.get();
    Assert.assertEquals(-1, s.distance(0, 255));
  }

  @Test
  public void test255Zero()
  {
    final SerialNumberIntType s = this.get();
    Assert.assertEquals(1, s.distance(255, 0));
  }
}
