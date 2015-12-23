package weijinglab.radioextractor.kicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import weijinglab.radioextractor.utils.CommonUtils;
import weijinglab.radioextractor.utils.RadioExtractorSettingReader;
import weijinglab.radioextractor.utils.SettingConstants;
import weijinglab.radiotable.entity.ProgramEntry;
import weijinglab.radiotable.entity.TimeTable;
import weijinglab.radiotable.net.webpage.HtmlExtractor;

public class ExtractorKicker {
	
	private static Logger logger = Logger.getLogger(ExtractorKicker.class);

	public static boolean runningFlag;
	
	public static void startListening() {
		TimeTable timeTable = new TimeTable();
		HtmlExtractor htmlExtractor = new HtmlExtractor();
		RadioExtractorSettingReader settingReader = RadioExtractorSettingReader.getInstance();
		
		try {
			htmlExtractor.setTimetableUrl(settingReader.getSetting(SettingConstants.RADIO_WEB_URL));
			htmlExtractor.fillTimeTable(timeTable);
		}  catch (IOException e) {
			e.printStackTrace();
		}
		
		List<ProgramEntry> allProgramList = timeTable.getAllProgramList();
		List<ProgramEntry> deleteProgramList = new ArrayList<ProgramEntry>();
		ProgramEntry currentProgramEntry = null;
		
		while(runningFlag) {

			
			//番組がなければ、スケジュールを取り直す
			if(allProgramList == null || 
					allProgramList.size() == 0) {
				try {
					htmlExtractor.fillTimeTable(timeTable);
					allProgramList = timeTable.getAllProgramList();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			Date currentTime = new Date();
			for(ProgramEntry programEntry: allProgramList) {
				Date endTime = DateUtils.addMinutes(
						programEntry.getStartTime(), programEntry.getDuration());
				//番組が終わった場合、番組リストから取り消す。
				if(endTime.compareTo(currentTime) < 0) {
					deleteProgramList.add(programEntry);
				}
				//開始時間と終了時間の間であれば、録画をスタートする
				//スタートした番組を番組リストから削除する。
				if(endTime.compareTo(currentTime) > 0
						&& programEntry.getStartTime().compareTo(currentTime) <= 0) {
					currentProgramEntry = programEntry;
					deleteProgramList.add(programEntry);
				}
			}

			for(ProgramEntry programEntry: deleteProgramList) {
				allProgramList.remove(programEntry);
			}
			
			//番組録画を起動する。
			startExtraction(currentProgramEntry);
			
			//一分後にまたスキャンする。
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public static void startExtraction(ProgramEntry currentProgram) {
		logger.info(String.format("「%s」録画スタート!保存ファイル名「%s.mp3」"
				, currentProgram.getProgramName()
				, CommonUtils.convertToYyyyMMdd_ssmm(currentProgram.getStartTime())));
//		TimeTable timeTable = new TimeTable();
//		HtmlExtractor htmlExtractor = new HtmlExtractor();
		RadioExtractorSettingReader settingReader = RadioExtractorSettingReader.getInstance();
		
		try {
			Date currentTime = new Date();
			
//			htmlExtractor.setTimetableUrl(settingReader.getSetting(SettingConstants.RADIO_WEB_URL));
//			htmlExtractor.fillTimeTable(timeTable);
//			ProgramEntry currentProgram = timeTable.getCurrentProgram(new Date());
			System.out.println(currentProgram.getProgramName());
			Date endTime = DateUtils.addMinutes(
					currentProgram.getStartTime(), currentProgram.getDuration());
			Long lastSeconds = (endTime.getTime() - currentTime.getTime()) / 1000;
			
			String rtmpdumpParamStr = String.format(
					settingReader.getSetting(SettingConstants.RTMPDUMP_PARAM_STR), 0, lastSeconds
					, CommonUtils.convertToYyyyMMdd_ssmm(currentProgram.getStartTime()));
			
			List<String> commandList = new ArrayList<String>();
			commandList.add(settingReader.getSetting(SettingConstants.RTMPDUMP_EXE_PATH));
			commandList.addAll(Arrays.asList(rtmpdumpParamStr.split("##")));
		
			ProcessBuilder rtmpdumpProcessBuilder = new ProcessBuilder(commandList);
//			rtmpdumpProcessBuilder.

			
			System.out.println(rtmpdumpProcessBuilder.command());
			rtmpdumpProcessBuilder.redirectErrorStream(true);
			
			rtmpdumpProcessBuilder.redirectOutput(new File(
					settingReader.getSetting(SettingConstants.RTMPDUMP_INFO_LOG)));
			rtmpdumpProcessBuilder.redirectError(new File(
					settingReader.getSetting(SettingConstants.RTMPDUMP_ERR_LOG)));
			
			Process process = rtmpdumpProcessBuilder.start();
			process.waitFor();
			
			System.out.println(process.exitValue());
			startConvert2Mp3(CommonUtils.convertToYyyyMMdd_ssmm(currentProgram.getStartTime()));
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void startConvert2Mp3(String filename) {
		RadioExtractorSettingReader settingReader = RadioExtractorSettingReader.getInstance();
		String ffmpegParamStr = String.format(
				settingReader.getSetting(SettingConstants.FFMPEG_PARAM_STR)
				, filename, filename);
		
		List<String> commandList = new ArrayList<String>();
		commandList.add(settingReader.getSetting(SettingConstants.FFMPEG_EXE_PATH));
		commandList.addAll(Arrays.asList(ffmpegParamStr.split("##")));
		
		ProcessBuilder ffmpegProcessBuilder = new ProcessBuilder(commandList);
		ffmpegProcessBuilder.redirectOutput(new File(
				settingReader.getSetting(SettingConstants.FFMPEG_INFO_LOG)));
		ffmpegProcessBuilder.redirectError(new File(
				settingReader.getSetting(SettingConstants.FFMPEG_ERR_LOG)));
		
		try {
			Process process = ffmpegProcessBuilder.start();
			process.waitFor();
			System.out.println(process.exitValue());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
