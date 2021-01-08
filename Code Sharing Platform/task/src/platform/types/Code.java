package platform.types;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "code")
public class Code {

    @Id
    @JsonIgnore
    private UUID uuid;

    private String code;
    private String date;
    private long time;
    private int views;

    @JsonIgnore
    private boolean hidden = false;
    @JsonIgnore
    private boolean showViewsAnyway = false;

    private boolean timeRestricted;
    private boolean viewRestricted;

    @JsonIgnore
    private final LocalDateTime localDateTime;

    {
        localDateTime = LocalDateTime.now();
    }

    public Code() { }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UUID getId() {
        return uuid;
    }

    public void setId(UUID id) {
        this.uuid = id;
    }

    @JsonIgnore
    public String getAsHTML() {
        final String HTML_VIEW_RESTRICTED =
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>Code</title>\n" +
                        "    <style>" +
                        "       span {" +
                        "           color: green;" +
                        "       }"+
                        "       pre {" +
                        "           border: 1px solid gray;" +
                        "           background-color: #dec7f1;" +
                        "           padding: 7px 5px;" +
                        "           margin: 0px;" +
                        "           width: 500px;" +
                        "       }"+
                        "    </style>" +
                        "<link rel=\"stylesheet\"\n" +
                        "       target=\"_blank\" href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                        "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                        "<script>hljs.initHighlightingOnLoad();</script>" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <span id=\"views_restriction\">" + views + "</span>" +
                        "    <span id=\"load_date\"> " + date + " </span>\n" +
                        "    <pre id=\"code_snippet\"><code>" + code + "</code></pre>" +
                        "    </pre>\n" +
                        "</body>\n" +
                        "</html>";

        final String HTML_BOTH_RESTRICTED = "<html>\n" +
                "<head>\n" +
                "    <title>Code</title>\n" +
                "    <style>" +
                "       span {" +
                "           color: green;" +
                "       }"+
                "       pre {" +
                "           border: 1px solid gray;" +
                "           background-color: #dec7f1;" +
                "           padding: 7px 5px;" +
                "           margin: 0px;" +
                "           width: 500px;" +
                "       }"+
                "    </style>" +
                "<link rel=\"stylesheet\"\n" +
                "       target=\"_blank\" href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script>" +
                "</head>\n" +
                "<body>\n" +
                "    <span id=\"views_restriction\">" + views + "</span>" +
                "    <span id=\"time_restriction\">" + time + "</span>" +
                "    <span id=\"load_date\"> " + date + " </span>\n" +
                "    <pre id=\"code_snippet\"><code>" + code + "</code></pre>" +
                "    </pre>\n" +
                "</body>\n" +
                "</html>";

        final String HTML_TIME_RESTRICTED =
                        "<html>\n" +
                        "<head>\n" +
                        "    <title>Code</title>\n" +
                        "    <style>" +
                        "       span {" +
                        "           color: green;" +
                        "       }"+
                        "       pre {" +
                        "           border: 1px solid gray;" +
                        "           background-color: #dec7f1;" +
                        "           padding: 7px 5px;" +
                        "           margin: 0px;" +
                        "           width: 500px;" +
                        "       }"+
                        "    </style>" +
                        "<link rel=\"stylesheet\"\n" +
                        "       target=\"_blank\" href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                        "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                        "<script>hljs.initHighlightingOnLoad();</script>" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <span id=\"time_restriction\">" + time + "</span>" +
                        "    <span id=\"load_date\"> " + date + " </span>\n" +
                        "    <pre id=\"code_snippet\"><code>" + code + "</code></pre>" +
                        "    </pre>\n" +
                        "</body>\n" +
                        "</html>";

        final String HTML_NOT_RESTRICTED =
                "<html>\n" +
                        "<head>\n" +
                        "    <title>Code</title>\n" +
                        "    <style>" +
                        "       span {" +
                        "           color: green;" +
                        "       }"+
                        "       pre {" +
                        "           border: 1px solid gray;" +
                        "           background-color: #dec7f1;" +
                        "           padding: 7px 5px;" +
                        "           margin: 0px;" +
                        "           width: 500px;" +
                        "       }"+
                        "    </style>" +
                        "<link rel=\"stylesheet\"\n" +
                        "       target=\"_blank\" href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                        "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                        "<script>hljs.initHighlightingOnLoad();</script>" +
                        "</head>\n" +
                        "<body>\n" +
                        "    <span id=\"load_date\"> " + date + " </span>\n" +
                        "    <pre id=\"code_snippet\"><code>" + code + "</code></pre>" +
                        "    </pre>\n" +
                        "</body>\n" +
                        "</html>";

        String selected = HTML_NOT_RESTRICTED;

        boolean showViews = views > 0 || showViewsAnyway && views == 0;
        boolean showTime = time > 0;

        if (showTime && showViews) {
            selected = HTML_BOTH_RESTRICTED;
        } else if (showTime) {
            selected = HTML_TIME_RESTRICTED;
        } else if (showViews) {
            selected = HTML_VIEW_RESTRICTED;
        }

        return selected;

        /*
        return time > 0 && views > 0 ?
                HTML_BOTH_RESTRICTED : time > 0 ?
                HTML_TIME_RESTRICTED : views > 0 ?
                HTML_VIEW_RESTRICTED : HTML_NOT_RESTRICTED;
         */
    }

