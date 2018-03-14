package com.springuni.hermes.core.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class RandomGenerator {

    // The default algorithm NativePRNG uses /dev/random which might block when there is no
    // sufficient entropy in the entropy pool. As a result the system could stall upon startup. Here
    // we chose the NativePRNGNonBlocking algorithm manually, because it uses /dev/urandom instead,
    // which never blocks.
    //
    // References:
    //  https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecureRandom
    //  https://bugs.openjdk.java.net/browse/JDK-8098581
    //  https://major.io/2007/07/01/check-available-entropy-in-linux
    //  https://www.2uo.de/myths-about-urandom/#man-page
    private static final String ALGORITHM = "NativePRNGNonBlocking";

    private static final RandomGenerator INSTANCE = new RandomGenerator();

    private final ThreadLocal<Random> threadLocalRandom;

    private RandomGenerator() {
        threadLocalRandom = ThreadLocal.withInitial(this::createSecureRandom);
    }

    private Random createSecureRandom() {
        SecureRandom randomGenerator;
        try {
            randomGenerator = SecureRandom.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException nae) {
            log.warn("Couldn't create strong secure random generator; reason: {}.",
                    nae.getMessage());
            randomGenerator = new SecureRandom();
        }

        log.info("Created random generator with algorithm {}.", randomGenerator.getAlgorithm());
        return randomGenerator;
    }

    public static RandomGenerator getInstance() {
        return INSTANCE;
    }

    public IntStream ints(long streamSize) {
        return threadLocalRandom.get().ints(streamSize);
    }

    public void nextBytes(byte[] bytes) {
        threadLocalRandom.get().nextBytes(bytes);
    }

    public int nextInt() {
        return threadLocalRandom.get().nextInt();
    }

    public long nextLong() {
        return threadLocalRandom.get().nextLong();
    }

}
