package com.flexcub.resourceplanning.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class ControllerException {

    private static final long serialVersionUID = 1L;
    private String errorCode;
    private String errorDesc;
}
