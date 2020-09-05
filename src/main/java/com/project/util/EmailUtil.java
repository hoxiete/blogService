package com.project.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;

import java.util.Random;

/**
 * <pre>
 * 描述：ab邮件工具
 * 作者:aschs
 * 邮箱:aschs@qq.com
 * 日期:2018年9月13日
 * 版权:summer
 * </pre>
 */
public class EmailUtil {

	private static MailAccount account;

	private static final String host = "smtp.qq.com";
	private static final Integer port = 465;
	private static final boolean isSsl = true;
	private static final String nickName = "787326576@qq.com";
	private static final String address = "787326576@qq.com";
	private static final String password = "dcrjhjnklnpebcfa";
//	private static final String password = "wchvkujdcmowbcgg";

	/**
	 * <pre>
	 * 用ab的邮箱发邮件
	 * </pre>
	 * 
	 * @param email
	 *            目标邮件 eg:aschs@qq.com
	 * @param subject
	 *            主题
	 * @param content
	 *            内容（内容支持html）
	 */
	public static void send(String email, String subject, String content) {
		MailUtil.send(account(), CollUtil.newArrayList(email), subject, content, true);
	}
	
	private static  MailAccount account() {
		if(account != null) return account;
		
		MailAccount mailAccount = new MailAccount();
		mailAccount.setHost(host);
		mailAccount.setPort(port);
		mailAccount.setFrom(address);
		mailAccount.setUser(nickName);
		mailAccount.setPass(password);
		mailAccount.setSslEnable(isSsl);
		setAccount(mailAccount);
		return mailAccount;
	}
	public static String getRandomNumCode(int number){
		String codeNum = "";
		int [] numbers = {0,1,2,3,4,5,6,7,8,9};
		Random random = new Random();
		for (int i = 0; i < number; i++) {
			//目的是产生足够随机的数，避免产生的数字重复率高的问题
			int next = random.nextInt(10000);
			codeNum+=numbers[next%10];
		}
		return codeNum;
	}
	
	/**
	 * spring boot项目启动的时候设置参数
	 * @param account
	 */
	public static void setAccount(MailAccount account) {
		EmailUtil.account = account;
	}
}
