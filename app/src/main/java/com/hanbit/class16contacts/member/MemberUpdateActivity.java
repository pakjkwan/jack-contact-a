package com.hanbit.class16contacts.member;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanbit.class16contacts.MainActivity;
import com.hanbit.class16contacts.R;

public class MemberUpdateActivity extends AppCompatActivity {
    public final static String COLUMNS=" _id, name, pw, email, phone, address, profile_img ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update);
        final Context context=MemberUpdateActivity.this;
        ImageView profileImg= (ImageView) findViewById(R.id.profileImg);
        TextView textID= (TextView) findViewById(R.id.textID);
        TextView textName= (TextView) findViewById(R.id.textName);
        final EditText changePass= (EditText) findViewById(R.id.changePass);
        final EditText changeEmail= (EditText) findViewById(R.id.changeEmail);
        final EditText changePhone= (EditText) findViewById(R.id.changePhone);
        final EditText changeAddress= (EditText) findViewById(R.id.changeAddress);
        Intent intent=this.getIntent();
        final String spec=intent.getExtras().getString("spec");
        Log.d("intent 가 전달한 spec:",spec);
        // id+","+pw+","+name+","+email+","+phone+","+profileImg+","+address;
        final String[] specArr=spec.split(",");
        int profile=getResources().getIdentifier(
                this.getPackageName()+":drawable/profile_img",null,null);
        profileImg.setImageDrawable(getResources().getDrawable(profile,context.getTheme()));
        textID.setText(specArr[0]);
        changePass.setHint(specArr[1]);
        textName.setText(specArr[2]);
        changeEmail.setHint(specArr[3]);
        changePhone.setHint(specArr[4]);
        changeAddress.setHint(specArr[6]);
        findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MemberUpdate update=new MemberUpdate(context);
                new UpdateService() {
                    @Override
                    public void update() {
                        Member member=new Member();
                        member.setId(Integer.parseInt(specArr[0]));
                        member.setName(specArr[2]);
                        member.setPw((changePass.getText().toString().equals(""))?specArr[1]:changePass.getText().toString());
                        member.setAddress((changeAddress.getText().toString().equals(""))?specArr[6]:changeAddress.getText().toString());
                        member.setEmail((changeEmail.getText().toString().equals(""))?specArr[3]:changeEmail.getText().toString());
                        member.setPhone((changePhone.getText().toString().equals(""))?specArr[4]:changePhone.getText().toString());
                        Log.d("수정할 회원정보:",member.toString());
                        update.execute(member);
                    }
                }.update();
                Intent intent=new Intent(context,MemberDetailActivity.class);
                intent.putExtra("id",Integer.parseInt(specArr[0]));
                startActivity(intent);
            }
        });
        findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private abstract class UpdateQuery extends MainActivity.QueryFactory {
        SQLiteOpenHelper helper;
        public UpdateQuery(Context context) {
            super(context);
            helper=new MainActivity.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberUpdate extends UpdateQuery{
        public MemberUpdate(Context context) {
            super(context);
        }

        public Member execute(Member member){
            String sql=String.format("UPDATE Member SET pw='%s', phone='%s', email='%s', address='%S' " +
                    " WHERE _id=%s", member.getPw(),member.getPhone(),member.getEmail(),member.getAddress(),member.getId());
            Log.d("SQL 내용보기:",sql);
            Cursor cursor=super
                    .getDatabase()
                    .rawQuery(sql,null);
            if(cursor.moveToNext()){
                Log.d("DB 조회 결과 if 내부:",cursor.getString(cursor.getColumnIndex("name")));
                member.setName(cursor.getString(cursor.getColumnIndex("name")));
                member.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                member.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                member.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                member.setId(cursor.getInt(cursor.getColumnIndex("_id")));
                member.setProfileImg(cursor.getString(cursor.getColumnIndex("profile_img")));
                member.setPw(cursor.getString(cursor.getColumnIndex("pw")));
            }
            Log.d("DB 조회 결과 이름:",member.getName());
            return member;
        }
    }
    public static interface UpdateService{
        public void update();
    }
}
