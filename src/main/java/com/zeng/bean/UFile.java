package com.zeng.bean;

import java.sql.Date;

public class UFile {
    private String uuid;
    private Integer fileSize;
    private String fileType;
    private String primeName ;
    private String createDate;
    private String SaveAddr;
    private String security;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getPrimeName() {
        return primeName;
    }

    public void setPrimeName(String primeName) {
        this.primeName = primeName;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSaveAddr() {
        return SaveAddr;
    }

    public void setSaveAddr(String saveAddr) {
        SaveAddr = saveAddr;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    @Override
    public String toString() {
        return "{" +
                "'uuid':'" + uuid + '\'' +
                ", 'fileSize':" + fileSize +
                ", 'fileType':'" + fileType + '\'' +
                ", 'primeName':'" + primeName + '\'' +
                ", 'createDate':'" + createDate + '\'' +
                ", 'SaveAddr':'" + SaveAddr + '\'' +
                ", 'security':'" + security + '\'' +
                '}';
    }
}
