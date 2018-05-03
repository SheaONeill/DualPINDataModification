#!system/bin/sh

#Author:        Shea O'Neill
#ID             B00084432
#Date:          28/03/18
#Version:       1.0
#Title:         bash.sh
#Description:   script modify databases
#--------------------------------------------------

contacts_dir="/data/data/com.android.providers.contacts/databases/";
sms_mms_dir="/data/data/com.android.providers.telephony/databases/";
contacts_db="contacts2.db";
sms_mms_db="mmssms.db";

#main function
main() {
    #call check_exists function
    check_exists $contacts_dir $contacts_db 
    #call check_exists function
    check_exists $sms_mms_dir $sms_mms_db
    # restart process
    kill $( ps | grep android.process.acore | awk '{ print $2 }')
    #sleep for 5 seconds
    sleep 5
    #exit script
    exit 0
}

check_exists() {
    #check if directory exists
    if [ -d "$1" ] ; then
        #change to directory
        cd $1
        #check if database file exists
        if [ -e "$2" ] ; then
           #call modify database function
           modify_db $2
        fi
    fi
}

#modify database function
modify_db() {
    #check database passed
    if [ "$1" == "$contacts_db" ];then
        #modify contacts database
        sqlite3 $1 "delete from raw_contacts where display_name like '$%'";
        sqlite3 $1 "delete from calls where name like '$%'";
    
    elif [ "$1" == "$sms_mms_db" ];then
        #modify mms/sms database
        sqlite3 $1 "delete from sms where person like '$%'";
    
    else
        return 1
    fi
}

#call main function
main
