package com.cubrid.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.sql.Connection;

import com.cubrid.database.DatabaseManager;
import com.cubrid.parser.SqlMapXMLParser;

public class SQLAnalyzerForCUBRID {
	private final String SUMMARY_FILE_NAME = "summary.log";
	private final String SQLMAP_PATH = "sqlmap";

	private String resultFilePath = null;
	private File resultFile = null;

	private URI rootPath = null;
	private BufferedWriter writerSummary = null;

	private int count = 0;
	private int totalCount = 0;
	private int totalSuccessCount = 0;
	private int totalErrorCount = 0;
	private int subTotalCount = 0;
	private int subTotalSuccessCount = 0;
	private int subTotalErrorCount = 0;

	private boolean isBeforeFile = false;
	
	private DatabaseManager databaseManager = null;

	public static void main(String[] args) {
		SQLAnalyzerForCUBRID sqlAnalyzerForCUBRID = new SQLAnalyzerForCUBRID();
		sqlAnalyzerForCUBRID.openSummary();
		sqlAnalyzerForCUBRID.start();
	}

	public SQLAnalyzerForCUBRID() {
		databaseManager = new DatabaseManager();
		
		try {
			rootPath = new File(".").toURI();
			writerSummary = new BufferedWriter(new FileWriter(SUMMARY_FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void openSummary() {
		try {
			writerSummary.write("Start SQL Analyzer For CUBRID.");
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.write("- T: Total");
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.write("- S: Success");
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.write("- E: Error");
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Start SQL Analyzer For CUBRID.");
		System.out.println("- T: Total");
		System.out.println("- S: Success");
		System.out.println("- E: Error");
	}

	private void appendDirectorySummary(String filePath) {
		try {
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.append("[ " + String.format("%4s", "-") + " ] ");
			writerSummary.append("[ ------------------------- ] ");
			writerSummary.append(filePath);
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("");
		System.out.println("[ " + String.format("%4s", "-") + " ] [ ------------------------- ] " + filePath);
	}

	private void appendQueryNumber() {
		count++;

		try {
			writerSummary.append("[ " + String.format("%4d", count) + " ] ");
			writerSummary.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.print("[ " + String.format("%4d", count) + " ] ");
	}

	private void appendQuerySummary(String filePath) {
		try {
			writerSummary.append(filePath);
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println(filePath);
	}

	public void appendResultSummary(int total, int success, int error) {
		subTotalCount += total;
		subTotalSuccessCount += success;
		subTotalErrorCount += error;

		try {
			writerSummary.append("[ T: " + String.format("%4d", total));
			writerSummary.append(", S: " + String.format("%4d", success));
			writerSummary.append(", E: " + String.format("%4d", error) + " ] ");
			writerSummary.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.print("[ T: " + String.format("%4d", total));
		System.out.print(", S: " + String.format("%4d", success));
		System.out.print(", E: " + String.format("%4d", error) + " ] ");
	}

	public void appendSubTotalSummary() {
		try {
			writerSummary.append("         ");
			writerSummary.append("[ T: " + String.format("%4d", subTotalCount));
			writerSummary.append(", S: " + String.format("%4d", subTotalSuccessCount));
			writerSummary.append(", E: " + String.format("%4d", subTotalErrorCount) + " ] ");
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.print("         ");
		System.out.print("[ T: " + String.format("%4d", subTotalCount));
		System.out.print(", S: " + String.format("%4d", subTotalSuccessCount));
		System.out.print(", E: " + String.format("%4d", subTotalErrorCount) + " ] ");
		System.out.println("");

		totalCount += subTotalCount;
		totalSuccessCount += subTotalSuccessCount;
		totalErrorCount += subTotalErrorCount;

		subTotalCount = 0;
		subTotalSuccessCount = 0;
		subTotalErrorCount = 0;
	}

	public void appendTotalSummary() {
		try {
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.append("Summary: ");
			writerSummary.append("[ T: " + String.format("%4d", totalCount));
			writerSummary.append(", S: " + String.format("%4d", totalSuccessCount));
			writerSummary.append(", E: " + String.format("%4d", totalErrorCount) + " ] ");
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("");
		System.out.print("Summary: ");
		System.out.print("[ T: " + String.format("%4d", totalCount));
		System.out.print(", S: " + String.format("%4d", totalSuccessCount));
		System.out.print(", E: " + String.format("%4d", totalErrorCount) + " ] ");
		System.out.println("");

		totalCount = 0;
		totalSuccessCount = 0;
		totalErrorCount = 0;
	}

	private void closeSummary() {
		try {
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.append("Stop SQL Analyzer For CUBRID.");
			writerSummary.append(System.getProperty("line.separator"));
			writerSummary.flush();
			writerSummary.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("");
		System.out.println("Stop SQL Analyzer For CUBRID.");
		System.out.println("");
	}

	private void start() {
		traverseDirectory(SQLMAP_PATH);

		if (isBeforeFile == true) {
			appendSubTotalSummary();
		}

		appendTotalSummary();
		closeSummary();
	}

	private void traverseDirectory(String path) {
		File fileOrDir = new File(path);
		if (fileOrDir.isDirectory()) {
			File[] listFiles = fileOrDir.listFiles();

			for (int i = 0; i < listFiles.length; i++) {
				File file = listFiles[i];

				if (file.isFile() && file.getName().matches(".*.xml")) {
					isBeforeFile = true;

					appendQueryNumber();
					parse(file);
					appendQuerySummary(rootPath.relativize(file.toURI()).toString());

					continue;
				}

				if (file.isDirectory()) {
					count = 0;

					if (isBeforeFile == true) {
						appendSubTotalSummary();
						isBeforeFile = false;
					}

					try {
						appendDirectorySummary(rootPath.relativize(file.toURI()).toString());
						traverseDirectory(file.getCanonicalPath().toString());
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	private void parse(File file) {
		String filePath = rootPath.relativize(file.toURI()).toString();
		String fileName = file.getName();

		SqlMapXMLParser sqlMapXMLParser = new SqlMapXMLParser();
		sqlMapXMLParser.analyze(this, databaseManager, filePath, fileName);
	}
}
