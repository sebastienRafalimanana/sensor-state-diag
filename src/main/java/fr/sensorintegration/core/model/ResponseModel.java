package fr.sensorintegration.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class ResponseModel<T> {
    private String message;
    private Boolean success;
    private String error;
    private Date timestamp = new Date();
    private T data;
}
