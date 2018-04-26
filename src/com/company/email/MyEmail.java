package com.company.email;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

/**
 * 对Apache Email 二次封装 ClassName: MyEmail
 * 
 * @author Mr.Wang
 * @date 2018年4月26日
 */
public class MyEmail {
	/**
	 * 发送者集合
	 */
	private static Map<String, EmailSenderUser> senders;
	static {
		init();
	}

	private static void init() {
		senders = new HashMap<String, EmailSenderUser>();
		// 可能账户和邮箱不相同
		SenderXml xml = new SenderXml("sender.xml");
		Map<String, String> sender = xml.getXmlData("security");
		EmailSenderUser security = new EmailSenderUser(
				sender.get(SenderXml.USERNAME), 
				sender.get(SenderXml.PASSWORD),
				sender.get(SenderXml.HOST), 
				Integer.parseInt(sender.get(SenderXml.PORT)), 
				sender.get(SenderXml.NICKNAME),
				sender.get(SenderXml.EMAIL));
		senders.put(Sender.SECURITY.name(), security);
	}

	public static void main(String[] args) {
		// /*分开调用*/
		// //用户内置html代码
		String html = getHtml("845******", "公司名", "3FE5CF");
		sendEmail("845******@qq.com", "kit", "标题", html, Sender.SECURITY);
		// // Map<String, String> params = new HashMap<String, String>();
		// // //用url请求html代码
		// // String html1 = getHtml("http://localhost:8080/",params);
		// // sendEmail("845******@qq.com", "kit", "标题", html1,
		// Sender.SECURITY);
		// // //验证码邮件测试
		// // System.out.println(MyEmail.sendCaptchaEmail("845******@qq.com",
		// "kid", "标题0", "公司名", "F45D4S", Sender.SECURITY));
		// // //自定义html邮件测试
		// // System.out.println(MyEmail.sendEmail("845******@qq.com", "标题1",
		// "<div>你好!</div>", Sender.SECURITY));
		// // //自定义html带用户别名测试
		// // System.out.println(MyEmail.sendEmail("845******@qq.com", "kid",
		// "标题2", "<div>你好</div>", Sender.SECURITY));
		// // //外部html内容邮件测试
		// // System.out.println(MyEmail.sendEmail("845******@qq.com", "标题3",
		// "http://localhost:8080/" , params , Sender.SECURITY));
		// // //外部html内容邮件带用户别名测试
		// // System.out.println(MyEmail.sendEmail("845******@qq.com","kid" ,
		// "f标题4", "http://localhost:8080/" , params , Sender.SECURITY));
	}

