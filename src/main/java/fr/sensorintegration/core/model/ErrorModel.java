package fr.sensorintegration.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorModel {
    public boolean success;
    private String message;
    private Date timestamp;
    private Integer code;
}
