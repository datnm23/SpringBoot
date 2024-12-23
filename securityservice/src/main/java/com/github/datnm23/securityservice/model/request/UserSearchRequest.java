package com.github.datnm23.securityservice.model.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UserSearchRequest extends BaseSearchRequest {

    String name;

    String email;

    Boolean activated;

    String gender;

}
