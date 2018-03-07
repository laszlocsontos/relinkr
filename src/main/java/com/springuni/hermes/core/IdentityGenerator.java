package com.springuni.hermes.core;

import static com.springuni.hermes.core.RandomUtil.nextInt;
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
 * <p>Going to the most to the least significant bits
 * <ul>
 * <li>the first bit (sign) is always zero
 * <li>the next 43 bits represent the elapsed milliseconds from a custom Epoch (2017-12-21)
 * <li>the next 20 bits represent a serial number XOR-ed with a per-thread random number
 * </ul>
 *
 * <p>With this technique 1.048.576 unique IDs can be generated per millisecond.
 */
public final class IdentityGenerator {

    /**
     * Custom Epoch (2018-03-01).
     */
    public static final Instant EPOCH = LocalDateTime.of(2018, 03, 01, 0, 0, 0, 0).toInstant(UTC);

    private static final ThreadLocal<Serial> THREAD_LOCAL_SERIAL;

    static {
        THREAD_LOCAL_SERIAL = ThreadLocal.withInitial(() -> new Serial(nextInt(), nextInt()));
    }

    private IdentityGenerator() {
    }

    /**
     * Extracts the timestamp parts as an {@link Instant} from the given ID.
     *
     * @param id ID
     * @return an {@link Instant}
     */
    public static Instant extractInstant(long id) {
        long time = (id & 0x7ffffffffff00000L) >>> 20;
        return EPOCH.plusMillis(time);
    }

    /**
     * Generates a new unique ID for the given shard.
     *
     * @return a new unique ID
     */
    public static long generate() {
        long time = MILLIS.between(EPOCH, now());
        int serial = THREAD_LOCAL_SERIAL.get().increment();
        return doGenerate(time, serial);
    }

    static long doGenerate(long time, int serial) {
        return (time & 0x7ffffffffffL) << 20 | (serial & 0xfffff);
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