    @JsonIgnore
    public String getAsLatestHTML() {
        return "<html>\n" +
                "<head>\n" +
                "    <title>Latest</title>\n" +
                "    <style>" +
                "       span {" +
                "           color: green;" +
                "       }"+
                "       pre {" +
                "           border: 1px solid gray;" +
                "           background-color: #dec7f1;" +
                "           padding: 7px 5px;" +
                "           margin: 0px;" +
                "           width: 500px;" +
                "       }"+
                "    </style>" +
                "<link rel=\"stylesheet\"\n" +
                "       target=\"_blank\" href=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css\">\n" +
                "<script src=\"//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js\"></script>\n" +
                "<script>hljs.initHighlightingOnLoad();</script>" +
                "</head>\n" +
                "<body>\n" +
                "    <span id=\"load_date\"> " + date + " </span>\n" +
                "    <pre id=\"code_snippet\"><code>" + code + "</code></pre>" +
                "    </pre>\n" +
                "</body>\n" +
                "</html>";
    }

    @JsonIgnore
    public JSONResponse getAsJSON() {
        return new JSONResponse(code, date, time, views);
    }

    @JsonIgnore
    public String getNewHTML() {
        return "<html>\n" +
                "<head>\n" +
                "    <title>Create</title>\n" +
                "    <script>" +
                "           function send() {\n" +
                "               let object = {\n" +
                "                   \"code\": document.getElementById(\"code_snippet\").value\n" +
                "               };\n" +
                "    \n" +
                "               let json = JSON.stringify(object);\n" +
                "    \n" +
                "               let xhr = new XMLHttpRequest();\n" +
                "               xhr.open(\"POST\", '/api/code/new', false)\n" +
                "               xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');\n" +
                "               xhr.send(json);\n" +
                "    \n" +
                "               if (xhr.status == 200) {\n" +
                "                   alert(\"Success!\");\n" +
                "               }\n" +
                "           }" +
                "    </script>" +
                "</head>\n" +
                "<body>\n" +
                "    <form action=\"/code\">" +
                "         <textarea id=\"code_snippet\" required placeholder=\"Write your code\" rows=5 cols=60></textarea><br><br>" +
                "         <button id=\"send_snippet\" type=\"submit\" onclick=\"send()\">Submit</button>" +
                "    </form>" +
                "</body>\n" +
                "</html>";
    }

    public static UUID generateUUID() {
        return UUID.randomUUID();
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void latestCheckHidden() {
        if (views > 0 || time > 0) {
            hidden = true;
        }
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public long getTimeElapsed() {
        return Math.abs(Duration.between(LocalTime.now(), getLocalDateTime().toLocalTime()).toSeconds());
    }

    public void setShowViewsAnyway(boolean showViewsAnyway) {
        this.showViewsAnyway = showViewsAnyway;
    }

    public boolean isTimeRestricted() {
        return timeRestricted;
    }

    public void setTimeRestricted(boolean timeRestricted) {
        this.timeRestricted = timeRestricted;
    }

    public boolean isViewRestricted() {
        return viewRestricted;
    }

    public void setViewRestricted(boolean viewRestricted) {
        this.viewRestricted = viewRestricted;
    }

    public boolean isShowViewsAnyway() {
        return showViewsAnyway;
    }
}
