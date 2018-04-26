package com.company.email;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SenderXml {
//	public static void main(String[] args) throws Exception, IOException {
//		SenderXml xml = new SenderXml();
//		xml.getXmlData("security");
//		System.out.println(xml.data);
//	}

	final public static String USERNAME = "username";
	final public static String PASSWORD = "password";
	final public static String NICKNAME = "nickname";
	final public static String EMAIL = "email";
	final public static String HOST = "host";
	final public static String PORT = "port";
	
	public SenderXml() {
		// TODO Auto-generated constructor stub
		this("sender.xml");
	}
	private String xml;
	public SenderXml(String xml){
		this.xml = xml;
		this.init();
	}
	private void init(){
		this.xmlPath = getClassPath(SenderXml.class) + xml;
	}
	/**
	 * 读取XML中ID为某个值的sender
	 * @param id sender ID 如 "security"
	 * @return Map<String,String> 包含了sender属性的MAP  
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public Map<String, String> getXmlData(String id) {
		Node sender = getSender(id);
		return putToMap(sender);
	}
	
	public void setXml(String xml) {
		this.xml = xml;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}
	
	private String xmlPath;
	
	public Node getSender(String id) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			db = dbf.newDocumentBuilder();
			Document document = db.parse(xmlPath);
			NodeList senders = document.getElementsByTagName("sender");
			for (int i = 0; i < senders.getLength(); i++) {
				Node t = senders.item(i);
				NamedNodeMap attrs = t.getAttributes();
				if (attrs.getNamedItem("id").getNodeValue().equals(id)) {
					return t;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private Map<String, String> data;

	/**
	 * 得到之前读取过的sender数据
	 * TODO Do something..
	 * @return Map<String,String>  sender 数据
	 * @author Mr.Wang
	 * @date 2018年4月26日
	 */
	public Map<String, String> getOldData() {
		return data;
	}

	public Map<String, String> putToMap(Node security) {
		if (security == null)
			return null;
		try {
			data = new HashMap<String, String>();
			if (security != null) {
				NodeList childs = security.getChildNodes();
				for (int i = 0; i < childs.getLength(); i++) {
					// 获取了element类型节点的节点名
					if (childs.item(i).getNodeType() == Node.ELEMENT_NODE) {
						data.put(childs.item(i).getNodeName(), childs.item(i)
								.getFirstChild().getNodeValue());
						// 获取了element类型节点的节点值em.out.println(childs.item(i).getTextContent().trim());
						// System.out.println("--节点值是：
						// childNodes.item(k).getTextContent());
					}
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return data;
	}

	public String getClassPath(Class<?> cla) {
		String path = null;
			try {
				path = cla.getClassLoader().getResource("").toURI().getPath();
				if(new File(path + xml).exists())return path;
				else throw new FileNotFoundException();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				File directory = new File("");
				path = directory.getAbsolutePath() + File.separator;
			}
		return path;
	}
}
