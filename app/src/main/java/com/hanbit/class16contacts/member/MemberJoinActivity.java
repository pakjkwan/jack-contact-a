package com.hanbit.class16contacts.member;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.hanbit.class16contacts.MainActivity;
import com.hanbit.class16contacts.R;

public class MemberJoinActivity extends AppCompatActivity {
    public final static String COLUMNS=" name, pw, email, phone, address ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_join);
        final Context context=MemberJoinActivity.this;
        final MemberJoin join=new MemberJoin(context);
        final EditText inputName= (EditText) findViewById(R.id.inputName);
        final EditText inputPass= (EditText) findViewById(R.id.inputPass);
        final EditText inputEmail= (EditText) findViewById(R.id.inputEmail);
        final EditText inputPhone= (EditText) findViewById(R.id.inputPhone);
        final EditText inputAddress= (EditText) findViewById(R.id.inputAddress);

        findViewById(R.id.submitBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberJoinActivity.RegisterService service=new RegisterService() {
                    @Override
                    public void register() {
                        Member member=new Member();
                        Log.d("회원가입자 이름:",inputName.getText().toString());
                        member.setPw(inputPass.getText().toString());
                        member.setAddress(inputAddress.getText().toString());
                        member.setEmail(inputEmail.getText().toString());
                        member.setPhone(inputPhone.getText().toString());
                        member.setName(inputName.getText().toString());
                        join.execute(member);
                        startActivity(new Intent(context,MainActivity.class));
                    }
                };
                service.register();
            }
        });
        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context,MemberJoinActivity.class));
            }
        });
    }
    private abstract class JoinQuery extends MainActivity.QueryFactory {
        SQLiteOpenHelper helper;
        public JoinQuery(Context context) {
            super(context);
            helper=new MainActivity.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class MemberJoin extends JoinQuery{
        public MemberJoin(Context context) {
            super(context);
        }

        public void execute(Member member){
            super.getDatabase().execSQL(
                    String.format("INSERT INTO Member( %s ) VALUES ( '%s','%s','%s','%s','%s' );",
                            COLUMNS, member.getName(), member.getPw(),
                            member.getEmail(), member.getPhone(), member.getAddress()));
        }
    }
    public static interface RegisterService{
        public void register();
    }
}
