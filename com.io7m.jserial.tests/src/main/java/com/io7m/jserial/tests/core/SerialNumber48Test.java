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

import com.io7m.jserial.core.SerialNumber48;
import com.io7m.jserial.core.SerialNumberLongType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SerialNumber48Test extends SerialNumberLongContract
{
  private static final Logger LOG;

  static {
    LOG = LoggerFactory.getLogger(SerialNumber48Test.class);
  }

  @Override
  protected SerialNumberLongType get()
  {
    return SerialNumber48.get();
  }

  @Override
  protected Logger log()
  {
    return LOG;
  }

  @Override
  protected long integerBits()
  {
    return 48L;
  }

  @Override
  protected long nearLargestValue()
  {
    return 0xffff_ffff_ffffL - 3L;
  }
}
