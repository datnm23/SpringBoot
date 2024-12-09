package com.github.datnm23.statisticservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.datnm23.statisticservice.entity.Statistic;
import com.github.datnm23.statisticservice.model.StatisticDTO;
import com.github.datnm23.statisticservice.repository.StatisticRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class StatisticService {
        final StatisticRepository statisticRepository;

        final ObjectMapper objectMapper;

        public void add(StatisticDTO statisticDTO) {
            try {
                Statistic statistic = objectMapper.convertValue(statisticDTO, Statistic.class);
                statisticRepository.save(statistic);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }

        public List<StatisticDTO> getAll() {
            List<StatisticDTO> statisticDTOs = new ArrayList<>();
            try {
                statisticRepository.findAll().forEach(statistic ->
                        statisticDTOs.add(objectMapper.convertValue(statistic, StatisticDTO.class))
                );
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return statisticDTOs;
        }
    }
