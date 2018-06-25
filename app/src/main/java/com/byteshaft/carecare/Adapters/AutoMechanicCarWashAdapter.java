package com.byteshaft.carecare.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.byteshaft.carecare.R;
import com.byteshaft.carecare.gettersetter.AutoMechanicCarWashItems;
import com.byteshaft.carecare.gettersetter.AutoMechanicCarWashSubItem;

import java.util.ArrayList;
import java.util.Random;

public class AutoMechanicCarWashAdapter extends ArrayAdapter<String> {

    private ViewHolder viewHolder;
    private ArrayList<AutoMechanicCarWashItems> arrayList;
    private Activity activity;

    private ArrayList<Integer> servicesArrayList;


    public AutoMechanicCarWashAdapter(Activity activity, ArrayList<AutoMechanicCarWashItems> arrayList) {
        super(activity, R.layout.delegate_auto_mechanic);
        this.activity = activity;
        this.arrayList = arrayList;
        servicesArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.delegate_auto_mechanic, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.linearLayout = convertView.findViewById(R.id.check_box_layout);
            viewHolder.categoryName = convertView.findViewById(R.id.category_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final AutoMechanicCarWashItems autoMechanicCarWashItems = arrayList.get(position);
        ArrayList<AutoMechanicCarWashSubItem> subItemArrayList = autoMechanicCarWashItems.getSubItemsArrayList();
        Log.e("AutoMechanicCarWash", " 9999999999999999999999999" +  subItemArrayList.size());
        if (subItemArrayList != null && subItemArrayList.size() > 0) {
            viewHolder.linearLayout.removeAllViews();
            for (int i = 0; i < subItemArrayList.size() ; i++) {
                AutoMechanicCarWashSubItem subItem = subItemArrayList.get(i);
                View childView = activity.getLayoutInflater().inflate(R.layout.raw_checkbox,
                        viewHolder.linearLayout, false);
                CheckBox serviceNameCheckBox = childView.findViewById(R.id.check_box);
                serviceNameCheckBox.setText(subItem.getServiceName());
                viewHolder.linearLayout.addView(childView);
                serviceNameCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    int serviceId = subItem.getServiceId();
                    if (isChecked) {
                        servicesArrayList.add(serviceId);
                    } else {
                        if (servicesArrayList.contains(serviceId)) {
                            servicesArrayList.remove(serviceId);
                        }
                    }
                });

            }
        } else {
            viewHolder.linearLayout.removeAllViews();
        }

        viewHolder.categoryName.setText(autoMechanicCarWashItems.getCategoryName());

        return convertView;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    public ArrayList<Integer> serviceRequestData() {
        return servicesArrayList;
    }

    class ViewHolder {
        LinearLayout linearLayout;
        TextView categoryName;
    }
}
