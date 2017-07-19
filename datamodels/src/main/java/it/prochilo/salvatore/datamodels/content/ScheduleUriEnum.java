package it.prochilo.salvatore.datamodels.content;

/**
 * @author Zippo
 * @version 1.0
 */
public enum ScheduleUriEnum {

    USERS(100, "", "", false, "");
    public int code;

    public String path;

    public String contentType;

    public String table;

    ScheduleUriEnum(int code, String path, String contentTypeId, boolean item, String table) {
        this.code = code;
        this.path = path;
    }
}
