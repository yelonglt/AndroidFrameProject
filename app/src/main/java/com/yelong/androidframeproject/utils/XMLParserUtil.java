package com.yelong.androidframeproject.utils;

import android.util.Xml;

import com.yelong.androidframeproject.model.Book;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;


/**
 * Android XML解析工具，主要SAX解析器、DOM解析器和PULL解析器
 * 
 * @author 800hr：yelong
 * 
 *         2015-5-14
 */
public class XMLParserUtil {

	/**
	 * SAX(Simple API forXML)解析器是一种基于事件的解析器，它的核心是事件处理模式，主要是围绕着事件源以及事件处理器来工作的。
	 * 当事件源产生事件后 ，调用事件处理器相应的处理方法，一个事件就可以得到处理。
	 * 在事件源调用事件处理器中特定方法的时候，还要传递给事件处理器相应事件的状态信息， 这样事件处理器才能够根据提供的事件信息来决定自己的行为。
	 * 
	 * SAX解析器的优点是解析速度快，占用内存少。非常适合在Android移动设备中使用
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Book> useSaxParser(InputStream is) throws Exception {
		// 取得SAXParserFactory实例
		SAXParserFactory factory = SAXParserFactory.newInstance();
		// 从factory获取SAXParser实例
		SAXParser parser = factory.newSAXParser();
		SaxHandler saxHandler = new SaxHandler();
		parser.parse(is, saxHandler);
		return saxHandler.getBooks();
	}

	/**
	 * 序列化Book对象集合，得到XML形式的字符串
	 * 
	 * @param books
	 * @return
	 * @throws Exception
	 */
	public static String serializeXMLBySax(ArrayList<Book> books)
			throws Exception {
		// 取得SAXTransformerFactory实例
		SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory
				.newInstance();
		// 从factory获取TransformerHandler实例
		TransformerHandler handler = factory.newTransformerHandler();
		// 从handler获取Transformer实例
		Transformer transformer = handler.getTransformer();
		// 设置输出采用的编码方式
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// 是否自动添加额外的空白
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// 是否忽略XML声明
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		handler.setResult(result);

		// 代表命名空间的URI 当URI无值时 须置为空字符串
		String uri = "";
		// 命名空间的本地名称(不包含前缀) 当没有进行命名空间处理时 须置为空字符串
		String localName = "";

		handler.startDocument();
		handler.startElement(uri, localName, "books", null);
		// 负责存放元素的属性信息
		AttributesImpl attrs = new AttributesImpl();
		char[] ch = null;

		for (Book book : books) {
			attrs.clear();
			// 添加一个名为id的属性(type影响不大,这里设为string)
			attrs.addAttribute(uri, localName, "id", "String",
					String.valueOf(book.getId()));

			// 开始一个book元素，关联上面设定的id属性
			handler.startElement(uri, localName, "book", attrs);

			// 开始一个name元素 没有属性
			handler.startElement(uri, localName, "name", null);
			ch = String.valueOf(book.getName()).toCharArray();
			// 设置name元素的文本节点
			handler.characters(ch, 0, ch.length);
			handler.endElement(uri, localName, "name");

			// 开始一个price元素 没有属性
			handler.startElement(uri, localName, "price", null);
			ch = String.valueOf(book.getPrice()).toCharArray();
			handler.characters(ch, 0, ch.length);
			handler.endElement(uri, localName, "price");

			// 结束一个book元素
			handler.endElement(uri, localName, "book");
		}
		handler.endElement(uri, localName, "books");
		handler.endDocument();

		return writer.toString();
	}

	/**
	 * DOM(Document Objrect Model)是基于树形结构的的节点或信息片段的集合，允许开发人员使用DOM
	 * API遍历XML树、检索所需数据。分析该结构通常需要加载整个文档和构造树形结构，然后才可以检索和更新节点信息。
	 * 
	 * 由于DOM在内存中以树形结构存放，因此检索和更新效率会更高。但是对于特别大的文档，解析和加载整个文档将会很耗资源。
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Book> useDomParser(InputStream is) throws Exception {
		ArrayList<Book> books = new ArrayList<Book>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(is);

		Element rootElement = document.getDocumentElement();
		NodeList items = rootElement.getElementsByTagName("book");
		Book book = null;
		for (int i = 0; i < items.getLength(); i++) {
			book = new Book();
			Node item = items.item(i);

			NodeList properties = item.getChildNodes();
			for (int j = 0; j < properties.getLength(); j++) {
				Node property = properties.item(j);

				String nodeName = property.getNodeName();
				if (nodeName.equals("id")) {
					book.setId(Integer.parseInt(property.getFirstChild()
							.getNodeValue()));
				} else if (nodeName.equals("name")) {
					book.setName(property.getFirstChild().getNodeValue());
				} else if (nodeName.equals("price")) {
					book.setPrice(Float.parseFloat(property.getFirstChild()
							.getNodeValue()));
				}
			}
			books.add(book);
		}
		return books;
	}

	/**
	 * 序列化Book对象集合，得到XML形式的字符串
	 * 
	 * @param books
	 * @return
	 * @throws Exception
	 */
	public static String serializeXMLByDom(ArrayList<Book> books)
			throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.newDocument();

