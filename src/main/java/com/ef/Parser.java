package com.ef;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import util.CliHelper;

public class Parser {

	public static void main(String args[]) throws IOException, ParseException, ClassNotFoundException, SQLException,
			org.apache.commons.cli.ParseException {
		
		Options options = new Options();
		Option startDateOption = new Option("s", "startDate", true, "Provide the Start Date");
		Option durationOption = new Option("d", "duration", true,
				"Provide the duration and the value can be Hourly Or Daily");
		Option threholdOption = new Option("t", "threshold", true, "Provide the Threshold ");
		Option accessLogLocation = new Option("a","accessLog",true,"Location of Log File");
		
		options.addOption(startDateOption);
		options.addOption(durationOption);
		options.addOption(threholdOption);
		options.addOption(accessLogLocation);

		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = parser.parse(options, args);
		if (cmd.hasOption("s") && cmd.hasOption("d") && cmd.hasOption("t") && cmd.hasOption("a")) {
			
			String fileLocation = cmd.getOptionValue("a");
			
			int threshold = Integer.parseInt(cmd.getOptionValue("t"));
			
			if(!CliHelper.durationValidator(cmd.getOptionValue("d")))
				throw new RuntimeException("Invalid Duration");
		
			if (!CliHelper.startTimeValidator(cmd.getOptionValue("s"))) {
				throw new RuntimeException("Invalid StartDate");
			}
			
			Parser.doWork(fileLocation, cmd.getOptionValue("s"),cmd.getOptionValue("d"), threshold);
		} else {
			 HelpFormatter formatter = new HelpFormatter();
			 formatter.printHelp("Log Parser", "Use following options", options, "", true);
			 System.exit(1);
		}

	}

	static void doWork(String fileLocation,String startDate, String duration,int threshold) throws ClassNotFoundException, SQLException, IOException {
		
		MySQLConnection db = new MySQLConnection();

		// Insert Code
		List<String> lines = Files.readAllLines(new File(fileLocation).toPath()) ; 
		List<SqlModel> sqlFileObject = new ArrayList<>(); 
  
		for (String field : lines) {
	  
			String arr[] = field.split("\\|"); 
			  
			if (arr.length != 5) throw new RuntimeException("Log Format MisMatch!!");
				  
			Timestamp requestDate =  Timestamp.valueOf(arr[0]); 
				  
				  
			int statusCode = Integer.parseInt(arr[3]);
				  
				  
			SqlModel sqlFile = new SqlModel(requestDate, arr[1], arr[2], statusCode, arr[4]);
				  
				  
			sqlFileObject.add(sqlFile);
			  
		}
 
		db.insertRawLogData(sqlFileObject);


		// Search Code
		Timestamp searchTime = Timestamp.valueOf(startDate.replace(".", " "));
		 
		List<RobotIP> robotIps = db.searchData(searchTime, duration, threshold);

		// Printing IpAddress on Console
		for (RobotIP ri : robotIps) {
			System.out.println(ri.getIpAddress());
		}

		// Insert Why IP Blocked
		db.insertRobotIP(robotIps);
	}

	
	
}