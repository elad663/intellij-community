/*
 * Copyright 2000-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jetbrains.jsonSchema.impl;

import com.intellij.codeInsight.completion.CodeCompletionHandlerBase;
import com.intellij.codeInsight.completion.CompletionType;
import com.jetbrains.jsonSchema.JsonSchemaHeavyAbstractTest;
import com.jetbrains.jsonSchema.JsonSchemaMappingsConfigurationBase;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

/**
 * @author Irina.Chernushina on 3/4/2017.
 */
public class JsonBySchemaHeavyCompletionTest extends JsonSchemaHeavyAbstractTest {
  @Override
  protected String getBasePath() {
    return "/tests/testData/jsonSchema/completion";
  }

  public void testInsertEnumValue() throws Exception {
    baseInsertTest("insertPropertyName", "testName");
  }

  public void testInsertNameWithDefaultStringValue() throws Exception {
    baseInsertTest("insertPropertyName", "testNameWithDefaultStringValue");
  }

  public void testInsertNameWithDefaultIntegerValue() throws Exception {
    baseInsertTest("insertPropertyName", "testNameWithDefaultIntegerValue");
  }

  public void testInsertIntegerType() throws Exception {
    baseInsertTest("insertPropertyName", "testIntegerType");
  }

  public void testInsertStringType() throws Exception {
    baseInsertTest("insertPropertyName", "testStringType");
  }

  public void testInsertObjectType() throws Exception {
    baseInsertTest("insertPropertyName", "testObjectType");
  }

  public void testInsertBooleanType() throws Exception {
    baseInsertTest("insertPropertyName", "testBooleanType");
  }

  //no quotes
  public void testNameWithDefaultStringValueNoQuotes() throws Exception {
    baseInsertTest("insertPropertyName", "testNameWithDefaultStringValueNoQuotes");
  }

  public void testNameWithDefaultIntegerValueNoQuotesComma() throws Exception {
    baseInsertTest("insertPropertyName", "testNameWithDefaultIntegerValueNoQuotesComma");
  }

  //comma
  public void testInsertIntegerTypeComma() throws Exception {
    baseInsertTest("insertPropertyName", "testIntegerTypeComma");
  }

  public void testInsertBooleanTypeComma() throws Exception {
    baseInsertTest("insertPropertyName", "testBooleanTypeComma");
  }

  public void testStringTypeComma() throws Exception {
    baseInsertTest("insertPropertyName", "testStringTypeComma");
  }

  public void testNameWithDefaultStringValueComma() throws Exception {
    baseInsertTest("insertPropertyName", "testNameWithDefaultStringValueComma");
  }

  public void testOneOfWithNotFilledPropertyValue() throws Exception {
    baseCompletionTest("oneOfWithEnumValue", "oneOfWithEmptyPropertyValue", "\"business\"", "\"home\"");
  }

  private void baseCompletionTest(@SuppressWarnings("SameParameterValue") final String folder,
                                  @SuppressWarnings("SameParameterValue") final String testFile, @NotNull String... items) throws Exception {
    baseTest(folder, testFile, () -> {
      complete();
      assertStringItems(items);
    });
  }

  private void baseInsertTest(@SuppressWarnings("SameParameterValue") final String folder, final String testFile) throws Exception {
    baseTest(folder, testFile, () -> {
      final CodeCompletionHandlerBase handlerBase = new CodeCompletionHandlerBase(CompletionType.BASIC);
      handlerBase.invokeCompletion(getProject(), getEditor());
      if (myItems != null) {
        selectItem(myItems[0]);
      }
      try {
        checkResultByFile("/" + folder + "/" + testFile + "_after.json");
      }
      catch (Exception e) {
        throw new RuntimeException(e);
      }
    });
  }

  private void baseTest(@NotNull final String folder, @NotNull final String testFile, @NotNull final Runnable checker) throws Exception {
    skeleton(new Callback() {
      @Override
      public void registerSchemes() {
        final String moduleDir = getModuleDir(getProject());

        final JsonSchemaMappingsConfigurationBase.SchemaInfo base =
          new JsonSchemaMappingsConfigurationBase.SchemaInfo("base", moduleDir + "/Schema.json", false,
                                                             Collections
                                                               .singletonList(new JsonSchemaMappingsConfigurationBase.Item("*.json", true, false)));
        addSchema(base);
      }

      @Override
      public void configureFiles() throws Exception {
        configureByFiles(null, "/" + folder + "/" + testFile + ".json", "/" + folder + "/Schema.json");
      }

      @Override
      public void doCheck() {
        checker.run();
      }
    });
  }
}
