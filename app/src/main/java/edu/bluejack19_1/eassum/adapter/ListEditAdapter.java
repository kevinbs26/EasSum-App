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

import androidx.appcompat.app.AlertDialog;

import edu.bluejack19_1.eassum.AdminEditActivity;
import edu.bluejack19_1.tpa_mobile.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListEditAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private Integer semester;

    public ListEditAdapter(ArrayList<String> tempList, Context context , int semester) {
        this.list = tempList;
        this.context = context;
        this.semester = semester;
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
            v = inflater.inflate(R.layout.list_course_admin_fragment, null);
        }

        final TextView listTitle = v.findViewById(R.id.list_course_frag);
        final Button update= v.findViewById(R.id.button_Update_frag);
        final Button delete= v.findViewById(R.id.button_Delete_frag);

        listTitle.setText(list.get(i));
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent x = new Intent(context , AdminEditActivity.class);
                x.putExtra("semester", semester);
                x.putExtra("title" , list.get(i));
                context.startActivity(x);
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Delete " + list.get(i) + " ?");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int in) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                        ref.child("semester").child(semester.toString()).child(list.get(i)).removeValue();
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

        return v;
    }
}
