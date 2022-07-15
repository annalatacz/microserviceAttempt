package TimerApp;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;    
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * Ignoring users and other security features
 */
public class WebHook {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    
    private Integer interval;
    private String url;
    private LocalDateTime startTime;


    public WebHook(String url, Integer interval){
        this.url = url;
        this.interval = interval;
        this.startTime = LocalDateTime.now();  
    }

    public WebHook(){
        this.url = null;
        this.interval = null;
        this.startTime = LocalDateTime.now();  
    }

    public String getUrl(){
        return this.url;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public Integer getInterval(){
        return this.interval;
    }

    public void setInterval(Integer interval){
        this.interval = interval;
    }

    public LocalDateTime getStartTime(){
        return this.startTime;
    }

    public void setStartTime(){
        this.startTime = LocalDateTime.now(); 
    }

    // for comparision and throwing errors - only based on url!
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        WebHook that = (WebHook) other;
        return this.url.equals(that.url);
    }

    public String toJsonString(){
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = null;
        try {
            jsonString = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
