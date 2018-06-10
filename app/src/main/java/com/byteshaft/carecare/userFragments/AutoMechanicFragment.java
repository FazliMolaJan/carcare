package com.byteshaft.carecare.userFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.byteshaft.carecare.Adapters.AutoMechanicCarWashAdapter;
import com.byteshaft.carecare.R;
import com.byteshaft.carecare.gettersetter.AutoMechanicCarWashItems;
import com.byteshaft.carecare.utils.AppGlobals;
import com.byteshaft.carecare.utils.Helpers;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class AutoMechanicFragment extends Fragment implements HttpRequest.OnReadyStateChangeListener,
        HttpRequest.OnErrorListener, View.OnClickListener {

    private View mBaseView;
    private ListView mAutoMechanicListView;
    private Button mNextButton;
    private HttpRequest request;

    private AutoMechanicCarWashAdapter adapter;
    private ArrayList<AutoMechanicCarWashItems> arrayList;
    private AutoMechanicCarWashItems autoMechanicCarWashItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_auto_mechanic, container, false);
        mAutoMechanicListView = mBaseView.findViewById(R.id.auto_mechanic_list_view);
        mNextButton = mBaseView.findViewById(R.id.button_next);
        mNextButton.setOnClickListener(this);
        arrayList = new ArrayList<>();
        adapter = new AutoMechanicCarWashAdapter(getActivity(), arrayList);
        mAutoMechanicListView.setAdapter(adapter);
        getAutoMechanicsServicesList();
        mAutoMechanicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                autoMechanicCarWashItems = arrayList.get(position);



            }
        });
        return mBaseView;
    }

    @Override
    public void onError(HttpRequest request, int readyState, short error, Exception exception) {
        Helpers.dismissProgressDialog();
        switch (readyState) {
            case HttpRequest.ERROR_CONNECTION_TIMED_OUT:
                Helpers.showSnackBar(getView(), getString(R.string.connection_time_out));
                break;
            case HttpRequest.ERROR_NETWORK_UNREACHABLE:
                Helpers.showSnackBar(getView(), exception.getLocalizedMessage());
                break;
        }

    }

    @Override
    public void onReadyStateChange(HttpRequest request, int readyState) {
        switch (readyState) {
            case HttpRequest.STATE_DONE:
                Helpers.dismissProgressDialog();
                switch (request.getStatus()) {
                    case HttpURLConnection.HTTP_OK:
                        try {
                            JSONObject mainJsonObject = new JSONObject(request.getResponseText());
                            JSONArray jsonArray = mainJsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                System.out.println("Test " + jsonArray.getJSONObject(i));
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                AutoMechanicCarWashItems items = new AutoMechanicCarWashItems();
                                items.setServiceId(jsonObject.getInt("id"));
                                items.setServiceName(jsonObject.getString("name"));
                                arrayList.add(items);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                }
        }

    }

    private void getAutoMechanicsServicesList() {
        request = new HttpRequest(getActivity());
        request.setOnReadyStateChangeListener(this);
        request.setOnErrorListener(this);
        request.open("GET", String.format("%smechanic-services", AppGlobals.BASE_URL));
        request.send();
        Helpers.showProgressDialog(getActivity(), "Fetching Services...");
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putInt("service_id", autoMechanicCarWashItems.getServiceId());
        ListOfServicesProviders listOfServicesProviders = new ListOfServicesProviders();
        listOfServicesProviders.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.container, listOfServicesProviders).commit();

    }
}
