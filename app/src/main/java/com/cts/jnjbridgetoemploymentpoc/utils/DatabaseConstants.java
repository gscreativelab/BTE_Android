package com.cts.jnjbridgetoemploymentpoc.utils;

import android.net.Uri;

public class DatabaseConstants {

	public static final String COVER_IMAGE = "coverImage";

	// Define CONTENT URI
	public static final String AUTHORITY = "com.cts.jnjbridgetoemploymentpoc.database.BTEContentProvider";
	public static final String GROUP_NAME = "tableGroup";
	public static final String GROUP_ANNOUNCEMENTS = "tableGroupAnnouncements";
	public static final String GROUP_MEMBERS = "tableMembersList";
	public static final String GROUP_EVENTS = "tableEventsList";
	public static final String GROUP_EVENT_DETAILS = "tableEventDetails";
	public static final String GROUP_EVENT_ATTENDEES = "tableEventAttendees";

	// Uri to the table of database.
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + GROUP_NAME);
	public static final Uri CONTENT_URI_ANNOUNCEMENTS = Uri.parse("content://"
			+ AUTHORITY + "/" + GROUP_ANNOUNCEMENTS);
	public static final Uri CONTENT_URI_MEMBERSLIST = Uri.parse("content://"
			+ AUTHORITY + "/" + GROUP_MEMBERS);
	public static final Uri CONTENT_URI_EVENTSLIST = Uri.parse("content://"
			+ AUTHORITY + "/" + GROUP_EVENTS);
	public static final Uri CONTENT_URI_EVENTSDETAILS = Uri.parse("content://"
			+ AUTHORITY + "/" + GROUP_EVENT_DETAILS);
	public static final Uri CONTENT_URI_EVENTATTENDEES = Uri.parse("content://"
			+ AUTHORITY + "/" + GROUP_EVENT_ATTENDEES);

	// Column names for group table.
	public static final String COL_GROUP_NAME = "groupName";

	// Column names for announcements table.
	public static final String COL_ANNOUNCEMNTS_NAME = "announcementsName";
	public static final String COL_ANNOUNCEMNTS_DATE = "announcementsDate";
	public static final String COL_ANNOUNCEMNTS_DESC = "announcementsDesc";

	// Column names for members table.
	public static final String COL_MEMBERS_ID = "_id";
	public static final String COL_MEMBERS_NAME = "memberName";
	public static final String COL_MEMBERS_ADMINISTRATOR = "memberAdministrator";

	// Column names for event table.
	public static final String COL_EVENT_ID = "_id";
	public static final String COL_EVENT_NAME = "eventName";
	public static final String COL_EVENT_LOCATION = "eventLocation";
	public static final String COL_EVENT_TIMEZONE = "eventTimeZone";
	public static final String COL_EVENT_STARTTIME = "eventStartTime";

	// Column names for event details table.
	public static final String COL_EVENTDETAILS_ID = "_id";
	public static final String COL_EVENTDETAILS_ORGANIZEDBY = "eventOrganizedBy";
	public static final String COL_EVENTDETAILS_FROM = "eventFrom";
	public static final String COL_EVENTDETAILS_CREATED = "eventCreated";
	public static final String COL_EVENTDETAILS_MESSAGE = "eventMessage";

	// Column names for event attendees table.
	public static final String COL_ATTENDEES_ID = "_id";
	public static final String COL_ATTENDEE_EVENT_ID = "event_id";
	public static final String COL_ATTENDEES_NAME = "attendeeName";
	public static final String COL_ATTENDEES_RSVP_STATUS = "attendeeRsvpStatus";

	/**
	 * FOR TESTING PERPOES ONLY, SHOULD BE REMOVED OR COMMENTED FROM WHERE IT IS
	 * CALLING AFTER TESTING, to copy jnjBTE.db to sdCard.
	 */
	/*
	 * @SuppressWarnings("resource") public static void copydbToSdcard() { try {
	 * File sd = Environment.getExternalStorageDirectory(); File data =
	 * Environment.getDataDirectory(); if (sd.canWrite()) { String currentDBPath
	 * = "//data//com.cts.jnjbridgetoemploymentpoc.ui.activity//databases//" +
	 * DbHelper.DATABASE_NAME; String backupDBPath = DbHelper.DATABASE_NAME;
	 * File currentDB = new File(data, currentDBPath); File backupDB = new
	 * File(sd, backupDBPath); if (currentDB.exists()) { FileChannel src = new
	 * FileInputStream(currentDB) .getChannel(); FileChannel dst = new
	 * FileOutputStream(backupDB) .getChannel(); dst.transferFrom(src, 0,
	 * src.size()); src.close(); dst.close(); } } } catch (Exception e) { } }
	 */

}
