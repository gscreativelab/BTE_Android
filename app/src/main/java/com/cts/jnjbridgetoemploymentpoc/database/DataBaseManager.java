package com.cts.jnjbridgetoemploymentpoc.database;

/**
 * DataBaseManager is the class through which only data can be inserted and retrieved
 */
import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.cts.jnjbridgetoemploymentpoc.model.Announcements;
import com.cts.jnjbridgetoemploymentpoc.model.Attendees;
import com.cts.jnjbridgetoemploymentpoc.model.Events;
import com.cts.jnjbridgetoemploymentpoc.model.Members;
import com.cts.jnjbridgetoemploymentpoc.utils.Constants;
import com.cts.jnjbridgetoemploymentpoc.utils.DatabaseConstants;

public class DataBaseManager {

	public static boolean isDropTable = false;

	/**
	 * isTableExists is called for the very first time to check if the table
	 * exists which means user has entered into the app in online mode atleast
	 * once. If not will be prompted to do so.
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTableExists(Context context, Uri uri) {
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	/**
	 * isEventTableExists is called for the very first time in event details screen to check if the table
	 * exists which means user has entered into the app in online mode atleast
	 * once. If not will be prompted to do so.
	 * 
	 * @param context
	 * @return
	 */
	
	public static boolean isEventTableExists(Context context, Uri uri, String eventID) {
		
		
		Cursor cursor = context.getContentResolver().query(uri, null, DatabaseConstants.COL_EVENTDETAILS_ID + "= ?",
				new String[] { eventID }, null);
		if (cursor != null) {
			if (cursor.getCount() > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	/**
	 * 
	 * method to insert Group Name in the table "tableGroup"
	 * 
	 * @param context
	 * @param uri
	 * @param key
	 * @param value
	 */
	public static void insertGroupRecord(Context context, Uri uri, String key,
			String value) {
		ContentValues values = new ContentValues();
		values.put(key, value);
		context.getContentResolver().insert(DatabaseConstants.CONTENT_URI,
				values);
	}

	/**
	 * method to insert Announcements in the table "tableGroupAnnouncements"
	 * 
	 * @param context
	 * @param uri
	 * @param announcements
	 */
	public static void insertAnnouncementsRecord(Context context, Uri uri,
			ArrayList<Announcements> announcements) {
		isDropTable = true;
		ContentValues values = new ContentValues();
		for (int i = 0; i < announcements.size(); i++) {
			values.put(DatabaseConstants.COL_ANNOUNCEMNTS_NAME, announcements
					.get(i).getName());
			values.put(DatabaseConstants.COL_ANNOUNCEMNTS_DATE, announcements
					.get(i).getDate());
			values.put(DatabaseConstants.COL_ANNOUNCEMNTS_DESC, announcements
					.get(i).getDescription());
			context.getContentResolver().insert(
					DatabaseConstants.CONTENT_URI_ANNOUNCEMENTS, values);
			isDropTable = false;
			if (i == Constants.ANNOUNCEMENTS_COUNT - 1)
				return;
		}

	}

	/**
	 * method to insert Group options like members in the table
	 * "tableGroupOptionsList"
	 * 
	 * @param context
	 * @param uri
	 * @param members
	 */
	public static void insertGroupOptionsRecord(Context context, Uri uri,
			ArrayList<Members> members) {
		isDropTable = true;
		ContentValues values = new ContentValues();
		for (int i = 0; i < members.size(); i++) {
			values.put(DatabaseConstants.COL_MEMBERS_ID, members.get(i).getId());
			values.put(DatabaseConstants.COL_MEMBERS_NAME, members.get(i)
					.getName());
			values.put(DatabaseConstants.COL_MEMBERS_ADMINISTRATOR, members
					.get(i).getAdministrator());
			context.getContentResolver().insert(
					DatabaseConstants.CONTENT_URI_MEMBERSLIST, values);
			isDropTable = false;
		}
	}

	/**
	 * 
	 * method to insert Events list in the table "tableEventsList"
	 * 
	 * @param context
	 * @param uri
	 * @param events
	 */
	public static void insertEventsListRecord(Context context, Uri uri,
			ArrayList<Events> events) {
		isDropTable = true;
		ContentValues values = new ContentValues();
		for (int i = 0; i < events.size(); i++) {
			values.put(DatabaseConstants.COL_EVENT_ID, events.get(i).getId());
			values.put(DatabaseConstants.COL_EVENT_NAME, events.get(i)
					.getName());
			values.put(DatabaseConstants.COL_EVENT_LOCATION, events.get(i)
					.getLocation());
			values.put(DatabaseConstants.COL_EVENT_TIMEZONE, events.get(i)
					.getTimezone());
			values.put(DatabaseConstants.COL_EVENT_STARTTIME, events.get(i)
					.getStart_time());
			context.getContentResolver().insert(
					DatabaseConstants.CONTENT_URI_EVENTSLIST, values);
			isDropTable = false;
		}
	}

	/**
	 * 
	 * method to insert Event details in the table "tableEventDetails"
	 * 
	 * @param context
	 * @param uri
	 * @param events
	 */
	public static void insertEventDetailsRecord(Context context,
			String eventID, Uri uri, ArrayList<Events> events) {

		context.getContentResolver().delete(uri,
				DatabaseConstants.COL_EVENTDETAILS_ID + "= ?",
				new String[] { eventID });

		ContentValues values = new ContentValues();
		for (int i = 0; i < events.size(); i++) {
			values.put(DatabaseConstants.COL_EVENTDETAILS_ID, eventID);
			values.put(DatabaseConstants.COL_EVENTDETAILS_ORGANIZEDBY, events
					.get(i).getOrganized_by());
			values.put(DatabaseConstants.COL_EVENTDETAILS_FROM, events.get(i)
					.getFrom());
			values.put(DatabaseConstants.COL_EVENTDETAILS_CREATED, events
					.get(i).getCreated_time());
			values.put(DatabaseConstants.COL_EVENTDETAILS_MESSAGE, events
					.get(i).getMessage());
			context.getContentResolver().insert(
					DatabaseConstants.CONTENT_URI_EVENTSDETAILS, values);
		}
	}
	
	
	public static void insertEventDetailsRecordForOrganizer(Context context,
			String eventID, Uri uri, String organizedBy) {

		context.getContentResolver().delete(uri,
				DatabaseConstants.COL_EVENTDETAILS_ID + "= ?",
				new String[] { eventID });

		ContentValues values = new ContentValues();
			values.put(DatabaseConstants.COL_EVENTDETAILS_ID, eventID);
			values.put(DatabaseConstants.COL_EVENTDETAILS_ORGANIZEDBY, organizedBy);
			context.getContentResolver().insert(
					DatabaseConstants.CONTENT_URI_EVENTSDETAILS, values);
	}

	/**
	 * 
	 * method to insert Attendees in the table "tableEventAttendees"
	 * 
	 * @param context
	 * @param uri
	 * @param events
	 */
	public static void insertAttendeesRecord(Context context, String eventID,
			Uri uri, HashMap<String, ArrayList<Attendees>> hashMap) {

		context.getContentResolver().delete(uri,
				DatabaseConstants.COL_ATTENDEE_EVENT_ID + "= ?",
				new String[] { eventID });

		ArrayList<Attendees> attendees = new ArrayList<Attendees>();
		ContentValues values = new ContentValues();
		for (String key : hashMap.keySet()) {
			attendees = hashMap.get(key);
			for (Attendees attendee : attendees) {
				values.put(DatabaseConstants.COL_ATTENDEES_ID, attendee.getId());
				values.put(DatabaseConstants.COL_ATTENDEE_EVENT_ID, eventID);
				values.put(DatabaseConstants.COL_ATTENDEES_NAME,
						attendee.getName());
				values.put(DatabaseConstants.COL_ATTENDEES_RSVP_STATUS,
						attendee.getRsvp_status());
				context.getContentResolver().insert(
						DatabaseConstants.CONTENT_URI_EVENTATTENDEES, values);
			}
		}
	}

	/**
	 * method to retrieve group name from table "tableGroup"
	 * 
	 * @param context
	 * @param uri
	 * @param columns
	 * @return
	 */
	public static String getGroupRecords(Context context, Uri uri,
			String columns[]) {
		Cursor cur = context.getContentResolver().query(uri, columns, null,
				null, null);
		String name = "";
		if (cur.moveToFirst()) {
			do {
				for (int j = 0; j < columns.length; j++) {
					name = cur.getString(cur.getColumnIndex(columns[j]));
				}
			} while (cur.moveToNext());

		}
		cur.close();
		return name;
	}

	/**
	 * method to retrieve Array of announcements from the table
	 * "tableGroupAnnouncements"
	 * 
	 * @param context
	 * @param uri
	 * @param columns
	 * @return
	 */
	public static ArrayList<Announcements> getAnnouncementsRecords(
			Context context, Uri uri, String columns[]) {
		Cursor cur = context.getContentResolver().query(uri, columns, null,
				null, null);
		ArrayList<Announcements> announcements = new ArrayList<Announcements>();

		if (cur.moveToFirst()) {
			int count = 0;
			do {
				Announcements announce = new Announcements();
				announce.setName(cur.getString(cur.getColumnIndex(columns[0])));
				announce.setDate(cur.getString(cur.getColumnIndex(columns[1])));
				announce.setDescription(cur.getString(cur
						.getColumnIndex(columns[2])));
				announcements.add(count, announce);
				count++;
			} while (cur.moveToNext());

		}
		cur.close();
		return announcements;
	}

	/**
	 * method to retrieve array of Members from the table "tableMembersList"
	 * 
	 * @param context
	 * @param uri
	 * @param columns
	 * @return
	 */
	public static ArrayList<Members> getGroupOptionsRecords(Context context,
			Uri uri, String columns[]) {
		Cursor cur = context.getContentResolver().query(uri, columns, null,
				null, null);
		ArrayList<Members> members = new ArrayList<Members>();

		if (cur.moveToFirst()) {
			int count = 0;
			do {
				Members member = new Members();
				member.setId(cur.getString(cur.getColumnIndex(columns[0])));
				member.setName(cur.getString(cur.getColumnIndex(columns[1])));
				member.setAdministrator(cur.getString(cur
						.getColumnIndex(columns[2])));
				members.add(count, member);
				count++;
			} while (cur.moveToNext());

		}
		cur.close();
		return members;
	}

	/**
	 * method to retrieve array of events from the table "tableEventsList"
	 * 
	 * @param context
	 * @param uri
	 * @param columns
	 * @return
	 */
	public static ArrayList<Events> getGroupEventsRecords(Context context,
			Uri uri, String columns[]) {
		Cursor cur = context.getContentResolver().query(uri, columns, null,
				null, null);
		ArrayList<Events> events = new ArrayList<Events>();

		if (cur.moveToFirst()) {
			int count = 0;
			do {
				Events event = new Events();
				event.setId(cur.getString(cur.getColumnIndex(columns[0])));
				event.setName(cur.getString(cur.getColumnIndex(columns[1])));
				event.setLocation(cur.getString(cur.getColumnIndex(columns[2])));
				event.setTimezone(cur.getString(cur.getColumnIndex(columns[3])));
				event.setStart_time(cur.getString(cur
						.getColumnIndex(columns[4])));
				events.add(count, event);
				count++;
			} while (cur.moveToNext());

		}
		cur.close();
		return events;
	}

	/**
	 * method to retrieve array of Members from the table "tableEventDetails"
	 * 
	 * @param context
	 * @param uri
	 * @param columns
	 * @return
	 */
	public static ArrayList<Events> getGroupEventDetailsRecords(
			Context context, String eventID, Uri uri, String columns[]) {
		Cursor cur = context.getContentResolver().query(uri, columns,
				DatabaseConstants.COL_EVENTDETAILS_ID + "= ?",
				new String[] { eventID }, null);
		ArrayList<Events> events = new ArrayList<Events>();

		if (cur.moveToFirst()) {
			int count = 0;
			do {
				Events event = new Events();
				event.setOrganized_by(cur.getString(cur
						.getColumnIndex(columns[0])));
				event.setFrom(cur.getString(cur.getColumnIndex(columns[1])));
				event.setCreated_time(cur.getString(cur
						.getColumnIndex(columns[2])));
				event.setMessage(cur.getString(cur.getColumnIndex(columns[3])));
				if(event.getFrom() != null)
					events.add(event);
				count++;
			} while (cur.moveToNext());

		}
		cur.close();
		return events;
	}
	/**
	 * method to retrieve the organizer of the event in offine.
	 * "tableEventDetails"
	 * 
	 * @param context
	 * @param uri
	 * @param columns
	 * @return
	 */
	public static String getEventOrganizer(
			Context context, String eventID, Uri uri, String columns[]) {
		Cursor cur = context.getContentResolver().query(uri, columns,
				DatabaseConstants.COL_EVENTDETAILS_ID + "= ?",
				new String[] { eventID }, null);
		String organizedby = "";
		if (cur.moveToFirst()) {
			do {
				organizedby = cur.getString(cur
						.getColumnIndex(columns[0]));
			} while (cur.moveToNext());

		}
		cur.close();
		return organizedby;
	}
	
	/**
	 * method to retrieve array of attendees from the table
	 * "tableEventAttendees"
	 * 
	 * @param context
	 * @param uri
	 * @param columns
	 * @return
	 */

	public static HashMap<String, ArrayList<Attendees>> getEventAttendeesList(
			Context context, String eventID, Uri uri, String columns[]) {
		Cursor cur = context.getContentResolver().query(uri, columns,
				DatabaseConstants.COL_ATTENDEE_EVENT_ID + "= ?",
				new String[] { eventID }, null);
		HashMap<String, ArrayList<Attendees>> hashMap = new HashMap<String, ArrayList<Attendees>>();

		if (cur.moveToFirst()) {
			int count = 0;
			do {
				Attendees attendee = new Attendees();
				attendee.setId(cur.getString(cur.getColumnIndex(columns[0])));
				attendee.setName(cur.getString(cur.getColumnIndex(columns[1])));
				attendee.setRsvp_status(cur.getString(cur
						.getColumnIndex(columns[2])));
				// attendees.add(count, attendee);
				ArrayList<Attendees> alArrayList = hashMap.get(attendee
						.getRsvp_status());
				if (alArrayList == null) {
					alArrayList = new ArrayList<Attendees>();
					alArrayList.add(attendee);
					hashMap.put(attendee.getRsvp_status(), alArrayList);
				} else {
					alArrayList.add(attendee);
				}
				count++;
			} while (cur.moveToNext());

		}
		cur.close();
		return hashMap;
	}

}
