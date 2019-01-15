package com.ngnl.templateData.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.io.Resources;
import com.ngnl.templateData.AbstractTemplateData;
import com.ngnl.templateData.TemplateDataLoader;


/**
 * @author 47.
 *
 */
public class DefaultXMLTemplateDataLoader extends TemplateDataLoader {
	
	@Override
	public <T extends AbstractTemplateData> Collection<T> loadTemplateData(String fileName, Class<T> templateDataClazz) throws Exception{
		URL url = Resources.getResource(getRootFileURL() + fileName);
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		
		List<JSONObject> list = doLoadXmlTemplate(document);
		
		List<T> entityList = new ArrayList<T>();
		for (int i = 0; i < list.size(); i++) {
			JSONObject bean = list.get(i);
			T entity = JSON.parseObject(bean.toJSONString(), templateDataClazz);
			if (entity != null) {
				entityList.add(entity);
			}
		}
		return entityList;
	}
	
	private static List<JSONObject> doLoadXmlTemplate(Document document) throws Exception {
		if(document == null)
			return Collections.emptyList();
		
		List<JSONObject> list = new ArrayList<JSONObject>();
		
		Element root = document.getRootElement();
		@SuppressWarnings("unchecked")
		Iterator<Element> iterator = root.elementIterator();
		while (iterator.hasNext()) {
			Element element = iterator.next();
			JSONObject bean = doLoadXmlTemplate(element);
			if (bean != null && !bean.isEmpty()) {
				list.add(bean);
			}
		}
		return list;
	}
	
	private static JSONObject doLoadXmlTemplate(Element element) {
		JSONObject json = new JSONObject();;
		
		if (element == null)
			return json;
		
		//读取属性
		Iterator<?> attributeIterator = element.attributeIterator();
		while (attributeIterator.hasNext()) {
			Attribute item = (Attribute) attributeIterator.next();
			json.put(item.getName(), item.getValue());
		}
		
		List<?> subElements = element.elements();

		//读取"textTrim".<value>textTrim</value>
		String textTrim = element.getTextTrim();
		if ("".equals(textTrim) == false)
			json.put("evalue", textTrim);

		// 读取行的子element元素
		Map<String, List<JSONObject>> subjsonMap = new HashMap<String, List<JSONObject>>();
		if (subElements != null) {
			for (int i = 0; i < subElements.size(); i++) {
				Element subElement = (Element) subElements.get(i);
				JSONObject jsonChildren = new JSONObject();
				if (subElement != null) {
					
					jsonChildren = doLoadXmlTemplate(subElement);
					
					if (subjsonMap.containsKey(subElement.getName())) {
						subjsonMap.get(subElement.getName()).add(jsonChildren);
					} else {
						List<JSONObject> list = new ArrayList<JSONObject>();
						list.add(jsonChildren);
						subjsonMap.put(subElement.getName(), list);
					}
				}
			}
			
			Set<String> key = subjsonMap.keySet();
			for (Iterator<String> it = key.iterator(); it.hasNext();) {
				String attr = (String) it.next();
				json.put(attr, subjsonMap.get(attr));
			}
		}
			
		return json;
	}

}
