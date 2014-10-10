package com.cts.jnjbridgetoemploymentpoc.database;

/**
 * 
 * Database helper class which creates, upgrades and drops required tables.
 */
import com.cts.jnjbridgetoemploymentpoc.utils.DatabaseConstants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	public static final String DATABASE_NAME = "jnjBte.db";

	public static final int DATABASE_VERSION = 1;

	public String mCreateGroupTable = "create table if not exists "
			+ DatabaseConstants.GROUP_NAME + " ("
			+ DatabaseConstants.COL_GROUP_NAME + " text);";

	public String mCreateAnnouncementsTable = "create table if not exists "
			+ DatabaseConstants.GROUP_ANNOUNCEMENTS + " ("
			+ DatabaseConstants.COL_ANNOUNCEMNTS_NAME + " text,"
			+ DatabaseConstants.COL_ANNOUNCEMNTS_DATE + " text,"
			+ DatabaseConstants.COL_ANNOUNCEMNTS_DESC + " text);";

	public String mCreateMembersTable = "create table if not exists "
			+ DatabaseConstants.GROUP_MEMBERS + " ("
			+ DatabaseConstants.COL_MEMBERS_ID + " text,"
			+ DatabaseConstants.COL_MEMBERS_NAME + " text,"
			+ DatabaseConstants.COL_MEMBERS_ADMINISTRATOR + " text);";

	public String mCreateEventsTable = "create table if not exists "
			+ DatabaseConstants.GROUP_EVENTS + " ("
			+ DatabaseConstants.COL_EVENT_ID + " text,"
			+ DatabaseConstants.COL_EVENT_NAME + " text,"
			+ DatabaseConstants.COL_EVENT_LOCATION + " text,"
			+ DatabaseConstants.COL_EVENT_TIMEZONE + " text,"
			+ DatabaseConstants.COL_EVENT_STARTTIME + " text);";

	public String mCreateEventDetailsTable = "create table if not exists "
			+ DatabaseConstants.GROUP_EVENT_DETAILS + " ("
			+ DatabaseConstants.COL_EVENTDETAILS_ID + " text,"
			+ DatabaseConstants.COL_EVENTDETAILS_ORGANIZEDBY + " text,"
			+ DatabaseConstants.COL_EVENTDETAILS_FROM + " text,"
			+ DatabaseConstants.COL_EVENTDETAILS_CREATED + " text,"
			+ DatabaseConstants.COL_EVENTDETAILS_MESSAGE + " text);";

	public String mCreateEventAttendeesTable = "create table if not exists "
			+ DatabaseConstants.GROUP_EVENT_ATTENDEES + " ("
			+ DatabaseConstants.COL_ATTENDEES_ID + " text,"
			+ DatabaseConstants.COL_ATTENDEE_EVENT_ID + " text,"
			+ DatabaseConstants.COL_ATTENDEES_NAME + " text,"
			+ DatabaseConstants.COL_ATTENDEES_RSVP_STATUS + " text);";

	public String mDropGroupTable = "DROP TABLE "
			+ DatabaseConstants.GROUP_NAME;
	public String mDropAnnouncementsTable = "DROP TABLE "
			+ DatabaseConstants.GROUP_ANNOUNCEMENTS;
	public String mDropMembersTable = "DROP TABLE "
			+ DatabaseConstants.GROUP_MEMBERS;
	public String mDropEventsTable = "DROP TABLE "
			+ DatabaseConstants.GROUP_EVENTS;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(mCreateGroupTable);
		db.execSQL(mCreateAnnouncementsTable);
		db.execSQL(mCreateMembersTable);
		db.execSQL(mCreateEventsTable);
		db.execSQL(mCreateEventDetailsTable);
		db.execSQL(mCreateEventAttendeesTable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
		}
	}

}