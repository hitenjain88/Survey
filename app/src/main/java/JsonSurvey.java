import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.survey.PreviewForm;
import com.example.survey.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class JsonSurvey {
    final String DIR_PATH = Environment.getExternalStorageDirectory() + "/SurveyApp"; //PATH OF Internal STORAGE FOR FORM and JSON


    public String ReadJson(String path) {
        File file = new File(DIR_PATH, path);
        StringBuilder json = new StringBuilder();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            br.close();
            Log.v("JsonSurvey_ReadJson", String.valueOf(json));
            return json.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{title:error}";
    }

    public boolean WriteJson(String path, String data) throws IOException {
        File file = new File(DIR_PATH, path);

        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data.getBytes());
        fos.close();
        Log.v("JsonSurvey_WriteJson", String.valueOf(file.exists()));
        return file.exists();
    }

    

}

/*
JSON FORMAT

{
   "title":"testing",
   "description":"testing",
   "date":"26-Feb-2019",
   "type":[
      {
         "type":"RadioGroup",
         "question":"Q. Radio Testing?",
         "group":[
            {
               "title":"Test 1"
            },
            {
               "title":"Test 2"
            }
         ]
      },
      {
         "type":"CheckBox",
         "question":"Q. Testing Check Box ?",
         "group":[
            {
               "title":"Test 1"
            },
            {
               "title":"Test 2"
            }
         ]
      },
      {
         "type":"EditText",
         "image":false,
         "question":"Q. Testing ?"
      },
      {
         "type":"MultiEditText",
         "question":"Q. Testing ?"
      }
   ]
}


JSON ANSWER FORMAT

[
   {
      "1":[
         {
            "RadioGroup":"Test 2"
         },
         {
            "CheckBox":"Test 2 | "
         },
         {
            "EditText":"Testing 1"
         }
      ]
   },
   {
      "2":[
         {
            "RadioGroup":"Test 1"
         },
         {
            "CheckBox":"Test 1 | "
         },
         {
            "EditText":"Testing 2"
         }
      ]
   },
   {
      "3":[
         {
            "RadioGroup":"Test 2"
         },
         {
            "CheckBox":"Test 2 | "
         },
         {
            "EditText":"Testing 3"
         }
      ]
   },
   {
      "4":[
         {
            "RadioGroup":"Test 1"
         },
         {
            "CheckBox":"Test 2 | Test 1 | "
         },
         {
            "EditText":"hiten"
         }
      ]
   }
]
 */

