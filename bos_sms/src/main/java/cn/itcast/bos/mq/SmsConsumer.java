package cn.itcast.bos.mq;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.springframework.stereotype.Service;

@Service
public class SmsConsumer implements MessageListener {

	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		MapMessage mapMessage = (MapMessage) message;
		
		//发送短信
		
		//result
		String result = "000/abcdef";
		if (result.startsWith("000")) {
			try {
				System.out.println("短信发送成功" + "手机号： " + mapMessage.getString("telephone") + " // " + mapMessage.getString("code"));
			} catch (JMSException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("短信发送失败， 信息码：" + result);
		}
	}

}
