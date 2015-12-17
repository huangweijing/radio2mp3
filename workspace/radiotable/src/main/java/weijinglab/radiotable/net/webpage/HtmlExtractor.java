package weijinglab.radiotable.net.webpage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

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
	
	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
//		InputStream webpageStream = extractUrl("http://www.agqr.jp/timetable/streaming.php");
		Document document = Jsoup.connect("http://www.agqr.jp/timetable/streaming.php").get();
//		System.out.println(document.html());
		Elements elements = document.getElementsByClass("scrollBody");
		if(elements.size() > 0)
		{
			Element tableElement = elements.get(0);
			System.out.println(tableElement.html());
			Elements trElements = tableElement.getElementsByTag("tr");
			System.out.println(trElements.size());
		}
//		System.out.println(elements.size());
//		for(Element element : elements) {
//			System.out.println(element.html());
//		}
		
	}

}
