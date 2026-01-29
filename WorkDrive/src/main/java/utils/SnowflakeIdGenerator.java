package utils;

public class SnowflakeIdGenerator {

    // Custom epoch: 2020-01-01
    private static final long EPOCH = 1577836800000L;

    private static final long MACHINE_ID_BITS = 10L;
    private static final long SEQUENCE_BITS   = 12L;

    private static final long MAX_MACHINE_ID = (1L << MACHINE_ID_BITS) - 1;
    private static final long MAX_SEQUENCE   = (1L << SEQUENCE_BITS) - 1;

    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long TIMESTAMP_SHIFT  = SEQUENCE_BITS + MACHINE_ID_BITS;

    // Singleton instance
    private static final SnowflakeIdGenerator INSTANCE;

    static {
        long machineId = Long.parseLong(
                System.getProperty("machine.id", "0")
        );

        if (machineId < 0 || machineId > MAX_MACHINE_ID) {
            throw new IllegalStateException("Invalid machine.id");
        }

        INSTANCE = new SnowflakeIdGenerator(machineId);
    }

    private final long machineId;
    private long lastTimestamp = -1L;
    private long sequence = 0L;

    // Private constructor
    private SnowflakeIdGenerator(long machineId) {
        this.machineId = machineId;
    }

    // Global access point
    public static long nextId() {
        return INSTANCE.generate();
    }

    // Core generation logic
    private synchronized long generate() {
        long currentTimestamp = System.currentTimeMillis();

        if (currentTimestamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE;
            if (sequence == 0) {
                while ((currentTimestamp = System.currentTimeMillis()) <= lastTimestamp) {
                    // wait next millisecond
                }
            }
        } else {
            sequence = 0;
        }

        lastTimestamp = currentTimestamp;

        return ((currentTimestamp - EPOCH) << TIMESTAMP_SHIFT)
                | (machineId << MACHINE_ID_SHIFT)
                | sequence;
    }
}

