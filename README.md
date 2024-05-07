jserial
===

[![Maven Central](https://img.shields.io/maven-central/v/com.io7m.jserial/com.io7m.jserial.svg?style=flat-square)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.io7m.jserial%22)
[![Maven Central (snapshot)](https://img.shields.io/nexus/s/com.io7m.jserial/com.io7m.jserial?server=https%3A%2F%2Fs01.oss.sonatype.org&style=flat-square)](https://s01.oss.sonatype.org/content/repositories/snapshots/com/io7m/jserial/)
[![Codecov](https://img.shields.io/codecov/c/github/io7m-com/jserial.svg?style=flat-square)](https://codecov.io/gh/io7m-com/jserial)
![Java Version](https://img.shields.io/badge/21-java?label=java&color=007fff)

![com.io7m.jserial](./src/site/resources/jserial.jpg?raw=true)

| JVM | Platform | Status |
|-----|----------|--------|
| OpenJDK (Temurin) Current | Linux | [![Build (OpenJDK (Temurin) Current, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/jserial/main.linux.temurin.current.yml)](https://www.github.com/io7m-com/jserial/actions?query=workflow%3Amain.linux.temurin.current)|
| OpenJDK (Temurin) LTS | Linux | [![Build (OpenJDK (Temurin) LTS, Linux)](https://img.shields.io/github/actions/workflow/status/io7m-com/jserial/main.linux.temurin.lts.yml)](https://www.github.com/io7m-com/jserial/actions?query=workflow%3Amain.linux.temurin.lts)|
| OpenJDK (Temurin) Current | Windows | [![Build (OpenJDK (Temurin) Current, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/jserial/main.windows.temurin.current.yml)](https://www.github.com/io7m-com/jserial/actions?query=workflow%3Amain.windows.temurin.current)|
| OpenJDK (Temurin) LTS | Windows | [![Build (OpenJDK (Temurin) LTS, Windows)](https://img.shields.io/github/actions/workflow/status/io7m-com/jserial/main.windows.temurin.lts.yml)](https://www.github.com/io7m-com/jserial/actions?query=workflow%3Amain.windows.temurin.lts)|

## jserial

A Java implementation of enhanced
[serial number arithmetic](https://en.wikipedia.org/wiki/Serial_number_arithmetic).

## Features

* 8-bit serial number arithmetic
* 16-bit serial number arithmetic
* 24-bit serial number arithmetic
* 32-bit serial number arithmetic
* 40-bit serial number arithmetic
* 48-bit serial number arithmetic
* 56-bit serial number arithmetic
* 62-bit serial number arithmetic
* High coverage test suite.
* [OSGi-ready](https://www.osgi.org/)
* [JPMS-ready](https://en.wikipedia.org/wiki/Java_Platform_Module_System)
* ISC license.

## Motivation

Many communications protocols use fixed-sized fields to store
[sequence numbers](https://en.wikipedia.org/wiki/Sequence_number).
Unfortunately, given that the fields are of a fixed size, it is inevitable
that the sequence numbers will eventually reach the maximum possible value
that can be stored in fields of that size and, using unsigned arithmetic,
will then wrap around to `0`. This can cause serious problems for systems
tracking sequence numbers as the numbers are no longer monotonically
increasing.

Using [serial number arithmetic](https://en.wikipedia.org/wiki/Serial_number_arithmetic),
it's possible to have sequence numbers that can wrap around indefinitely, but
that the application can still treat as being conceptually monotonically
increasing.

## Usage

Choose an implementation based on the number of bits used to store serial
numbers in your intended protocol. Most protocols will want to use 32 bits:

```
var s = SerialNumber32.get();
```

Given a starting number `x`, get the next number in the sequence according
to serial number arithmetic at the specified implementation size:

```
var y = s.add(x, 1L);
```

The `SerialNumberLongType` and `SerialNumberIntType` interfaces expose
numerous methods to increment and compare numbers. In the above code, the
value `y` can be compared with `x`, and the `compare` implementation will
indicate that `y` is greater than `x`, even if `y` has wrapped around to zero.

