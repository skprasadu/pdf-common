package com.pdfextract.common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface ExtractStrategy {
	public List<String[]> extractData(List<String> data, Layout layout) throws IOException;
}