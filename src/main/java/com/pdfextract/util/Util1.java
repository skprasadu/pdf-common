package com.pdfextract.util;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

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

import lombok.val;

public class Util1 {

	public static String extractCsvFromPdfExtract(InputStream in, List<String> tables, String layoutStr) {
		// Layout layout = ExtractSections.loadYaml("ccl_layout.yaml");
		try (val pdfDocument = PDDocument.load(in)) {
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
		// Layout layout = ExtractSections.loadYaml("ccl_layout.yaml");
		try (val sw = new StringWriter();
				val writer = new BufferedWriter(sw);
				val csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);) {
			csvData(pdfDocument, tables, layoutStr, csvPrinter);
			return sw.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	public static String extractJsonFromPdfExtract(InputStream in, List<String> tables, String layoutStr) {
		// Layout layout = ExtractSections.loadYaml("ccl_layout.yaml");
		try (val pdfDocument = PDDocument.load(in)) {
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
		val arr = new JSONArray();
		val root = new JSONObject();
		arr.add(root);
		root.put("extraction_method", "lattice");
		root.put("top", "506.75195");
		root.put("left", "72.06707");
		root.put("width", "221.339080810547");
		root.put("height", "64.64892578125");
		val data = jsonData(pdfDocument, tables, layoutStr);

		root.put("data", data);
		root.put("spec_index", "0");
		return arr.toJSONString();
	}

	private static String csvData(PDDocument pdfDocument, List tables, String layoutStr,
			CSVPrinter csvPrinter) {
		try {
			val m = new ObjectMapper();
			val layout = m.readValue(layoutStr, Layout.class);
			val extractSections = (ExtractStrategy) Class
					.forName(layout.getExtractStrategyDetails().getExtractStrategy()).newInstance();

			val arrItem = new ArrayList<String>();

			val sections = layout.getSections();

			for (val columnHeader : sections) {
				arrItem.add(columnHeader.getName());
			}

			csvPrinter.printRecord(arrItem);

			val ss = extractSections.extractData(pdfDocument, layout);
			System.out.println("ss.size()=" + ss.size() + "tables.size()=" + tables.size());

			for (int i = 0; i < ss.size(); i++) {
				val arrItem1 = new ArrayList<String>();
				String[] row = ss.get(i);
				for (int j = 0; j < row.length; j++) {
					Section s = sections[j];
					if (!s.getIsTabular()) {
						if (row[j] != null) {
							arrItem1.add(row[j].replace("\n", " ").replace("\r", " "));
						} else {
							arrItem1.add(" ");
						}
					} else {
						if (i < tables.size()) {
							JSONParser parser = new JSONParser();
							val item1 = (LinkedHashMap) tables.get(i);

							arrItem1.add(getJsonDataString((ArrayList) item1.get("data")));
						} else {
							arrItem1.add("data");
						}
					}
				}
				csvPrinter.printRecord(arrItem1);
			}
		} catch (IOException | InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static JSONArray jsonData(PDDocument pdfDocument, List tables, String layoutStr) {
		try {
			val m = new ObjectMapper();
			val layout = m.readValue(layoutStr, Layout.class);
			val extractSections = (ExtractStrategy) Class
					.forName(layout.getExtractStrategyDetails().getExtractStrategy()).newInstance();

			val data = new JSONArray();

			val arrItem = new JSONArray();

			val sections = layout.getSections();

			for (val columnHeader : sections) {
				val header = new JSONObject();
				header.put("top", "");
				header.put("left", "");
				header.put("width", "");
				header.put("height", "");
				header.put("text", columnHeader.getName());
				arrItem.add(header);
			}

			data.add(arrItem);

			val ss = extractSections.extractData(pdfDocument, layout);
			System.out.println("ss.size()=" + ss.size() + "tables.size()=" + tables.size());

			for (int i = 0; i < ss.size(); i++) {
				val arrItem1 = new JSONArray();
				val row = ss.get(i);
				for (int j = 0; j < row.length; j++) {
					val s = sections[j];
					val item = new JSONObject();
					item.put("top", "");
					item.put("left", "");
					item.put("width", "");
					item.put("height", "");
					item.put("text", row[j]);
					if (!s.getIsTabular()) {
						arrItem1.add(item);
					} else {
						if (i < tables.size()) {
							val parser = new JSONParser();
							//val item1 = (JSONObject) parser.parse(tables.get(i) + "}");
							val item1 = (LinkedHashMap) tables.get(i);
							item.put("text", getJsonDataString((ArrayList) item1.get("data")));
						} else {
							item.put("text", "data");
						}
						arrItem1.add(item);
					}
				}
				data.add(arrItem1);
			}

			return data;
		} catch (IOException | InstantiationException | IllegalAccessException
				| ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private static String getJsonDataString(ArrayList jsonArray) {
		// TODO Auto-generated method stub
		val tempArray = new JSONArray();
		int i = 0;
		for (Object obj : jsonArray) {
			val jArr = (ArrayList) obj;
			val jArr1 = new JSONArray();

			if (i > 0) {
				for (Object obj1 : jArr) {
					val jObj1 = (LinkedHashMap) obj1;
					jArr1.add(jObj1.get("text"));
				}
				tempArray.add(jArr1);
			}
			i++;
		}

		return tempArray.toJSONString();
	}
}
