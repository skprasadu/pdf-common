package com.pdfextract.common.section;

import java.io.IOException;

public class SingleColumnExtractSection extends ExtractSections {
	public SingleColumnExtractSection() throws IOException {
		this.stripper = new SingleColumnTextStripper();
	}

}
