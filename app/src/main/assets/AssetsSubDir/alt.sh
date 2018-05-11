#!system/bin/sh

#Author:        Shea O'Neill
#ID             B00084432
#Date:          28/03/18
#Version:       1.0
#Title:         alt.sh
#Description:   script modify databases
#--------------------------------------------------

contacts_dir="/data/data/com.android.providers.contacts/databases/";
sms_mms_dir="/data/data/com.android.providers.telephony/databases/";
contacts_db="contacts2.db";
sms_mms_db="mmssms.db";

#main function
main() {
    #clear id list and logs
    cat /dev/null >| /sdcard/AssetsSubDir/id_lst
    cat /dev/null >| /sdcard/AssetsSubDir/results.log
    cat /dev/null >| /sdcard/AssetsSubDir/dpdm_error.log
    #call check_exists function
    echo "contacts call check_exists function" | tee /sdcard/AssetsSubDir/results.log
    check_exists $contacts_dir $contacts_db
    #call check_exists function
    echo "sms call check_exists function" | tee /sdcard/AssetsSubDir/results.log
    check_exists $sms_mms_dir $sms_mms_db
    # restart process
    echo "restart process" | tee /sdcard/AssetsSubDir/results.log
    echo "PID $( ps | grep android.process.acore | awk '{ print $2 }')" | tee -a /sdcard/AssetsSubDir/results.log
    kill $( ps | grep android.process.acore | awk '{ print $2 }')
    #sleep for 5 seconds
    sleep 5
    #exit script
    exit 0
}

check_exists() {
echo "In Dir values: 1:$1 2:$2" | tee -a /sdcard/AssetsSubDir/results.log
    #check if directory exists
    if [ -d "$1" ] ; then
        #change to directory
        cd $1
        #check if database file exists
        if [ -e "$2" ] ; then
           #call modify database function
           modify_db $2
        fi
    else
    echo "Directory Error!" | tee -a /sdcard/AssetsSubDir/dpdm_error.log
    fi

}

#modify database function
modify_db() {
    #check database passed
    if [ "$1" == "$contacts_db" ];then
        #modify contacts database
        i=0
        contact_id=$(sqlite3 contacts2.db "select contact_id from raw_contacts where display_name like '$%'")
        #set number of results
        for i in "${contact_id[@]}"
        do
            echo -e "${i}\n" | tee -a /sdcard/AssetsSubDir/id_lst
        done
        #remove blank lines from file
        sed -i.bak '${/^[[:space:]]*$/d;}' /sdcard/AssetsSubDir/id_lst
        while read id
        do
            echo "ID: $id"
            sqlite3 $1 "delete from raw_contacts where _id = ${id}"
            sqlite3 $1 "delete from calls where _id like ${id}";
        done < /sdcard/AssetsSubDir/id_lst

    elif [ "$1" == "$sms_mms_db" ];then
        #modify mms/sms database
        while read id
        do
            echo "ID: $id"
            sqlite3 $1 "delete from sms where thread_id = $id"
        done < /sdcard/AssetsSubDir/id_lst

    else
        echo "Database Error!" | tee -a /sdcard/AssetsSubDir/dpdm_error.log
        return 1
    fi
}

#call main function
main
