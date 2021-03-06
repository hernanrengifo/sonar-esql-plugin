/*
 * Sonar ESQL Plugin
 * Copyright (C) 2013 Thomas Pohl and EXXETA AG
 * http://www.exxeta.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exxeta.iss.sonar.esql.check;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.server.rule.RulesDefinition;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.squidbridge.AstScannerExceptionHandler;
import org.sonar.squidbridge.annotations.SqaleConstantRemediation;
import org.sonar.squidbridge.annotations.SqaleSubCharacteristic;
import org.sonar.squidbridge.checks.SquidCheck;

import com.sonar.sslr.api.Grammar;
import com.sonar.sslr.api.RecognitionException;

@Rule(
  key = ParsingErrorCheck.CHECK_KEY,
  priority = Priority.MAJOR,
  name = "Parser failure")
@SqaleSubCharacteristic(RulesDefinition.SubCharacteristics.INSTRUCTION_RELIABILITY)
@SqaleConstantRemediation("30min")
public class ParsingErrorCheck extends SquidCheck<Grammar> implements AstScannerExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(ParsingErrorCheck.class);
	public static final String CHECK_KEY="ParsingError";

  public void processException(Exception e) {
    StringWriter exception = new StringWriter();
    e.printStackTrace(new PrintWriter(exception));
    LOGGER.debug("Parsing error", e);
    getContext().createFileViolation(this, exception.toString());

  }

  public void processRecognitionException(RecognitionException e) {
    getContext().createLineViolation(this, e.getMessage(), e.getLine());
  }

}
