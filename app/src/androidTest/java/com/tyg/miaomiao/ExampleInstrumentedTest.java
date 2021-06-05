package com.tyg.miaomiao;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.tyg.miaomiao.info.dto.LoginDTO;
import com.tyg.miaomiao.info.dto.RegistDTO;
import com.tyg.miaomiao.utils.OkHttp;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.tyg.miaomiao", appContext.getPackageName());
    }


    @Test
    public void testHttpClient() throws IOException, JSONException {
        new Thread() {
            @Override
            public void run() {
                try {
                    RegistDTO registDTO = new RegistDTO();
                    registDTO.setEmail("345678");
                    registDTO.setPassword("007");
                    //todo 登录的地方
                    String register_url = Config.server_ip + "/maomao/user/regist";
                    Log.d("userRegistBegin", register_url + ":" + registDTO.toString());
                    String result = OkHttp.post(register_url, registDTO.toString());

                    JSONObject res = new JSONObject(result);

                    System.out.println(new Gson().toJson(res));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.run();
    }


    @Test
    public void testHttpClient2() throws IOException, JSONException {

        try {
            RegistDTO registDTO = new RegistDTO();
            registDTO.setEmail("345678");
            registDTO.setPassword("007");
            //todo 登录的地方
            String register_url = Config.server_ip + "/maomao/user/regist";
            Log.d("userRegistBegin", register_url + ":" + registDTO.toString());
            String result = OkHttp.post(register_url, registDTO.toString());

            JSONObject res = new JSONObject(result);

            System.out.println(new Gson().toJson(res));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void testLogin() throws IOException {
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setPhone("3456789");
        loginDTO.setPassword("007");

        //todo 登录的地方
        String login_url = Config.server_ip + "/maomao/user/login";
        Log.d("userLoginBegin", login_url + ":" + loginDTO.toString());
        String result = OkHttp.post(login_url, loginDTO.toString());
        System.out.println(result);

    }
}
