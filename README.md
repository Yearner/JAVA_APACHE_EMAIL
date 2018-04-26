	//Java JDK 1.6
	//sender.xml 默认在classes目录找,找不到在根目录找
	public static void main(String[] args) {
		// /*分开调用*/
		// //用户内置html代码
		String html = getHtml("845******", "公司名称", "3FE5CF");
		sendEmail("845******@qq.com", "kit", "标题", html, Sender.SECURITY);
		 // Map<String, String> params = new HashMap<String, String>();
		 // //用url请求html代码
		 // String html1 = getHtml("http://localhost:8080/",params);
		// // sendEmail("84****@qq.com", "kit", "标题", html1,
		// Sender.SECURITY);
		// // //验证码邮件测试
		// // System.out.println(MyEmail.sendCaptchaEmail("845******@qq.com",
		// "kid", "标题0", "公司名称", "F45D4S", Sender.SECURITY));
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