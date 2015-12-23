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

/**
 * ラジオを抽出し、mp3ファイルに変換する。
 * @author HuangWeijing
 * @version 20151223
 */
public class ExtractionExecutor implements Runnable{
	/** ログ記録 */
	Logger logger = Logger.getLogger(ExtractionExecutor.class);
	/** 関連番組情報 */
	private ProgramEntry currentProgram;
	/** コンストラクターよって番組情報を設定する. */
	public ExtractionExecutor(ProgramEntry currentProgram) {
		this.currentProgram = currentProgram;
	}
	/** 抽出を実行する */
	public void run() {
		startExtraction(currentProgram);
		startConvert2Mp3(currentProgram);
	}
	
	/**
	 * flvストリームを抽出する。
	 * @param currentProgram 抽出する番組情報
	 */
	private void startExtraction(ProgramEntry currentProgram) {
		logger.info(String.format("「%s」録画スタート!保存ファイル名「%s.mp3」"
				, currentProgram.getProgramName()
				, CommonUtils.convertToYyyyMMdd_ssmm(currentProgram.getStartTime())));
		RadioExtractorSettingReader settingReader = RadioExtractorSettingReader.getInstance();
		
		try {
			Date currentTime = new Date();
			
			//番組の残時間を取得する。
			Date endTime = DateUtils.addMinutes(
					currentProgram.getStartTime(), currentProgram.getDuration());
			Long lastSeconds = (endTime.getTime() - currentTime.getTime()) / 1000;
			
			//RTMPDUMP引数文字列を作成する。
			String filename = CommonUtils.convertToYyyyMMdd_ssmm(currentProgram.getStartTime());
			String rtmpdumpParamStr = String.format(
					settingReader.getSetting(SettingConstants.RTMPDUMP_PARAM_STR)
					, 0
					, lastSeconds
					, filename);
			
			//ProcessBuilderの引数フォーマットに合わせる。
			List<String> commandList = new ArrayList<String>();
			commandList.add(settingReader.getSetting(SettingConstants.RTMPDUMP_EXE_PATH));
			commandList.addAll(Arrays.asList(rtmpdumpParamStr.split(
					SettingConstants.COMMAND_SPLIT_CHAR)));
		
			ProcessBuilder rtmpdumpProcessBuilder = new ProcessBuilder(commandList);
			
			rtmpdumpProcessBuilder.redirectErrorStream(true);
			//INFOログを出力する。
			File infoLog = new File(
					String.format(settingReader.getSetting(SettingConstants.RTMPDUMP_INFO_LOG), filename)); 
			if(!infoLog.getParentFile().exists()) {
				infoLog.getParentFile().mkdir();
			}
			rtmpdumpProcessBuilder.redirectOutput(infoLog);
			
			//ERRログを出力する。
			File errLog = new File(
					String.format(settingReader.getSetting(SettingConstants.RTMPDUMP_INFO_LOG), filename)); 
			if(!errLog.getParentFile().exists()) {
				errLog.getParentFile().mkdir();
			}
			rtmpdumpProcessBuilder.redirectError(errLog);
			
			Process process = rtmpdumpProcessBuilder.start();
			process.waitFor();
			
			System.out.println(process.exitValue());
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * mp3転換を実行する
	 * @param currentProgram 転換する番組情報
	 */
	private void startConvert2Mp3(ProgramEntry currentProgram) {
		//ファイル名を定義する（日付＋時間）
		String filename = CommonUtils.convertToYyyyMMdd_ssmm(currentProgram.getStartTime());
		RadioExtractorSettingReader settingReader = RadioExtractorSettingReader.getInstance();
		//引数
		String ffmpegParamStr = String.format(
				settingReader.getSetting(SettingConstants.FFMPEG_PARAM_STR)
				, filename, filename);
		
		List<String> commandList = new ArrayList<String>();
		commandList.add(settingReader.getSetting(SettingConstants.FFMPEG_EXE_PATH));
		commandList.addAll(Arrays.asList(ffmpegParamStr.split(SettingConstants.COMMAND_SPLIT_CHAR)));
		
		ProcessBuilder ffmpegProcessBuilder = new ProcessBuilder(commandList);
		//INFOログを出力する。
		File infoLog = new File(
				String.format(settingReader.getSetting(SettingConstants.FFMPEG_INFO_LOG), filename)); 
		if(!infoLog.getParentFile().exists()) {
			infoLog.getParentFile().mkdir();
		}
		ffmpegProcessBuilder.redirectOutput(infoLog);
		
		//ERRログを出力する。
		File errLog = new File(
				String.format(settingReader.getSetting(SettingConstants.FFMPEG_ERR_LOG), filename)); 
		if(!errLog.getParentFile().exists()) {
			errLog.getParentFile().mkdir();
		}
		ffmpegProcessBuilder.redirectError(errLog);
		
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
