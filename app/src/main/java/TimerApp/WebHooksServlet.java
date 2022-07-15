package TimerApp;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class WebHooksServlet extends HttpServlet {
    static final long serialVersionUID = 1L;
    private WebHooks webHooks;

    // Executed when servlet is first loaded into container.
    @Override
    public void init() {
        this.webHooks = new WebHooks();
        webHooks.setServletContext(this.getServletContext());
    }

    // GET /webHooks
    // GET /webHooks?id=1
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("id");
        Integer key = (param == null) ? null : Integer.valueOf((param.trim()));

        // If no query string, assume client made error
        if (key == null) {
            sendResponse(response, "Prvide id!");
        }
        // Otherwise, return the specified WebHook.
        else {
            WebHook webHook = webHooks.getConcurrentMap().get(key);
            if (webHook == null) { // no such WebHook
                String msg = key + " does not map to a webHook.\n";
                sendResponse(response, msg);
            }
            else { // requested WebHook found
                sendResponse(response, webHook.toJsonString());
            }
        }
    }

    // POST /webHooks
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        String url = request.getParameter("url");
        String interval = request.getParameter("interval");

        // Are the data to create a new webHook present?
        if (url == null || interval == null)
            throw new RuntimeException(Integer.toString(HttpServletResponse.SC_BAD_REQUEST));

        // Create a webHook.
        WebHook tempWebHook = new WebHook();
        tempWebHook.setUrl(url);
        tempWebHook.setInterval(Integer.parseInt(interval));

        // Save the ID of the newly created WebHook.
        int id = webHooks.addWebHook(tempWebHook);

        // Generate the confirmation message.
        String msg = "WebHook " + id + " created.\n";
        sendResponse(response, msg);
    }

    // PUT /webHooks
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        /* A workaround is necessary for a PUT request because Tomcat does not
           generate a workable parameter map for the PUT verb. */
        String key = null;
        String rest = null;
        try {
            BufferedReader br =
                new BufferedReader(new InputStreamReader(request.getInputStream()));
            String data = br.readLine();

            String[] args = data.split("#");      // url in args[0], rest in args[1]
            String[] parts1 = args[0].split("="); // url = parts1[1]
            key = parts1[1];

            String[] parts2 = args[1].split("="); // parts2[0] is key
            rest = parts2[1];
        }
        catch(Exception e) {
            throw new RuntimeException(Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }

        // If no key, then the request is ill formed.
        if (key == null)
            throw new RuntimeException(Integer.toString(HttpServletResponse.SC_BAD_REQUEST));

        // Look up the specified webHook.
        WebHook tempHook = new WebHook(key.trim(), Integer.valueOf(rest));
        Boolean checkIfExists = webHooks.getConcurrentMap().containsValue(tempHook);
        if (checkIfExists) { 
            if (rest == null) {
                throw new RuntimeException(Integer.toString(HttpServletResponse.SC_BAD_REQUEST));
            }
            else {
                boolean checkIfUpdated = webHooks.updateWebHook(tempHook);

                String msg = "WebHook has" + (checkIfUpdated ? null : " not") + " been edited.\n";
                sendResponse(response, msg);
            }
        }
        else { // found
           
            String msg = key + " does not map to a webHook.\n";
            sendResponse(response, msg);
        }
    }

    // DELETE /webHooks?url
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String param = request.getParameter("url");
        String key = (param == null) ? null : String.valueOf((param.trim()));
        // Only one WebHook can be deleted at a time.
        if (key == null)
            throw new RuntimeException(Integer.toString(HttpServletResponse.SC_BAD_REQUEST));
        try {
            WebHook tempWebHook = new WebHook(param,0);
            Boolean check = webHooks.deleteWebHook(tempWebHook);
            String msg = "WebHook " + (check ? "was" :"was not") + " removed.\n";
            sendResponse(response, msg);
        }
        catch(Exception e) {
            throw new RuntimeException(Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }

    // Methods Not Allowed
    @Override
    public void doTrace(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException(Integer.toString(HttpServletResponse.SC_METHOD_NOT_ALLOWED));
    }

    @Override
    public void doHead(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException(Integer.toString(HttpServletResponse.SC_METHOD_NOT_ALLOWED));
    }

    @Override
    public void doOptions(HttpServletRequest request, HttpServletResponse response) {
        throw new RuntimeException(Integer.toString(HttpServletResponse.SC_METHOD_NOT_ALLOWED));
    }

    // Send the response 
    private void sendResponse(HttpServletResponse response, String message) {
        try {
            OutputStream out = response.getOutputStream();
            out.write(message.getBytes());
            out.flush();
        }
        catch(Exception e) {
            throw new RuntimeException(Integer.toString(HttpServletResponse.SC_INTERNAL_SERVER_ERROR));
        }
    }
}
