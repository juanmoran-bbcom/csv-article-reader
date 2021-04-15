package com.bodybuilding;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        BufferedReader csvReader = null;
        HashMap<String, Set<String>> resultMap= new HashMap<>();
        //Audit
        int counter = 1;
        Instant start = Instant.now();
        try {
            csvReader = new BufferedReader(new FileReader("/Users/bbdev/Downloads/articles_with_store_link_3.csv"));
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                CSVArticle csvArticle = buildArticleFromRow(data);
                if (csvArticle != null && !StringUtils.isEmpty(csvArticle.findURL())){
                    System.out.println("Analyzing article "+csvArticle.findURL() +", counter="+counter++);
                    if (!resultMap.containsKey(csvArticle.findURL())){
                        resultMap.put(csvArticle.findURL(), new HashSet<String>());
                    }
                    List<String> extractedUrls = extractUrls(csvArticle.getContent());
                    for (String url : extractedUrls)
                    {
                        if (url.contains("bodybuilding.com/store/")){
                            url = cleanUrl(url);
                            System.out.println("Adding "+url);
                            (resultMap.get(csvArticle.findURL())).add(url);                            
                        }
                    }
                }
            }
            csvReader.close();
            printMapResult(resultMap);
            //audit
            Instant finish = Instant.now();
            long timeElapsedMinutes = Duration.between(start, finish).toMinutes();
            long timeElapsedMillis = Duration.between(start, finish).toMillis();
            System.out.println("It took "+timeElapsedMillis+" millis / "+timeElapsedMinutes+" minutes");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printMapResult(HashMap<String, Set<String>> resultMap) {
        try{
            FileWriter resultWriter = new FileWriter("/Users/bbdev/Downloads/result_"+System.currentTimeMillis()+".txt");
            BufferedWriter bufferedWriter = new BufferedWriter(resultWriter);

            for (String urlArticle: resultMap.keySet()) {
                Set<String> extractedUrls = resultMap.get(urlArticle);
                
                if (extractedUrls != null && extractedUrls.size()>0){
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                    bufferedWriter.newLine();
                    bufferedWriter.write("In " +urlArticle+", these links have been added to it historically:");
                    for (String url : extractedUrls)
                    {
                        bufferedWriter.newLine();
                        bufferedWriter.write(url);
                    }                    
                }
            }
            bufferedWriter.close();
            resultWriter.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private static String cleanUrl(String url) {
        if (url.contains(")")){
            return url.substring(0, url.indexOf(")"));
        }
        return url;
    }

    private static CSVArticle buildArticleFromRow(String[] data) {
        if (data != null && data.length>1){
            CSVArticle csvArticle = new CSVArticle();
            csvArticle.setContentId(data[1].trim());
            csvArticle.setUrl1(data[2].trim());
            csvArticle.setUrl2(data[3].trim());
            csvArticle.setContent(data[4].trim());
            return csvArticle;
        }
        return null;
    }
    /**
     * Returns a list with all links contained in the input
     */
    public static List<String> extractUrls(String text)
    {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|http|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find())
        {
            containedUrls.add(text.substring(urlMatcher.start(1),
                    urlMatcher.end()));
        }

        return containedUrls;
    }
}
