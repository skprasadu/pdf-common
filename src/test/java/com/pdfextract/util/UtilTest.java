package com.pdfextract.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pdfextract.common.Layout;
import com.pdfextract.common.TableDetail;

import lombok.val;

public class UtilTest {

	@Test
	public void testExtract() throws IOException {
		//fail("Not yet implemented");
		InputStream in = UtilTest.class.getResourceAsStream("/sample.pdf");
		InputStream in1 = UtilTest.class.getResourceAsStream("/layout.json");
		
		List<String> tables = new ArrayList<String>();
		String st = Util.extractJsonFromPdfExtract(in, tables, IOUtils.toString(in1));
		
		ObjectMapper m = new ObjectMapper();
		JSONArray arr = m.readValue(st, JSONArray.class);
	}
	
	@Test
	public void testExtractCsvFromPdfExtract() throws IOException {
		//fail("Not yet implemented");
		InputStream in = UtilTest.class.getResourceAsStream("/CCL1.pdf");
		InputStream in1 = UtilTest.class.getResourceAsStream("/ccl-layout.json");
		
		List<String> tables = new ArrayList<String>();
		String st = Util.extractCsvFromPdfExtract(in, tables, IOUtils.toString(in1));
		
		System.out.println(st);
	}

	@Test
	public void testExtractJsonFromPdfExtract() throws IOException {
		//fail("Not yet implemented");
		InputStream in = UtilTest.class.getResourceAsStream("/CCL1.pdf");
		InputStream in1 = UtilTest.class.getResourceAsStream("/ccl-layout.json");
		
		List<String> tables = new ArrayList<String>();
		String st = Util.extractJsonFromPdfExtract(in, tables, IOUtils.toString(in1));
		
		System.out.println(st);
	}

	@Test
	public void testExtractJsonFromPdfExtractWithColumns() throws IOException {
		//fail("Not yet implemented");
		InputStream in = UtilTest.class.getResourceAsStream("/CCL1.pdf");
		InputStream in1 = UtilTest.class.getResourceAsStream("/ccl-layout-wo-regex.json");
		
		List<String> tables = new ArrayList<String>();
		InputStream in2 = UtilTest.class.getResourceAsStream("/table1.json");
		tables.add(IOUtils.toString(in2));
		in2 = UtilTest.class.getResourceAsStream("/table2.json");
		tables.add(IOUtils.toString(in2));
		
		String st = Util.extractJsonFromPdfExtract(in, tables, IOUtils.toString(in1));
		
		System.out.println(st);
	}
	
	@Test
	public void testTableDetail() throws IOException, ParseException {
		//fail("Not yet implemented");
		InputStream in = UtilTest.class.getResourceAsStream("/table1.json");

		TableDetail td = new TableDetail(IOUtils.toString(in) );
		
		assertEquals(2, td.getColumns().size());
		assertEquals(3, td.getContent().size());
		
		in = UtilTest.class.getResourceAsStream("/table2.json");

		td = new TableDetail(IOUtils.toString(in) );
		
		assertEquals(2, td.getColumns().size());
		assertEquals(2, td.getContent().size());

	}
}
