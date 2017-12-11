package cd.go.authorization.guest.executor;

import cd.go.authorization.guest.Authenticator;
import cd.go.authorization.guest.Authorizer;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.request.GoPluginApiRequest;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.HashMap;

import static cd.go.authorization.guest.Constants.*;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserAuthenticationExecutorTest {
    @Mock
    GoPluginApiRequest request;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldAuthenticate() throws Exception {
        HashMap<String, String> configuration = new HashMap<>();
        configuration.put(SETTINGS_SERVER_URL_KEY, "server-url");
        configuration.put(SETTINGS_USERNAME_KEY, "username");
        configuration.put(SETTINGS_USER_DISPLAY_NAME_KEY, "display-name");
        configuration.put(SETTINGS_USER_EMAIL_KEY, "email-id");

        HashMap<String, Object> authConfig = new HashMap<>();
        authConfig.put("id", "dummy_id");
        authConfig.put("configuration", configuration);

        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("auth_configs", Arrays.asList(authConfig));

        when(request.requestBody()).thenReturn(new Gson().toJson(requestBody));

        GoPluginApiResponse actualResponse = new UserAuthenticationExecutor(request, new Authenticator(), new Authorizer()).execute();
        DefaultGoPluginApiResponse expectedResponse = new DefaultGoPluginApiResponse(200, "{\"roles\":[],\"user\":{\"username\":\"username\",\"display_name\":\"display-name\",\"email\":\"email-id\"}}");

        assertThat(actualResponse.responseBody(), is(expectedResponse.responseBody()));
        assertThat(actualResponse.responseCode(), is(expectedResponse.responseCode()));
    }
}