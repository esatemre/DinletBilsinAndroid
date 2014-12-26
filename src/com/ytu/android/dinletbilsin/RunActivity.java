package com.ytu.android.dinletbilsin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.actionbarsherlock.app.SherlockActivity;
import com.ytu.android.dinletbilsin.util.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.view.KeyEvent;
import android.widget.TextView;

public class RunActivity extends SherlockActivity {
	private AudioRecord recorder = null;
	private Thread recordingThread = null;
	private boolean isRecording = false;
	private int bufferSize = 0;
	private static final File sd = Environment.getExternalStorageDirectory();
	private File file = null;
	// private byte[] audio = null;
	private static final int RECORDER_SAMPLERATE = 44100;
	private static final int RECORDER_CHANNELS = AudioFormat.CHANNEL_IN_MONO;
	private static final int RECORDER_AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
	private TextView txt;
	private static long time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DinletBilsin.ChooseLanguage(this);
		setContentView(R.layout.activity_run);
		txt = (TextView) findViewById(R.id.txtView);
		time = DinletBilsin.GetRecordTime(RunActivity.this)*1000;
		if (DinletBilsin.SkipRecordForTest(this))
			SkipRecord();
		else
			StartRecord();
		
	}

	private void SkipRecord() {
		Intent intent = new Intent(RunActivity.this, ResultActivity.class);
		intent.putExtra("info", " Test - TesT ");
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		finish();
		
	}

	private void StartRecord() {
		try {
			file = File.createTempFile("dinletbilsin", ".wav", sd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// supressNoise(); //Android 4.1 ve üstü icin gurultu azaltma
		bufferSize = AudioRecord.getMinBufferSize(RECORDER_SAMPLERATE,
				RECORDER_CHANNELS, RECORDER_AUDIO_ENCODING);
		if (bufferSize == AudioRecord.ERROR_BAD_VALUE) {
			bufferSize = 2 << (int) ((Math.log(RECORDER_SAMPLERATE) / Math
					.log(2)) - 1);
		}
		recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,
				RECORDER_SAMPLERATE, RECORDER_CHANNELS,
				RECORDER_AUDIO_ENCODING, bufferSize);
		recorder.startRecording();

		isRecording = true;		
		recordingThread = new Thread(new Runnable() {
			public void run() {
				writeAudioDataToFile();
			}
		}, "AudioRecorder Thread");
		
		CountDownTimer timer = new CountDownTimer(time, 1000) {

			public void onTick(long millisUntilFinished) {
				// mProgress.setProgress((int) (millisUntilFinished/100));
			}
			public void onFinish() {
				StopRecord();
			}
		};
		timer.start();
		recordingThread.start();

	}

	private void StopRecord() {
		// stops the recording activity
		if (null != recorder) {
			isRecording = false;
			DinletBilsin.setContent(txt, this.getString(R.string.loading_2));
			recorder.stop();
			recorder.release();
			recorder = null;
			recordingThread = null;
			
			if (!DinletBilsin.CheckForSaving(this)) {
				new UploadTrack(this).execute(file.getAbsolutePath());
			} else {
				Intent intent = new Intent(RunActivity.this, ResultActivity.class);
				intent.putExtra("info", ("Saved to : " + file.getName()));
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		}
	}

	@SuppressLint("SdCardPath")
	private void writeAudioDataToFile() {
		// Write the output audio in byte
		short sData[] = new short[bufferSize];
		FileOutputStream os = null;
		try {
			os = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		while (isRecording) {

			recorder.read(sData, 0, bufferSize);
			byte bData[] = shortToByte(sData);
			try {
				os.write(bData, 0, bufferSize / 2);
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] shortToByte(short[] sData) {
		int shortArrsize = sData.length;
		byte[] bytes = new byte[shortArrsize];
		for (int i = 0; i < shortArrsize; i++) {
			bytes[i] = (byte) (Short.reverseBytes(sData[i]) & 0xFF);
		}
		return bytes;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			isRecording = false;
			recordingThread = null;
			if (null != recorder) {
				recorder.stop();
				recorder.release();
				recorder = null;			
			}
			if (file != null)
				file.delete();
			finish();
		}

		return super.onKeyDown(keyCode, event);
	}

}
