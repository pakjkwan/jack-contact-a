package com.hanbit.class16contacts.member;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hanbit.class16contacts.MainActivity;
import com.hanbit.class16contacts.R;

import java.util.ArrayList;

public class MemberListActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_list);
        final Context context=MemberListActivity.this;
        /*String name=this.getIntent().getExtras().getString("name").toString();
        Log.d("넘어온 이름",name);
        TextView tvName= (TextView) findViewById(R.id.tvName);
        tvName.setText(name);*/
        final String[] members={"홍길동","김유신","이순신","강감찬","유관순"};
        final ListView listview= (ListView) findViewById(R.id.listView);
        final MemberList memberList=new MemberList(context);
        MainActivity.ListService service=new MainActivity.ListService() {
            @Override
            public ArrayList<?> list() {
                return memberList.list();
            }
        };
        ArrayList<Member>list=(ArrayList<Member>)service.list();
        Toast.makeText(context,list.get(0).getName(),Toast.LENGTH_LONG).show();
      /*  ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1,members);*/
        listview.setAdapter(new MemberAdapter(context,list));
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int i, long l) {
                Intent intent=new Intent(context,MemberDetailActivity.class);
                Member member=(Member) listview.getItemAtPosition(i);
                Log.d("선택된 ID",String.valueOf(member.getId()));
                intent.putExtra("id",member.getId());
                startActivity(intent);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int i, long l) {
                final Member member=(Member) listview.getItemAtPosition(i);
                new AlertDialog.Builder(context).setTitle("DELETE")
                        .setMessage("정말로 삭제하시겠습니까?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final MemberDelete delete=new MemberDelete(context);
                                new DeleteService() {
                                    @Override
                                    public void delete() {
                                        delete.execute(String.valueOf(member.getId()));
                                    }
                                }.delete();
                                startActivity(new Intent(context,MemberListActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();

                return true;
            }
        });

    }
    private abstract class ListQuery extends MainActivity.QueryFactory {
        SQLiteOpenHelper helper;
        public ListQuery(Context context) {
            super(context);
            helper=new MainActivity.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getReadableDatabase();
        }
    }
    private class MemberList extends ListQuery{
        public MemberList(Context context) {
            super(context);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }
        public ArrayList<Member> list(){
            ArrayList<Member>list=new ArrayList<>();
            SQLiteDatabase db=this.getDatabase();
            String sql="SELECT _id AS id, name, phone, email, address, profile_img AS profileImg, pw " +
                    " FROM Member;";
            Cursor cursor=db.rawQuery(sql,null);
            Member member=null;
            if(cursor!=null){
                if(cursor.moveToFirst()){
                    do{
                        member=new Member();
                        member.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex("id"))));
                        member.setName(cursor.getString(cursor.getColumnIndex("name")));
                        member.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                        member.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                        member.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                        member.setProfileImg(cursor.getString(cursor.getColumnIndex("profileImg")));
                        member.setPw(cursor.getString(cursor.getColumnIndex("pw")));
                        list.add(member);
                    }while(cursor.moveToNext());
                }
            }
            return list;
        }
    }
    private abstract class DeleteQuery extends MainActivity.QueryFactory {
        SQLiteOpenHelper helper;
        public DeleteQuery(Context context) {
            super(context);
            helper=new MainActivity.SQLiteHelper(context);
        }
        @Override
        public SQLiteDatabase getDatabase() {
            return helper.getWritableDatabase();
        }
    }
    private class MemberDelete extends DeleteQuery {
        public MemberDelete(Context context) {
            super(context);
        }

        @Override
        public SQLiteDatabase getDatabase() {
            return super.getDatabase();
        }
        public void execute(String id){
            String sql=String.format("DELETE FROM Member WHERE _id = %s",id);
            Log.d("삭제쿼리:",sql);
            super.getDatabase().execSQL(sql);
            super.getDatabase().close();
        }
    }
    interface DeleteService{
        public void delete();
    }
    class MemberAdapter extends BaseAdapter{
        ArrayList<Member>list;
        LayoutInflater inflater;

        public MemberAdapter(Context context,ArrayList<Member> list) {
            this.list=list;
            this.inflater=LayoutInflater.from(context);
        }

        private int[] photos={
            R.drawable.cupcake,
            R.drawable.donut,
            R.drawable.eclair,
            R.drawable.froyo,
            R.drawable.gingerbread,
            R.drawable.honeycomb,
            R.drawable.icecream,
            R.drawable.jellybean,
            R.drawable.kitkat,
            R.drawable.lollipop,
                R.drawable.mov1,
                R.drawable.mov2

        };
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup g) {
            ViewHolder holder;
            if(v==null){
                v=inflater.inflate(R.layout.member_item,null);
                holder=new ViewHolder();
                holder.profileImg= (ImageView) v.findViewById(R.id.profileImg);
                holder.tvName= (TextView) v.findViewById(R.id.tvName);
                holder.tvPhone= (TextView) v.findViewById(R.id.tvPhone);
                v.setTag(holder);
            }else{
                holder= (ViewHolder) v.getTag();
            }
            holder.profileImg.setImageResource(photos[i]);
            holder.tvName.setText(list.get(i).getName());
            holder.tvPhone.setText(list.get(i).getPhone());
            return v;
        }

    }
    static class ViewHolder{
        ImageView profileImg;
        TextView tvName;
        TextView tvPhone;
    }
}
