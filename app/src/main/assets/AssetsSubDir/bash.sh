#!/usr/bin/env bash
su
cd /data/data/com.android.providers.contact/databases
#sqlite3 contacts2.db "select * from raw_contacts where display_name like '$%'";
sqlite3 contacts2.db "delete from raw_contacts where display_name like '$%'";
kill $(ps | grep android.process.acore | awk '{ print $2 }')
