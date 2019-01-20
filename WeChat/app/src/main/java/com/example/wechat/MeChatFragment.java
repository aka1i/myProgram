package com.example.wechat;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.wechat.adapter.NoteAdapater;
import com.example.wechat.bean.Note;
import com.example.wechat.bean.NoteLab;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeChatFragment extends Fragment {

    private FloatingActionButton floatingActionButton;
    private  RecyclerView recyclerView;
    private List<Note> notes;
    private List<Note> searchNotes;
    private  NoteAdapater adapater;
    private EditText mNoteSearchText;
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
        mNoteSearchText = v.findViewById(R.id.note_search_text);
        mNoteSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (searchNotes == null)
                    searchNotes = new ArrayList<>();
                else
                    searchNotes.clear();
                if (s.length() != 0) {
                    for (Note note : notes) {
                        if (note.getTitle().contains(s) || note.getDetail().contains(s))
                            searchNotes.add(note);
                    }
                }
                else
                    searchNotes.addAll(notes);
                adapater.setNotes(searchNotes);
                adapater.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        recyclerView = v.findViewById(R.id.note_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        floatingActionButton = v.findViewById(R.id.fad_create_diary);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note = new Note(UUID.randomUUID(),"","",new Date(),"", 0);
                NoteLab.get(getContext()).add(note);
                Intent intent = DiaryEditorActivity.newIntent(getContext(),note.getUuid(),0);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    void update(){
        notes = NoteLab.get(getContext()).getNotes();
        if (adapater == null)
            adapater = new NoteAdapater(getContext(),notes);
        recyclerView.setAdapter(adapater);
        adapater.setNotes(notes);
        adapater.notifyDataSetChanged();
    }
}
