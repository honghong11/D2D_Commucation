package com.example.hongtao.d2d_commucation;

import android.app.Fragment;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DeviceListFragment extends ListFragment implements WifiP2pManager.PeerListListener{
    private WifiP2pDevice device;
    ProgressDialog progressDialog = null;
    View mContentView = null;
    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //把adapter和xml资源文件相连
        this.setListAdapter(new WifiPeerListAdapter(getActivity(),R.layout.device_list,peers));
    }

    public View onCreatView(LayoutInflater layoutInflater, ViewGroup container,Bundle savedInstanceState){
        layoutInflater.inflate(R.layout.device_list,null);
        return mContentView;
    }

    private class WifiPeerListAdapter extends ArrayAdapter<WifiP2pDevice>{
        private List<WifiP2pDevice> items;
        public WifiPeerListAdapter(Context context,int textViewResourceId,List<WifiP2pDevice> objects){
            super(context,textViewResourceId,objects);
            items = objects;
        }
        //**为数据集中特定位置的数据给予展示
        // 方法getView 的原理？？？
        //如果convertView非空并且适合现在的数据，那么可以复用old view
        //否则，新建view
        public View getView(int positoin, View convertView, ViewGroup parent){
            View v = convertView;
            if(v==null){
                LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.device_list,null);
            }
            //如果convertView 合适，那么继续使用（reuse）convertView视图
            //接下来要把得到的数据放到控件上
            WifiP2pDevice wifiP2pDevice = items.get(positoin);
            if(wifiP2pDevice!=null){
                TextView top = (TextView) v.findViewById(R.id.myname);
                if(top!=null){
                    top.setText(wifiP2pDevice.deviceName);
                }
                TextView bottom = (TextView) v.findViewById(R.id.mystatus);
                if(bottom!=null){
                    bottom.setText(wifiP2pDevice.status);
                }
            }
            return v;
        }
    }
    public void updateThisDevice(WifiP2pDevice wifiP2pDevice){
        this.device = wifiP2pDevice;
        TextView textView = (TextView)mContentView.findViewById(R.id.myname);
        textView.setText(wifiP2pDevice.deviceName);
        textView = (TextView)mContentView.findViewById(R.id.mystatus);
        //status在Wifip2pDevice中定义的是整型，所以要转成对应的字符
        textView.setText(getDeviceStatus(wifiP2pDevice.status));
    }

    public void clearPeers(){
        peers.clear();
        ((WifiPeerListAdapter)getListAdapter()).notifyDataSetChanged();
    }
    private static String getDeviceStatus(int wifiP2pDevice){
        switch(wifiP2pDevice){
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknow";
        }
    }

    //WIfip2pDeviceList使用了一个map类型数据结构，<String,WifiP2pDevice>
    //onPeersAvailable方法，the requested peer list is available
    //这个函数是显示周围设备信息的关键
    public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList){
        if(progressDialog!=null&&progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        peers.clear();
        peers.addAll(wifiP2pDeviceList.getDeviceList());
        ((WifiPeerListAdapter)getListAdapter()).notifyDataSetChanged();
        if(peers.size()==0){
            Log.d("wifiDirect","no device found");
            return;
        }
    }
}
