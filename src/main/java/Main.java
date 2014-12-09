import au.com.bytecode.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Main{
    private static CSVWriter writer;
    private static final String flatTypes[] = new String[]{"03", "04", "05"};
    private static final String towns[] = new String[]{"QT++++++Queenstown", "BD++++++Bedok"};

    public static void main(String[] args) {
        try {
            writer = new CSVWriter(new FileWriter("hdb.csv"), ',');
            writer.writeNext(new String[]{"block", "Street name", "story", "floor area", "lease commence date", "resale price", "resale registration date"});

            String url;
            for(int i = 0; i < flatTypes.length; i++){
                String flatType = flatTypes[i];
                for(int j = 0; j < towns.length; j++){
                    String town = towns[j];
                    url = "http://services2.hdb.gov.sg/webapp/BB33RTIS/BB33SSearchWidget?q=&Process=continue&FLAT_TYPE=" + flatType  + "&NME_NEWTOWN=" + town;
                    url += "&NME_STREET=&NUM_BLK_FROM=&NUM_BLK_TO=&AMT_RESALE_PRICE_FROM=&AMT_RESALE_PRICE_TO=&DTE_APPROVAL_FROM=201401&DTE_APPROVAL_TO=201412&DTE_APPROVAL_TO2=";
                    System.out.println(url);
                    writeToCSV(url);
                }
            }

            writer.close();
            System.out.println("Done");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToCSV(String url) throws IOException {
        Element doc = Jsoup.connect(url).get();
        Elements rows = doc.select("#myScrollTable tbody tr");

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements cols = row.select("td");
            ArrayList<String> entries = new ArrayList<String>();
            for(int j = 0; j < cols.size(); j++){
                entries.add(cols.get(j).text());
            }
            String entriesInArray[] = new String[entries.size()];
            entriesInArray = entries.toArray(entriesInArray);
            writer.writeNext(entriesInArray);
        }
    }
}