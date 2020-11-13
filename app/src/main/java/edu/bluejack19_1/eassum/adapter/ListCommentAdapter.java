package edu.bluejack19_1.eassum.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import edu.bluejack19_1.eassum.CommentUpdateActivity;
import edu.bluejack19_1.eassum.Model.User;
import edu.bluejack19_1.tpa_mobile.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListCommentAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> listName = new ArrayList<String>();
    private ArrayList<String> listKey = new ArrayList<String>();
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private Integer semester;
    private String course;
    private Integer type = User.getInstanceUserType();
    private String name = User.getInstanceUserName();

    public ListCommentAdapter(ArrayList<String> list, Context context, Integer semester , String course , ArrayList<String> listName , ArrayList<String> listKey) {
        this.list = list;
        this.context = context;
        this.semester = semester;
        this.course = course;
        this.listName = listName;
        this.listKey = listKey;
    }

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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_comment_fragment, null);
        }

        TextView commentView = v.findViewById(R.id.textView_comment_list);
        commentView.setText(list.get(i));

        Button deleteBtn = v.findViewById(R.id.button_delete_comment_list);

        if(type == 0){
            deleteBtn.setVisibility(View.INVISIBLE);
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Delete " + list.get(i) + " ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int in) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("semester").child(semester.toString()).child(course).child("comment").child(listName.get(i)).child(listKey.get(i)).removeValue();

                        list.remove(i);
                        listName.remove(i);
                        listKey.remove(i);

                        Toast.makeText(context , "Success" , Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialog.show();
            }
        });

        Button updateBtn = v.findViewById(R.id.button_update_comment_list);

        if(!listName.get(i).equals(name)){
            updateBtn.setVisibility(View.INVISIBLE);
        }

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                ref.child("semester").child(semester.toString()).child(course).child("comment").child(listName.get(i)).child(listKey.get(i)).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String comment = dataSnapshot.getValue().toString();
                        Intent x = new Intent(context , CommentUpdateActivity.class);
                        x.putExtra("comment", comment);
                        x.putExtra("key" , listKey.get(i));
                        x.putExtra("name" , listName.get(i));
                        x.putExtra("semester", semester);
                        x.putExtra("course" , course);

                        context.startActivity(x);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return v;
    }
}
