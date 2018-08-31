package com.pdfextract.common.section;

import java.io.IOException;

public class TwoColumnExtractSection extends ExtractSections {
	public TwoColumnExtractSection() throws IOException {
		this.stripper = new TwoColumnTextStripper();
	}
}
