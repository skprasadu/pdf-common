package com.pdfextract.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class UtilTest {

	@Test
	public void testExtractJsonFromPdfExtract() throws IOException {
		//fail("Not yet implemented");
		InputStream in = UtilTest.class.getResourceAsStream("/sample.pdf");
		InputStream in1 = UtilTest.class.getResourceAsStream("/layout.json");
		
		List<String> tables = new ArrayList<String>();
		String st = Util.extractJsonFromPdfExtract(in, tables, IOUtils.toString(in1));
		
		ObjectMapper m = new ObjectMapper();
		JSONArray arr = m.readValue(st, JSONArray.class);
	}
}
