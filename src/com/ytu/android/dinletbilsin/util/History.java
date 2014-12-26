package com.ytu.android.dinletbilsin.util;

import java.util.LinkedList;
import java.util.Queue;
import android.content.Context;
import android.content.SharedPreferences;

public class History {

	public static void addHistory(Context con, String str) {
		int no = DinletBilsin.GetMaxHistory(con);
		Queue<String> list = new LinkedList<String>();
		if (no > 0) {
			list = load(con);
			if (list.size() >= no) {
				int turn = list.size() - no;
				for (int i = 0; i < turn + 1; i++) {
					list.poll();
				}
			}
			list.add(str);
		}
		save(con, list);
	}

	public static void deleteHistory(Context con, int pos) {
		Queue<String> list = load(con);
		if (list != null) {
			list.remove(((LinkedList<String>) list).get(pos));
		}
		save(con, list);
	}

	public static boolean save(Context context, Queue<String> list) {
		SharedPreferences prefs = context.getSharedPreferences(
				"history_tracks", 0);
		SharedPreferences.Editor editor = prefs.edit();
		int size = list.size();
		editor.putInt("history_tracks_size", size);
		for (int i = 0; i < size; i++) {
			String str = list.poll();
			if (str != null)
				editor.putString("history_tracks_" + String.valueOf(i), str);
		}
		return editor.commit();
	}

	public static Queue<String> load(Context context) {
		Queue<String> list = new LinkedList<String>();
		SharedPreferences prefs = context.getSharedPreferences(
				"history_tracks", 0);
		int size = prefs.getInt("history_tracks_size", 0);
		for (int i = 0; i < size; i++) {
			String str = prefs.getString("history_tracks_" + i, null);
			if (str != null)
				list.add(str);
		}
		return list;
	}

}
