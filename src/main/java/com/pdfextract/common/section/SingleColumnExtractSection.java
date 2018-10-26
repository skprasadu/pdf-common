package com.pdfextract.common.section;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.pdfextract.common.ExtractStrategy;
import com.pdfextract.common.Layout;
import com.pdfextract.common.PdfUtil;
import com.pdfextract.common.Section;
import com.pdfextract.common.TextStripper;

public class SingleColumnExtractSection implements ExtractStrategy {
	
	protected TextStripper stripper;

	public List<String[]> extractData(List<String> data, Layout layout) throws IOException {
		Section[] sections = layout.getSections();
		// List<LineDetails> data = null;

		PdfUtil.extractPDF(layout, this.stripper);
		// data = stripper.getData();
		//PDFTextStripper pdfStripper = new PDFTextStripper();
		//String st = pdfStripper.getText(document);
		//String[] data = st.split("\n");

		ArrayList<String[]> data1 = new ArrayList<>();
		String[] eccnDetail = null;

		short flag = -2;

		for (String ln : data) {

			short locFlag = checkSectionChange(flag, sections, ln);
			if (locFlag != -1) {
				flag = locFlag;
			}
			if (flag == -2) {
				continue;
			}

			if (flag == 1 && locFlag == 1) {
				if (eccnDetail != null) {
					data1.add(eccnDetail);
				}
				eccnDetail = new String[sections.length];
			}

			if (eccnDetail != null) {
				String line = cleanse(ln.trim().equals("") ? "\n" : ln.trim().replaceAll("\"", "\'"));

				for (short i = 1; i <= sections.length; i++) {
					if (flag == i) {
						String summary = eccnDetail[i - 1];
						String summary1 = summary != null ? summary : "";
						eccnDetail[i - 1] = summary1 + "\n" + line;
						break;
					}
				}

			}
		}
		if (eccnDetail != null) {
			data1.add(eccnDetail);
		}
		return data1;
	}

	private short checkSectionChange(short flag, Section[] sections, String ln) {

		if ((flag == -2 || flag == sections.length || flag == 1) && patternMatch(sections[0].getRegex(), ln)) {
			return 1;
		}
		for (short i = 1; i <= sections.length; i++) {
			if (i < 2 && (flag == 1 && patternMatch(sections[1].getRegex(), ln))) {
				// System.out.println(" secondSection=" + secondSection + "
				// Actualline=" + line);
				return 2;
			} else if ((flag == (i - 2) || flag == (i - 1)) && patternMatch(sections[i - 1].getRegex(), ln)) {
				// System.out.println(" thirdSection=" + thirdSection + "
				// Actualline=" + line);
				return i;
			}
		}

		return -1;
	}

	private boolean patternMatch(String pattern, String ln) {
		// TODO Auto-generated method stub
		String line = ln.replace("\r", "");
		String[] split = pattern.split("#####");
		// if (split.length == 1) {
		boolean b = Pattern.matches(pattern, line);
		return b;
		// } else {
		// boolean a = Pattern.matches(split[0], line);
		// if (ln.getFont() != null) {
		// boolean b = ln.getFont().getName().contains(split[1]);
		// return a && b;
		// }
		// }
		// return false;
	}

	private static String cleanse(String line) {
		char[] arr = line.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			int ic = (int) c;

			if (ic == 8220 || ic == 8221) {
				arr[i] = '\'';
			}
		}
		return new String(arr);
	}


}
