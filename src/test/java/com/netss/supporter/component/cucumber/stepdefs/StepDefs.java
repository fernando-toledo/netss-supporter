package com.netss.supporter.component.cucumber.stepdefs;

import com.netss.supporter.SupporterApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = SupporterApplication.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
