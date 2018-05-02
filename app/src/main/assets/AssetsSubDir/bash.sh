#!system/bin/sh
# change to contacts database directory
cd /data/data/com.android.providers.contacts/databases/
# delete all contacts beginning with $
sqlite3 contacts2.db "delete from raw_contacts where display_name like '$%'";
# restart contacts application
kill $( ps | grep android.process.acore | awk '{ print $2 }')
# change to sms/mms database directory
cd /data/data/com.android.providers.telephony/databases/
# delete all sms/mms for contacts beginning with $
sqlite3 mmssms.db "delete from raw_contacts where display_name like '$%'";
# restart sms/mms application
kill $( ps | grep android.process.acore | awk '{ print $2 }')
#sleep for 5 seconds
sleep 5
#exit script
exit 0
