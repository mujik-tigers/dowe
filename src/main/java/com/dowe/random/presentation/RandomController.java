package com.dowe.random.presentation;

import com.dowe.random.application.RandomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/random")
public class RandomController {

  private final RandomService randomService;

}
