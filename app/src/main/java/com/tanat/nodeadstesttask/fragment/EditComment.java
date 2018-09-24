package com.tanat.nodeadstesttask.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.tanat.nodeadstesttask.R;
import com.tanat.nodeadstesttask.model.Declaration;

public class EditComment extends DialogFragment {
    public static int ADD = 0;
    public static int EDIT = 1;

    private Declaration declaration;

    private EditText commentEditText;
    private Button saveButton;
    private Button cancelButton;

    private CommentListener listener;
    private int operationType;

    public EditComment newInstance(Declaration declaration, int operationType) {
        this.declaration = declaration;
        this.operationType = operationType;
        return new EditComment();
    }

    public void initView(View content) {
        commentEditText = (EditText) content.findViewById(R.id.commentEditText);
        saveButton = (Button) content.findViewById(R.id.saveButton);
        cancelButton = (Button) content.findViewById(R.id.cancelButton);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_edit_comment, container);
        initView(content);

        if (getDialog().getWindow() != null) {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }

        if (declaration.getComment() != null) {
            commentEditText.setText(declaration.getComment());
        }

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declaration.setComment(String.valueOf(commentEditText.getText()).trim());
                if (null != listener) {
                    listener.commentSave(declaration, operationType);
                }
                dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return content;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (CommentListener) context;
        } catch (ClassCastException e) {
        }
    }

    public interface CommentListener {
        void commentSave(Declaration declaration, int operationType);
    }
}
