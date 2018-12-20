package com.example.wechat;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeChatFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    public MeChatFragment() {
        // Required empty public constructor
    }

    public static MeChatFragment newInstance(){
        return new MeChatFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_me_chat, container, false);
        init(v);
        return v;
    }
    private void init(View v){
        floatingActionButton = v.findViewById(R.id.fad_create_diary);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DiaryEditorActivity.class);
                startActivity(intent);
            }
        });
    }
}
