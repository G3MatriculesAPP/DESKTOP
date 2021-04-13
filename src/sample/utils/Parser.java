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
	
	public static void main(String[] args) throws IOException {
		parseAlumnesCSV(new File("alumnes.csv"));
	}

	/**
	 * Parsea un archivo CSV de alumnos admitidos a un objeto JSONArray.
	 * @param csvFile Archivo CSV de alumnos admitidos.
	 * @return El contenido del CSV parseado a un objeto JSONArray.
	 * @throws IOException
	 */
	public static JSONArray parseAlumnesCSV(File csvFile) throws IOException {
		CSVParser csvParser = CSVParser.parse(csvFile, Charset.forName("UTF-8"), CSVFormat.EXCEL);

		List<List<String>> records = new ArrayList<List<String>>();
		boolean firstRow = true;
		for (CSVRecord csvRecord : csvParser) {
			if (!firstRow) {
				List<String> columnData = new ArrayList<String>();
				for (int i = 0; i < csvRecord.size(); i++)
					columnData.add(checkIfBlank(csvRecord.get(i)));
				records.add(columnData);
			} else
				firstRow = false;
		}

		JSONArray jsonArrayAlumnes = new JSONArray();
		for (List<String> record : records) {
			JSONObject jsonObjectAlumne = new JSONObject();
			jsonObjectAlumne.put("nom", record.get(4));
			jsonObjectAlumne.put("primerCognom", record.get(5));
			jsonObjectAlumne.put("segonCognom", record.get(6));
			jsonObjectAlumne.put("idRALC", record.get(7));
			jsonObjectAlumne.put("tipusAlumne", record.get(8));
			jsonObjectAlumne.put("dni", record.get(21));
			jsonObjectAlumne.put("nie", record.get(22));
			jsonObjectAlumne.put("pass", record.get(23));
			jsonObjectAlumne.put("tis", record.get(24));
			jsonObjectAlumne.put("dataNaixement", record.get(25));
			jsonObjectAlumne.put("sexe", record.get(26));
			jsonObjectAlumne.put("nacionalitat", record.get(27));
			jsonObjectAlumne.put("paisNaixement", record.get(28));
			jsonObjectAlumne.put("municipiNaixement", record.get(29));
			jsonObjectAlumne.put("telefon", record.get(39));
			jsonObjectAlumne.put("email", record.get(40));
			jsonObjectAlumne.put("codiCentreAssignat", record.get(58));

			JSONObject jsonObjectDireccio = new JSONObject();
			jsonObjectDireccio.put("tipusVia", record.get(30));
			jsonObjectDireccio.put("nom", record.get(31));
			jsonObjectDireccio.put("numero", record.get(32));
			jsonObjectDireccio.put("altresDades", record.get(33));
			jsonObjectDireccio.put("provincia", record.get(34));
			jsonObjectDireccio.put("municipi", record.get(35));
			jsonObjectDireccio.put("localitat", record.get(36));
			jsonObjectDireccio.put("codiPostal", record.get(37));
			jsonObjectDireccio.put("pais", record.get(38));
			jsonObjectAlumne.put("direccio", jsonObjectDireccio);

			JSONArray jsonArrayTutors = new JSONArray();

			JSONObject jsonObjectTutor1 = new JSONObject();
			jsonObjectTutor1.put("tipusDocument", record.get(41));
			jsonObjectTutor1.put("numDocument", record.get(42));
			jsonObjectTutor1.put("nom", record.get(43));
			jsonObjectTutor1.put("primerCognom", record.get(44));
			jsonObjectTutor1.put("segonCognom", record.get(45));
			jsonArrayTutors.put(jsonObjectTutor1);

			JSONObject jsonObjectTutor2 = new JSONObject();
			jsonObjectTutor2.put("tipusDocument", record.get(46));
			jsonObjectTutor2.put("numDocument", record.get(47));
			jsonObjectTutor2.put("nom", record.get(48));
			jsonObjectTutor2.put("primerCognom", record.get(49));
			jsonObjectTutor2.put("segonCognom", record.get(50));
			jsonArrayTutors.put(jsonObjectTutor2);

			jsonObjectAlumne.put("tutors", jsonArrayTutors);

			JSONObject jsonObjectCentreProcedencia = new JSONObject();
			jsonObjectCentreProcedencia.put("codi", record.get(51));
			jsonObjectCentreProcedencia.put("nom", record.get(52));
			jsonObjectCentreProcedencia.put("codiEnsenyament", record.get(53));
			jsonObjectCentreProcedencia.put("nomEnsenyament", record.get(54));
			jsonObjectCentreProcedencia.put("curs", parseInt(record.get(55)));
			jsonObjectCentreProcedencia.put("llengua", record.get(56));
			jsonObjectCentreProcedencia.put("religio", record.get(57));
			jsonObjectAlumne.put("centreProcedencia", jsonObjectCentreProcedencia);

			JSONObject jsonObjectConvocatoria = new JSONObject();
			jsonObjectConvocatoria.put("nom", record.get(0));
			jsonObjectConvocatoria.put("codi", record.get(1));
			jsonObjectConvocatoria.put("tipus", record.get(2));
			jsonObjectConvocatoria.put("estatSolicitud", record.get(3));

			JSONObject jsonObjectCentre = new JSONObject();
			jsonObjectCentre.put("codi", record.get(9));
			jsonObjectCentre.put("nom", record.get(10));
			jsonObjectCentre.put("naturalesa", record.get(11));
			jsonObjectCentre.put("municipi", record.get(12));
			jsonObjectCentre.put("sstt", record.get(13));

			JSONObject jsonObjectEnsenyament = new JSONObject();
			jsonObjectEnsenyament.put("codi", record.get(14));
			jsonObjectEnsenyament.put("nom", record.get(15));
			jsonObjectEnsenyament.put("curs", parseInt(record.get(18)));
			jsonObjectEnsenyament.put("regim", record.get(19));
			jsonObjectEnsenyament.put("torn", record.get(19));

			JSONObject jsonObjectModalitat = new JSONObject();
			jsonObjectModalitat.put("codi", record.get(16));
			jsonObjectModalitat.put("nom", record.get(17));
			jsonObjectEnsenyament.put("modalitat", jsonObjectModalitat);

			jsonObjectConvocatoria.put("centre", jsonObjectCentre);
			jsonObjectConvocatoria.put("ensenyament", jsonObjectEnsenyament);
			jsonObjectAlumne.put("convocatoria", jsonObjectConvocatoria);

			jsonArrayAlumnes.put(jsonObjectAlumne);
		}
		return jsonArrayAlumnes;
	}

	/**
	 * Parsea un archivo CSV de ciclos a un objeto JSONArray.
	 * @param csvFile Archivo CSV de ciclos formativos.
	 * @return El contenido del CSV parseado a un objeto JSONArray.
	 * @throws IOException
	 */
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
			jsonObjectCicle.put("dataInici", firstCicleRecord.get(4));
			jsonObjectCicle.put("dataFi", checkIfBlank(firstCicleRecord.get(5)));

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


	/**
	 * @param ciclosAgrupados
	 * @param key
	 * @return
	 * @see {@link #parseCiclesCSV(File)}
	 */
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
	
	private static Object parseInt(String string) {
		if (string != null)
			return Integer.parseInt(string);
		else
			return null;
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
			jsonObject.put("date", date.toGMTString());
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