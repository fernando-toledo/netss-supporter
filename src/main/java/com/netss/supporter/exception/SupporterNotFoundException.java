package com.netss.supporter.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@ResponseStatus(value = NOT_FOUND, reason = "Supporter not found")
public class SupporterNotFoundException extends RuntimeException {
}
