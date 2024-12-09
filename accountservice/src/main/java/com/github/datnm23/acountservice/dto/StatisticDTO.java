package com.github.datnm23.acountservice.dto;

import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class StatisticDTO {
    private Long id;

    @NonNull
    private String message;

    @NonNull
    private Date createdDate;
}
