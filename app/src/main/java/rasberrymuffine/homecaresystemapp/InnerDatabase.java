package rasberrymuffine.homecaresystemapp;

import android.provider.BaseColumns;

/**
 * Created by 예림 on 2015-09-13.
 */
public final class InnerDatabase {
    public static final class CreateDB implements BaseColumns {
        public static final String SOUND = "sound";
        public static final String ALARM = "alarm";
        public static final String REPORT_CALL = "report_call";
        public static final String _TABLENAME = "setting";
        public static final String _CREATE =
                "create table "+_TABLENAME+"("
                        +_ID+" integer primary key autoincrement, "
                        +SOUND+" text not null , "
                        +ALARM+" text not null , "
                        +REPORT_CALL+" text not null );";
    }
}
