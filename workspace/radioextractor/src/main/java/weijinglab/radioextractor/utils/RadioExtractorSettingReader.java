package weijinglab.radioextractor.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 抽出に関する設定ファイルを読み込むクラス 
 * @author HuangWeijing
 * @version 20151220
 */
public class RadioExtractorSettingReader {

	private final static String settingFileName = "extractor_settings.properties";
	
	private Properties extractorSettings = new Properties();
	private static RadioExtractorSettingReader instance;
	
	public static RadioExtractorSettingReader getInstance() {
		if(instance == null) {
			instance = new RadioExtractorSettingReader();
		}
		return instance;
	}
	
	private RadioExtractorSettingReader() {
		try {
			extractorSettings.load(new FileInputStream(settingFileName));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getSetting(String key) {
		return extractorSettings.getProperty(key);
	}
	
	
	
}
