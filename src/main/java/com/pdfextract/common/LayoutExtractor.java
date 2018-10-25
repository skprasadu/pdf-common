package com.pdfextract.common;

import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

public interface LayoutExtractor{
	public List<String> extractData(PDDocument document, Layout layout) throws Exception;
}