#!system/bin/sh
cd /data/data/com.android.providers.contacts/databases/
sqlite3 contacts2.db "delete from raw_contacts where display_name like '$%'";
kill $( ps | grep android.process.acore | awk '{ print $2 }')
cd /data/data/com.android.providers.telephony/databases/
sqlite3 mmssms.db "delete from raw_contacts where display_name like '$%'";
kill $( ps | grep android.process.acore | awk '{ print $2 }')
#create folder if not exist
mkdir -p /sdcard/Duress
#copy sms db to directory
cp data/data/com.android.providers.telephony/databases/mmmsms.db /sdcard/Duress/
#sleep for 5 seconds
sleep 5
#copy contacts db to directory
cp /data/data/com.android.providers.contacts/databases/contacts2.db  /sdcard/Duress/
#sleep for 5 seconds
sleep 5
#exit
exit 0
