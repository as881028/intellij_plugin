package views;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intellij.openapi.wm.ToolWindow;

import net.minidev.json.parser.ParseException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

public class MyToolWindow {

  private String token;
  private String host;
  private JPanel myToolWindowContent;

  public MyToolWindow(@NotNull ToolWindow toolWindow) {
    myToolWindowContent = new JPanel();
    myToolWindowContent.setLayout(new BoxLayout(myToolWindowContent, BoxLayout.Y_AXIS));
    String[] inputArray = {"Host", "Token"};

    // Add JTextField
    for (int i = 0; i < inputArray.length; i++) {
      JLabel label = new JLabel(inputArray[i]);
      label.setAlignmentX(Component.CENTER_ALIGNMENT);
      myToolWindowContent.add(label);

      JTextField textField = new JTextField();
      textField.setName(inputArray[i]);
      Dimension preferredSize = new Dimension(250, 30);
      textField.setPreferredSize(preferredSize);
      textField.setMaximumSize(preferredSize);
      textField.setAlignmentX(Component.CENTER_ALIGNMENT);
      myToolWindowContent.add(textField);
    }

    // add Button
    JButton button = new JButton("Submit");
    button.setAlignmentX(Component.CENTER_ALIGNMENT);
    myToolWindowContent.add(button);

    // if user click button get text from JTextField
    button.addActionListener(e -> {
      Component[] components = myToolWindowContent.getComponents();
      for (Component component : components) {
        if (component instanceof JTextField) {
          JTextField textField = (JTextField) component;
          // if text field is empty show error message
//                    if (textField.getText().isEmpty()) {
//                        JOptionPane.showMessageDialog(null, "Please fill all fields");
//                        return;
//                    }

          // if label is Host get text from JTextField and show it in message
          if (textField.getName().equals("Host")) {
            host = textField.getText();
          }

          // if label is Token get text from JTextField and show it in message
          if (textField.getName().equals("Token")) {
//                        token = "glpat-4GsMV_TWE7KG-XrqqQPm";
//            token = "glpat-1FnayfehYbRNev-Qmd6k";
            token = "glpat-36YfxMjKQ9_AFmdpA5yi";
          }
        }
      }

      try {
        getPipelineStatus(host, token);
      } catch (IOException ex) {
        throw new RuntimeException(ex);
      } catch (ParseException ex) {
        throw new RuntimeException(ex);
      }
    });

  }

  public JComponent getContent() {
    return myToolWindowContent;
  }

  public void getPipelineStatus(String host, String token) throws IOException, ParseException {
    int project_id = 46959727;
    String apiUrl = host;
    Proxy proxy = Proxy.NO_PROXY;

    URL url = new URL("https://gitlab.com/api/v4/projects/" + project_id + "/pipelines");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty("PRIVATE-TOKEN", token);

    if (conn.getResponseCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
          + conn.getResponseCode() + conn.getResponseMessage());
    }
    BufferedReader br = new BufferedReader(new java.io.InputStreamReader((conn.getInputStream())));

    String response = "";
    String output;
    while ((output = br.readLine()) != null) {
      response += output;

    }

    JsonArray jsonArray = new JsonParser().parse(response).getAsJsonArray();
    JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
    int pipeline_id = jsonObject.get("id").getAsInt();
    String status = jsonObject.get("status").getAsString();

//    System.out.println("pipeline_id: " + pipeline_id);
//    System.out.println("status: " + status);
//    System.out.println("response: " + response);

//        getSinglePipeline(pipeline_id);
    listPipelineJobs(pipeline_id);
    // if status is skipped playPipeline
    if (status.equals("skipped")) {
//      playPipeline(pipeline_id);
    }

