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
import android.widget.ImageView;
import android.widget.TextView;

import com.hanbit.class16contacts.MainActivity;
import com.hanbit.class16contacts.R;
import com.hanbit.class16contacts.tools.Album;
import com.hanbit.class16contacts.tools.Email;
import com.hanbit.class16contacts.tools.MapsActivity;
import com.hanbit.class16contacts.tools.Phone;

import static com.hanbit.class16contacts.MainActivity.SQLiteHelper.TABLE_NAME;

public class MemberDetailActivity extends AppCompatActivity {
    public final static String COLUMNS=" _id, name, pw, email, phone, address, profile_img ";
    Phone phone;
    Email email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_detail);
        final Context context=MemberDetailActivity.this;
        ImageView profileImg= (ImageView) findViewById(R.id.profileImg);
        TextView textID= (TextView) findViewById(R.id.textID);
        TextView textName= (TextView) findViewById(R.id.textName);
        TextView textPass= (TextView) findViewById(R.id.textPass);
        TextView textEmail= (TextView) findViewById(R.id.textEmail);
        TextView textPhone= (TextView) findViewById(R.id.textPhone);
        TextView textAddress= (TextView) findViewById(R.id.textAddress);
        phone = new Phone(context,this);
        email=new Email(context,this);
        Intent intent=this.getIntent();
        final String id=String.valueOf(intent.getExtras().getInt("id"));
        Log.d("intent 가 전달한 id:",id);
        final MemberDetail detail=new MemberDetail(context);
        final Member member=new DetailService() {
            @Override
            public Member detail() {
                return detail.execute(id);
            }
        }.detail();
        int profile=getResources().getIdentifier(
                this.getPackageName()+":drawable/profile_img",null,null);
        profileImg.setImageDrawable(getResources().getDrawable(profile,context.getTheme()));
        textID.setText(String.valueOf(member.getId()));
        textName.setText(member.getName());
        textPass.setText(member.getPw());
        textPhone.setText(member.getPhone());
        textEmail.setText(member.getEmail());
        textAddress.setText(member.getAddress());
        findViewById(R.id.updateBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MemberUpdateActivity.class);
                intent.putExtra("spec",member.toString());
                startActivity(intent);
            }
        });
        findViewById(R.id.listBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context,MemberListActivity.class));
                }
        });
        findViewById(R.id.dialBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.dial(member.getPhone());
            }
        });
        findViewById(R.id.callBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.call(member.getPhone());
            }
        });
        findViewById(R.id.smsBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
        findViewById(R.id.emailBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.sendEmail(member.getEmail());
            }
        });
        findViewById(R.id.albumBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Album.class));
            }
        });
        findViewById(R.id.movieBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        findViewById(R.id.mapBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, MapsActivity.class);
                intent.putExtra("address",member.getAddress());
                startActivity(intent);
            }
        });
        findViewById(R.id.musicBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
    private abstract class DetailQuery extends MainActivity.QueryFactory {
        SQLiteOpenHelper helper;
        public DetailQuery(Context context) {
            super(context);
            helper=new MainActivity.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberDetail extends DetailQuery{
        public MemberDetail(Context context) {
            super(context);
        }

        public Member execute(String id){
            Member member=new Member();
            String sql=String.format("SELECT %s FROM %s WHERE _id = %s",
                    COLUMNS,TABLE_NAME,id);
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
    public static interface DetailService{
        public Member detail();
    }
}
