package com.hanbit.class16contacts;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hanbit.class16contacts.member.MemberJoinActivity;
import com.hanbit.class16contacts.member.MemberListActivity;

import java.util.ArrayList;

import static com.hanbit.class16contacts.MainActivity.SQLiteHelper.COLUMNS;
import static com.hanbit.class16contacts.MainActivity.SQLiteHelper.TABLE_NAME;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context=MainActivity.this;
        final EditText inputID= (EditText) findViewById(R.id.inputID);
        final EditText inputPW= (EditText) findViewById(R.id.inputPW);
        final MemberLogin login=new MemberLogin(context);
        findViewById(R.id.loginBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String id=inputID.getText().toString();
                final String pw=inputPW.getText().toString();
                SQLiteHelper dao=new SQLiteHelper(context);
                Log.d("넘어갈 ID:",id);
                Log.d("넘어갈 PW:",pw);
                new LoginService() {
                    @Override
                    public void login() {
                        boolean loginOk=login.execute(id,pw);
                        if(loginOk){
                            Toast.makeText(context,"LOGIN SUCCESS",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context, MemberListActivity.class));
                        }else{
                            Toast.makeText(context,"LOGIN FAIL",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(context, MainActivity.class));
                        }
                    }
                }.login();
            }
        });
        findViewById(R.id.joinBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, MemberJoinActivity.class));
            }
        });
    }
    public static class SQLiteHelper extends SQLiteOpenHelper {
        public final static String DB_NAME="hanbit.db";
        public final static String TABLE_NAME="Member";
        public final static String ID="_id";
        public final static String PW="pw";
        public final static String NAME="name";
        public final static String EMAIL="email";
        public final static String PHONE="phone";
        public final static String PROFILE_IMG="profile_img";
        public final static String ADDRESS="address";
        public final static String COLUMNS=ID+","+PW;
        public SQLiteHelper(Context context) {
            super(context, DB_NAME, null, 1);
            this.getWritableDatabase();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String sql=
                    String.format("CREATE TABLE IF NOT EXISTS %s( %s INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                    "%s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT, %s TEXT);"
                            ,TABLE_NAME,ID, PW, NAME, EMAIL,PHONE,PROFILE_IMG,ADDRESS);
            db.execSQL(sql);
           /* for(int i=0;i<10;i++){
                db.execSQL(String.format(
                        "INSERT INTO %s(%s,%s,%s,%s,%s,%s) " +
                                " VALUES('%s','%s','%s','%s','%s','%s');"
                        ,TABLE_NAME, PW, NAME, EMAIL,PHONE,PROFILE_IMG,ADDRESS,
                        1,"홍길동"+i,"hong"+i+"@test.com","010-2222-333"+i,"hong"+i,"서울"+i));
            }
*/
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
            onCreate(db);
        }

    }
    public static abstract class QueryFactory {
        Context context;
        public QueryFactory(Context context) {
            this.context = context;
        }
        public abstract SQLiteDatabase getDatabase();
    }
    private abstract class LoginQuery extends MainActivity.QueryFactory {
        SQLiteOpenHelper helper;
        public LoginQuery(Context context) {
            super(context);
            helper=new MainActivity.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberLogin extends LoginQuery{
        public MemberLogin(Context context) {
            super(context);
        }

        public boolean execute(String id, String pw){
            return super
                    .getDatabase()
                    .rawQuery(String.format("SELECT %s FROM %s WHERE _id = '%s' AND pw = '%s'",COLUMNS,TABLE_NAME,id,pw),null)
                    .moveToNext();
        }
    }
    public static interface ListService{
        public ArrayList<?>list();
    }
    public static interface LoginService{
        public void login();
    }


}
