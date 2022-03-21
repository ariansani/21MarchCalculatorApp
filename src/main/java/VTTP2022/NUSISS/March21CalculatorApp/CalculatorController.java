package VTTP2022.NUSISS.March21CalculatorApp;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

@RestController
@RequestMapping("/calculate")
public class CalculatorController {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postCalcForm(
            @RequestBody String payload,
            @RequestHeader("user-agent") String userAgent
            ) {
                   JsonObject responseJson;

        try (InputStream is = new ByteArrayInputStream(payload.getBytes())) {
            JsonReader reader = Json.createReader(is);
            JsonObject o = reader.readObject();
         

            // {"oper1":2,"oper2":1,"ops":"minus"}
            Integer oper1 = o.getInt("oper1");
            Integer oper2 = o.getInt("oper2");
            String ops = o.getString("ops");
            Integer result;
            switch (ops) {
                case "minus":
                    result = oper1 - oper2;
                    break;
                case "plus":
                    result = oper1 + oper2;
                    break;
                case "divide":
                    result = oper1 / oper2;
                    break;
                case "multiply":
                    result = oper1 * oper2;
                    break;
                default:
                    result = 0;
                    break;
            }

            
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
            Date date = new Date();
            
            responseJson = Json.createObjectBuilder()
            .add("result",result)
            .add("timestamp", date.toString())
            .add("userAgent",userAgent)
            .build();
            System.out.println(">>>>>>>>>>>"+responseJson.toString());
          

        } catch (Exception e) {
            // TODO: handle exception
            JsonObject errJson = Json.createObjectBuilder()
                    .add("error", e.getMessage()).build();
            return ResponseEntity.status(400).body(errJson.toString());
        }

        return ResponseEntity.ok(responseJson.toString());
    }

}
