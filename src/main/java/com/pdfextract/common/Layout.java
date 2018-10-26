package com.pdfextract.common;

import lombok.Data;

@Data
public class Layout {
	private Integer selection_count;
	private String name;
	private Integer time;
	private String id;
	private Integer page_count;
	
	private String tableDelimiter;
	private Section[] sections;
	private Integer rightStartX;
	private Integer leftStartX;
	private Integer startY;
	private Integer endY;
	private Double tableGap;
	private String extractorRules;
	private ExtractStrategyDetails extractStrategyDetails;
	private LayoutExtractorDetails layoutExtractorDetails;
	private String[] headers;
}
