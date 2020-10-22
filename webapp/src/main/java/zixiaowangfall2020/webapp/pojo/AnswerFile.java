package zixiaowangfall2020.webapp.pojo;

/**
 * @Author: Zixiao Wang
 * @Version: 1.0.0
 * @Description:
 **/

public class AnswerFile {
    private String answerId;
    private String fileId;

    public AnswerFile(String answerId, String fileId) {
        this.answerId = answerId;
        this.fileId = fileId;
    }

    public String getAnswerId() {
        return answerId;
    }

    public void setAnswerId(String answerId) {
        this.answerId = answerId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
