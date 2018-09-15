package com.pdfextract.common;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import lombok.Getter;

public class TableDetail {
	
	@Getter
	private List columns;
	
	@Getter
	private List<String[]> content;
		
	public TableDetail(LinkedHashMap tableData) throws ParseException{

		ArrayList tableArray = (ArrayList)tableData.get("data");

		
		ArrayList firstArr = (ArrayList) tableArray.get(0);
		
		columns = (List) firstArr.stream().map(x -> ((LinkedHashMap)x).get("text")).collect(Collectors.toList());
		
		int i = 0;
		content =  new LinkedList<String[]>();
		for(Object obj : tableArray){
			ArrayList jArr = (ArrayList) obj;
			if(i > 0){
				String[] sts = new String[jArr.size()];
				int j = 0;
				for(Object obj1 : jArr){
					LinkedHashMap jObj = (LinkedHashMap) obj1;
					sts[j++] = (String) jObj.get("text"); 
				}
				content.add(sts);
			}
			i++;
		}
	}

}