	/**
	 * 代表邮件发送者KEY名 ClassName: Sender
	 * 
	 * @Description: TODO
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public enum Sender {
		/**
		 * 代表安全邮箱账户
		 */
		SECURITY
	}

	/**
	 * 发送自定义邮件 , 需要有html代码传入
	 * 
	 * @param targetEmail
	 *            目标邮箱地址
	 * @param title
	 *            标题
	 * @param html
	 *            HTML内容如"<div>内容</div>"
	 * @param sender
	 *            发送者 ,详见 enum.Sender
	 * @return boolean 是否发送成功
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public static boolean sendEmail(String targetEmail, String title,
			String html, Sender sender) {
		return sendEmailBase(targetEmail, null, title, html,
				senders.get(sender.name()));
	}

	/**
	 * 发送验证码邮件 , 需要有html代码传入
	 * 
	 * @param targetEmail
	 *            目标邮箱地址
	 * @param targetName
	 *            代表邮箱别名,用作别名或问候
	 * @param title
	 *            标题
	 * @param html
	 *            HTML内容如<div>内容</div>
	 * @param sender
	 *            发送者 ,详见 enum.Sender
	 * @return boolean 是否发送成功
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public static boolean sendCaptchaEmail(String targetEmail,
			String targetName, String title, String company, String code,
			Sender sender) {
		String html = getHtml(targetName, company, code);
		return sendEmailBase(targetEmail, targetName, title, html,
				senders.get(sender.name()));
	}

	/**
	 * 发送自定义邮件 , 需要有html代码传入
	 * 
	 * @param targetEmail
	 *            目标邮箱地址
	 * @param targetName
	 *            代表邮箱别名(无明显用处)
	 * @param title
	 *            标题
	 * @param html
	 *            HTML内容如"<div>内容</div>"
	 * @param sender
	 *            发送者 ,详见 enum.Sender
	 * @return boolean 是否发送成功
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public static boolean sendEmail(String targetEmail, String targetName,
			String title, String html, Sender sender) {
		return sendEmailBase(targetEmail, targetName, title, html,
				senders.get(sender.name()));
	}

	/**
	 * 发送自定义邮件 , 用一个页面的内容来当做邮件内容
	 * 
	 * @param targetEmail
	 *            目标邮箱地址
	 * @param title
	 *            标题
	 * @param url
	 *            目标页面地址需要 HTTP协议 如 "http://xx.xx
	 * @param params
	 *            参数集合,用于传入 url使用
	 * @param sender
	 *            发送者 ,详见 enum.Sender
	 * @return boolean 是否发送成功
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public static boolean sendEmail(String targetEmail, String title,
			String url, Map<String, String> params, Sender sender) {
		return sendEmailBase(targetEmail, null, title, getHtml(url, params),
				senders.get(sender.name()));
	}

	/**
	 * 发送自定义邮件 , 用一个页面的内容来当做邮件内容
	 * 
	 * @param targetEmail
	 *            目标邮箱地址
	 * @param targetName
	 *            代表邮箱别名(无明显用处)
	 * @param title
	 *            标题
	 * @param url
	 *            目标页面地址需要 HTTP协议 如 "http://xx.xx
	 * @param params
	 *            参数集合,用于传入 url使用
	 * @param sender
	 *            发送者 ,详见 enum.Sender
	 * @return boolean 是否发送成功
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public static boolean sendEmail(String targetEmail, String targetName,
			String title, String url, Map<String, String> params, Sender sender) {
		return sendEmailBase(targetEmail, targetName, title,
				getHtml(url, params), senders.get(sender.name()));
	}

	/**
	 * 邮件发送者用户 ClassName: EmailSenderUser
	 * 
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	static class EmailSenderUser {
		private String username;// 用户名
		private String password;// 密码或授权码
		private String nickname;// 昵称
		private String email;// 邮箱地址
		private String host;// smtp地址
		private int port;// 端口

		/**
		 * 邮件发送者配置
		 * <p>
		 * Description:
		 * </p>
		 * 
		 * @param username
		 *            账号
		 * @param password
		 *            密码
		 * @param host
		 *            邮件服务器地址
		 * @param port
		 *            端口
		 * @param nickname
		 *            昵称
		 * @param email
		 *            发送者邮箱地址
		 */
		public EmailSenderUser(String username, String password, String host,
				int port, String nickname, String email) {
			super();
			this.username = username;
			this.nickname = nickname;
			this.password = password;
			this.email = email;
			this.host = host;
			this.port = port;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public int getPort() {
			return port;
		}

		public void setPort(int port) {
			this.port = port;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getEmail() {
			return email;
		}
		
		@Override
		public String toString() {
			return "{ username:"+ this.username +", password:"+this.password+", nickname:"+this.nickname+", host:"+this.host+", port:"+this.port+", email:"+this.email +"}";
		}
	}

	private static String charset = "utf-8";// 邮件编码

	/**
	 * 发送邮件
	 * 
	 * @param targetEmail
	 *            目标邮箱地址
	 * @param text
	 *            文本内容,可与html,在html之前,尽量任选其一
	 * @param html
	 *            网页html 内容,可与text混用,在text之后,尽量任选其一
	 * @param sender
	 *            发送者
	 * @return boolean 发送是否成功
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	private static boolean sendEmailBase(String targetEmail,
			String targetNickname, String title, String html,
			EmailSenderUser sender) {
		HtmlEmail email = new HtmlEmail();
		email.setHostName(sender.getHost());// 邮件服务器地址
		email.setSmtpPort(465);// 端口
		email.setAuthentication(sender.getUsername(), sender.getPassword());// 账号密码(授权码等于密码,但密码不一定等于授权码)
		email.setSSLOnConnect(true);// SSL加密,使用POST请求
		try {
			if (sender.getNickname() != null)
				email.setFrom(sender.getEmail(), sender.getNickname());// 发送人,和昵称
			else
				email.setFrom(sender.getEmail());// 发送人
				// String cid = email.embed(new File("logo.jpg"),"1");可添加图片
				// email.setHtmlMsg("<div><img src=\"cid:"+cid+"\"></div>");//发送HTML页
			email.setSubject(title);// 标题
			// email.setMsg("这是从JAVA中发出的邮件");// 消息体可用,但最好不要与setHtmlMsg同用
			email.setCharset(charset);// 设置编码
			email.setHtmlMsg(html);// 发送HTML页面内容
									// email.setHtmlMsg().setHtmlMsg().setMsg().setHtmlMsg();....
			if (targetNickname != null)
				email.addTo(targetEmail, targetNickname);// 收件人.收件人昵称
			else
				email.addTo(targetEmail);// 收件人
			email.send();// 发送
			return true;
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 请求某个地址,返回邮件主题html
	 * 
	 * @param url
	 *            地址 如 : http://www.baidu.com/tt , 注意必须包含
	 *            HTTP协议
	 * @param params
	 *            参数集合
	 * @return String 页面html
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public static String getHtml(String url, Map<String, String> params) {
		String param = null;
		if (params != null) {
			Set<String> keys = params.keySet();
			param = "";
			for (Iterator<String> iterator = keys.iterator(); iterator
					.hasNext();) {
				String key = iterator.next();
				param += key + "=" + params.get(key);
				if (iterator.hasNext())
					param += "&";
			}
		}
		return sendGet(url, param);
	}

	/**
	 * 得到邮箱验证码页面HTML
	 * 
	 * @param targetUser
	 *            目标用户昵称,用于 say hello , 如 '123456@qq.com' , 'zhangsan'
	 * @param company
	 *            发送方署名,用于显示发送方署名 如 xx安全团队 , xx公司
	 * @param code
	 *            验证码
	 * @return String html页面代码
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public static String getHtml(String targetUser, String company, String code) {
		StringBuffer sb = new StringBuffer();
		sb.append("<center bgcolor=\"#000000\" style=\"margin: 0; padding: 0; background-color: #67a116;\"><br>");
		sb.append("<table style=\"background-color: #002650;\" bgcolor=\"#002650\">");
		sb.append("<tbody>");
		sb.append("<tr>");
		sb.append("<td style=\"background-color: #002650;\" bgcolor=\"#002650\">");
		sb.append("<table valign=\"top\" style=\"width: 600px; min-width: 600px; border-spacing: 0; border-collapse: collapse; margin: 0 auto; word-wrap: break-word; word-break: break-word; -ms-word-break: break-word; overflow-wrap: break-word; background-color: #002650;\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\" bgcolor=\"#002650\" align=\"center\">");
		sb.append("<tbody>");
		sb.append("<tr>");
		sb.append("<td class=\"body-text-section-padding-0px-15px-10px\" style=\"padding: 0;\" align=\"center\">");
		sb.append("<table width=\"600\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">");
		sb.append("<tbody>");
		sb.append("<tr>");
		sb.append("<td bgcolor=\"#312450\">");
		sb.append("<table style=\"width: 100%; min-width: 100%; border-spacing: 0; border-collapse: collapse; word-wrap: break-word; word-break: break-word; -ms-word-break: break-word; overflow-wrap: break-word;\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">");
		sb.append("<tbody>");
		sb.append("<tr>");
		sb.append("<td class=\"body-text-padding-0px-5p-30px-font-12px-left\" style=\"font-family: 'Open Sans', Frutiger, 'Frutiger Linotype', Univers, 'Helvetica Neue', Helvetica, Arial, 'Gill Sans', 'Gill Sans MT', 'Myriad Pro', Myriad, 'DejaVu Sans Condensed', 'Liberation Sans', 'Nimbus Sans L', 'Malgun Gothic', 'Microsoft YaHei', AppleSDGothicNeo, AppleGothic, Dotum, 'Microsoft JhengHei', 'Hiragino Kaku Gothic Pro', 'Hiragino Kaku Gothic ProN W3', Osaka, メイリオ, Meiryo, 'ＭＳ Ｐゴシック', Calibri, Geneva, Display, Tahoma, Verdana, sans-serif; font-size: 14px; font-weight: 300; line-height: 24px; text-align: left; padding: 0 40px 40px; color: #bbbbbb;\">");
		sb.append("<table width=\"100%\" height=\"\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\">");
		sb.append("<tbody>");
		sb.append("<tr>");
		sb.append("<td style=\"font-family: 'Open Sans', Frutiger, 'Frutiger Linotype', Univers, 'Helvetica Neue', Helvetica, Arial, 'Gill Sans', 'Gill Sans MT', 'Myriad Pro', Myriad, 'DejaVu Sans Condensed', 'Liberation Sans', 'Nimbus Sans L', 'Malgun Gothic', 'Microsoft YaHei', AppleSDGothicNeo, AppleGothic, Dotum, 'Microsoft JhengHei', 'Hiragino Kaku Gothic Pro', 'Hiragino Kaku Gothic ProN W3', Osaka, メイリオ, Meiryo, 'ＭＳ Ｐゴシック', Calibri, Geneva, Display, Tahoma, Verdana, sans-serif;font-weight: 300; font-size: 24px; color: #FFFFFF; line-height: 32px;\">");
		sb.append("<p>以下是您的验证码：</p>");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("</tbody>");
		sb.append("</table>");
		sb.append("<p>");
		sb.append("<em style=\"font-family: 'Open Sans', Frutiger, 'Frutiger Linotype', Univers, 'Helvetica Neue', Helvetica, Arial, 'Gill Sans', 'Gill Sans MT', 'Myriad Pro', Myriad, 'DejaVu Sans Condensed', 'Liberation Sans', 'Nimbus Sans L', 'Malgun Gothic', 'Microsoft YaHei', AppleSDGothicNeo, AppleGothic, Dotum, 'Microsoft JhengHei', 'Hiragino Kaku Gothic Pro', 'Hiragino Kaku Gothic ProN W3', Osaka, メイリオ, Meiryo, 'ＭＳ Ｐゴシック', Calibri, Geneva, Display, Tahoma, Verdana, sans-serif;font-size: 28px; font-weight: 300; color: #3ed500; font-style: normal;\">");
		/* 验证码 */
		sb.append(code);
		sb.append("</em>");
		sb.append("</p>");
		/* 目标用户昵称 */
		sb.append("<p> ");
		sb.append(targetUser);
		sb.append("，您好！</p>");
		sb.append("<p> 我们收到了来自您的验证码请求。请使用上面的验证码验证您的账号邮箱。</p>");
		sb.append("<p> ");
		sb.append("<i>");
		sb.append("<b>请注意：</b>");
		sb.append("该验证码将在10分钟后过期，请尽快验证！");
		sb.append("</i>");
		sb.append("</p>");
		sb.append("<p>");
		sb.append("享受您的历险！<br>");
		/* 公司署名 */
		sb.append(company);
		sb.append("</p>");
		sb.append("</td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></td></tr></tbody></table></center");
		return sb.toString();
	}

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            如 http://www.abc.com/efg/h.html 需要http协议名 发送请求的URL
	 * @param param
	 *            ?后面的参数 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	private static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url + "?" + param;
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// Map<String, List<String>> map = connection.getHeaderFields();
			// 遍历所有的响应头字段
			// for (String key : map.keySet()) {
			// //System.out.println(key + "--->" + map.get(key));
			// }
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}
	
}
