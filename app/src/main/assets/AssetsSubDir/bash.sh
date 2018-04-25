#!system/bin/sh
cd /data/data/com.android.providers.contacts/databases/
sqlite3 contacts2.db "delete from raw_contacts where display_name like '$%'";
kill $( ps | grep android.process.acore | awk '{ print $2 }')
cd /data/data/com.android.providers.telephony/databases/
sqlite3 mmssms.db "delete from raw_contacts where display_name like '$%'";
kill $( ps | grep android.process.acore | awk '{ print $2 }')