    // if status is success triggerPipeline
    if (status.equals("success")) {
//      triggerPipeline(host, token);
    }
  }

  private void playJob(int pipeline_id, String job_id) throws IOException {
    int project_id = 46959727;
    String apiUrl = host;
    Proxy proxy = Proxy.NO_PROXY;

    URL url = new URL(
        "https://gitlab.com/api/v4/projects/" + project_id + "/jobs/" + job_id + "/play");

    HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);
    conn.setInstanceFollowRedirects(false);
    conn.setRequestMethod("POST");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty("PRIVATE-TOKEN", "glpat-ifpQC5iNddwG5hBM-SfA");

    if (conn.getResponseCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
          + conn.getResponseCode() + conn.getResponseMessage() + "\n" + "url: "
          + conn.getErrorStream());
    }
    BufferedReader br = new BufferedReader(new java.io.InputStreamReader((conn.getInputStream())));

    String response = "";
    String output;
    while ((output = br.readLine()) != null) {
      response += output;
    }

    System.out.println("getSinglePipeline: " + response);

  }

  public void listPipelineJobs(int pipeline_id) throws IOException {
    int project_id = 46959727;
    String apiUrl = host;
    Proxy proxy = Proxy.NO_PROXY;

    URL url = new URL(
        "https://gitlab.com/api/v4/projects/" + project_id + "/pipelines/" + pipeline_id + "/jobs");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty("PRIVATE-TOKEN", token);

    if (conn.getResponseCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
          + conn.getResponseCode() + conn.getResponseMessage());
    }
    BufferedReader br = new BufferedReader(new java.io.InputStreamReader((conn.getInputStream())));

    String response = "";
    String output;
    while ((output = br.readLine()) != null) {
      response += output;
    }

//    System.out.println("listPipelineJobs" + response);
    JsonArray jsonArray = new JsonParser().parse(response).getAsJsonArray();
    // for each json object in json array
    for (JsonElement jsonElement : jsonArray) {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      // if stage is build and status is manual
      if (jsonObject.get("stage").getAsString().equals("build") && jsonObject.get("status")
          .getAsString().equals("manual")) {
        // get job id
        String job_id = jsonObject.get("id").getAsString();
        System.out.println("job_id: " + job_id);
        System.out.println(jsonObject);
        // play job
        playJob(pipeline_id, job_id);
      }

//      int job_id = jsonObject.get("id").getAsInt();
//      String job_status = jsonObject.get("status").getAsString();
//      System.out.println("id: " + job_id);
//      System.out.println("job_status: " + job_status);
      // if job status is success
//      if (job_status.equals("success")) {
//        // get job artifacts
//        getJobArtifacts(job_id);
//      }
    }
  }

  // get a single pipeline
  public void getSinglePipeline(int pipeline_id) throws IOException {
    int project_id = 46959727;
    String apiUrl = host;
    Proxy proxy = Proxy.NO_PROXY;

    URL url = new URL(
        "https://gitlab.com/api/v4/projects/" + project_id + "/pipelines/" + pipeline_id);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

    conn.setRequestMethod("GET");
    conn.setRequestProperty("Accept", "application/json");
    conn.setRequestProperty("PRIVATE-TOKEN", token);

    if (conn.getResponseCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
          + conn.getResponseCode() + conn.getResponseMessage());
    }
    BufferedReader br = new BufferedReader(new java.io.InputStreamReader((conn.getInputStream())));

    String response = "";
    String output;
    while ((output = br.readLine()) != null) {
      response += output;
    }

    System.out.println("getSinglePipeline: " + response);
  }


  public void triggerPipeline(String host, String token) throws IOException, ParseException {
    int project_id = 46959727;
    String apiUrl = host;
    Proxy proxy = Proxy.NO_PROXY;

    URL url = new URL("https://gitlab.com/api/v4/projects/" + project_id + "/trigger/pipeline");
    HttpURLConnection conn = (HttpURLConnection) url.openConnection(proxy);

    conn.setRequestMethod("POST");
    conn.setRequestProperty("token", token);
    conn.setRequestProperty("ref", "refs/heads/main");

    if (conn.getResponseCode() != 200) {
      throw new RuntimeException("Failed : HTTP error code : "
          + conn.getResponseCode() + conn.getResponseMessage());
    }
    BufferedReader br = new BufferedReader(new java.io.InputStreamReader((conn.getInputStream())));

    String response = "";
    String output;
    while ((output = br.readLine()) != null) {
      response += output;

    }
  }

}
