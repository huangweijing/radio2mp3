package weijinglab.radiotable.net.webpage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import weijinglab.radiotable.entity.TimeTable;

/**
 * 
 * @author HuangWeijing
 *
 */
public class HtmlExtractor {

	/**
	 * 
	 * @param pageUrl
	 * @return
	 * @throws IOException
	 */
	public static InputStream extractUrl(String pageUrl) throws IOException {
		URL url = new URL(pageUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return connection.getInputStream();
		} 
		return null;
	}
	
	public static void fillTimeTable(TimeTable timeTable) throws IOException {
		
		Element tableArr[][] = new Element[7][100];
		
//		InputStream webpageStream = extractUrl("http://www.agqr.jp/timetable/streaming.php");
		Document document = Jsoup.connect("http://www.agqr.jp/timetable/streaming.php").get();
//		System.out.println(document.html());
		Elements elements = document.getElementsByClass("scrollBody");
		if(elements.size() > 0)
		{
			Element tableElement = elements.get(0);
//			System.out.println(tableElement.html());
			Elements trElements = tableElement.getElementsByTag("tr");
			System.out.println(trElements.size());
			Integer trIdx = 1;
			for(Element trElement : trElements) {
				
				System.out.println(trIdx);
				trIdx++;
				
				Elements tdElements = trElement.getElementsByTag("td");
				for(Element tdElement : tdElements) {
					//rowspan属性を取得する
					String strRowSpan = tdElement.attr("rowspan");
					Integer intRowSpan = 1;
					if(!StringUtils.isEmpty(strRowSpan)) {
						intRowSpan = new Integer(strRowSpan);
					}
					Pair<Integer, Integer> minNotBlankVector = getMinNotBlank(tableArr);
					for(int i=0; i < intRowSpan; i++) {
						tableArr[minNotBlankVector.getLeft()][minNotBlankVector.getRight() + i] = tdElement; 
					}
					//minNotBlankVector = getMinNotBlank(tableArr);
					System.out.println(String.format("%s, %s"
							, minNotBlankVector.getLeft(), minNotBlankVector.getRight()));
				}
				//System.out.println(trElement.getElementsByTag("td").size());
			}
		}
//		System.out.println(elements.size());
//		for(Element element : elements) {
//			System.out.println(element.html());
//		}
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
	
	public static void main(String[] args) throws IOException {
		TimeTable timeTable = new TimeTable();
		fillTimeTable(timeTable);		
	}

}
