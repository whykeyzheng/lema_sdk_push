package com.rance.chatui.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.lema.imsdk.callback.LMBasicApiCallback;
import com.lema.imsdk.client.LMClient;
import com.rance.chatui.R;


public class AddFriendFragment extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    EditText edtUser;
    EditText edtAccount;
    Button btnAdd;
    Button btnWait;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_friend, container, false);
        LMClient.init(LMClient.getContext());   //初始化
        edtUser = view.findViewById(R.id.edt_username);
        edtAccount = view.findViewById(R.id.edt_account);
        btnAdd = view.findViewById(R.id.btn_add);
        btnWait = view.findViewById(R.id.btn_add_wait);
        setListener();
        return view;
    }


    public static AddFriendFragment newInstance() {
        AddFriendFragment fragment = new AddFriendFragment();
        return fragment;
    }


    private void setListener() {
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LMClient.addFriend(edtUser.getText().toString(), new LMBasicApiCallback() {
                    @Override
                    public void gotResultFail(int i, String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void gotResultSuccess() {
                        Toast.makeText(getActivity(), "添加成功", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        btnWait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LMClient.friendRequest(edtAccount.getText().toString(), new LMBasicApiCallback() {
                    @Override
                    public void gotResultSuccess() {
                        Toast.makeText(getActivity(), "发送成功", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void gotResultFail(int i, String s) {
                        Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }

}
