package com.github.ideahut.sbms.common.multipart;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Multipart implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5716720042051222738L;

	private Map<String, List<String>> parameters = new HashMap<String, List<String>>();
	
	private Map<String, Set<Integer>> filePartIndex = new HashMap<String, Set<Integer>>();
	
	private List<Filepart> filePartList = new ArrayList<Filepart>();	
	
	public void addParameter(String key, String value) {
		if (!parameters.containsKey(key)) {
			parameters.put(key, new ArrayList<String>());
		}
		List<String> list = parameters.get(key);
		list.add(value);
	}

	
	public String getParameter(String key) {
		String[] p = getParameterValues(key);
		return p != null && p.length != 0 ? p[0] : null;
	}
	
	public Set<String> getParameterNames() {
		return parameters.keySet();
	}
	
	public String removeParameter(String key) {
		String[] p = removeParameterValues(key);
		return p != null && p.length != 0 ? p[0] : null;
	}
	
	
	public String[] getParameterValues(String key) {
		List<String> list = parameters.get(key);
		if (list == null) {
			return null;
		}
		return list.toArray(new String[0]);
	}
	
	public String[] removeParameterValues(String key) {
		List<String> list = parameters.remove(key);
		if (list == null) {
			return null;
		}
		return list.toArray(new String[0]);
	}
	
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (String key : parameters.keySet()) {
			map.put(key, getParameterValues(key));
		}
		return Collections.unmodifiableMap(map);
	}
	
	public void addFilepart(String key, Filepart filepart) {
		filePartList.add(filepart);
		if (key == null || "".equals(key)) {
			return;
		}
		Set<Integer> indexSet = filePartIndex.get(key);
		if (indexSet == null) {
			filePartIndex.put(key, new HashSet<Integer>());
		}
		filePartIndex.get(key).add(filePartList.size() - 1);
	}
	
	public void removeFilepartValues(String key) {
		Set<Integer> indexSet = filePartIndex.get(key);
		if (indexSet == null) {
			return;
		}
		for (Integer idx : indexSet) {
			filePartList.remove(idx);
		}
		filePartIndex.remove(key);
	}
	
	public Filepart[] getFilepartValues(String key) {
		Set<Integer> indexSet = filePartIndex.get(key);
		if (indexSet == null) {
			return null;
		}
		Filepart[] fileparts = new Filepart[indexSet.size()];
		for (Integer idx : indexSet) {
			fileparts[fileparts.length - 1] = filePartList.get(idx);
		}
		return fileparts;
	}
	
	public Set<String> getFilepartNames() {
		return filePartIndex.keySet();
	}
	
	public List<Filepart> getFilepartList() {
		return filePartList;
	}
	
}
