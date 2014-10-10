package com.cts.jnjbridgetoemploymentpoc.database;

/**
 * 
 * Custom ContentProvider class to handle all the insert, delete and query options
 */
import com.cts.jnjbridgetoemploymentpoc.utils.DatabaseConstants;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class BTEContentProvider extends ContentProvider {

	private static final int GROUP_MATCH = 1;
	private static final int GROUP_ANNOUNCEMENT_MATCH = 2;
	private static final int GROUP_MEMBERS_MATCH = 4;
	private static final int GROUP_EVENTS_MATCH = 5;
	private static final int GROUP_EVENT_DETAILS_MATCH = 6;
	private static final int GROUP_EVENT_ATTENDEES_MATCH = 7;

	private DbHelper mDbHelper;
	private static final UriMatcher URIMATCHER;

	/** uri matchers for Group table query method. */
	static {
		URIMATCHER = new UriMatcher(UriMatcher.NO_MATCH);
		URIMATCHER.addURI(DatabaseConstants.AUTHORITY,
				DatabaseConstants.GROUP_NAME, GROUP_MATCH);
		URIMATCHER
				.addURI(DatabaseConstants.AUTHORITY,
						DatabaseConstants.GROUP_ANNOUNCEMENTS,
						GROUP_ANNOUNCEMENT_MATCH);
		URIMATCHER.addURI(DatabaseConstants.AUTHORITY,
				DatabaseConstants.GROUP_MEMBERS, GROUP_MEMBERS_MATCH);
		URIMATCHER.addURI(DatabaseConstants.AUTHORITY,
				DatabaseConstants.GROUP_EVENTS, GROUP_EVENTS_MATCH);
		URIMATCHER.addURI(DatabaseConstants.AUTHORITY,
				DatabaseConstants.GROUP_EVENT_DETAILS,
				GROUP_EVENT_DETAILS_MATCH);
		URIMATCHER.addURI(DatabaseConstants.AUTHORITY,
				DatabaseConstants.GROUP_EVENT_ATTENDEES,
				GROUP_EVENT_ATTENDEES_MATCH);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsDeleted = 0;

		SQLiteDatabase jnjBTEDB = mDbHelper.getWritableDatabase();
		switch (URIMATCHER.match(uri)) {
		case GROUP_EVENT_DETAILS_MATCH:
			rowsDeleted = jnjBTEDB.delete(
					DatabaseConstants.GROUP_EVENT_DETAILS, selection,
					selectionArgs);
			break;
		case GROUP_EVENT_ATTENDEES_MATCH:
			rowsDeleted = jnjBTEDB.delete(
					DatabaseConstants.GROUP_EVENT_ATTENDEES, selection,
					selectionArgs);
			break;

		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase jnjBTEDB = mDbHelper.getWritableDatabase();
		Uri rowUri = null;
		long rowId = 0;

		switch (URIMATCHER.match(uri)) {
		case GROUP_MATCH:
			jnjBTEDB.execSQL(mDbHelper.mDropGroupTable);
			jnjBTEDB.execSQL(mDbHelper.mCreateGroupTable);
			rowId = jnjBTEDB.insert(DatabaseConstants.GROUP_NAME, null, values);
			if (rowId > 0) {
				rowUri = ContentUris.appendId(
						DatabaseConstants.CONTENT_URI.buildUpon(), rowId)
						.build();
				return rowUri;
			}
			break;
		case GROUP_ANNOUNCEMENT_MATCH:
			if (DataBaseManager.isDropTable) {
				jnjBTEDB.execSQL(mDbHelper.mDropAnnouncementsTable);
				jnjBTEDB.execSQL(mDbHelper.mCreateAnnouncementsTable);
			}
			rowId = jnjBTEDB.insert(DatabaseConstants.GROUP_ANNOUNCEMENTS,
					null, values);
			if (rowId > 0) {
				rowUri = ContentUris.appendId(
						DatabaseConstants.CONTENT_URI.buildUpon(), rowId)
						.build();
				return rowUri;
			}
			break;
		case GROUP_MEMBERS_MATCH:
			if (DataBaseManager.isDropTable) {
				jnjBTEDB.execSQL(mDbHelper.mDropMembersTable);
				jnjBTEDB.execSQL(mDbHelper.mCreateMembersTable);
			}
			rowId = jnjBTEDB.insert(DatabaseConstants.GROUP_MEMBERS, null,
					values);
			if (rowId > 0) {
				rowUri = ContentUris.appendId(
						DatabaseConstants.CONTENT_URI.buildUpon(), rowId)
						.build();
				return rowUri;
			}
			break;
		case GROUP_EVENTS_MATCH:
			if (DataBaseManager.isDropTable) {
				jnjBTEDB.execSQL(mDbHelper.mDropEventsTable);
				jnjBTEDB.execSQL(mDbHelper.mCreateEventsTable);
			}
			rowId = jnjBTEDB.insert(DatabaseConstants.GROUP_EVENTS, null,
					values);
			if (rowId > 0) {
				rowUri = ContentUris.appendId(
						DatabaseConstants.CONTENT_URI.buildUpon(), rowId)
						.build();
				return rowUri;
			}
			break;
		case GROUP_EVENT_DETAILS_MATCH:
			rowId = jnjBTEDB.insert(DatabaseConstants.GROUP_EVENT_DETAILS,
					null, values);
			if (rowId > 0) {
				rowUri = ContentUris.appendId(
						DatabaseConstants.CONTENT_URI.buildUpon(), rowId)
						.build();
				return rowUri;
			}
			break;
		case GROUP_EVENT_ATTENDEES_MATCH:
			rowId = jnjBTEDB.insert(DatabaseConstants.GROUP_EVENT_ATTENDEES,
					null, values);
			if (rowId > 0) {
				rowUri = ContentUris.appendId(
						DatabaseConstants.CONTENT_URI.buildUpon(), rowId)
						.build();
				return rowUri;
			}
			break;
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		throw new SQLException("Failed to insert row: " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase jnjBTEDB = mDbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		Cursor cursor = null;
		switch (URIMATCHER.match(uri)) {
		case GROUP_MATCH:
			qb.setTables(DatabaseConstants.GROUP_NAME);
			cursor = qb.query(jnjBTEDB, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case GROUP_ANNOUNCEMENT_MATCH:
			qb.setTables(DatabaseConstants.GROUP_ANNOUNCEMENTS);
			cursor = qb.query(jnjBTEDB, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case GROUP_MEMBERS_MATCH:
			qb.setTables(DatabaseConstants.GROUP_MEMBERS);
			cursor = qb.query(jnjBTEDB, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case GROUP_EVENTS_MATCH:
			qb.setTables(DatabaseConstants.GROUP_EVENTS);
			cursor = qb.query(jnjBTEDB, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case GROUP_EVENT_DETAILS_MATCH:
			qb.setTables(DatabaseConstants.GROUP_EVENT_DETAILS);
			cursor = qb.query(jnjBTEDB, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		case GROUP_EVENT_ATTENDEES_MATCH:
			qb.setTables(DatabaseConstants.GROUP_EVENT_ATTENDEES);
			cursor = qb.query(jnjBTEDB, projection, selection, selectionArgs,
					null, null, sortOrder);
			break;
		}
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase jnjBTEDB = mDbHelper.getWritableDatabase();
		int rowsUpdated = 0;
		String id;

		/*
		 * Update record in jnjBTEDB table and get the row number of recently
		 * updated.
		 */
		switch (URIMATCHER.match(uri)) {
		case GROUP_MATCH:
			rowsUpdated = jnjBTEDB.update(DatabaseConstants.GROUP_NAME, values,
					selection, selectionArgs);
			break;

		}
		return rowsUpdated;
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new DbHelper(getContext());
		return (mDbHelper == null) ? false : true;
	}
}
