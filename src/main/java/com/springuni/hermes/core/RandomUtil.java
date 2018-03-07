package com.springuni.hermes.core;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RandomUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RandomUtil.class);

    // The default algorithm NativePRNG uses /dev/random which might block when there is no
    // sufficient entropy in the entropy pool. As a result the system could stall. Here we chose the
    // NativePRNGNonBlocking algorithm manually, because it uses /dev/urandom instead, which never blocks.
    //
    // References:
    //  https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#SecureRandom
    //  https://bugs.openjdk.java.net/browse/JDK-8098581
    //  https://major.io/2007/07/01/check-available-entropy-in-linux
    //  https://www.2uo.de/myths-about-urandom/#man-page
    private static final String ALGORITHM = "NativePRNGNonBlocking";

    private static final ThreadLocal<Random> THREAD_LOCAL_RANDOM;

    static {
        THREAD_LOCAL_RANDOM = ThreadLocal.withInitial(RandomUtil::createSecureRandom);
    }

    public static IntStream ints(long streamSize) {
        return THREAD_LOCAL_RANDOM.get().ints(streamSize);
    }

    public static void nextBytes(byte[] bytes) {
        THREAD_LOCAL_RANDOM.get().nextBytes(bytes);
    }

    public static int nextInt() {
        return THREAD_LOCAL_RANDOM.get().nextInt();
    }

    private static Random createSecureRandom() {
        SecureRandom randomGenerator;
        try {
            randomGenerator = SecureRandom.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException nae) {
            LOGGER.warn("Couldn't create strong secure random generator; reason: {}.", nae.getMessage());
            randomGenerator = new SecureRandom();
        }

        LOGGER.info("Created random generator with algorithm {}.", randomGenerator.getAlgorithm());
        return randomGenerator;
    }

}
