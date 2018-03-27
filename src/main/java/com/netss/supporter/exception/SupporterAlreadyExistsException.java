package com.netss.supporter.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(value = CONFLICT, reason = "Supporter already exists")
public class SupporterAlreadyExistsException extends RuntimeException {}
