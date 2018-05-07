package com.example.hongtao.d2d_commucation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;

public class WifiDirectBroadcastReceiver extends BroadcastReceiver {
    private WifiP2pManager manager;
    private WifiP2pManager.Channel channel;
    private MainActivity mainActivity;
    public WifiDirectBroadcastReceiver(WifiP2pManager p2pManager, WifiP2pManager.Channel channel, MainActivity mainActivity){
        super();
        this.channel = channel;
        this.mainActivity = mainActivity;
        this.manager = p2pManager;
        //没有全部用到，但是可能会用到
    }
    public void onReceive(Context context, Intent intent){
        String actions = intent.getAction();
        if(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(actions)){
            //判断设备是否支持Wi-Fi p2p
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE,-1);
            if(state == WifiP2pManager.WIFI_P2P_STATE_ENABLED){
                //这里用到了构造函数中的第三个参数
                mainActivity.setIsWifiP2pEnabled(true);
            }else{
                mainActivity.setIsWifiP2pEnabled(false);
                //当前设备不能支持Wi-Fi p2p时，对该设备周围感知对设备和连接上的设备做一次刷新
                mainActivity.resetData();
            }
        }else if(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(actions)){
            //这个意图主要是更新设备周边连接设备的信息
            if(manager!=null){
                //调用requestPeers()函数，第二个参数作为监听
                manager.requestPeers(channel,(WifiP2pManager.PeerListListener)mainActivity.getFragmentManager()
                        .findFragmentById(R.id.fragment_list));
            }
        }else if(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(actions)){
            //和Wifi_p2p_peers_changed_action 意图相似，获取设备的连接状态，如果连接上则获取对应的连接信息，如果断开，则在列表重置连接信息
            if(manager==null){
                return;
            }
            NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);
            if(networkInfo.isConnected()){
                DeviceDetileFragment deviceDetileFragment = (DeviceDetileFragment) mainActivity.getFragmentManager().
                        findFragmentById(R.id.fragment_detile);
                manager.requestConnectionInfo(channel,deviceDetileFragment);
            }else{
                mainActivity.resetData();
            }
        }else if(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(actions)){
            //wifi_p2p_this_device_changed_action意图，用来广播对本设备的一些信息进行修改比如说设备名称。别的设备无法同步本设备的修改
            DeviceListFragment deviceListFragment = (DeviceListFragment) mainActivity.getFragmentManager().
                    findFragmentById(R.id.fragment_list);
            deviceListFragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
        }
    }
}
