package com.pdfextract.common;

import org.json.simple.JSONObject;

import lombok.Data;

@Data
public class LayoutExtractorDetails {
	private String layoutExtractor;
	private JSONObject additionalParameters;
}
