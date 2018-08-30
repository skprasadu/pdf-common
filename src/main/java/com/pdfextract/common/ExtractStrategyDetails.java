package com.pdfextract.common;

import org.json.simple.JSONObject;

import lombok.Data;

@Data
public class ExtractStrategyDetails {
	private String extractStrategy;
	private JSONObject additionalParameters;
}
