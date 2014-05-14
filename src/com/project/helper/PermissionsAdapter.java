package com.project.helper;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.project.model.Permission;
import com.project.sandroid.R;

public class PermissionsAdapter extends BaseAdapter
{
	private final Context context;
	private ArrayList<Permission> permissions= null;
	private LayoutInflater inflater;
	private static final String TAG = "PermissionsAdapter";
	public PermissionsAdapter(Context context, ArrayList<Permission> permissions)
	{
		this.context = context;
		this.permissions = permissions;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
    public int getCount() {return permissions.size();}

    @Override
    public Object getItem(int position) {return permissions.get(position);}

    @Override
    public long getItemId(int position) {return position;}
    
	@Override
  	public View getView(int position, View convertView, ViewGroup parent) 
	{
		
		View row = convertView;
		try {
			row = (View)inflater.inflate(R.layout.selectpermissions_row, parent, false);
			
			TextView permName = (TextView) row.findViewById(R.id.permissionname);
			CheckBox check = (CheckBox) row.findViewById(R.id.permissioncheck);
			TextView permFull = (TextView) row.findViewById(R.id.permission);
			TextView permDesc = (TextView) row.findViewById(R.id.permissiondesc);
			
			Permission p = permissions.get(position);
			
			String permission = p.getPermissionName();
			if(permission.equalsIgnoreCase("access network state") | permission.equalsIgnoreCase("receive") | permission.equalsIgnoreCase("camera") | permission.equalsIgnoreCase("access wifi state")| permission.equalsIgnoreCase("system alert window") | permission.equalsIgnoreCase("read contacts") | permission.equalsIgnoreCase("write contacts"))
				permName.setTextColor(0xFF000000 + Integer.parseInt("FF0000",16));
			
			permName.setText(permission);
			permFull.setText(p.getPermissionFull());
			permDesc.setText(p.getPermissionDescription());
			
			check.setOnCheckedChangeListener(myCheckChangList);
			check.setTag(position);
			check.setChecked(p.check);
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in getView : "+e.toString());
		}
        
		return row;
	}
	Permission getPermission(int position) 
	{
        return ((Permission) getItem(position));
    }

    public ArrayList<Permission> getPermissions() 
    {
        ArrayList<Permission> permission = new ArrayList<Permission>();
        for (Permission p : permissions) 
        {
            if (p.isCheck())
            	permission.add(p);
        }
        return permission;
    }
        
	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() 
	{
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) 
        {
        	getPermission((Integer) buttonView.getTag()).check = isChecked;
        }
    };
}
