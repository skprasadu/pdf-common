package com.pdfextract.common.sample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.text.TextPosition;

import com.pdfextract.common.Layout;
import com.pdfextract.common.LineDetails;
import com.pdfextract.common.TextStripper;

import lombok.Getter;
import lombok.Setter;

public class SampleTextStripper extends TextStripper {

	public SampleTextStripper() throws IOException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Getter
	public ArrayList<LineDetails> data = new ArrayList<LineDetails>();
	
	@Setter
	public Layout layout;
	
	@Override
	protected void writeString(String line, List<TextPosition> textPositions) throws IOException {

		TextPosition t = textPositions.get(0);

		String currentLine = line;
		
		LineDetails l = LineDetails.builder().line(currentLine).build();
		data.add(l);
	}

}
