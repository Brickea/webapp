package zixiaowangfall2020.webapp.pojo;

import javax.validation.constraints.Null;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class WebappFile {
    private String fileId;
    private String fileName;
    private String s3ObjectName;
    private String createdTimestamp;

    public WebappFile(String fileName, String s3ObjectName, String createdTimestamp) {
        this.fileName = fileName;
        this.s3ObjectName = s3ObjectName;
        this.createdTimestamp = createdTimestamp;
    }

    public WebappFile(String fileId, String fileName, String s3ObjectName, String createdTimestamp) {
        this.fileId = fileId;
        this.fileName = fileName;
        this.s3ObjectName = s3ObjectName;
        this.createdTimestamp = createdTimestamp;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getS3ObjectName() {
        return s3ObjectName;
    }

    public void setS3ObjectName(String s3ObjectName) {
        this.s3ObjectName = s3ObjectName;
    }

    public String getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(String createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }
}
