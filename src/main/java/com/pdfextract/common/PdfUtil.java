package com.pdfextract.common;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfUtil {

	public static void extractPDF(PDDocument document, Layout layout, TextStripper stripper)
			throws IOException {
		System.out.println("***" + layout.getStripperStrategy());
		stripper.setLayout(layout);

		stripper.setSortByPosition(true);
		stripper.setStartPage(0);
		stripper.setEndPage(document.getNumberOfPages());

		Writer writer1 = new OutputStreamWriter(new ByteArrayOutputStream());
		stripper.writeText(document, writer1);
	}
}
