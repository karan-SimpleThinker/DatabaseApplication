package com.example.databaseapplication;


import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {
    EditText editEmpid,editName,editsalary;
    Button btnAdd,btnDelete,btnModify,btnView,btnViewAll;
    SQLiteDatabase db;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editEmpid=(EditText)findViewById(R.id.editEmpid);
        editName=(EditText)findViewById(R.id.editName);
        editsalary=(EditText)findViewById(R.id.editsalary);
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnModify=(Button)findViewById(R.id.btnModify);
        btnDelete=(Button)findViewById(R.id.btnDelete);
        btnView=(Button)findViewById(R.id.btnView);
        btnViewAll=(Button)findViewById(R.id.btnViewAll);

        btnAdd.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnModify.setOnClickListener(this);
        btnView.setOnClickListener(this);
        btnViewAll.setOnClickListener(this);
        db=openOrCreateDatabase("EmployeeDB", Context.MODE_PRIVATE,null);
        db.execSQL("CREATE TABLE IF NOT EXISTS employee(empid VARCHAR,name VARCHAR,salary VARCHAR)");
    }
    public void onClick(View view)
    {
        if (view==btnAdd)
        {if (editEmpid.getText().toString().trim().length()==0||editName.getText().toString().trim().length()==0||editsalary.getText().toString().trim().length()==0)
             {showMessage("ERROR","PLEASE ENTER ALL VALUES");
              return;
             }
             db.execSQL("INSERT INTO employee VALUES('"+editEmpid.getText()+"','"+editName.getText()+"','"+editsalary.getText()+"');");
             showMessage("Success","Record added");
             clearText();
        }
        if (view==btnDelete)
        {if (editEmpid.getText().toString().trim().length()==0||editName.getText().toString().trim().length()==0||editsalary.getText().toString().trim().length()==0)
            { showMessage("ERROR","PLEASE ENTER EMPLOYEE ID");
            return;
            }
            Cursor c=db.rawQuery("SELECT * FROM employee WHERE empid='"+editEmpid.getText()+"'",null);
            if (c.moveToFirst())
            {
            db.execSQL("DELETE FROM employee WHERE empid='"+editEmpid.getText()+"'");
            showMessage("Success","Record DELETED");
            }
            else
            { showMessage("ERROR","INVALID EMPLOYEE ID");
            }
            clearText();
        }
        if (view==btnModify)
        {if (editEmpid.getText().toString().trim().length()==0)
        { showMessage("ERROR","PLEASE ENTER EMPLOYEE ID");
            return;
        }
            Cursor c=db.rawQuery("SELECT * FROM employee WHERE empid='"+editEmpid.getText()+"'",null);
            if (c.moveToFirst())
            {
                db.execSQL("UPDATE employee SET name='"+editName.getText()+"',salary='"+editsalary.getText()+"'WHERE empid="+editEmpid.getText()+"'");
                showMessage("Success","Record MODIFIED");
            }
            else {
                showMessage("ERROR", "INVALID EMPLOYEE ID");
                clearText();
            }
        }
        if (view==btnView)
        {if (editEmpid.getText().toString().trim().length()==0)
        { showMessage("ERROR","PLEASE ENTER EMPLOYEE ID");
            return;
        }
            Cursor c=db.rawQuery("SELECT * FROM employee WHERE empid='"+editEmpid.getText()+"'",null);
            if (c.moveToFirst())
            {
                editName.setText(c.getString(1));
                editsalary.setText(c.getString(2));
            }
            else
            {
                showMessage("ERROR", "INVALID EMPLOYEE ID");
                clearText();
            }
        }
        if (view==btnViewAll)
        {
            Cursor c=db.rawQuery("SELECT * FROM employee",null);
            if (c.getCount()==0)
            { showMessage("Error"," No Record found");
            return;
            }
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("Employee id:"+c.getString(0)+"\n");
                buffer.append("Name:"+c.getString(1)+"\n");
                buffer.append("salary:"+c.getString(2)+"\n\n");
            }
            showMessage("Employee details Details",buffer.toString());
        }
    }
    public void showMessage(String title,String message)
    {
        Builder builder=new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText()
    {editEmpid.setText("");
    editName.setText("");
    editsalary.setText("");
    editEmpid.requestFocus();
    }
}
