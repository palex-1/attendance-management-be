package it.palex.attendanceManagement.batch.report.generator;

import org.apache.commons.lang3.StringUtils;

public class ReportGeneratorFactory {
	
	public static ReportGenerator makeGenerator(String generator) {
		if(StringUtils.equals(generator, ReportGeneratorTypesEnum.StandardXLSReportCreator.name())) {
			return new StandardReportGeneratorXLS();
		}
		if(StringUtils.equals(generator, ReportGeneratorTypesEnum.StandardXLSXReportCreator.name())) {
			return new StandardReportGeneratorXLSX();
		}
		
		throw new IllegalArgumentException("Unknown generator gen:"+generator);
	}
	
	
}
