package weijinglab.radiotable.net.webpage;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import weijinglab.radiotable.entity.ProgramEntry;
import weijinglab.radiotable.entity.TimeTable;
import weijinglab.radiotable.utils.RadioTableUtil;

/**
 * 
 * @author HuangWeijing
 *
 */
public class HtmlExtractor {
	
	/** 番組スケジュールのURL */
	private String timetableUrl;
	/** 一日中最大の番組数 */
	private Integer maxSizeOfTimeTable = 100;
	/** メインテーブルのCSSクラス名 */
	private final static String TABLE_CLASS_NAME = "scrollBody";
	
	/**
	 * HTMLコードを分析して、タイムスケジュールに記入する
	 * @param timeTable
	 * @throws IOException
	 */
	public void fillTimeTable(TimeTable timeTable) throws IOException {
		//一週間分の番組 * maxSizeOfTimeTable
		Element tableArr[][] = new Element[7][maxSizeOfTimeTable];

		//
		Document document = Jsoup.connect(this.timetableUrl).get();
		Elements elements = document.getElementsByClass(TABLE_CLASS_NAME);
		if(elements.size() > 0)
		{
			Element tableElement = elements.get(0);
			Elements trElements = tableElement.getElementsByTag(HtmlConstants.TR);
			Integer trIdx = 1;
			for(Element trElement : trElements) {
				
				trIdx++;
				
				Elements tdElements = trElement.getElementsByTag(HtmlConstants.TD);
				for(Element tdElement : tdElements) {
					//rowspan属性を取得する
					String strRowSpan = tdElement.attr(HtmlConstants.ROWSPAN);
					Integer intRowSpan = 1;
					if(!StringUtils.isEmpty(strRowSpan)) {
						intRowSpan = new Integer(strRowSpan);
					}
					Pair<Integer, Integer> minNotBlankVector = getMinNotBlank(tableArr);
					for(int i=0; i < intRowSpan; i++) {
						tableArr[minNotBlankVector.getLeft()][minNotBlankVector.getRight() + i] = tdElement; 
					}
					ProgramEntry programEntry = createProgramEntryByTd(tdElement);
					
					timeTable.addProgramEntry(
							RadioTableUtil.convertArrayIndexToWeekday(minNotBlankVector.getLeft())
							, programEntry);
						
//					System.out.println(String.format("%s, %s"
//							, minNotBlankVector.getLeft(), minNotBlankVector.getRight()));
				}
			}
		}
	}
	
	private ProgramEntry createProgramEntryByTd(Element tdElement) {
		ProgramEntry programEntry = new ProgramEntry();
		Elements timeElements = tdElement.getElementsByTag("time");
		if(timeElements.size() > 0) {
			System.out.println(timeElements.get(0).text());
		}
		return programEntry;
	}
	
	/**
	 * 行列の中に要素の少ない列と最大要素のインデックスを返す
	 * @param elementDiArray
	 * @return （行列の中に要素の少ない列、最大要素のインデックス）
	 */
	private static Pair<Integer, Integer> getMinNotBlank(Element[][] elementDiArray) {
		Integer diarrIndex = 0;
		//複数の列の中に、要素が一番少ない最後要素のインデックス
		Integer minIndex = Integer.MAX_VALUE;
		//要素が一番少ない列のインデックス
		Integer minIndexRelatedIndex = -1; 
		for(Element[] elementArray : elementDiArray) {
			//NULLじゃない最小のインデックスを抽出する
			Integer index = 0;
			for(Element element : elementArray) {
				if(element == null) {
					break;
				}
				index++;
			}
			//最小のインデックスより小さいものがあれば、書き換える
			if(minIndex > index) {
				minIndex = index;
				minIndexRelatedIndex = diarrIndex;
			}
			diarrIndex++;
		}
		if(minIndexRelatedIndex == -1) {
			return Pair.of(0, -1);
		}
		return Pair.of(minIndexRelatedIndex, minIndex);
	}
	
	/**
	 * @return 番組スケジュールのURL
	 */
	public String getTimetableUrl() {
		return timetableUrl;
	}

	/**
	 * @param timetableUrl 番組スケジュールのURL to set
	 */
	public void setTimetableUrl(String timetableUrl) {
		this.timetableUrl = timetableUrl;
	}

	/**
	 * @return 一日中最大の番組数
	 */
	public Integer getMaxSizeOfTimeTable() {
		return maxSizeOfTimeTable;
	}

	/**
	 * @param maxSizeOfTimeTable 一日中最大の番組数 to set
	 */
	public void setMaxSizeOfTimeTable(Integer maxSizeOfTimeTable) {
		this.maxSizeOfTimeTable = maxSizeOfTimeTable;
	}
	
	public static void main(String[] args) throws IOException {
		HtmlExtractor htmlExtractor = new HtmlExtractor();
		TimeTable timeTable = new TimeTable();
		htmlExtractor.setTimetableUrl("http://www.agqr.jp/timetable/streaming.php");
		htmlExtractor.fillTimeTable(timeTable);		
	}


}
