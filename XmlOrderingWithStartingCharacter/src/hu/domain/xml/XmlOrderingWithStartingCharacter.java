package hu.domain.xml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlOrderingWithStartingCharacter {
	public static List<String> datas;
	public static char initialParameter;

	public static void main(String[] args) throws ParserConfigurationException, Exception, IOException {
		Document doc = readXmlFile();
		readRecord(doc);
		Collections.sort(datas, new Comparator<String>() {

			@Override
			public int compare(String firstString, String secondString) {
				return firstString.compareToIgnoreCase(secondString);
			}
		});
		try (Scanner scannerParameter = new Scanner(System.in)) {
			System.out.println("Initial parameter: ");
			String str = scannerParameter.next();
			initialParameter = str.charAt(0);
			sortByInitial(initialParameter);
		}

	}

	// Read XML file
	private static Document readXmlFile() throws ParserConfigurationException, SAXException, IOException {
		File inputFile = new File("./src/test-data.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(inputFile);
		doc.getDocumentElement().normalize();
		return doc;
	}

	// Read record tag
	private static void readRecord(Document doc) {
		NodeList nList = doc.getElementsByTagName("record");
		datas = new ArrayList<>();
		for (int i = 0; i < nList.getLength(); i++) {
			Node nNode = nList.item(i);
			getNamesAndCountries(nNode);
		}
		datas.removeIf(Objects::isNull);
	}

	// Get names and countries from element node
	private static void getNamesAndCountries(Node nNode) {
		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
			Element eElement = (Element) nNode;
			datas.add(eElement.getElementsByTagName("name").item(0).getTextContent() + ", "
					+ eElement.getElementsByTagName("country").item(0).getTextContent());
		}
	}

	// Sort names by initial letter
	private static void sortByInitial(char initialParameter) throws IOException {
		int numberOfElements = 0;
		for (int elementNumber = 1; elementNumber < datas.size(); elementNumber++) {
			numberOfElements = Collections.frequency(datas, datas.get(elementNumber));
			if (numberOfElements != 1) {
					if (!datas.get(elementNumber).equals(datas.get(elementNumber + 1))) {
						printNames(initialParameter, numberOfElements, elementNumber);
					}
			} else 
				{
				printNames(initialParameter, numberOfElements, elementNumber);
			}
		}
}

	// Print names in an organized form
	private static void printNames(char initialParameter, int numberOfElements, int elementNumber) {
		if (String.valueOf(datas.get(elementNumber).substring(0, 1).toLowerCase())
				.equals(String.valueOf(initialParameter).toLowerCase())) {
			System.out.println(datas.get(elementNumber));
		}
	}
}
