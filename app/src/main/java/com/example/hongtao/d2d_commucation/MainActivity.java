package com.example.hongtao.d2d_commucation;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private WifiP2pManager wifiP2pManager = null;
    private boolean isWifiP2pEnabled = false;
    private WifiP2pManager.Channel channel;
    private WifiDirectBroadcastReceiver broadcastReceiver;
    private final IntentFilter intentFilter = new IntentFilter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //只有过滤广播,intentfilter可以在代码中写。
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        wifiP2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        //将Wi-Fip2p manger 和广播接收器联合
        broadcastReceiver = new WifiDirectBroadcastReceiver(wifiP2pManager,channel,this);
        //getMainLooper()返回主线程的Looper对象，initialize()函数将应用与Wi-Fi框架相连接
        channel = wifiP2pManager.initialize(this,getMainLooper(),null);
    }

    public void onResume(){
        super.onResume();
        //广播接收器需要在onResume方法中注册，在onPause()方法中解除注册
    }
    public void setIsWifiP2pEnabled(boolean isWifiP2pEnabled){
        this.isWifiP2pEnabled = isWifiP2pEnabled;
    }

    public void resetData(){
        DeviceListFragment deviceListFragment = (DeviceListFragment)
                getFragmentManager().findFragmentById(R.id.fragment_list);
        DeviceDetileFragment deviceDetileFragment = (DeviceDetileFragment)
                getFragmentManager().findFragmentById(R.id.fragment_detile);
        //清除本设备所感知的设备列表
        if(deviceListFragment!=null){
            deviceListFragment.clearPeers();
        }
        //对被设备之前所连接对设备信息视图重置
        if(deviceDetileFragment!=null){
            deviceDetileFragment.resetViews();
        }
    }
}
