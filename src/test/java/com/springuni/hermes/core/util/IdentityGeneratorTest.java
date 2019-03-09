package com.springuni.hermes.core.util;

import static com.springuni.hermes.core.util.IdentityGenerator.EPOCH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class IdentityGeneratorTest {

    private static final long TIME_DIFF = 10000;
    private static final Instant INSTANT = EPOCH.plusMillis(TIME_DIFF);

    private IdentityGenerator identityGenerator = IdentityGenerator.getInstance();

    @Test
    public void testCollision() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(100);

        final int NUM_IDS = 1_000_000;
        final ConcurrentMap<Long, AtomicInteger> collisionMap = new ConcurrentHashMap<>(NUM_IDS);

        for (int i = 0; i < NUM_IDS; i++) {
            executorService.submit(() -> {
                long id = identityGenerator.generate();

                AtomicInteger collisionCount = collisionMap.get(id);
                if (collisionCount == null) {
                    collisionCount = new AtomicInteger(0);
                    AtomicInteger oldCollisionCount = collisionMap.putIfAbsent(id, collisionCount);
                    if (oldCollisionCount != null) {
                        collisionCount.addAndGet(oldCollisionCount.get());
                    }
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        double uniquePercent = collisionMap.size() * 100.0 / NUM_IDS;
        log.info("{}% of all IDs are unique.", uniquePercent);
    }

    @Test
    public void testDoGenerate_withSerial() {
        long id = IdentityGenerator.doGenerate(0L, 1);
        assertEquals(1L, id);
    }

    @Test
    public void testDoGenerate_withTime() {
        long id = IdentityGenerator.doGenerate(1L, 0);
        assertEquals((long) 1L << 14, id);
    }

    @Test
    public void testExtractInstant() {
        long id = IdentityGenerator.doGenerate(TIME_DIFF, 10);
        Instant instant = IdentityGenerator.extractInstant(id);
        assertEquals(INSTANT.getEpochSecond(), instant.getEpochSecond());
    }

    @Test
    public void testGenerate() {
        long id = identityGenerator.generate();
        assertNotEquals(0, id);
        log.info("Generated: {}", id);
    }

}
