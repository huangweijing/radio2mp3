package weijinglab.radiotable.net.webpage;

import java.io.IOException;
import java.util.Calendar;

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
	
	/** 番組スケジュールのURL. */
	private String timetableUrl;
	/** 一日中最大の番組数. */
	private Integer maxSizeOfTimeTable = 100;
	/** メインテーブルのCSSクラス名. */
	private final static String TABLE_CLASS_NAME = "scrollBody";
	/** 一週間に７日がある */
	private final static Integer DAY_COUNT_OF_WEEK = 7;
	/** 一日の開始時間 */
	private final static Integer START_HOUR_OF_DAY = 6;
	/** 一区切りの長さ（min） */
	private final static Integer DURATION_FOR_ONE_SPAN = 30;
	
	/**
	 * HTMLコードを分析して、タイムスケジュールに記入する
	 * @param timeTable
	 * @throws IOException
	 */
	public void fillTimeTable(TimeTable timeTable) throws IOException {
		//一週間分の番組 * maxSizeOfTimeTable
		Element tableArr[][] = new Element[DAY_COUNT_OF_WEEK][maxSizeOfTimeTable];

		//
		Document document = Jsoup.connect(this.timetableUrl).get();
		
//		System.out.println(document.html());
		
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
					ProgramEntry programEntry = createProgramEntryByTd(
							RadioTableUtil.convertArrayIndexToWeekday(minNotBlankVector.getLeft())
							, tdElement);
					
					timeTable.addProgramEntry(
							RadioTableUtil.convertArrayIndexToWeekday(minNotBlankVector.getLeft())
							, programEntry);
						
//					System.out.println(String.format("%s, %s"
//							, minNotBlankVector.getLeft(), minNotBlankVector.getRight()));
				}
			}
		}
	}
	
	/**
	 * 抽出したテーブルセルから番組の情報入手
	 * @param dayOfWeek
	 * @param tdElement
	 * @return
	 */
	private ProgramEntry createProgramEntryByTd(Integer dayOfWeek, Element tdElement) {
		ProgramEntry programEntry = new ProgramEntry();
		//当日日付を取得する
		Calendar programStartDate = Calendar.getInstance();
		
		Integer arrIdxWeekday = RadioTableUtil.convertWeekdayToArrayIndex(dayOfWeek);
		Integer todayArrIdxWeekday = RadioTableUtil.convertWeekdayToArrayIndex(
				programStartDate.get(Calendar.DAY_OF_WEEK));
//		System.out.println(String.format("%s %s", arrIdxWeekday, todayArrIdxWeekday));
		programStartDate.add(Calendar.DATE, arrIdxWeekday - todayArrIdxWeekday);
		
//		System.out.println(programStartDate.get(Calendar.DATE));
		
		Elements timeElements = tdElement.getElementsByClass("time");
		if(timeElements.size() > 0) {
			//番組表から開始時間を取得する
			String time = timeElements.get(0).text();
			String[] timeSplit = time.split(":");
			Integer hour = Integer.valueOf(timeSplit[0]);
			Integer minute = Integer.valueOf(timeSplit[1]);
			//開始時間が一日の開始時間より早かった場合は翌日日付とする
			if(hour < START_HOUR_OF_DAY) {
				programStartDate.add(Calendar.DATE, 1);
			}
			programStartDate.set(Calendar.HOUR_OF_DAY, hour);
			programStartDate.set(Calendar.MINUTE, minute);
			programStartDate.set(Calendar.SECOND, 0);
			programEntry.setStartTime(programStartDate.getTime());
			//System.out.println(programStartDate.getTime());
		}
		

		//番組の持続時間を取得する
		String strRowSpan = tdElement.attr(HtmlConstants.ROWSPAN);
		Integer intRowSpan = 1;
		if(!StringUtils.isEmpty(strRowSpan)) {
			intRowSpan = new Integer(strRowSpan);
		}
		programEntry.setDuration(DURATION_FOR_ONE_SPAN * intRowSpan);

		Elements titleElements = tdElement.getElementsByClass("title-p");
		if(titleElements.size() > 0) {
			programEntry.setProgramName(titleElements.get(0).text());
		}

		Elements titleLinkElements = tdElement.select(".title-p a");
		if(titleLinkElements.size() > 0) {
			programEntry.setProgramLink(titleLinkElements.get(0).attr("href"));
		}

		Elements casterElements = tdElement.select(".rp");
		if(casterElements.size() > 0 
				&& !StringUtils.isEmpty(casterElements.get(0).text())) {
			programEntry.setCaster(casterElements.get(0).text());
		}
		
		Elements casterMailElements = tdElement.select(".rp a");
		if(casterMailElements.size() > 0) {
			String mailAddress = casterMailElements.get(0).attr("href");
			programEntry.setCasterMail(mailAddress.replace("mailto:", StringUtils.EMPTY));
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

}
