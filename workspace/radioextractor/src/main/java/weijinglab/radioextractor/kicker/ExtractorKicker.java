package weijinglab.radioextractor.kicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;

import weijinglab.radioextractor.utils.RadioExtractorSettingReader;
import weijinglab.radioextractor.utils.SettingConstants;
import weijinglab.radiotable.entity.ProgramEntry;
import weijinglab.radiotable.entity.TimeTable;
import weijinglab.radiotable.net.webpage.HtmlExtractor;

public class ExtractorKicker {

	public static void startExtraction() {
		TimeTable timeTable = new TimeTable();
		HtmlExtractor htmlExtractor = new HtmlExtractor();
		RadioExtractorSettingReader settingReader = RadioExtractorSettingReader.getInstance();
		
		try {
			Date currentTime = new Date();
			
			htmlExtractor.setTimetableUrl(settingReader.getSetting(SettingConstants.RADIO_WEB_URL));
			htmlExtractor.fillTimeTable(timeTable);
			ProgramEntry currentProgram = timeTable.getCurrentProgram(new Date());
			System.out.println(currentProgram.getProgramName());
			Date endTime = DateUtils.addMinutes(
					currentProgram.getStartTime(), currentProgram.getDuration());
			Long lastSeconds = (endTime.getTime() - currentTime.getTime()) / 1000;
			
			String rtmpdumpParamStr = String.format(
					settingReader.getSetting(SettingConstants.RTMPDUMP_PARAM_STR), 0, 100
//					, "test.flv");
					, currentProgram.getProgramName());
			
			List<String> commandList = new ArrayList<String>();
			commandList.add(settingReader.getSetting(SettingConstants.RTMPDUMP_EXE_PATH));
			commandList.addAll(Arrays.asList( rtmpdumpParamStr.split("##")));
		
			ProcessBuilder rtmpdumpProcessBuilder = new ProcessBuilder(
					commandList
					);
//			rtmpdumpProcessBuilder.

			
			System.out.println(rtmpdumpProcessBuilder.command());
			rtmpdumpProcessBuilder.redirectErrorStream(true);
			rtmpdumpProcessBuilder.redirectOutput(new File("d:\\log.txt"));
			rtmpdumpProcessBuilder.redirectError(new File("d:\\log2.txt"));
			
			Process process = rtmpdumpProcessBuilder.start();
			process.waitFor();
			System.out.println(process.exitValue());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
