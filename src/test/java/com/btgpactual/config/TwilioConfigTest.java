package com.btgpactual.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TwilioConfig.class)
@EnableConfigurationProperties(TwilioConfig.class)
@TestPropertySource(properties = {
        "twilio.account-sid=TEST_SID",
        "twilio.auth-token=TEST_TOKEN",
        "twilio.phone-number=+1234567890"
})
class TwilioConfigTest {

    @Autowired
    private TwilioConfig twilioConfig;

    @Test
    void givenConfigurationProperties_whenConfigLoaded_thenAccountSidIsCorrect() {
        assertThat(twilioConfig.getAccountSid()).isEqualTo("TEST_SID");
    }

    @Test
    void givenConfigurationProperties_whenConfigLoaded_thenAuthTokenIsCorrect() {
        assertThat(twilioConfig.getAuthToken()).isEqualTo("TEST_TOKEN");
    }

    @Test
    void givenConfigurationProperties_whenConfigLoaded_thenPhoneNumberIsCorrect() {
        assertThat(twilioConfig.getPhoneNumber()).isEqualTo("+1234567890");
    }

    @Test
    void givenTwilioConfig_whenSetAccountSid_thenAccountSidIsUpdated() {
        String originalSid = twilioConfig.getAccountSid();
        twilioConfig.setAccountSid("NEW_SID");
        assertThat(twilioConfig.getAccountSid()).isEqualTo("NEW_SID");
        // Restaurar el valor original para no afectar otros tests
        twilioConfig.setAccountSid(originalSid);
    }

    @Test
    void givenTwilioConfig_whenSetAuthToken_thenAuthTokenIsUpdated() {
        String originalToken = twilioConfig.getAuthToken();
        twilioConfig.setAuthToken("NEW_TOKEN");
        assertThat(twilioConfig.getAuthToken()).isEqualTo("NEW_TOKEN");
        // Restaurar el valor original para no afectar otros tests
        twilioConfig.setAuthToken(originalToken);
    }

    @Test
    void givenTwilioConfig_whenSetPhoneNumber_thenPhoneNumberIsUpdated() {
        String originalPhoneNumber = twilioConfig.getPhoneNumber();
        twilioConfig.setPhoneNumber("+9876543210");
        assertThat(twilioConfig.getPhoneNumber()).isEqualTo("+9876543210");
        // Restaurar el valor original para no afectar otros tests
        twilioConfig.setPhoneNumber(originalPhoneNumber);
    }
}