package cn.itcast.test;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class JedisTest {
	@Test
	public void jedisTest() {
		Jedis jedis = new Jedis("localhost");
		jedis.set("email", "163");
		String string = jedis.get("email");
		System.out.println(string);
		jedis.close();
	}
}
