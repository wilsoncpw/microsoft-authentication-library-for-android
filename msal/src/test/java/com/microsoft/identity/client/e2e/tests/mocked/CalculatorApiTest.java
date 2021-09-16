// Copyright (c) Microsoft Corporation.
// All rights reserved.
//
// This code is licensed under the MIT License.
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files(the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and / or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions :
//
// The above copyright notice and this permission notice shall be included in
// all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// THE SOFTWARE.
package com.microsoft.identity.client.e2e.tests.mocked;

import static com.microsoft.identity.internal.testutils.TestConstants.Configurations.CALCULATOR_API_TEST_CONFIG_FILE_PATH;

import com.microsoft.identity.client.e2e.tests.PublicClientApplicationAbstractTest;

import androidx.annotation.NonNull;

import com.microsoft.identity.client.AuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplicationConfiguration;
import com.microsoft.identity.client.e2e.shadows.ShadowDeviceCodeFlowCommandAuthError;
import com.microsoft.identity.client.e2e.shadows.ShadowDeviceCodeFlowCommandSuccessful;
import com.microsoft.identity.client.e2e.shadows.ShadowDeviceCodeFlowCommandTokenError;
import com.microsoft.identity.client.e2e.shadows.ShadowPublicClientApplicationConfiguration;
import com.microsoft.identity.client.e2e.tests.PublicClientApplicationAbstractTest;
import com.microsoft.identity.client.e2e.utils.RoboTestUtils;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.common.java.exception.ClientException;
import com.microsoft.identity.common.java.exception.ErrorStrings;
import com.microsoft.identity.common.java.authorities.Authority;
import com.microsoft.identity.common.java.providers.microsoft.microsoftsts.MicrosoftStsAuthorizationErrorResponse;
import com.microsoft.identity.common.java.providers.microsoft.microsoftsts.MicrosoftStsAuthorizationRequest;
import com.microsoft.identity.common.java.providers.microsoft.microsoftsts.MicrosoftStsAuthorizationResponse;
import com.microsoft.identity.common.java.providers.microsoft.microsoftsts.MicrosoftStsTokenRequest;
import com.microsoft.identity.common.java.providers.oauth2.AuthorizationResult;
import com.microsoft.identity.common.java.providers.oauth2.OAuth2Strategy;
import com.microsoft.identity.common.java.providers.oauth2.OAuth2StrategyParameters;
import com.microsoft.identity.common.java.providers.oauth2.TokenRequest;
import com.microsoft.identity.common.java.providers.oauth2.TokenResult;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowPublicClientApplicationConfiguration.class})
public class CalculatorApiTest extends PublicClientApplicationAbstractTest {
    @Before
    public void setup() {
        super.setup();
    }

    @Override
    public String getConfigFilePath() {
        return CALCULATOR_API_TEST_CONFIG_FILE_PATH;
    }

    //===========================================================================================================
    // API-Side Testing
    //===========================================================================================================
    @Test
    public void testCalculatorApiSuccess() throws MsalException {
        double x = 5;
        double y = 7;
        char op = '+';
        mApplication.calculatorApi(x, y, op, new IPublicClientApplication.CalculatorAPICallback() {
            @Override
            public void onCalculationResult(double result) {
                Assert.assertTrue(result == 12);
            }

            @Override
            public void onError(@NonNull MsalException error) {
                Assert.fail();
            }
        });
    }

    @Test
    public void testCalculatorApiDivideByZero() throws MsalException {
        double x = 4;
        double y = 0;
        char op = '/';
        mApplication.calculatorApi(x, y, op, new IPublicClientApplication.CalculatorAPICallback() {
            @Override
            public void onCalculationResult(double result) {
                Assert.fail();
            }

            @Override
            public void onError(@NonNull MsalException error) {
                Assert.assertNotNull(error);
                Assert.assertNotNull(error.getMessage());
                Assert.assertEquals(error.getMessage(), "/ by zero");
            }
        });
    }
}


