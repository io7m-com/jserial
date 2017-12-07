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

import com.io7m.jserial.core.SerialNumber24;
import com.io7m.jserial.core.SerialNumberIntType;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SerialNumber24Test extends SerialNumberIntContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SerialNumber24Test.class);
  }

  @Override
  protected SerialNumberIntType get()
  {
    return SerialNumber24.get();
  }

  @Override
  protected Logger log()
  {
    return LOG;
  }

  @Override
  protected int integerBits()
  {
    return 24;
  }

  @Override
  protected int nearLargestValue()
  {
    return 16777215 - 3;
  }

  @Test
  public void testDifficultValues()
  {
    final SerialNumberIntType s = this.get();
    Assert.assertEquals(0, s.distance(0, 0));
    Assert.assertEquals(1, s.distance(16777215, 0));
    Assert.assertEquals(2, s.distance(16777214, 0));
    Assert.assertEquals(3, s.distance(16777213, 0));
    Assert.assertEquals(4, s.distance(16777212, 0));
    Assert.assertEquals(5, s.distance(16777211, 0));
  }
}