		Element rootElement = document.createElement("books");
		for (Book book : books) {
			Element bookElement = document.createElement("book");
			bookElement.setAttribute("id", book.getId() + "");

			Element nameElement = document.createElement("name");
			nameElement.setTextContent(book.getName());
			bookElement.appendChild(nameElement);

			Element priceElement = document.createElement("price");
			priceElement.setTextContent(book.getPrice() + "");
			bookElement.appendChild(priceElement);

			rootElement.appendChild(bookElement);
		}

		document.appendChild(rootElement);

		// 取得TransformerFactory实例
		TransformerFactory transFactory = TransformerFactory.newInstance();
		// 从transFactory获取Transformer实例
		Transformer transformer = transFactory.newTransformer();
		// 设置输出采用的编码方式
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		// 是否自动添加额外的空白
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// 是否忽略XML声明
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");

		StringWriter writer = new StringWriter();

		// 表明文档来源是document
		Source source = new DOMSource(document);
		// 表明目标结果为writer
		Result result = new StreamResult(writer);
		// 开始转换
		transformer.transform(source, result);
		return writer.toString();
	}

	/**
	 * PULL解析器的运行方式和SAX类似，都是基于事件的模式。不同的是，在PULL解析过程中，我们需要自己获取产生的事件然后做相应的操作，
	 * 而不像SAX那样由处理器触发一种事件的方法，执行我们的代码。PULL解析器小巧轻便，解析速度快，简单易用，非常适合在Android移动设备中使用，
	 * Android系统内部在解析各种XML时也是用PULL解析器。
	 * 
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static ArrayList<Book> usePullParser(InputStream is)
			throws Exception {
		ArrayList<Book> books = null;
		Book book = null;
		// XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
		// XmlPullParser parser=factory.newPullParser();

		// 由android.util.Xml创建一个XmlPullParser实例
		XmlPullParser parser = Xml.newPullParser();
		// 设置输入流 并指明编码方式
		parser.setInput(is, "UTF-8");

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				books = new ArrayList<Book>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("book")) {
					book = new Book();
				} else if (parser.getName().equals("id")) {
					eventType = parser.next();
					book.setId(Integer.parseInt(parser.getText()));
				} else if (parser.getName().equals("name")) {
					eventType = parser.next();
					book.setName(parser.getText());
				} else if (parser.getName().equals("price")) {
					eventType = parser.next();
					book.setPrice(Float.parseFloat(parser.getText()));
				}

				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("book")) {
					books.add(book);
					book = null;
				}

			}
			eventType = parser.next();
		}
		return books;
	}

	/**
	 * 序列化Book对象集合，得到XML形式的字符串
	 * 
	 * @param books
	 * @return
	 * @throws Exception
	 */
	public static String serializeXMLByPull(ArrayList<Book> books)
			throws Exception {
		// 由android.util.Xml创建一个XmlSerializer实例
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		serializer.setOutput(writer);

		serializer.startDocument("UTF-8", true);
		serializer.startTag("", "books");
		for (Book book : books) {
			serializer.startTag("", "book");
			serializer.attribute("", "id", book.getId() + "");

			serializer.startTag("", "name");
			serializer.text(book.getName());
			serializer.endTag("", "name");

			serializer.startTag("", "price");
			serializer.text(book.getPrice() + "");
			serializer.endTag("", "price");

			serializer.endTag("", "book");
		}
		serializer.endTag("", "books");
		serializer.endDocument();

		return writer.toString();
	}

	/**
	 * SAX解析，必须定义自己的事件处理逻辑，重写了DefaultHandler的几个重要的事件方法
	 * 
	 * @author 800hr：yelong
	 * 
	 *         2015-5-14
	 */
	public static class SaxHandler extends DefaultHandler {
		private ArrayList<Book> books;
		private Book book;
		private StringBuilder builder;

		public ArrayList<Book> getBooks() {
			return books;
		}

		@Override
		public void startDocument() throws SAXException {
			super.startDocument();
			books = new ArrayList<Book>();
			builder = new StringBuilder();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if (localName.equals("book")) {
				book = new Book();
			}
			// 将字符长度设置为0 以便重新开始读取元素内的字符节点
			builder.setLength(0);
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			super.characters(ch, start, length);
			// 将读取的字符数组追加到builder中
			builder.append(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			super.endElement(uri, localName, qName);
			if (localName.equals("id")) {
				book.setId(Integer.parseInt(builder.toString()));
			} else if (localName.equals("name")) {
				book.setName(builder.toString());
			} else if (localName.equals("price")) {
				book.setPrice(Float.parseFloat(builder.toString()));
			} else if (localName.equals("book")) {
				books.add(book);
			}
		}

		@Override
		public void endDocument() throws SAXException {
			super.endDocument();
		}

	}

}
