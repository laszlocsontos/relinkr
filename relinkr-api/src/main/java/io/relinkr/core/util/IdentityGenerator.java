package io.relinkr.core.util;

import static java.time.Instant.now;
import static java.time.ZoneOffset.UTC;
import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * Database agnostic unique ID generator inspired by
 * <a href="https://engineering.instagram.com/sharding-ids-at-instagram-1cf5a71e5a5c">
 * Sharding & IDs at Instagram</a>
 *
 * The maximum value generated is 2^53 - 1 in order to maintain compatibility with hashids.
 * <a href="https://github.com/10cella/hashids-java#limitations">Hashids's limitations</a>
 *
 * <p>Going to the most to the least significant bits
 * <ul>
 * <li>the first bit (sign) is always zero</li>
 * <li>the next nine bits are zero</li>
 * <li>the next 40 bits represent the elapsed milliseconds from a custom Epoch (2019-03-01).
 * This one would overflow on 2054-01-01 19:53:47.775</li>
 * <li>the next 14 bits represent a per-thread serial XOR-ed with a per-thread random number</li>
 * </ul>
 *
 * <p>With this technique 16.384 unique IDs can be generated per millisecond.
 */
public final class IdentityGenerator {

  /**
   * Custom Epoch (2018-03-01).
   */
  public static final Instant EPOCH = LocalDateTime.of(2019, 3, 1, 0, 0, 0, 0).toInstant(UTC);

  private static final IdentityGenerator INSTANCE = new IdentityGenerator();

  private final ThreadLocal<Serial> threadLocalSerial;

  private IdentityGenerator(RandomGenerator randomGenerator) {
    threadLocalSerial = ThreadLocal.withInitial(
        () -> new Serial(randomGenerator.nextInt(), randomGenerator.nextInt())
    );
  }

  private IdentityGenerator() {
    this(RandomGenerator.getInstance());
  }

  public static IdentityGenerator getInstance() {
    return INSTANCE;
  }

  /**
   * Extracts the timestamp parts as an {@link Instant} from the given ID.
   *
   * @param id ID
   * @return an {@link Instant}
   */
  public static Instant extractInstant(long id) {
    long time = (id & 0x7ffffffffc000L) >>> 14;
    return EPOCH.plusMillis(time);
  }

  static long doGenerate(long time, int serial) {
    return (time & 0xffffffffffL) << 14 | (serial & 0x3fff);
  }

  /**
   * Generates a new unique ID for the given shard.
   *
   * @return a new unique ID
   */
  public long generate() {
    long time = MILLIS.between(EPOCH, now());
    int serial = threadLocalSerial.get().increment();
    return doGenerate(time, serial);
  }

  static class Serial {

    final int mask;
    int value;

    Serial(int value, int mask) {
      this.value = value;
      this.mask = mask;
    }

    int increment() {
      return (value++ ^ mask);
    }

  }

}
