package weijinglab.radioextractor.utils;

/**
 * 抽出ツールの設定値などの管理
 * @author HuangWeijing
 * @version 20151221
 */
public final class SettingConstants {

	/** ラジオスケジュールのURL */
	public final static String RADIO_WEB_URL = "net.timetable";
	/** ダウンロードしたファイルやmp3の保存場所 */
	public final static String FOLDER_TO_SAVE = "file.folder";
	/** ffmpegの置き場所 */
	public final static String FFMPEG_EXE_PATH = "extractor.tools.ffmpeg.exepath";
	/** ffmpegのパラメーター */
	public final static String FFMPEG_PARAM_STR = "extractor.tools.ffmpeg.param";
	/** ffmpegのインフォログ出力先 */
	public final static String FFMPEG_INFO_LOG = "extractor.tools.ffmpeg.infolog";
	/** ffmpegのエラーログ出力先 */
	public final static String FFMPEG_ERR_LOG = "extractor.tools.ffmpeg.errlog";
	/** ffmpegのmp3出力先 */
	public final static String FFMPEG_OUTPUT_PATH = "extractor.tools.ffmpeg.outputpath";
	/** rtmpdumpの置き場所 */
	public final static String RTMPDUMP_EXE_PATH = "extractor.tools.rtmpdump.exepath";
	/** rtmpdumpのパラメーター */
	public final static String RTMPDUMP_PARAM_STR = "extractor.tools.rtmpdump.param";
	/** rtmpdumpのインフォログ出力先 */
	public final static String RTMPDUMP_INFO_LOG = "extractor.tools.rtmpdump.infolog";
	/** rtmpdumpのエラーログ出力先 */
	public final static String RTMPDUMP_ERR_LOG = "extractor.tools.rtmpdump.errlog";
	/** rtmpdumpのflv出力先 */
	public final static String RTMPDUMP_OUTPUT_PATH = "extractor.tools.rtmpdump.outputpath";
	/** MP3AGIC転換出力先*/
	public final static String MP3AGIC_OUTPUT_PATH = "extractor.tools.mp3agic.outputpath";
	
	/** コマンドの分割符号 */
	public final static String COMMAND_SPLIT_CHAR = "##";
}
