package TimerApp;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletContext;

public class WebHooks {

    private ConcurrentMap<Integer, WebHook> webHooks;
    private ServletContext servletContext;
    private AtomicInteger mapKey;

    public WebHooks() {
        webHooks = new ConcurrentHashMap<Integer, WebHook>();
        mapKey = new AtomicInteger();
    }

    public void setServletContext(ServletContext servletContext) { this.servletContext = servletContext; }
    public ServletContext getServletContext() { return this.servletContext; }

    public ConcurrentMap<Integer, WebHook> getConcurrentMap() {
        if (getServletContext() == null) return null; // not initialized
        return this.webHooks;
    }

    public String toJson(WebHook webHook) {
        try {
            String jobt = webHook.toJsonString();
            return jobt;
        }
        catch(Exception e) { }
        return null;
    }

    public int addWebHook(WebHook webHook) {
        int id = mapKey.incrementAndGet();
        webHooks.put(id, webHook);
        return id;
    }

    public boolean deleteWebHook(WebHook webHook) {
        webHooks.forEach((tempInt,tempWebHook) -> {{if(tempWebHook.equals(webHook)){
            webHooks.remove(tempInt);
        }}});
        
        return !webHooks.containsValue(webHook);
    }

    public boolean updateWebHook(WebHook webHook) {
        webHooks.forEach((tempInt,tempWebHook) -> {{if(tempWebHook.equals(webHook)){
            tempWebHook.setInterval(webHook.getInterval());
        }}});
        
        return !webHooks.containsValue(webHook);
    }
}