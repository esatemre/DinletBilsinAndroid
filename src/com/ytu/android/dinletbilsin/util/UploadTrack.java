package com.ytu.android.dinletbilsin.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import com.ytu.android.dinletbilsin.ResultActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

@SuppressWarnings({ "deprecation" })
public class UploadTrack extends AsyncTask<String, String, String> {
	public static byte[] byteArrayAudio = null;
	public static String result;
	private Context context;
	public static File WAV_FILE;

	public UploadTrack(Context context) {
		this.context = context.getApplicationContext();
	}

	protected String doInBackground(String... urls) {

		String wav = urls[0];
		HttpPost httppost = new HttpPost(DinletBilsin.SERVICE_UPLOAD_URL);
		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		HttpClient httpClient = new DefaultHttpClient();
		try {
			if (wav != "") {
				wavToByteArray(wav, 4096);
			}
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		if (byteArrayAudio != null) {
			ByteArrayBody bab = new ByteArrayBody(byteArrayAudio, "record.wav");
			entity.addPart("file", bab);
		}
		try {
			StringBody sname = new StringBody("record");
			entity.addPart("name", sname);

		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		result = "";
		InputStream inputStream = null;
		httppost.setEntity(entity);
		try {
			HttpResponse hr = httpClient.execute(httppost);
			inputStream = hr.getEntity().getContent();

			if (inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "error!";

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();

	}

	protected void onPostExecute(String result) {

		super.onPostExecute(result);
		Intent intent = new Intent(context, ResultActivity.class);
		intent.putExtra("info", result);
		WAV_FILE.delete();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	private void wavToByteArray(String WAV, int BYTE_SIZE) throws IOException {

		WAV_FILE = new File(WAV);

		ByteArrayOutputStream out = new ByteArrayOutputStream(
				(int) WAV_FILE.length());
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(
				WAV_FILE));

		int read;
		byte[] buff = new byte[BYTE_SIZE];
		while ((read = in.read(buff)) > 0) {
			out.write(buff, 0, read);
		}
		out.flush();
		byteArrayAudio = out.toByteArray();

		in.close();
		out.close();
	}

	private String convertInputStreamToString(InputStream inputStream)
			throws IOException {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream));
		String line = "";
		String result = "";
		while ((line = bufferedReader.readLine()) != null)
			result += line;

		inputStream.close();
		return result;

	}

}
