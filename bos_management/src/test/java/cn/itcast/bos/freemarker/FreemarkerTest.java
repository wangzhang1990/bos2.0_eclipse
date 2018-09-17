package cn.itcast.bos.freemarker;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerTest {
	@Test
	public void demo1() throws IOException, TemplateException {
		Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
		configuration.setDirectoryForTemplateLoading(new File("src/main/webapp/WEB-INF/templates"));
		Template template = configuration.getTemplate("hello.ftl");
		
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("title", "freemarkerDemo");
		hashMap.put("msg", "hello world");
		
		System.out.println();
		template.process(hashMap, new PrintWriter(System.out));
	}
}
