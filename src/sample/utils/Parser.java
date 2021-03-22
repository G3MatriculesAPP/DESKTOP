package sample.utils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.json.JSONArray;
import org.json.JSONObject;

public class Parser {

	public static JSONArray parseCiclesCSV(File csvFile) throws IOException {
		CSVParser csvParser = CSVParser.parse(csvFile, Charset.forName("UTF-8"), CSVFormat.EXCEL);

		// Agrupamos por ciclo
		Map<String, List<CSVRecord>> ciclosAgrupados = getCiclosAgrupados(csvParser);

		// Parseamos a JSON
		JSONArray jsonArrayCicles = new JSONArray();
		for (String key : ciclosAgrupados.keySet()) {
			JSONObject jsonObjectCicle = new JSONObject();
			CSVRecord firstCicleRecord = ciclosAgrupados.get(key).get(0);

			// Informacion del ciclo
			jsonObjectCicle.put("codi", firstCicleRecord.get(0).substring(firstCicleRecord.get(0).lastIndexOf(" ") + 1));
			jsonObjectCicle.put("nom", firstCicleRecord.get(1));
			jsonObjectCicle.put("codiAdaptacioCurricular", firstCicleRecord.get(2));
			jsonObjectCicle.put("hores", Integer.valueOf(firstCicleRecord.get(3)));
			JSONObject jsonObjectDate = new JSONObject();
			jsonObjectDate.put("$date", new Date().toString());
			try {
				jsonObjectCicle.put("dataInici", parsearFecha(firstCicleRecord.get(4)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			jsonObjectCicle.put("dataFi", checkIfBlank(firstCicleRecord.get(5)));

			// Agrupamos por modulos
			Map<String, List<CSVRecord>> modulosAgrupados = getModulosAgrupados(ciclosAgrupados, key);
			JSONArray jsonArrayModuls = new JSONArray();
			for (String moduleKey : modulosAgrupados.keySet()) {
				JSONObject jsonObjectModul = new JSONObject();
				CSVRecord firstModulRecord = modulosAgrupados.get(moduleKey).get(0);
				jsonObjectModul.put("codiModul", firstModulRecord.get(6));
				jsonObjectModul.put("nomModul", firstModulRecord.get(7));
				jsonObjectModul.put("duradaMinModul", Integer.valueOf(firstModulRecord.get(8)));
				jsonObjectModul.put("duradaMaxModul", Integer.valueOf(firstModulRecord.get(9)));
				try {
					jsonObjectModul.put("dataIniciModul", parsearFecha(firstModulRecord.get(10)));
					jsonObjectModul.put("dataFiModul", parsearFecha(firstModulRecord.get(11)));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				JSONArray jsonArrayUFs = new JSONArray();
				for (CSVRecord recordsUFs : modulosAgrupados.get(moduleKey)) {
					JSONObject jsonObjectUF = new JSONObject();
					jsonObjectUF.put("codiUnitatFormativa", recordsUFs.get(12));
					jsonObjectUF.put("nomUnitatFormativa", recordsUFs.get(13));
					jsonObjectUF.put("duradaUnitatFormativa", Integer.valueOf(recordsUFs.get(14)));
					if (recordsUFs.get(15).equalsIgnoreCase("S"))
						jsonObjectUF.put("esFCT", true);
					else
						jsonObjectUF.put("esFCT", false);
					if (recordsUFs.get(16).equalsIgnoreCase("S"))
						jsonObjectUF.put("esSintesis", true);
					else
						jsonObjectUF.put("esSintesis", false);
					if (recordsUFs.get(17).equalsIgnoreCase("S"))
						jsonObjectUF.put("esIdioma", true);
					else
						jsonObjectUF.put("esIdioma", false);
					if (recordsUFs.get(18).equalsIgnoreCase("S"))
						jsonObjectUF.put("esProjecte", true);
					else
						jsonObjectUF.put("esProjecte", false);
					jsonArrayUFs.put(jsonObjectUF);
				}
				jsonObjectModul.put("unitatsFormatives", jsonArrayUFs);
				jsonArrayModuls.put(jsonObjectModul);
			}
			jsonObjectCicle.put("moduls", jsonArrayModuls);

			jsonArrayCicles.put(jsonObjectCicle);
		}
		return jsonArrayCicles;
	}

	private static Map<String, List<CSVRecord>> getCiclosAgrupados(CSVParser csvParser) {
		Map<String, List<CSVRecord>> ciclosAgrupados = new HashMap<String, List<CSVRecord>>();
		boolean firstRow = true;
		for (CSVRecord csvRecord : csvParser) {
			if (!firstRow) {
				if (!ciclosAgrupados.containsKey(csvRecord.get(0))) {
					List<CSVRecord> list = new ArrayList<CSVRecord>();
					list.add(csvRecord);
					ciclosAgrupados.put(csvRecord.get(0), list);
				} else
					ciclosAgrupados.get(csvRecord.get(0)).add(csvRecord);
			}
			firstRow = false;
		}
		return ciclosAgrupados;
	}

	private static Map<String, List<CSVRecord>> getModulosAgrupados(Map<String, List<CSVRecord>> ciclosAgrupados, String key) {
		Map<String, List<CSVRecord>> modulosAgrupados = new HashMap<String, List<CSVRecord>>();
		for (CSVRecord csvRecord : ciclosAgrupados.get(key)) {
			if (!modulosAgrupados.containsKey(csvRecord.get(6))) {
				List<CSVRecord> list = new ArrayList<CSVRecord>();
				list.add(csvRecord);
				modulosAgrupados.put(csvRecord.get(6), list);
			} else
				modulosAgrupados.get(csvRecord.get(6)).add(csvRecord);
		}
		return modulosAgrupados;
	}

	@SuppressWarnings("deprecation")
	private static JSONObject parsearFecha(String stringDate) throws ParseException {
		if (stringDate.isBlank())
			return null;
		else {
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yy");
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Date date = dateFormat.parse(stringDate);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("$date", date.toGMTString());
			return jsonObject;
		}
	}

	private static String checkIfBlank(String string) {
		if (string.isBlank())
			return null;
		else
			return string;
	}

}
