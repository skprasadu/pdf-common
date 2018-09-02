package com.pdfextract.common.regex;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import com.pdfextract.common.Layout;

import lombok.val;

public class RegexCommonUtil {
	public static final String delimiter = "#####";

	public static List<String[]> applyRegex(List<String[]> ss, int index, Layout layout) throws IOException {
		val regexRules = loadRegexRules(layout, index);

		val csvPrinter = new LinkedList<String[]>();

		if (regexRules.size() > 0) {
			val map = new LinkedHashMap<String, List<String>>();
			for (String[] csvRecord : ss) {

				for (int i = 0; i < csvRecord.length; i++) {
					if (csvRecord[i] != null) {
						extractRegexData(i, csvRecord[i], regexRules, map);
					}
				}
				val list = new String[map.keySet().size()];
				int i = 0;

				for (String key : map.keySet()) {
					String st = String.join(delimiter, map.get(key));
					String clean = CharMatcher.ASCII.retainFrom(st);

					list[i++] = clean;
				}

				csvPrinter.add(list);

				clearMap(map);
			}
			return csvPrinter;
		} else {
			return ss;
		}
	}

	private static void clearMap(LinkedHashMap<String, List<String>> map) {
		for (String key : map.keySet()) {
			map.put(key, new LinkedList<String>());
		}
	}

	public static void extractRegexData(int i, String sent, HashMap<String, Pattern> regexRules,
			LinkedHashMap<String, List<String>> map) {
		String sent1 = sent.replaceAll("\n", " ").replaceAll("\r", " ").trim();

		String sum = sent1.replaceAll("\\s+", " ");
		for (String key : regexRules.keySet()) {
			if (key.startsWith(i + "-")) {
				val summaryMap = extractDataAsGroup(regexRules.get(key), sum);
				for (String key1 : summaryMap.keySet()) {
					if (map.containsKey(key1)) {
						List<String> list = map.get(key1);
						list.addAll(summaryMap.get(key1));
					} else {
						map.put(key1, summaryMap.get(key1));
					}
				}
			}
		}
	}

	public static HashMap<String, Pattern> loadRegexRules(Layout layout, int index) throws IOException {
		val map = new HashMap<String, Pattern>();

		if (layout.getExtractStrategyDetails().getAdditionalParameters() != null) {
			HashMap ruleListSt = (HashMap) layout.getExtractStrategyDetails().getAdditionalParameters()
					.get("regex-rules");

			if (ruleListSt != null) {
				List lst = (List) ruleListSt.get("steps");

				if (lst != null) {
					HashMap ruleMap = (HashMap) lst.get(index);

					if (ruleMap != null) {

						for (Object keyObj : ruleMap.keySet()) {
							String key = (String) keyObj;
							Pattern r = Pattern.compile(((String) ruleMap.get(key)).trim(), Pattern.DOTALL);
							map.put(key.trim(), r);
						}
					}
				}
			}
		}
		return map;
	}

	public static Map<String, List<String>> extractDataAsGroup(Pattern r, String data) {
		val map = new HashMap<String, List<String>>();
		val matcher = r.matcher(data);
		val set = getNamedGroupCandidates(r.pattern());
		while (matcher.find()) {
			for (String st : set) {
				List<String> list = null;
				if (map.containsKey(st)) {
					list = map.get(st);
				} else {
					list = new LinkedList<>();
				}
				list.add(matcher.group(st));
				map.put(st, list);
			}
		}
		return map;
	}

	public static Set<String> getNamedGroupCandidates(String regex) {
		Set<String> namedGroups = new TreeSet<String>();

		Matcher m = Pattern.compile("\\(\\?<([a-zA-Z][a-zA-Z0-9]*)>").matcher(regex);

		while (m.find()) {
			namedGroups.add(m.group(1));
		}

		return namedGroups;
	}

	public static Map<String, Map<String, List<String>>> extract(Map<String, Pattern> regexRules, String sent) {
		val localMap = new HashMap<String, Map<String, List<String>>>();
		for (String key : regexRules.keySet()) {
			val rule = regexRules.get(key);

			val map = extractDataAsGroup(rule, sent);
			localMap.put(key, map);
		}
		return localMap;
	}
}
