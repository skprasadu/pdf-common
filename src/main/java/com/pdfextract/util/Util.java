package com.pdfextract.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdfextract.common.ExtractStrategy;
import com.pdfextract.common.Layout;
import com.pdfextract.common.Section;
import com.pdfextract.common.regex.RegexCommonUtil;

import lombok.val;

public class Util {

	public static String extractCsvFromPdfExtract(InputStream in, List<String> tables, String layoutStr) {
		try (PDDocument pdfDocument = PDDocument.load(in)) {
			return extractCsvFromPdfExtract(pdfDocument, tables, layoutStr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String extractCsvFromPdfExtract(PDDocument pdfDocument, List<String> tables, String layoutStr) {
		try (StringWriter sw = new StringWriter();
				BufferedWriter writer = new BufferedWriter(sw);
				CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
			csvData(pdfDocument, tables, layoutStr, csvPrinter);
			return sw.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	public static String extractJsonFromPdfExtract(InputStream in, List<String> tables, String layoutStr) {
		try (PDDocument pdfDocument = PDDocument.load(in)) {
			return extractJsonFromPdfExtract(pdfDocument, tables, layoutStr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public static String extractJsonFromPdfExtract(PDDocument pdfDocument, List<String> tables, String layoutStr) {
		JSONArray arr = new JSONArray();
		JSONObject root = new JSONObject();
		arr.add(root);
		root.put("extraction_method", "lattice");
		root.put("top", "506.75195");
		root.put("left", "72.06707");
		root.put("width", "221.339080810547");
		root.put("height", "64.64892578125");
		JSONArray data = jsonData(pdfDocument, tables, layoutStr);

		root.put("data", data);
		root.put("spec_index", "0");
		return arr.toJSONString();
	}

	private static String csvData(PDDocument pdfDocument, List<String> tables, String layoutStr,
			CSVPrinter csvPrinter) {
		try {
			val m = new ObjectMapper();
			val layout = m.readValue(layoutStr, Layout.class);

			val arrItem = new ArrayList<String>();

			List sections = getHeaders(layout);

			for (Object columnHeader : sections) {
				arrItem.add((String) columnHeader);
			}

			csvPrinter.printRecord(arrItem);

			// get the data before RegEx
			val ss1 = extractData(pdfDocument, tables, layout);

			// Apply RegEx IF ANY
			val ss = RegexCommonUtil.applyRegex(ss1, 0, layout);

			System.out.println("csvData ss.size()=" + ss.size() + "tables.size()=" + tables.size());

			for (int i = 0; i < ss.size(); i++) {
				val arrItem1 = new ArrayList<String>();
				String[] row = ss.get(i);
				for (int j = 0; j < row.length; j++) {
					if (row[j] != null && !row[j].trim().equals("")) {
						arrItem1.add(row[j].replace("\n", " ").replace("\r", " "));
					} else {
						arrItem1.add("no data");
					}
				}
				csvPrinter.printRecord(arrItem1);
			}
		} catch (IOException | InstantiationException | IllegalAccessException | ClassNotFoundException
				| ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static JSONArray jsonData(PDDocument pdfDocument, List<String> tables, String layoutStr) {
		try {
			val m = new ObjectMapper();
			val layout = m.readValue(layoutStr, Layout.class);

			val data = new JSONArray();

			val arrItem = new JSONArray();

			List sections = getHeaders(layout);

			for (Object columnHeader : sections) {
				JSONObject header = new JSONObject();
				header.put("top", "");
				header.put("left", "");
				header.put("width", "");
				header.put("height", "");
				header.put("text", columnHeader);
				arrItem.add(header);
			}

			data.add(arrItem);

			val ss1 = extractData(pdfDocument, tables, layout);
			val ss = RegexCommonUtil.applyRegex(ss1, 0, layout);
			System.out.println("jsonData ss.size()=" + ss.size() + "tables.size()=" + tables.size());

			for (int i = 0; i < ss.size(); i++) {
				JSONArray arrItem1 = new JSONArray();
				String[] row = ss.get(i);
				for (int j = 0; j < row.length; j++) {
					JSONObject item = new JSONObject();
					item.put("top", "");
					item.put("left", "");
					item.put("width", "");
					item.put("height", "");
					if (row[j] != null && !row[j].trim().equals("")) {
						item.put("text", row[j].replace("\n", " ").replace("\r", " "));
					} else {
						item.put("text", "no data");
					}

					arrItem1.add(item);
				}
				data.add(arrItem1);
			}

			return data;
		} catch (IOException | ParseException | InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static List getHeaders(Layout layout) {
		// TODO Auto-generated method stub
		if (layout.getHeaders() != null) {
			return Arrays.asList(layout.getHeaders());
		} else {
			return Arrays.stream(layout.getSections()).map(Section::getName).collect(Collectors.toList());
		}
	}

	private static List<String[]> extractData(PDDocument pdfDocument, List<String> tables, Layout layout)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException, ParseException {
		val extractSections = (ExtractStrategy) Class.forName(layout.getExtractStrategyDetails().getExtractStrategy())
				.newInstance();
		val sections = layout.getSections();
		val ss = extractSections.extractData(pdfDocument, layout);
		System.out.println("extractData ss.size()=" + ss.size() + "tables.size()=" + tables.size());
		List<String[]> list = new LinkedList<String[]>();

		for (int i = 0; i < ss.size(); i++) {
			String[] row = ss.get(i);
			val arrItem1 = new String[row.length];
			for (int j = 0; j < row.length; j++) {
				Section s = sections[j];
				if (!s.getIsTabular()) {

					if (row[j] != null) {
						arrItem1[j] = row[j].replace("\n", " ").replace("\r", " ");
					} else {
						arrItem1[j] = " ";
					}
				} else {
					if (i < tables.size()) {
						JSONParser parser = new JSONParser();
						JSONObject item1 = (JSONObject) parser.parse(tables.get(i) + "}");

						arrItem1[j] = getJsonDataString((JSONArray) item1.get("data"));
					} else {
						arrItem1[j] = "[]";
					}
				}
			}
			list.add(arrItem1);
		}
		return list;
	}

	private static String getJsonDataString(JSONArray jsonArray) {
		// TODO Auto-generated method stub
		JSONArray tempArray = new JSONArray();
		int i = 0;
		for (Object obj : jsonArray) {
			JSONArray jArr = (JSONArray) obj;
			JSONArray jArr1 = new JSONArray();

			if (i > 0) {
				for (Object obj1 : jArr) {
					JSONObject jObj1 = (JSONObject) obj1;
					jArr1.add(jObj1.get("text"));
				}
				tempArray.add(jArr1);
			}
			i++;
		}

		return tempArray.toJSONString();
	}
}
