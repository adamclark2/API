import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.*;
import com.google.gson.*;
import com.durp.Model.*;

import java.nio.Buffer;
import java.util.*;

/**
 * A class that can be run in the IDE to test things
 *
 * NOTE: This does NOT launch Tomcat NOR does it build the war file
 */
public class TestJig {
    public static void main(String[] args) throws Exception{
        HttpURLConnection url = (HttpURLConnection) new URL("http://localhost:8080/API/Users").openConnection();
        url.setRequestMethod("POST");
        url.setDoOutput(true);
        DataOutputStream w = new DataOutputStream(url.getOutputStream());

        User user = new User();
        user.email="adam.clark2@maine.edu";
        w.writeBytes("Action=login&APIKey=\"0X1234\"&Content=" + new Gson().toJson(user) + "" );
        w.flush();
        w.close();

        Scanner sc = new Scanner(url.getInputStream());
        while(sc.hasNextLine()){
            System.out.println(sc.nextLine());
        }

    }
}
