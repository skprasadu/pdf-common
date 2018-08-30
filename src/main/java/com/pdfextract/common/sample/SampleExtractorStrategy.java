package com.pdfextract.common.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;

import com.pdfextract.common.ExtractStrategy;
import com.pdfextract.common.Layout;
import com.pdfextract.common.LineDetails;
import com.pdfextract.common.PdfUtil;
import com.pdfextract.common.TextStripper;

import lombok.val;

public class SampleExtractorStrategy implements ExtractStrategy {
	
	private TextStripper stripper;
	
	public SampleExtractorStrategy() {
		// TODO Auto-generated constructor stub
		try {
			stripper = new SampleTextStripper();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public List<String[]> extractData(PDDocument document, Layout layout) throws IOException {
		// TODO Auto-generated method stub
		PdfUtil.extractPDF(document, layout, stripper);
		
		val list = new ArrayList<String[]>();
		for(LineDetails l : stripper.getData()){
			String[] dat = new String[1];
			dat[0] = l.getLine();
			list.add(dat);
		}
		return list;
	}
}
