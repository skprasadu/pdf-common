package com.pdfextract.common;

import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

public interface ExtractStrategy {
	public List<String[]> extractData(PDDocument document, Layout layout) throws IOException;
}