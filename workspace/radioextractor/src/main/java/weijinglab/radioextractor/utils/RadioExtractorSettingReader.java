package weijinglab.radioextractor.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 抽出に関する設定ファイルを読み込むクラス.
 * @author HuangWeijing
 * @version 20151220
 */
public class RadioExtractorSettingReader {

	/** log4jログ記録 */
	private final static Logger logger = Logger.getLogger(
			RadioExtractorSettingReader.class);
	/** 設定ファイルのファイル名. */
	private final static String settingFileName = "extractor_settings.properties";
	/** 設定プロパティ. */
	private Properties extractorSettings = new Properties();
	/** Singleton instance. */
	private static RadioExtractorSettingReader instance;
	
	/**
	 * Singleton.
	 * @return Singleton instance
	 */
	public synchronized static RadioExtractorSettingReader getInstance() {
		if(instance == null) {
			instance = new RadioExtractorSettingReader();
		}
		return instance;
	}
	
	/**
	 * コンストラクター.
	 */
	private RadioExtractorSettingReader() {
		try {
			extractorSettings.load(new FileInputStream(settingFileName));
			logger.info(extractorSettings);
			logger.info(LogMessage.LOG_SETTING_LOAD_COMPLETED);
		} catch (IOException e) {
			logger.error(e);
			//設定ファイル読めなかったらエラーとする
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 設定を取得する
	 * @param key 設定キー
	 * @return 設定バリュー
	 */
	public String getSetting(String key) {
		return extractorSettings.getProperty(key);
	}
	
	
	
}
