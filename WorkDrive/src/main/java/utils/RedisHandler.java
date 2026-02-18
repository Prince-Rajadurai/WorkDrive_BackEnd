package utils;

import redis.clients.jedis.Jedis;

public class RedisHandler {

	public static void redisTest() {
		try (Jedis jedis = new Jedis("localhost", 6379)) {
			String response = jedis.ping();
			System.out.println("Ping Response: " + response);
			jedis.set("mykey", "Hello Redis!");
			String value = jedis.get("mykey");
			System.out.println("Stored value: " + value);
		}

	}

}
