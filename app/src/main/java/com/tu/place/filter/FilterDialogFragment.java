/* Copyright 2016 Esri
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For additional information, contact:
 * Environmental Systems Research Institute, Inc.
 * Attn: Contracts Dept
 * 380 New York Street
 * Redlands, California, USA 92373
 *
 * email: contracts@esri.com
 *
 */
package com.tu.place.filter;

import android.R.style;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tu.place.R;
import com.tu.place.activity.MainActivity;
import com.tu.place.data.CategoryKeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing the filter dialog used to set what types of places are
 * viewed in list or map.
 */
public class FilterDialogFragment extends DialogFragment implements FilterContract.View {
  private FilterContract.Presenter mPresenter = null;

  public FilterDialogFragment(){}

  @Override
  public final View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 final Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    getDialog().setTitle("Lọc địa điểm");
    final View view = inflater.inflate(R.layout.filter_view, container,false);
    final ListView listView = (ListView) view.findViewById(R.id.filterView);
    final EditText edtName = (EditText) view.findViewById(R.id.edtPlaceName);
    final SeekBar sbScore = (SeekBar) view.findViewById(R.id.sbScore);
    final SeekBar sbRadius = (SeekBar) view.findViewById(R.id.sbRadius);
    final List<FilterItem> filters = mPresenter.getFilteredCategories();
    final ArrayList<FilterItem> arrayList = new ArrayList<>();
    arrayList.addAll(filters);
    final FilterItemAdapter mFilterItemAdapter = new FilterItemAdapter(getActivity(), arrayList);
    listView.setAdapter(mFilterItemAdapter);
    sbRadius.setProgress((((MainActivity)getActivity()).radius.intValue()-1)/1000);
    sbScore.setProgress((((MainActivity)getActivity()).score-1));

    // Listen for Cancel/Apply
    final Button cancel = (Button) view.findViewById(R.id.btnCancel);
    cancel.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(final View v) {
        dismiss();
      }
    });
    final Button apply = (Button) view.findViewById(R.id.btnApply);
    apply.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(final View v) {
        final Activity activity = getActivity();
        final CategoryKeeper keeper = CategoryKeeper.getInstance();
        final ArrayList<String> selectedTypes = new ArrayList<>();
        selectedTypes.addAll(keeper.getSelectedTypes());
        ((MainActivity)activity).filterTypeList.clear();
        ((MainActivity)activity).filterTypeList.addAll(selectedTypes);
        ((MainActivity)activity).placeKey = edtName.getText().toString();
        ((MainActivity)activity).score = sbScore.getProgress()+1;
        ((MainActivity)activity).radius = (sbRadius.getProgress()+1) * 1000f;
        ((MainActivity)activity).mapFragment.mapController.loadPlace();
        if (activity instanceof FilterContract.FilterView){
          ((FilterContract.FilterView) activity).onFilterDialogClose(true);
        }
        dismiss();
      }
    });
    return view;
  }

  @Override
  public final void onCreate(final Bundle savedBundleState){
    super.onCreate(savedBundleState);
    setStyle(DialogFragment.STYLE_NORMAL, style.Theme_Material_Light_Dialog);
    mPresenter.start();
  }

  @Override
  public final void setPresenter(final FilterContract.Presenter presenter) {
    mPresenter = presenter;
  }


  public class FilterItemAdapter extends ArrayAdapter<FilterItem> {
    public FilterItemAdapter(final Context context, final List<FilterItem> items){
      super(context,0, items);
    }

    private class ViewHolder {
      Button btn = null;
      TextView txtName = null;
    }
    @NonNull
    @Override
    public final View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
      final FilterDialogFragment.FilterItemAdapter.ViewHolder holder;

      // Get the data item for this position
      final FilterItem item = getItem(position);
      // Check if an existing view is being reused, otherwise inflate the view
      if (convertView == null) {
        convertView = LayoutInflater.from(getActivity()).inflate(R.layout.filter_list_item, parent, false);
        holder = new FilterDialogFragment.FilterItemAdapter.ViewHolder();
        holder.btn = (Button) convertView.findViewById(R.id.categoryBtn);
        holder.txtName =  (TextView) convertView.findViewById(R.id.categoryName);
        convertView.setTag(holder);
      }else{
        holder = (FilterDialogFragment.FilterItemAdapter.ViewHolder) convertView.getTag();
      }
      // Lookup view for data population
      holder.txtName.setText((item != null) ? item.getTitle() : null);

      if (item.getSelected()) {
        holder.btn.setBackgroundResource(item.getSelectedIconId());
        holder.btn.setAlpha(1.0f);
      } else {
        holder.btn.setBackgroundResource(item.getIconId());
        holder.btn.setAlpha(0.5f);
      }

      // Attach listener that toggles selected state of category
      convertView.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(final View v) {
          final FilterItem clickedItem = getItem(position);
          if (clickedItem !=  null ){
            if (clickedItem.getSelected()){
              clickedItem.setSelected(false);
              holder.btn.setBackgroundResource(item.getIconId());
              holder.btn.setAlpha(0.5f);
            }else {
              clickedItem.setSelected(true);
              holder.btn.setBackgroundResource(item.getSelectedIconId());
              holder.btn.setAlpha(1.0f);
            }
          }

        }
      });

      // Return the completed view to render on screen
      return convertView;
    }
  }

}
