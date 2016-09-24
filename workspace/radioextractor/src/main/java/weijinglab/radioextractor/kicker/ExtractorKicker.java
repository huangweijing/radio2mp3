package weijinglab.radioextractor.kicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;

import weijinglab.radioextractor.utils.LogMessage;
import weijinglab.radioextractor.utils.RadioExtractorSettingReader;
import weijinglab.radioextractor.utils.SettingConstants;
import weijinglab.radiotable.entity.ProgramEntry;
import weijinglab.radiotable.entity.TimeTable;
import weijinglab.radiotable.net.webpage.HtmlExtractor;

/**
 * 番組抽出モジュールを起動するクラス
 * @author HuangWeijing
 * @version 20151223
 */
public class ExtractorKicker {
	
	/** ログ記録 */
	private static Logger logger = Logger.getLogger(ExtractorKicker.class);
	/** 実行フラグ（trueに設定すると実行中になる） */
	public static boolean runningFlag;
	
	/**
	 * 番組表のリスニングを開始する。 
	 */
	public static void startListening() {
		TimeTable timeTable = new TimeTable();
		HtmlExtractor htmlExtractor = new HtmlExtractor();
		RadioExtractorSettingReader settingReader = RadioExtractorSettingReader.getInstance();
		
		try {
			//一週間分の番組表を取得する。
			logger.info(LogMessage.LOG_RADIOTABLE_LOADING);
			htmlExtractor.setTimetableUrl(settingReader.getSetting(SettingConstants.RADIO_WEB_URL));
			htmlExtractor.fillTimeTable(timeTable);
			logger.info(LogMessage.LOG_RADIOTABLE_LOAD_COMPLETED);
		}  catch (IOException e) {
			e.printStackTrace();
		}
		//一週間分の番組をプーリングする。
		List<ProgramEntry> allProgramList = timeTable.getAllProgramList();
		//削除とした番組をここに入れる。
		List<ProgramEntry> deleteProgramList = new ArrayList<ProgramEntry>();
		
		while(runningFlag) {

			//今抽出しようとする番組
			ProgramEntry currentProgramEntry = null;
			
			//番組がなくなれば、スケジュールを取り直す
			if(allProgramList == null || allProgramList.size() == 0) {
				try {
					logger.info(LogMessage.LOG_RADIOTABLE_RELOAD);
					logger.info(LogMessage.LOG_RADIOTABLE_LOADING);
					htmlExtractor.fillTimeTable(timeTable);
					allProgramList = timeTable.getAllProgramList();
					logger.info(LogMessage.LOG_RADIOTABLE_LOAD_COMPLETED);
				} catch (IOException e) {
					logger.info(LogMessage.LOG_RADIOTABLE_LOAD_FAILED);
					e.printStackTrace();
				}
			}
			
			//現在時刻
			Date currentTime = new Date();
			for(ProgramEntry programEntry: allProgramList) {
				//終了時刻を計算する
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

			//削除リストに入ったものは削除すべし
			for(ProgramEntry programEntry: deleteProgramList) {
				allProgramList.remove(programEntry);
			}

			if(currentProgramEntry != null) {
				//番組録画を起動する。
				Thread extractorKickerThread = new Thread(
						new ExtractionExecutor(currentProgramEntry));
				extractorKickerThread.start();
			}
			
			//一分後にまたスキャンする。
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
}
