package utils;

import redis.clients.jedis.Jedis;

public class RedisHandler {

    private static final String HOST = "localhost";
    private static final int PORT = 6379;

    public static void redisTest() {
        try (Jedis jedis = new Jedis(HOST, PORT)) {
            String response = jedis.ping();
            System.out.println("Ping Response: " + response);

            jedis.set("mykey", "Hello Redis!");
            String value = jedis.get("mykey");
            System.out.println("Stored value: " + value);
        }
    }

    public static boolean setKey(String key, String value) {
        try (Jedis jedis = new Jedis(HOST, PORT)) {
            return "OK".equals(jedis.set(key, value));
        }
    }

    public static String getKey(String key) {
        try (Jedis jedis = new Jedis(HOST, PORT)) {
            return jedis.get(key);
        }
    }

    
    public static boolean expireKey(String key,int seconds) {
    	try (Jedis jedis = new Jedis(HOST, PORT)) {
            jedis.expire(key,seconds);
            return true;
        }
    }
}
