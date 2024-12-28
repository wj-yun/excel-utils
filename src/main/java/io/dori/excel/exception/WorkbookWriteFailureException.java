package io.dori.excel.exception;

public class WorkbookWriteFailureException extends RuntimeException {
    public WorkbookWriteFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
