package com.fingertec.elevatormaintenance;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.fingertec.common.Constants;
import com.fingertec.utils.AMapUtil;
import com.fingertec.utils.ToastUtil;
import com.fingertec.widget.TitleBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import butterknife.BindView;

public class MapActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener {


    @BindView(R.id.map)
    MapView mapView;
    private AMap aMap;
    private Marker regeoMarker;
    private ExecutorService mExecutorService;
    private String addressName;
    private GeocodeSearch geocoderSearch;
    private LatLonPoint latLonPoint = new LatLonPoint(39.90865, 116.39751);
    private UiSettings mUiSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        elevatorLocation();
    }

    @Override
    void initWidget() {
        new TitleBuilder(this).setTitleText(getResources().getString(R.string.elevetor_location)).setLeftImage(R.mipmap.back)
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).setBackgroundColor(R.color.transparent);
        if (aMap == null) {
            aMap = mapView.getMap();
            mUiSettings = aMap.getUiSettings();
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            aMap.setOnMarkerClickListener(this);
            initMap();
        }
    }

    private void initMap() {
        mUiSettings.setScaleControlsEnabled(true);//控制比例尺控件是否显示
        mUiSettings.setCompassEnabled(true);//显示指南针
        mUiSettings.setScrollGesturesEnabled(true);
        mUiSettings.setZoomGesturesEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void elevatorLocation() {
        showWaitDialog("正在获取地址");
        String location = getIntent().getStringExtra(Constants.LOCATION_KEY);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        if (!TextUtils.isEmpty(location)) {
            GeocodeQuery query = new GeocodeQuery(location, "022");
            geocoderSearch.getFromLocationNameAsyn(query);
        } else {
            ToastUtil.show(this, "地址获取失败!");
            finish();
        }


    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        dismissWaitDialog();
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
               /* addressName = result.getRegeocodeAddress().getFormatAddress()
                        + "附近";*/
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                //ToastUtil.show(MapActivity.this, addressName);
            } else {
                ToastUtil.show(MapActivity.this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        dismissWaitDialog();
        if (rCode == 1000) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(address
                        .getLatLonPoint()));
                /*addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
                        + address.getFormatAddress();*/
                //ToastUtil.show(MapActivity.this, addressName);
            } else {
                ToastUtil.show(MapActivity.this, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }


    private Handler msgHandler = new Handler() {
        public void handleMessage(Message msg) {
            ToastUtil.showerror(MapActivity.this, msg.arg1);
        }
    };


}
