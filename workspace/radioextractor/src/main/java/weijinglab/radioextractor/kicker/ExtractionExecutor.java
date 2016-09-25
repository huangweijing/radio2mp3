package weijinglab.radioextractor.kicker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import weijinglab.radioextractor.utils.CommonUtils;
import weijinglab.radioextractor.utils.ExtractionConstants;
import weijinglab.radioextractor.utils.LogMessage;
import weijinglab.radioextractor.utils.RadioExtractorSettingReader;
import weijinglab.radioextractor.utils.SettingConstants;
import weijinglab.radiotable.entity.ProgramEntry;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * ラジオを抽出し、mp3ファイルに変換する。
 * @author HuangWeijing
 * @version 20151223
 */
public class ExtractionExecutor implements Runnable{
	/** ログ記録 */
	Logger logger = Logger.getLogger(ExtractionExecutor.class);

	RadioExtractorSettingReader settingReader = RadioExtractorSettingReader.getInstance();
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
	private boolean startExtraction(ProgramEntry currentProgram) {
		logger.info(String.format("「%s」録画スタート!保存ファイル名「%s.mp3」"
				, currentProgram.getProgramName()
				, CommonUtils.convertToYyyyMMdd_ssmm(currentProgram.getStartTime())));
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
					, String.format(ExtractionConstants.FILE_PATH_PATTERN
							, settingReader.getSetting(SettingConstants.RTMPDUMP_OUTPUT_PATH)
							, filename
							, ExtractionConstants.FILE_TYPE_FLV));
			
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

			//プログラム終了結果を取得する。
			Integer exitValue = process.exitValue();
			if(exitValue == 0) {
				logger.info(String.format(
						LogMessage.LOG_RADIO_EXTRACTION_COMPLETED, filename));
				return true;
			} else {
				logger.error(String.format(
						LogMessage.LOG_RADIO_EXTRACTION_FAILED, exitValue));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * mp3転換を実行する
	 * @param currentProgram 転換する番組情報
	 */
	private boolean startConvert2Mp3(ProgramEntry currentProgram) {
		//ファイル名を定義する（日付＋時間）
		String filename = CommonUtils.convertToYyyyMMdd_ssmm(currentProgram.getStartTime());
		//引数
		String ffmpegParamStr = String.format(
				settingReader.getSetting(SettingConstants.FFMPEG_PARAM_STR)
				, String.format(ExtractionConstants.FILE_PATH_PATTERN
						, settingReader.getSetting(SettingConstants.RTMPDUMP_OUTPUT_PATH)
						, filename
						, ExtractionConstants.FILE_TYPE_FLV) 
				,  String.format(ExtractionConstants.FILE_PATH_PATTERN
						, settingReader.getSetting(SettingConstants.FFMPEG_OUTPUT_PATH)
						, filename
						, ExtractionConstants.FILE_TYPE_MP3));
		
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
			//プログラム終了結果を取得する。
			Integer exitValue = process.exitValue();
			if(exitValue == 0) {
				logger.info(String.format(LogMessage.LOG_MP3_CONVERTION_COMPLETED, filename));
				//MP3ファイル名＆タグを編集する
				editMp3Tag(filename, currentProgram);
				return true;
			} else {
				logger.error(String.format(LogMessage.LOG_MP3_CONVERTION_FAILED, exitValue));
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (UnsupportedTagException e) {
			e.printStackTrace();
		} catch (InvalidDataException e) {
			e.printStackTrace();
		} catch (NotSupportedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean editMp3Tag(String filename, ProgramEntry currentProgram) 
			throws UnsupportedTagException, InvalidDataException, IOException, NotSupportedException {
		String srcMp3FullPath = String.format(ExtractionConstants.FILE_PATH_PATTERN
				, settingReader.getSetting(SettingConstants.FFMPEG_OUTPUT_PATH)
				, filename
				, ExtractionConstants.FILE_TYPE_MP3);
		String destMp3FullPath = String.format(ExtractionConstants.FILE_PATH_PATTERN
				, settingReader.getSetting(SettingConstants.MP3AGIC_OUTPUT_PATH)
				, String.format("%s_%s", filename, CommonUtils.deleteReservedPathChar(currentProgram.getProgramName())) 
				, ExtractionConstants.FILE_TYPE_MP3);
		
		Mp3File mp3file = new Mp3File(srcMp3FullPath);
		ID3v1 id3v1Tag;
		if (mp3file.hasId3v1Tag()) {
			  id3v1Tag =  mp3file.getId3v1Tag();
			} else {
			  // mp3 does not have an ID3v1 tag, let's create one..
			  id3v1Tag = new ID3v1Tag();
			  mp3file.setId3v1Tag(id3v1Tag);
		}
		id3v1Tag.setTrack(Integer.valueOf(2).toString());
		id3v1Tag.setArtist(CommonUtils.changeEncoding(currentProgram.getCaster()));
		id3v1Tag.setTitle(CommonUtils.changeEncoding(currentProgram.getProgramName()));
		id3v1Tag.setAlbum(CommonUtils.changeEncoding(ExtractionConstants.RADIO_ALBUM));
		id3v1Tag.setYear(Integer.valueOf(
				Calendar.getInstance().get(Calendar.YEAR)).toString());
		id3v1Tag.setComment(CommonUtils.changeEncoding(String.format("メール：%s \n 番組リンク：%s "
				, currentProgram.getCasterMail(), currentProgram.getProgramLink())));
		mp3file.save(destMp3FullPath);
		return false;
	}
}
