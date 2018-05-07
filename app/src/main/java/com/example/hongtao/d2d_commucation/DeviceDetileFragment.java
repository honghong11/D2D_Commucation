package com.example.hongtao.d2d_commucation;

import android.app.Fragment;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.view.View;
import android.widget.TextView;

public class DeviceDetileFragment extends Fragment implements WifiP2pManager.ConnectionInfoListener{
    private View myContentView = null;
    public void onConnectionInfoAvailable(WifiP2pInfo wifiP2pInfo){

    }
    public void resetViews(){
       // myContentView.findViewById(R.id.connect).setVisibility(View.VISIBLE);
        //将控件中的数据清空
        TextView textView = (TextView) myContentView.findViewById(R.id.device_address);
        textView.setText("");
        textView = (TextView) myContentView.findViewById(R.id.device_info);
        textView.setText("");
        textView = (TextView) myContentView.findViewById(R.id.device_owner);
        textView.setText("");
        textView = (TextView) myContentView.findViewById(R.id.device_ip);
        textView.setText("");
        //将不该显示的控件设置为不可见
        //demo代码中设置一些控件隐藏，包括该方法第一行显示connect我觉得没用。因为此处把整个detilefragment隐藏了。
        this.getView().setVisibility(View.GONE);
    }
}
