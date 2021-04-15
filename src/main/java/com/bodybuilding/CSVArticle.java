package com.bodybuilding;
import org.apache.commons.lang3.StringUtils;

public class CSVArticle {

    String contentId;
    String url1;
    String url2;
    String content;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String findURL(){
        if (!StringUtils.isEmpty(url1) && url1.startsWith("http")){
            return url1;
        }
        if (!StringUtils.isEmpty(url2) && url2.startsWith("http")){
            return url2;
        }
        
        return StringUtils.EMPTY;
    }
}
