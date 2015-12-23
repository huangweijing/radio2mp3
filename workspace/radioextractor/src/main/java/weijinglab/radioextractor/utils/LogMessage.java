package weijinglab.radioextractor.utils;

/**
 * ログメッセージを定義するクラス
 * @author HuangWeijing
 * @version 20151223
 */
public final class LogMessage {

	/** 設定ファイル読み込みログ */
	public final static String LOG_SETTING_LOAD_COMPLETED =
			"設定ファイルの取得が完了しました！";
	/** 番組表取得します */
	public final static String LOG_RADIOTABLE_LOADING =
			"ラジオ番組表を取得します...";
	/** 番組表取得が完了 */
	public final static String LOG_RADIOTABLE_LOAD_COMPLETED =
			"ラジオ番組表の取得が完了しました！";
	/** 番組表抽出が失敗 */
	public final static String LOG_RADIOTABLE_LOAD_FAILED =
			"ラジオ番組表の取得が失敗しました！";
	/** 抽出可能な番組がないため、番組表を再度取得します。 */
	public final static String LOG_RADIOTABLE_RELOAD =
			"抽出可能な番組がないため、番組表を再度取得します。";
	
	
}
