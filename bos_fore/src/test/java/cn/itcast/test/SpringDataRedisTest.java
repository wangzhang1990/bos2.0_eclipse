package cn.itcast.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class SpringDataRedisTest {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Test
	public void tempTest() {
		//redisTemplate.opsForValue().set("size", "xxxl", 30, TimeUnit.SECONDS);
		String string = redisTemplate.opsForValue().get("15120042000");
		System.out.println(string);
	}
}
