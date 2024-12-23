package com.github.datnm23.securityservice.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchUserDto {

    Long id;
    String email;
    String status;
    Long totalRecord;
}
