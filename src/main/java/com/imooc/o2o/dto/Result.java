package com.imooc.o2o.dto;

/**
 * @Author : Wang Zhen.
 * @Date : Created in 13:58 2019/11/20
 * @Description : 封装json对象
 * @Modified By   :
 * @Version :
 */
public class Result<T> {
    private boolean success;
    /**
     * 成功时返回的数据
     */
    private T data;
    private int errCode;
    private String errMsg;


    public Result() {
    }

    /**
     * 成功时的返回构造器
     * @param success
     * @param data
     */
    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    /**
     * 失败时返回的构造器
     * @param success
     * @param errMsg
     * @param errCode
     */
    public Result(boolean success,  int errCode, String errMsg) {
        this.success = success;
        this.errMsg = errMsg;
        this.errCode = errCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
