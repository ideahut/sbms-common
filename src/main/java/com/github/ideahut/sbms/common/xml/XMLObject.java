package com.github.ideahut.sbms.common.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLObject implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7885819756167652351L;

	private static final String VERSION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	
	private static final String SEPARATOR = "/";
	
	private String name;
	
	private String value;
	
	private boolean CDATA = false;
	
	private Map<String, List<XMLObject>> child = null;
	
	private Map<String, String> attr = new HashMap<String, String>();
	
	private String version = VERSION;
	
	
	public XMLObject () {}
	
	public XMLObject(String name) {
		this(name, null, false);
	}
	
	public XMLObject(String name, String value) {
		this(name, value, false);
	}
	
	public XMLObject(String name, String value, boolean CDATA) {
		this.name = name;
		this.value = value;
		this.CDATA = CDATA;
	}

	/*
	 * STATIC
	 */
	public static XMLObject parse(String s) throws Exception {
		return parse(new ByteArrayInputStream(s.getBytes("UTF-8")));
	}
	
	public static XMLObject parse(InputStream is) throws Exception {
		return parse(is, VERSION, SEPARATOR);
	}
	
	public static XMLObject parse(InputStream is, final String version, String separator) throws Exception {
		DocumentBuilderFactory factory;
		DocumentBuilder builder;
		Document document = null;
		try {
			factory = DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver() {
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					if (systemId.endsWith(".dtd")) {
						// deactivates all DTDs by giving empty XML docs
						return new InputSource(new ByteArrayInputStream(version.getBytes()));
					} else {
						return null;
					}
				}
			});
			document = builder.parse(is);
			document.getDocumentElement().normalize();
			NodeList nl = document.getChildNodes();
			Element el = null;
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					if (el != null) {
						throw new Exception("Root has more than 1 element");
					}
					el = (Element)n;
				}
			}
			if (el == null) {
				throw new Exception("No root element found");
			}			
			return fillObject(el, version, separator);
		} catch (ParserConfigurationException e) {
			throw new Exception(e.getMessage());
		} catch (SAXException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			throw new Exception(e.getMessage());
		} finally {
			factory = null;
			builder = null;
			document = null;
		}
	}
	
	private static XMLObject fillObject(Node node, String version, String separator) {
		if (node.getNodeType() != Node.ELEMENT_NODE) {
			return null;
		}
		XMLObject o = new XMLObject();
		o.setName(node.getNodeName());
		o.version = version;
		NamedNodeMap m = node.getAttributes();
		if (m.getLength() > 0) {
			for (int i = 0; i < m.getLength(); i++) {
				Attr attr = (Attr)m.item(i);
				o.setAttr(attr.getName(), attr.getValue());
			}
		}		
		
		NodeList nl = node.getChildNodes();		
		if (nl != null && nl.getLength() > 0) {
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					XMLObject c = fillObject(n, version, separator);
					if (c != null) {
						o.addChild(c);
					}
				} else if (n.getNodeType() == Node.TEXT_NODE) {
					o.setValue(n.getTextContent());
				} else if (n.getNodeType() == Node.CDATA_SECTION_NODE) {
					o.setValue(n.getTextContent());
					o.CDATA(true);
				}
			}
		}
		return o;
	}
	
	
	
	
	/*
	 * FIND
	 */	
	public XMLObject getObject(String path) {
		List<XMLObject> l = find(path);
		return l != null && l.size() == 1 ? (XMLObject)l.get(0) : null;
	}
	
	
	public List<XMLObject> find(String path) {
		String p = path != null ? path.trim() : "";
		if ("".equals(p)) {
			return null;
		}
		String[] s = p.split("\\" + SEPARATOR, -1);
		int len = s.length;
		int start = 0;
		if (s[0].equals(getName())) {
			start = 1;
		}
		List<XMLObject> l = null;
		XMLObject xml = this;
		for (int i = start; i < len; i++) {
			l = xml.getChildrenByName(s[i]);
			if (l == null || l.size() == 0) {
				return null;
			}
			if (i == len - 1) {
				return l;
			}
			if (l.size() > 1 && i < len - 1) {
				throw new RuntimeException("Tag <"+s[i]+"> has more than 1 element");
			}
			xml =  (XMLObject)l.get(0);
		}
		return null;
	}	
	
	
	
	/*
	 * CHILD
	 */
	public void addChild(XMLObject o) {
		if (o != null && o.getName() != null && !"".equals(o.getName().trim())) {
			if (child == null) {
				child = new HashMap<String, List<XMLObject>>();				
			}
			String key = o.getName();
			if (!child.containsKey(key)) {
				child.put(key, new ArrayList<XMLObject>());
			}			
			List<XMLObject> l = child.get(key);
			l.add(o);			
		}		
	}
	
	public List<XMLObject> removeChild(String name) {
		if (name == null || "".equals(name.trim()) || child == null) {
			return null;
		}		
		return child.remove(name); 
	}
	
	public XMLObject removeChild(String name, int index) {
		if (name == null || "".equals(name.trim()) || index < 0 || child == null) {
			return null;
		}
		List<XMLObject> l = child.get(name);
		if (l == null || l.size() - 1 > index) {
			return null;
		}
		return (XMLObject)l.remove(index);
	}
	
	public List<XMLObject> getChildrenByName(String name) {
		if (name == null || "".equals(name.trim()) || child == null) {
			return null;
		}		
		return child.get(name);
	}
	
	public Set<String> getChildrenNames() {
		if (child == null) {
			return null;
		}
		return child.keySet();
	}
	
	public boolean hasChildren() {
		return child != null && child.size() > 0;
	}
	
	
	
	
	/*
	 * ATTRIBUTE
	 */
	public void setAttr(String key, String value) {
		attr.put(key, value);
	}
	
	public String getAttr(String key) {
		return (String)attr.get(key);
	}
	
	public String removeAttr(String key) {
		return (String)attr.remove(key);
	}
	
	public Set<String> getAttrKeys() {
		return attr.keySet();
	}
	
	
	
	
	/*
	 * SET & GET FIELD
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public void CDATA(boolean b) {
		CDATA = true;
	}
	
	public boolean CDATA() {
		return CDATA;
	}
	
	
	/*
	 * CLEAR
	 */
	public void clear() {
		if (attr != null) {
			attr.clear();			
		}
		if (child != null) {
			child.clear();			
		}
		attr = null;
		child = null;
		name = null;
		value = null;
	}
	
		
	/*
	 * TO STRING
	 */
	public String toString() {
		return toString(0, 0, false);
	}
	public String toString(int indent) {
		return toString(indent, 0, false);
	}
	public String toString(boolean version) {
		return toString(0, 0, version);
	}
	public String toString(int indent, boolean version) {
		return toString(indent, 0, version);
	}
	public String toString(int indent, int index, boolean includeVersion) {
		return (includeVersion ? version + "\n" : "") + formatString(this, indent, index);
	}
	
	private String formatString(XMLObject o, int indent, int index) {
		StringBuilder sb = new StringBuilder();
		sb.append(indent > 0 ? getIndent(indent, index) : "").append("<").append(o.getName());
		Set<String> set = o.getAttrKeys();
		if (set != null) {
			int size = set.size();
			if (size > 0) {
				sb.append(" ");
				int i = 0;
				for (Iterator<String> it = set.iterator(); it.hasNext();) {
					String s = (String)it.next();
					sb.append(s).append("=\"").append(o.getAttr(s)).append("\"").append(i < size - 1 ? " " : "");
					i++;
				}
			}
		}
		set = o.getChildrenNames();
		if (set != null && !set.isEmpty()) {
			sb.append(">");
			if (indent > 0) {
				sb.append("\n");
			}
			for (Iterator<String> it = set.iterator(); it.hasNext();) {
				String s = (String)it.next();
				List<XMLObject> list = o.getChildrenByName(s);
				for (int i = 0; i < list.size(); i++) {
					XMLObject obj = (XMLObject)list.get(i);
					sb.append(formatString(obj, indent, index + 1));
				}
			}
			if (indent > 0) {
				sb.append(getIndent(indent, index));
			}
			sb.append("</").append(o.getName()).append(">").append(indent > 0 ? "\n" : "");
		} else {
			if (o.getValue() != null && !"".equals(o.getValue())) {
				sb.append(">");
				if (o.CDATA()) {
					sb.append("<![CDATA[").append(o.getValue()).append("]]>");
				} else {
					sb.append(o.getValue());
				}				
				sb.append("</").append(o.getName()).append(">");
			} else {
				sb.append("/>");
			}
			if (indent > 0) {
				sb.append("\n");
			}
		}	
		return sb.toString();
	}
	
	private String getIndent(int indent, int index) { 
		int len = index * indent;
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < len; i++) {
			sb.append(" ");
		}
		return sb.toString();
	}
	
}
