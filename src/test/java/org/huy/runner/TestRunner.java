package org.huy.runner;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.core.options.Constants.FEATURES_PROPERTY_NAME;
import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("resources")
@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = "org.huy.steps")
@ConfigurationParameter(key = FEATURES_PROPERTY_NAME, value = "resources/feature")
public class TestRunner {
}
