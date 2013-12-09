#!/usr/bin/python
# -*- coding: utf-8 -*-

import sqlite3 as lite
import sys

DATABASE_NAME = "whackAMole.db"

DEFAULT_ID_CONTENT = "_id integer primary key"

GAMELOG_TABLE_NAME = "gameLogs"
GAMELOG_TABLE_CONTENT = DEFAULT_ID_CONTENT + ", userId integer, scoreId integer, level integer, round integer"

SCORE_TABLE_NAME = "scores"
SCORE_TABLE_CONTENT = DEFAULT_ID_CONTENT + ", userId integer, score integer"

USER_TABLE_NAME = "users"
USER_TABLE_CONTENT = DEFAULT_ID_CONTENT + ", name text, password text"

# LEVEL_TABLE_NAME = "levels"
# LEVEL_TABLE_CONTENT = DEFAULT_ID_CONTENT + ", spriteLocation text"

# LOCATION_TABLE_NAME = "locations"
# LOCATION_TABLE_CONTENT = DEFAULT_ID_CONTENT + ", levelId integer, x integer, y integer"

ROUND_TABLE_NAME = "rounds"
ROUND_TABLE_CONTENT = DEFAULT_ID_CONTENT + ", levelId integer, moleId integer, time integer, appearanceTime integer"

# MOLE_TABLE_NAME = "moles"
# MOLE_TABLE_CONTENT = DEFAULT_ID_CONTENT + ", type integer, clicks integer, score integer, spriteLocation text"

METADATA_TABLE_NAME = "android_metadata"
METADATA_TABLE_CONTENT = "\"locale\" TEXT DEFAULT 'en_US'"

def clearTable(connection, cursor, name):
	sql = "drop table if exists %s " %(name)
	cursor.execute(sql)
	connection.commit()

def createTable(connection, cursor, name, content):
	sql = "create table if not exists %s (%s)" %(name, content)
	cursor.execute(sql)
	connection.commit()

def remakeTable(connection, cursor, name, content):
	clearTable(connection, cursor, name)
	createTable(connection, cursor, name, content)

def insertRecord(cursor, name, *args):
	if len(args) > 0:

		sql = "INSERT INTO %s VALUES(" %(name)

		for arg in args[:-1]:
			if arg == None:
				sql += "null,"
			elif type(arg) == str:
				sql += "'" + str(arg) + "',"
			else:
				sql += str(arg) + ","
		
		arg = args[-1]
		if arg == None:
			sql += "null)"
		elif type(arg) == str:
			sql += "'" + str(arg) + "')"
		else:
			sql += str(arg) + ")"
		
		cursor.execute(sql)
	else:
		raise ValueError("No args given to insert in table %s!" %(name))

if __name__ == "__main__":
	connection = lite.connect(DATABASE_NAME)

	with connection:
	    cursor = connection.cursor()

	    remakeTable(connection, cursor, GAMELOG_TABLE_NAME, GAMELOG_TABLE_CONTENT)
	    remakeTable(connection, cursor, SCORE_TABLE_NAME, SCORE_TABLE_CONTENT)
	    remakeTable(connection, cursor, USER_TABLE_NAME, USER_TABLE_CONTENT)
	    # remakeTable(connection, cursor, LEVEL_TABLE_NAME, LEVEL_TABLE_CONTENT)
	    # remakeTable(connection, cursor, LOCATION_TABLE_NAME, LOCATION_TABLE_CONTENT)
	    remakeTable(connection, cursor, ROUND_TABLE_NAME, ROUND_TABLE_CONTENT)
	    # remakeTable(connection, cursor, MOLE_TABLE_NAME, MOLE_TABLE_CONTENT)
	    remakeTable(connection, cursor, METADATA_TABLE_NAME, METADATA_TABLE_CONTENT)

	    insertRecord(cursor, METADATA_TABLE_NAME, 'en_US')
	    insertRecord(cursor, ROUND_TABLE_NAME, None, 0, 0, 0, 3)